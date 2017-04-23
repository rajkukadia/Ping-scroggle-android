package edu.neu.madcourse.raj__kukadia.ping.network;

import java.util.Queue;

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
    int maxSleepTime=10000;
    int sleepTime=DefaultTime;
    Boolean threadRunning=false;
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
        threadRunning=true;
        while(QueInterent.size()>0){
            myTasks tasks=(myTasks) QueInterent.peek();
            boolean result=tasks.doTask();
            if(result){QueInterent.remove(tasks);
                resetSleep();
            }
            else{
                if(sleepTime()>maxSleepTime){
                    QueInterent.remove(tasks);
                    tasks.OnTaskfailed();
                    resetSleep();
                }
                //tasks.OnTaskfailed();
                try {
                    Thread.sleep(sleepTime());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //internet failure do something about it;
            }
        }
        threadRunning=false;
    }
    public void addTasks(myTasks tasks){
        if(QueInterent==null){
            QueInterent=MyInternetQue.getInstance();
            //QueInterent.add(tasks);
        }

        //add tasks to Internet Tread
        QueInterent.add(tasks);


    if(threadRunning){
        //do nothing
    }
    else{
        thread=new Thread(this);
        //if thread is not active start it
        thread.start();
    }
    }
    private int sleepTime(){
        sleepTime*=2;
        return sleepTime;
    }
    private void resetSleep(){
        sleepTime=DefaultTime;
    }
}
