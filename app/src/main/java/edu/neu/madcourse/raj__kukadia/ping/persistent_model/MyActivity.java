package edu.neu.madcourse.raj__kukadia.ping.persistent_model;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Date;

public class MyActivity implements Comparable<MyActivity>
{

    public String activityname;
    public long activitytimestamp;

    public String getActivityname() {
        return activityname;
    }

    public void setActivityname(String activityname) {
        this.activityname = activityname;
    }

    public long getActivitytimestamp() {
        return activitytimestamp;
    }
    private long getCurrentTime(){
        Date d=new Date();
        return d.getTime();
    }
    public void setActivitytimestamp(long activitytimestamp) {
        this.activitytimestamp = activitytimestamp;
    }
    public MyActivity(){

    }

    @Override
    public int compareTo(@NonNull MyActivity o) {
        return (int) (o.getActivitytimestamp()-getActivitytimestamp());
    }

    public String getTimeString(){
        if(getActivitytimestamp()==0)return null;
        return formatTime(getActivitytimestamp(),getCurrentTime());
    }
    private @Nullable
    String formatTime(Long time, Long currentTime){
        /*
        Gives time hour ago two hour age
         */
        Long ans=currentTime-time;
        ans=ans/1000;
        if(ans<0)return null;

        String []arry={"s","m","h"};
        for(int i=0;i<arry.length;i++){
            Long div=ans/60;
            if(i==3){
                String value= convertFormatTime(ans%60,arry[i]);
                return value;
            }
            if(div<1){
                String value= convertFormatTime(ans%60,arry[i]);
                return value;
            }
            ans=ans/60;
        }
        return null;

    }
    public @Nullable String convertFormatTime(Long time,String st){
        if(time==0){
            return "now";
        }
        return String.valueOf(time)+st;
    }
}