package dev.app;

import dev.app.classes.*;

public class Test {
    public static void main(String[] args) {
        admin foo = new admin(123, "foo", "oof");
        subject sub = new subject("Deutsch");

        System.out.println(foo.getFirstname());
        System.out.println(sub.getSubjName());
    }
}
