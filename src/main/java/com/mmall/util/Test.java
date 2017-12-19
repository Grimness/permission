package com.mmall.util;

/**
 * @author liliang
 * @date 2017/11/22.
 */
class A{
    public String show(D d){
        return ("AD");
    }
    public String show(A a){
        return ("AA");
    }
}
class B extends A{
    public String show(B b){
        return ("BB");
    }
    public String show(A a){
        return ("BA");
    }
}

class C extends B{}
class D extends B{}

public class Test {
    public static void main(String [] args){
        A a = new A();
        B b = new B();
        C c = new C();
        D d = new D();
        //System.out.println(a.show(b)+"_"+a.show(c)+"_"+a.show(d)+"_"+b.show(a)+"_"+b.show(c)+"_"+b.show(d));
        System.out.println(a.show(b));
    }
}
