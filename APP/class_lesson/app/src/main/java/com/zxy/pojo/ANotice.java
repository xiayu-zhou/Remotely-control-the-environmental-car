package com.zxy.pojo;

public class ANotice {
    private String ID;
    private String title;
    private String Content;

    public ANotice(){

    }

    public ANotice(String ID, String title, String content) {
        this.ID = ID;
        this.title = title;
        Content = content;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    @Override
    public String toString() {
        return "ANoticeMapper{" +
                "ID='" + ID + '\'' +
                ", title='" + title + '\'' +
                ", Content='" + Content + '\'' +
                '}';
    }
}
