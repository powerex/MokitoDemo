package ua.edu.npu;

public class Bar {
    private Foo foo;

    public Bar(Foo foo) {
        this.foo = foo;
    }

    public String bar(String parameter) {
        return foo.foo(parameter);
    }
}
