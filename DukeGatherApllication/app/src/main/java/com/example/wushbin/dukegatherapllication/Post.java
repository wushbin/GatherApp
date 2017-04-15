package com.example.wushbin.dukegatherapllication;

import java.util.ArrayList;

/**
 * Created by wushbin on 3/8/17.
 */

public class Post {


    private String fromPlace;
    private String toPlace;
    private String leaveTime;
    private String leaveDate;
    private int numOfPeople;
    private  String userName;

    private String key;
    private ArrayList<User> Users;

    public Post(){
    }

    public Post( String fromPlace, String toPlace, String leaveTime, String leaveDate, int numOfPeople, String userName){
        this.fromPlace = fromPlace;
        this.toPlace = toPlace;
        this.leaveTime = leaveTime;
        this.leaveDate = leaveDate;
        this.numOfPeople = numOfPeople;
        this.userName = userName;
    }

    public String getFromPlace() {
        return  fromPlace;
    }
    public String getToPlace(){
        return  toPlace;
    }
    public String getLeaveTime(){
        return leaveTime;
    }
    public int getNumOfPeople(){
        return numOfPeople;
    }
    public String getLeaveDate(){return  leaveDate;}
    public String getUserName(){
        return userName;
    }
    public String getKey(){return  key;}
    public void setKey(String key){
     this.key = key;
    }
    public void setUsers(ArrayList<User> Users){
        this.Users = Users;
    }
    public int getCurrentNumofMembers(){
        if(Users == null){
            return 0;
        }
        return Users.size();
    }
    public ArrayList<User> getUsers(){
        return Users;
    }

}
