package edu.neu.madcourse.raj__kukadia.ping;

/**
 * Created by Dharak on 4/12/2017.
 */

public class contactUser {
    private String name;
    private String number;

    contactUser(String name,String number){
    this.name=name;
    this.number=numberFormat(number);

    }
    private String numberFormat(String number){
/*
try not to put number less than 10 this handles data for greater than 10
 */
        return number.substring(number.length()-10,number.length());
    }
}
