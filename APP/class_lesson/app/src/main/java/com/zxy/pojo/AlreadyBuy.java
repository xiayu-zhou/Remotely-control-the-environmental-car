package com.zxy.pojo;
public class AlreadyBuy {
    private String ID;
    private String UID;
    private String CID;
    private int Count;
    private String Goodsname;
    private String BuyDate;
    private int Expense;
    private String Pic;

    public AlreadyBuy(){

    }

    public AlreadyBuy(String ID, String UID, String CID, int count, String goodsname, String buyDate, int expense, String pic) {
        this.ID = ID;
        this.UID = UID;
        this.CID = CID;
        Count = count;
        Goodsname = goodsname;
        BuyDate = buyDate;
        Expense = expense;
        Pic = pic;
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

    public String getBuyDate() {
        return BuyDate;
    }

    public void setBuyDate(String buyDate) {
        BuyDate = buyDate;
    }

    public int getExpense() {
        return Expense;
    }

    public void setExpense(int expense) {
        Expense = expense;
    }

    public String getGoodsname() {
        return Goodsname;
    }

    public void setGoodsname(String goodsname) {
        Goodsname = goodsname;
    }

    public String getPic() {
        return Pic;
    }

    public void setPic(String pic) {
        Pic = pic;
    }

    @Override
    public String toString() {
        return "alreadyBuy{" +
                "ID='" + ID + '\'' +
                ", UID='" + UID + '\'' +
                ", CID='" + CID + '\'' +
                ", Count=" + Count +
                ", Goodsname='" + Goodsname + '\'' +
                ", BuyDate='" + BuyDate + '\'' +
                ", Expense=" + Expense +
                ", Pic='" + Pic + '\'' +
                '}';
    }
}
