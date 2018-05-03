package com.example.me.intern;

public class Userinfo {
    public  String name;
    public String age;
    public String email;
    public String number;
    public String password;
    public String gender;

    public  Userinfo(){

    }

    public Userinfo(String name, String gender, String age, String email, String number, String password){
        this.name=name;
        this.gender=gender;
        this.age=age;
        this.number=number;
        this.email=email;
        this.password=password;
    }
}
