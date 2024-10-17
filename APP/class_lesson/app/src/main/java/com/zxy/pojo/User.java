package com.zxy.pojo;

/**
 * 数据库的字段定义要和这里的一样
 */
public class User {
    private String UserName;
    private String UserPassword;
    private String sex;
    private int Age;
    private String ID;
    private String Phone;
    //职业
    private String Occupation;
    private String Address;
    private String PersonSign;
    private int RMB;

    public User(String userName, String userPassword, String sex, int age, String ID, String phone, String occupation, String address, String personSign, int RMB) {
        UserName = userName;
        UserPassword = userPassword;
        this.sex = sex;
        Age = age;
        this.ID = ID;
        Phone = phone;
        Occupation = occupation;
        Address = address;
        PersonSign = personSign;
        this.RMB = RMB;
    }

    public User() {
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserPassword() {
        return UserPassword;
    }

    public void setUserPassword(String userPassword) {
        UserPassword = userPassword;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getOccupation() {
        return Occupation;
    }

    public void setOccupation(String occupation) {
        Occupation = occupation;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPersonSign() {
        return PersonSign;
    }

    public void setPersonSign(String personSign) {
        PersonSign = personSign;
    }

    public int getRMB() {
        return RMB;
    }

    public void setRMB(int RMB) {
        this.RMB = RMB;
    }

    @Override
    public String toString() {
        return "User{" +
                "UserName='" + UserName + '\'' +
                ", UserPassword='" + UserPassword + '\'' +
                ", sex='" + sex + '\'' +
                ", Age=" + Age +
                ", ID='" + ID + '\'' +
                ", Phone='" + Phone + '\'' +
                ", Occupation='" + Occupation + '\'' +
                ", Address='" + Address + '\'' +
                ", PersonSign='" + PersonSign + '\'' +
                ", RMB=" + RMB +
                '}';
    }
}
