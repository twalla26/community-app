package org.techtown.HowAboutThisDay;

public class commentList_item {
    String comment, user;

    public commentList_item(String comment, String user){
        this.comment = comment;
        this.user = user;
    }
    public String getComment(){
        return comment;
    }
    public String getUser(){
        return user;
    }
    public void setComment(String comment){
        this.comment = comment;
    }
    public void setUser(String user){
        this.user = user;
    }
}
