package com.github.catbert.tlma.api.task.v2.test;


public abstract class ClassA1 {

    public boolean needWater() {
        return false;
    }



    public static class ClassA2 extends ClassA1 implements NeedWater{

    }
}
