package com.codi.testjackson;

/**
 * Created by Codi on 2015/5/18 0018.
 */
public class User {

    public String name;
    public String password;

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
