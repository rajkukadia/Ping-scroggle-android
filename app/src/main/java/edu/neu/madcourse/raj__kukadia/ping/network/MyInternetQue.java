package edu.neu.madcourse.raj__kukadia.ping.network;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

import edu.neu.madcourse.raj__kukadia.ping.applicatonlogic.myTasks;

/**
 * Created by Dharak on 4/11/2017.
 * This class is specifically created for internet
 * Every call will be handled here
 * Basically idea is create a queue which running handle all internet
 *
 * Just put <>
 *
 */

public class MyInternetQue implements Queue {
    private ArrayList<myTasks> quelist=new ArrayList();
    private static MyInternetQue instance;
    static MyInternetQue getInstance(){
        if(instance!=null){
            return instance;
        }
        else{
            instance= new MyInternetQue();
            return instance;
        }
    }
    private MyInternetQue(){

    }

    @Override
    public int size() {
        return quelist.size();
    }

    @Override
    public boolean isEmpty() {
        return quelist.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return quelist.contains(o);
    }

    @NonNull
    @Override
    public Iterator iterator() {
        return null;
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return quelist.toArray();
    }

    @NonNull
    @Override
    public Object[] toArray(@NonNull Object[] a) {
        return new Object[0];
    }

    @Override
    public boolean add(Object o) {
        return quelist.add((myTasks) o);
    }

    @Override
    public boolean remove(Object o) {
        return quelist.remove((myTasks) o);
    }

    @Override
    public boolean addAll(@NonNull Collection c) {
        return quelist.addAll(c);
    }

    @Override
    public void clear() {
    quelist.clear();
    }

    @Override
    public boolean retainAll(@NonNull Collection c) {
        quelist.clear();
        for(Object o:c){
            boolean result=quelist.add((myTasks) o);
            if(!result)return false;

        }
        return true;
    }

    @Override
    public boolean removeAll(@NonNull Collection c) {
        for(Object o:c){
            quelist.remove(o);
        }
        return true;
    }

    @Override
    public boolean containsAll(@NonNull Collection c) {
        for(Object o:c){
            if(!quelist.contains(o))return false;
        }
        return true;
    }

    @Override
    public boolean offer(Object o) {
        return false;
    }

    @Override
    public Object remove() {
        return quelist.remove(0);
    }

    @Override
    public Object poll() {

        return quelist.remove(0);
    }

    @Override
    public Object element() {
        return quelist.get(0);
    }

    @Override
    public Object peek() {
        return quelist.get(0);
    }
}
