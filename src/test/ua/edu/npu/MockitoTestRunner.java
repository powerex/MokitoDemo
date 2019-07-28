package ua.edu.npu;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MockitoTestRunner {
    @Mock
    private Foo foo;

//*
    @InjectMocks
    private Bar bar = new Bar(null);

//*/


/*
    @Before
    public void init() {
        foo = Mockito.mock(Foo.class);
        bar = new Bar(foo);
    }
//*/

    @Test
    public void test() {
        bar.bar("qwe");
        verify(foo).foo("qwe");
    }

    @Test
    public void simpleMocking() {
        Foo foo = Mockito.mock(Foo.class);
        Bar bar  = new Bar(foo);
        bar.bar("qwe");
        verify(foo).foo("qwe");
    }

    @Test
    public void ignoreParameter() {
        bar.bar("dfs");
        verify(foo).foo(anyString());
    }

    @Test
    public void stubParameter() {
        when(foo.foo("qwe")).thenReturn("asd");
        stub(foo.foo("qwe")).toReturn("asd");
        doReturn("asd").when(foo).foo("qwe");

        assertEquals("asd",bar.bar("qwe"));
    }

    @Test
    public void stubParameterWrong() {
        when(foo.foo("qwe")).thenReturn("asd");

        assertNull(bar.bar("zxc"));
    }

    @Test
    public void ignoreString() {
        when(foo.foo(anyString())).thenReturn("asd");

        assertEquals("asd", bar.bar("qwe"));
        assertEquals("asd", bar.bar("zxc"));
    }

    @Test
    public void parameterMathes() {
        bar.bar("qwe");
        verify(foo).foo(matches("..."));
    }

    @Test
    public void scenarioMatches() {
        when(foo.foo(matches("..."))).thenReturn("asd");

        assertEquals("asd", bar.bar("qwe"));
        assertNull(bar.bar("qwer"));
    }

    @Test
    public void basisMatchers() {
        when(foo.foo(endsWith("we"))).thenReturn("asd");
        when(foo.foo(contains("w"))).thenReturn("asd");
        when(foo.foo(startsWith("qw"))).thenReturn("asd");

        assertEquals("asd", bar.bar("qwe"));
    }

    @Test
    public void basisMatchersVerify() {
        bar.bar("qwe");

        verify(foo).foo(endsWith("we"));
        verify(foo).foo(contains("w"));
        verify(foo).foo(startsWith("qw"));
    }

    @Test
    public void equals() {
        when(foo.foo(eq("qwe"))).thenReturn("asd");
        when(foo.foo("qwe")).thenReturn("asd");

        assertEquals("asd", bar.bar("qwe"));
    }

    private ArgumentMatcher isQwe() {
        return new ArgumentMatcher() {
            @Override
            public boolean matches(Object argument) {
                return argument.equals("qwe");
            }
        };
    }

    @Test
    public void matchers() {
        when(foo.foo((String) argThat(isQwe()))).thenReturn("asd");
        assertEquals("asd", bar.bar("qwe"));
        assertNull(bar.bar("asd"));
    }

    @Test
    public void simpleVoidMocking() {
        doNothing().when(foo).foo("qwe");
        bar.bar("qwe");
        verify(foo).foo("qwe");
    }

    @Test(expected = Exception.class)
    public void throwException() {
        when(foo.foo(anyString())).thenThrow(new Exception());
        bar.bar("qwe");
    }

    @Test(expected = Exception.class)
    public void voidThrows() {
        doThrow(new Exception()).when(foo).foo("qwe");
        bar.bar("qwe");
    }

    @Test
    public void checkTimes() {
        bar.bar("qwe");
        bar.bar("qwe");
        bar.bar("qwe");
        bar.bar("asd");

        verify(foo, times(3)).foo("qwe");
        verify(foo, atLeastOnce()).foo("qwe");
        verify(foo, never()).foo("zxc");
        verify(foo, atMost(5)).foo(anyString());
    }

    @Test
    public void spyParametr() {
        Foo foo = spy(new FooImplementation());
        Bar bar = new Bar(foo);

        assertEquals("qwe", bar.bar("qwe"));
        when(foo.foo("qwe")).thenReturn("asd");
        doReturn("asd").when(foo).foo("qwe");

        assertEquals("asd", bar.bar("qwe"));
    }

    @Test
    public void mockWithoutScenario() {
        Foo foo = mock(FooImplementation.class);
        Bar bar = new Bar(foo);

        assertNull(bar.bar("qwe")); // was

        doReturn("asd").when(foo).foo("qwe");    // scenario
        assertEquals("asd", bar.bar("qwe"));         // is
    }

    @Test
    public void byDefaultReturns() {
        List list  = mock(List.class);

        assertEquals(0, list.size());
        assertFalse(list.isEmpty());
        assertNull(list.iterator());
        assertEquals("[]", list.subList(1, 2).toString());
    }

    @Test
    public void stubThenCall() {
        Foo foo = mock(FooImplementation.class);
        Bar bar = new Bar(foo);

        when(foo.foo("qwe")).thenReturn("asd").thenCallRealMethod();

        assertEquals("asd", bar.bar("qwe"));
        assertEquals("qwe", bar.bar("qwe"));
        assertEquals("qwe", bar.bar("qwe"));
        assertEquals("qwe", bar.bar("qwe"));
    }

    @Test
    public void voidCallRealMethod() {
        FooVoid foo = mock(FooVoidImplementation.class);
        BarVoid bar = new BarVoid(foo);

        doCallRealMethod().when(foo).foo("qwe");
        bar.bar("qwe");
        verify(foo).foo("qwe");
    }

    @Test
    public void someFlow() {
        when(foo.foo("qwe")).thenReturn("asd").thenReturn("zxc");
//        when(foo.foo("qwe")).thenReturn("asd", "zxc");

        assertEquals("asd", bar.bar("qwe"));
        assertEquals("zxc", bar.bar("qwe"));
        assertEquals("zxc", bar.bar("qwe"));
    }

    @Test
    public void twoMocksFlow() {
        Foo foo1 = mock(Foo.class);
        Foo foo2 = mock(Foo.class);
        Bar2 bar = new Bar2(foo1, foo2);

        InOrder inOrder = inOrder(foo1, foo2);
        bar.bar("qwe");

        inOrder.verify(foo1).foo("qwe");
        inOrder.verify(foo2).foo("qwe");
    }

    @Test
    public void thenAnswer() {
        when(foo.foo(anyString())).thenAnswer(new Answer() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                if (args[0].equals("qwe"))
                    return "asd";
                else
                    return "qwe";
            }
        });

        assertEquals("asd", bar.bar("qwe"));
        assertEquals("qwe", bar.bar("asd"));
    }

    @Test
    public void neverCallMock() {
//        bar.bar(anyString());
        verifyZeroInteractions(foo);
    }

    @Test
    public void neverCallMockMethod() {
        bar.bar("qwe");

        verify(foo).foo("qwe");
        verifyNoMoreInteractions(foo);

//        verify(foo, only()).foo("qwe");
    }
}
