package com.example.wushbin.dukegatherapllication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    private String userUId;
    private String key;
    private ArrayList<User> Users;
    private boolean openStatus;
    public Post(){
        this.openStatus = true;
    }

    public Post( String fromPlace, String toPlace, String leaveTime, String leaveDate,
                 int numOfPeople){
        this.fromPlace = fromPlace;
        this.toPlace = toPlace;
        this.leaveTime = leaveTime;
        this.leaveDate = leaveDate;
        this.numOfPeople = numOfPeople;
        this.openStatus = true;
    }

    public Post( String fromPlace, String toPlace, String leaveTime, String leaveDate,
                 int numOfPeople, String userName,String userUId){
        this.fromPlace = fromPlace;
        this.toPlace = toPlace;
        this.leaveTime = leaveTime;
        this.leaveDate = leaveDate;
        this.numOfPeople = numOfPeople;
        this.userName = userName;
        this.userUId = userUId;
        this.openStatus = true;
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
    public void setOpenStatus(boolean openStatus){
        this.openStatus = openStatus;
    }
    public boolean getOpenStatus(){
        return openStatus;
    }


    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("fromPlace",fromPlace);
        result.put("toPlace",toPlace);
        result.put("leaveTime",leaveTime);
        result.put("leaveDate",leaveDate);
        result.put("numOfPeople",numOfPeople);
        result.put("openStatus",openStatus);
        return result;
    }

    public void setUserUId(String userUId){
        this.userUId = userUId;
    }
    public String getUserUId(){
        return userUId;
    }
}
