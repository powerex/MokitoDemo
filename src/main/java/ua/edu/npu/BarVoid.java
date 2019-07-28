package ua.edu.npu;

public class BarVoid {
    private FooVoid foo;

    public BarVoid(FooVoid foo) {
        this.foo = foo;
    }

    public void bar(String parameter) {
        foo.foo(parameter);
    }
}
