package com.example.aliia.traveljournal;

public class User {
    public String name,email,city,country,phone,gender,age;

    public User(String name, String email, String city, String country, String phone, String gender,String age) {
        this.name = name;
        this.email = email;
        this.city = city;
        this.country = country;
        this.phone = phone;
        this.gender = gender;
        this.age=age;

    }
    public User()
    {
        name="";
        email="";
        phone="";
        age="";
        city="";
        country="";
        gender="";
    }

    public void setName(String name) {
        this.name = name;
    }



    public void setEmail(String email) {
        this.email = email;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getPhone() {
        return phone;
    }

    public String getGender() {
        return gender;
    }

    public String getAge() {
        return age;
    }
}
