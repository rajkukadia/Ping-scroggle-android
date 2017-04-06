package edu.neu.madcourse.raj__kukadia.assignment7;

import com.google.firebase.database.IgnoreExtraProperties;


@IgnoreExtraProperties
public class GameInfo {

    public String gameState;
    public String gamePlaying;
    public String aggreed;
    public String largeTileOwnerList;
    public String userA;
    public String userB;


    public GameInfo(){
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public GameInfo(String gameState, String gamePlaying, String aggreed, String largeTileOwnerList, String userA, String userB){
        this.gameState = gameState;
        this.gamePlaying = gamePlaying;
        this.aggreed = aggreed;
        this.largeTileOwnerList = largeTileOwnerList;
        this.userA = userA;
        this.userB = userB;
    }

}
