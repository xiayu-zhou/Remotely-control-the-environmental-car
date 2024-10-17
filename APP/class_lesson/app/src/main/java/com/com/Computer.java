package com.com;

public class Computer {
    private String IP;
    public Computer(){
        //this.IP = "192.168.31.100";
        //this.IP = "192.168.31.95";
        this.IP = "192.168.1.5";
        this.IP = "192.168.1.9";
        //this.IP = "192.168.67.242";
        //this.IP = "127.0.0.1";
        //this.IP = "192.168.107.147";
//        this.IP = "192.168.197.90";
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    @Override
    public String toString() {
        return "Computer{" +
                "IP='" + IP + '\'' +
                '}';
    }
}
