package com.zxy.pojo;

public class Administrator {
    private String AdminName;
    private String AdminPassword;
    private String Sex;
    private int Age;
    private String ID;
    private String Phone;
    private String Department;
    private String Position;
    private String Address;

    public Administrator(String adminName, String adminPassword, String sex, int age, String ID, String phone, String department, String position, String address) {
        AdminName = adminName;
        AdminPassword = adminPassword;
        Sex = sex;
        Age = age;
        this.ID = ID;
        Phone = phone;
        Department = department;
        Position = position;
        Address = address;
    }

    public Administrator(){

    }

    public String getAdminName() {
        return AdminName;
    }

    public void setAdminName(String adminName) {
        AdminName = adminName;
    }

    public String getAdminPassword() {
        return AdminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        AdminPassword = adminPassword;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
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

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public String getPosition() {
        return Position;
    }

    public void setPosition(String position) {
        Position = position;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    @Override
    public String toString() {
        return "Administrator{" +
                "AdminName='" + AdminName + '\'' +
                ", AdminPassword='" + AdminPassword + '\'' +
                ", Sex='" + Sex + '\'' +
                ", Age=" + Age +
                ", ID='" + ID + '\'' +
                ", Phone='" + Phone + '\'' +
                ", Department='" + Department + '\'' +
                ", Position='" + Position + '\'' +
                ", Address='" + Address + '\'' +
                '}';
    }
}
