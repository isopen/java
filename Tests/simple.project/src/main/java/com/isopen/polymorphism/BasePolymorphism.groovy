package com.isopen.polymorphism

/**
 * Created by isopen on 19.09.2016.
 */

class BasePolymorphism implements Polymorphism {
    String sayHello() {
        'Hello'
    }
    String sayHi() {
        'Hi'
    }
    String say() {
        3 + 5 / 2
    }
    protected String sayHao() {
        2 * 2
        3 * 3
        4 * 4
        'Hao'
    }
    String getSayHao() {
        this.sayHao();
    }
}