package edu.neu.madcourse.raj__kukadia.ping;

/**
 * Created by Dharak on 4/12/2017.
 */

public class ContactUser {
    private String name;
    private String number;
    private String allowedNumber="1234567890";
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    ContactUser(String name, String number){
    this.name=name;
        try {

            this.number = numberFormat(number);
        }
        catch(Exception exe){
            return ;
        }

    }

    @Override
    public String toString() {
        return "ContactUser{" +
                "name='" + name + '\'' +
                ", number='" + number + '\'' +
                '}';
    }

    private String numberFormat(String number){
/*
try not to put number less than 10 this handles data for greater than 10
 */
        String newNumber="";
        for(char c:number.toCharArray()){
            if((0<=Character.getNumericValue(c))&(10>Character.getNumericValue(c))){
                newNumber+=c;
            }
        }
        //newNumber.contentEquals()
        return newNumber.substring(newNumber.length()-10,newNumber.length());
    }
}
