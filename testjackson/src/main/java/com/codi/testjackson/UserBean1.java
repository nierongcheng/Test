package com.codi.testjackson;

/**
 * Created by Codi on 2015/5/18 0018.
 */
public class UserBean1 {

    private String mName;
    private String mPassword;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        this.mPassword = password;
    }

    @Override
    public String toString() {
        return "UserBean1{" +
                "mName='" + mName + '\'' +
                ", mPassword='" + mPassword + '\'' +
                '}';
    }
}
