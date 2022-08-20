package org.techtown.HowAboutThisDay;

public class planList_item {
    String title, user, date;

    public planList_item(String title, String user, String date){
        this.title = title;
        this.user = user;
        this.date = date;
    }
    public String getTitle(){
        return title;
    }
    public String getUser(){
        return user;
    }
    public String getDate(){
        return date;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public void setUser(String user){
        this.user = user;
    }
    public void setDate(String date){
        this.date = date;
    }
}
