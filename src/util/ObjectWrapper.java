package util;

public class ObjectWrapper<A, B, C, D> {
    public final A FIRST;
    public final B SECOND;
    public final C THIRD;
    public final D FOURTH;

    public ObjectWrapper(A a, B b, C c, D d) {
        FIRST = a;
        SECOND = b;
        THIRD = c;
        FOURTH = d;
    }
}
