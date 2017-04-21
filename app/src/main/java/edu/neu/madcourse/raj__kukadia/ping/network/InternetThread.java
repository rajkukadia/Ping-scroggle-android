package edu.neu.madcourse.raj__kukadia.ping.network;

import edu.neu.madcourse.raj__kukadia.ping.applicatonlogic.myTasks;

/**
 * Created by Dharak on 4/11/2017.
 * class created for purpose of sole working with internet thread
 * update internet que
 *
 */

public class InternetThread implements Runnable  {
    MyInternetQue QueInterent;
    Thread thread;
    int DefaultTime=1000;
    int maxSleepTime=32000;
    int sleepTime=DefaultTime;

    private static  InternetThread instance;
    public static InternetThread getinstance(){
        if(instance==null){
            instance=new InternetThread();
            return instance;
        }
        else{
            return instance;
        }

    }
    private InternetThread(){

    }

    @Override
    public void run() {
        //QueInterent=myInternetQue.getInstance();
        while(QueInterent.size()>0){
            myTasks tasks=(myTasks) QueInterent.peek();
            boolean result=tasks.doTask();
            if(result){QueInterent.remove(tasks);
                resetSleep();
            }
            else{
                tasks.OnTaskfailed();
                try {
                    Thread.sleep(sleepTime());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //internet failure do something about it;
            }
        }
    }
    public void addTasks(myTasks tasks){
        if(QueInterent==null){
            QueInterent=MyInternetQue.getInstance();
            //QueInterent.add(tasks);
        }

        //add tasks to Internet Tread
        QueInterent.add(tasks);
    if(thread==null){
        thread=new Thread(this);
    }
    if(thread.isAlive()){
        //do nothing
    }
    else{
        //if thread is not active start it
        thread.start();
    }
    }
    private int sleepTime(){
        sleepTime*=2;
        if(sleepTime>=maxSleepTime){
            sleepTime=maxSleepTime;
        }
        return sleepTime;
    }
    private void resetSleep(){
        sleepTime=DefaultTime;
    }
}
