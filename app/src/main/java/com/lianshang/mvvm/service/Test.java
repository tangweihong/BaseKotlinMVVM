package com.lianshang.mvvm.service;

/**
 * Created by hong on 2021/7/27 17:20.
 */
public class Test {

    public static void main(String[] args) {
        int a = 0;
        try {
            a = 1;
            return;
        } catch (Exception e) {
            a = 2;
        } finally {
            a = 3;
        }
        System.out.println(a+"");
    }
}
