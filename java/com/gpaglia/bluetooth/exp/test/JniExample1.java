package com.gpaglia.bluetooth.exp.test;

@SuppressWarnings("java:S106")
public class JniExample1 {

    private JniExample1() {}

    public static void main(String[] args) {
        final JniExample1 example = new JniExample1();
        final Another another = new Another(314);
        
        final int result = example.callc(another);

        System.out.printf("Result from jni is %d %n", result);

    }

    private native int callc(final Another ao);

    
}

@SuppressWarnings("java:S106")
class Another {
    private static final String FMT = "This is the callback with local value %d and provided value %d %n";
    private final int aval;


    Another(final int v) {
        this.aval = v;
    }

    public void callback(final int extv) {
        System.out.printf(FMT, aval, extv);
    }
}
