package com.zxy.pojo;

public class Commodity {
    //产品编号
    private String ID;
    //产品名字
    private String CName;
    //产品参数
    private String Parameter;
    //上市日期
    private String CDate;
    //产品的介绍
    private String Introduce;
    //产品类型
    private String Type;
    //图片定位
    private String GPSPicG;
    //图片定位
    private String GPSPicS;
    //产品剩余
    private  int Count;
    //价格
    private int RMB;
    //销售
    private int sellCount;
    //上架状态
    private String CStatus;

    public Commodity(){

    }

    public Commodity(Commodity commodity){
        this.ID = commodity.ID;
        this.CName = commodity.CName;
        Parameter = commodity.Parameter;
        this.CDate = commodity.CDate;
        Introduce = commodity.Introduce;
        Type = commodity.Type;
        this.GPSPicG = commodity.GPSPicG;
        this.GPSPicS = commodity.GPSPicS;
        Count = commodity.Count;
        this.RMB = commodity.RMB;
        this.sellCount = commodity.sellCount;
        this.CStatus = commodity.CStatus;
    }


    public Commodity(String ID, String CName, String parameter, String CDate, String introduce, String type, String GPSPicG, String GPSPicS, int count, int RMB, int sellCount, String CStatus) {
        this.ID = ID;
        this.CName = CName;
        Parameter = parameter;
        this.CDate = CDate;
        Introduce = introduce;
        Type = type;
        this.GPSPicG = GPSPicG;
        this.GPSPicS = GPSPicS;
        Count = count;
        this.RMB = RMB;
        this.sellCount = sellCount;
        this.CStatus = CStatus;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCName() {
        return CName;
    }

    public void setCName(String CName) {
        this.CName = CName;
    }

    public String getParameter() {
        return Parameter;
    }

    public void setParameter(String parameter) {
        Parameter = parameter;
    }

    public String getCDate() {
        return CDate;
    }

    public void setCDate(String CDate) {
        this.CDate = CDate;
    }

    public String getIntroduce() {
        return Introduce;
    }

    public void setIntroduce(String introduce) {
        Introduce = introduce;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getGPSPicG() {
        return GPSPicG;
    }

    public void setGPSPicG(String GPSPicG) {
        this.GPSPicG = GPSPicG;
    }

    public String getGPSPicS() {
        return GPSPicS;
    }

    public void setGPSPicS(String GPSPicS) {
        this.GPSPicS = GPSPicS;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    public int getRMB() {
        return RMB;
    }

    public void setRMB(int RMB) {
        this.RMB = RMB;
    }

    public int getSellCount() {
        return sellCount;
    }

    public void setSellCount(int sellCount) {
        this.sellCount = sellCount;
    }

    public String getCStatus() {
        return CStatus;
    }

    public void setCStatus(String CStatus) {
        this.CStatus = CStatus;
    }

    @Override
    public String toString() {
        return "Commodity{" +
                "ID='" + ID + '\'' +
                ", CName='" + CName + '\'' +
                ", Parameter='" + Parameter + '\'' +
                ", CDate='" + CDate + '\'' +
                ", Introduce='" + Introduce + '\'' +
                ", Type='" + Type + '\'' +
                ", GPSPicG='" + GPSPicG + '\'' +
                ", GPSPicS='" + GPSPicS + '\'' +
                ", Count=" + Count +
                ", RMB=" + RMB +
                ", sellCount=" + sellCount +
                ", CStatus='" + CStatus + '\'' +
                '}';
    }
}
