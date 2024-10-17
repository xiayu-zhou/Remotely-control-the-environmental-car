package com.zxy.pojo;

public class CarDetail {
    private String ID;
    private String UID;
    private String CID;
    private int Count;
    private String Cname;
    private String Introduce;
    private int Rmb;
    private String GPSPicG;

    public CarDetail(String ID, String UID, String CID, int count, String cname, String introduce, int Rmb, String GPSPicG) {
        this.ID = ID;
        this.UID = UID;
        this.CID = CID;
        Count = count;
        Cname = cname;
        Introduce = introduce;
        this.Rmb = Rmb;
        this.GPSPicG = GPSPicG;
    }

    public CarDetail(){

    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getCID() {
        return CID;
    }

    public void setCID(String CID) {
        this.CID = CID;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    public String getCname() {
        return Cname;
    }

    public void setCname(String cname) {
        Cname = cname;
    }

    public String getIntroduce() {
        return Introduce;
    }

    public void setIntroduce(String introduce) {
        Introduce = introduce;
    }

    public int getRmb() {
        return Rmb;
    }

    public void setRmb(int CRmb) {
        this.Rmb = CRmb;
    }

    public String getGPSPicG() {
        return GPSPicG;
    }

    public void setGPSPicG(String GPSPicG) {
        this.GPSPicG = GPSPicG;
    }

    @Override
    public String toString() {
        return "CarDetail{" +
                "ID='" + ID + '\'' +
                ", UID='" + UID + '\'' +
                ", CID='" + CID + '\'' +
                ", Count=" + Count +
                ", Cname='" + Cname + '\'' +
                ", Introduce='" + Introduce + '\'' +
                ", CRmb=" + Rmb +
                ", GPSPicG='" + GPSPicG + '\'' +
                '}';
    }
}
