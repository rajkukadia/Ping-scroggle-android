
package edu.neu.madcourse.raj__kukadia.assignment7;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IntegerRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import edu.neu.madcourse.raj__kukadia.DictionaryAssignment3;
import edu.neu.madcourse.raj__kukadia.MainActivity;
import edu.neu.madcourse.raj__kukadia.R;
import edu.neu.madcourse.raj__kukadia.Tile;
import edu.neu.madcourse.raj__kukadia.assignment5.ScroggleAssignment5;
import edu.neu.madcourse.raj__kukadia.assignment5.ScroggleStatusAssignment5;
import edu.neu.madcourse.raj__kukadia.assignment5.TileAssignment5;

import static android.content.Context.MODE_PRIVATE;


public class ScroggleMultiplayerFragment extends Fragment {
    static private int mLargeIds[] = {R.id.largescroggle1, R.id.largescroggle2, R.id.largescroggle3,
            R.id.largescroggle4, R.id.largescroggle5, R.id.largescroggle6, R.id.largescroggle7, R.id.largescroggle8,
            R.id.largescroggle9,};
    static private int mSmallIds[] = {R.id.smallscroggle1, R.id.smallscroggle2, R.id.smallscroggle3,
            R.id.smallscroggle4, R.id.smallscroggle5, R.id.smallscroggle6, R.id.smallscroggle7, R.id.smallscroggle8,
            R.id.smallscroggle9,};
    private Handler mHandler = new Handler();
    private Handler m1Handler = new Handler();

    private TileMultiplayer mEntireBoard = new TileMultiplayer(this);
    private TileMultiplayer mLargeTiles[] = new TileMultiplayer[9];
    private TileMultiplayer mSmallTiles[][] = new TileMultiplayer[9][9];
    private TileMultiplayer.Owner mPlayer = TileMultiplayer.Owner.CLICKED;
    private Set<TileMultiplayer> mAvailable = new HashSet<TileMultiplayer>();
    private int mSoundX, mSoundO, mSoundMiss, mSoundRewind;
    private SoundPool mSoundPool;
    private float mVolume = 1f;
    private int mLastLarge;
    private int mLastSmall;
    private String enteredStringSroggle="";
    private ImageButton doneView;
    private static Boolean done = false;
    private static Boolean donePhaseTwo = false;
    public static int touchedLargeTile =0;
    private boolean atLeastOneClicked = false;
    public static int [] touchedSmallTiles=new int[9];
    public static TextView e;
    private TextView v1;
    private boolean popup = false;
    private AlertDialog.Builder builder;
    private AlertDialog mDialog;
    private HashSet<Integer> DoneTiles = new HashSet<Integer>();
    private ArrayList<int[]> adjacencyList = new ArrayList<int[]>();
    private static Boolean comingFirstTime = true;
    int t = 90;
    private TextView v;
    private HashMap<String, Integer> score = new HashMap<String, Integer>();
    public static int currentScore = 0;
    private static boolean phaseTwo = false;
    public static int totalClicks = 0;
    private ImageButton pause;
    private Boolean muteClicked = false;
    private HashMap<Integer, String> wordsDetectedByUser = new HashMap<Integer, String>();
    private int counterForRepeatedWord = 0;
    private static boolean notValidWord = false;
    private static boolean canShowDialogBox = false;
    public static int hashKey = 0;
    private static ImageButton muteMusic;
    private boolean gameOver = false;
    //private StringBuilder e= new StringBuilder();
    private DatabaseReference mRootRef;
    private String user_one;
    private String user_two;
    public static String gameID;
    private String newGameData;
    private GameInfo gi;
    private FirebaseAuth mAuth;
    private Thread loadTheRemainingDictionary;
    private HashMap<Integer, String> LargeTileOwner = new HashMap<Integer, String>();
    private Set<TileMultiplayer> mAvailableForLargeTile= new HashSet<TileMultiplayer>();
    private boolean entryCheck;
    private boolean firstClick = true;
    private boolean secondClick = true;
    private Thread openDict ;
    private boolean repeatClick = false;
    private String userOne;
    private String userTwo;
    private Tile.Owner user;










    private String[] nineNineLetterWords = new String[9];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        openDict = new Thread(new myThread());
        openDict.start();


        // Retain this fragment across configuration changes.
        setRetainInstance(true);
firstClick = true;
        repeatClick = false;

        initGame();
        setAdjacencyList();
        mRootRef = FirebaseDatabase.getInstance().getReference();

        Bundle b = getActivity().getIntent().getExtras();
        gameID = b.getString("GameKey");

        if(b.getString("CallingActivity").equals(WaitingForOpponentActivity.class.toString())){
            userOne = b.getString("username");
            mRootRef.child("SynchronousGames").child(gameID).child("userA").setValue(userOne);
            user = Tile.Owner.X;
         //   gi.userA =userOne;

        }
        else
        if(b.getString("CallingActivity").equals(PreInitializingGameActivity.class.toString())){
            userTwo = b.getString("username");
            mRootRef.child("SynchronousGames").child(gameID).child("userB").setValue(userTwo);
            user = Tile.Owner.O;
         //   gi.userB = userTwo;


        }


        Log.d("gameID at frag", gameID);

        addAllTilesForLargeTiles();

        mAuth = FirebaseAuth.getInstance();



        mSoundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        mSoundX = mSoundPool.load(getActivity(), R.raw.sergenious_movex, 1);
        mSoundO = mSoundPool.load(getActivity(), R.raw.sergenious_moveo, 1);
        mSoundMiss = mSoundPool.load(getActivity(), R.raw.erkanozan_miss, 1);
        mSoundRewind = mSoundPool.load(getActivity(), R.raw.joanne_rewind, 1);


        loadTheRemainingDictionary = new Thread(new RemainingDictionary());
        loadTheRemainingDictionary.start();

//restartGame();

    }



            private void saveGameDataOnFireBase(){
/*
               mRootRef.child("SynchronousGames").child(gameID).addChildEventListener(new ChildEventListener() {
                   @Override
                   public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                       for(DataSnapshot d : dataSnapshot.getChildren()){
                           if(d.getKey().equals("userA")){
                               userOne = d.getValue().toString();
                           }
                           if(d.getKey().equals("userB")){
                               userTwo = d.getValue().toString();
                           }
                       }
                   }

                   @Override
                   public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                       for(DataSnapshot d : dataSnapshot.getChildren()){
                           if(d.getKey().equals("userA")){
                               userOne = d.getValue().toString();
                           }
                           if(d.getKey().equals("userB")){
                               userTwo = d.getValue().toString();
                           }
                       }
                   }


                   @Override
                   public void onChildRemoved(DataSnapshot dataSnapshot) {

                   }

                   @Override
                   public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                   }

                   @Override
                   public void onCancelled(DatabaseError databaseError) {

                   }
               });

*/
               // gi = new GameInfo(getState(), "yes", "yes", null, userOne, userTwo);
                mRootRef.child("SynchronousGames").child(gameID).child("gamePlaying").setValue("yes");

                mRootRef.child("SynchronousGames").child(gameID).child("gameState").setValue(getState());


            }

            private void putGameState(){
                mRootRef.child("SynchronousGames").addChildEventListener(
                        new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                gi = dataSnapshot.getValue(GameInfo.class);

                                putState(gi.gameState);


                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                                gi = dataSnapshot.getValue(GameInfo.class);
                                putState(gi.gameState);


                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {
                          //      mRootRef.child("active users").child(mAuth.getCurrentUser().getUid().toString()).child("opponent").removeValue();
if(getActivity()!=null) {
    builder = new AlertDialog.Builder(getActivity());
    builder.setMessage("Your opponent quitted the game!");
    builder.setCancelable(false);
    builder.setPositiveButton(R.string.ok_label,
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    getActivity().finish();
                }
            });
    mDialog = builder.show();

}


                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        }
                );
            }


            private void doTransactionForGameState(){
                mRootRef
                        .child("SynchronousGames")
                        .child(gameID)
                        .child("gameState")
                        .runTransaction(new Transaction.Handler() {
                            @Override
                            public Transaction.Result doTransaction(MutableData mutableData) {

                            gi.gameState = getState();
                                mutableData.setValue(getState());
                                return Transaction.success(mutableData);
                            }

                            @Override
                            public void onComplete(DatabaseError databaseError, boolean b,
                                                   DataSnapshot dataSnapshot) {

                            }
                        });
            }




/*
    private void gameStateChanger(){
        mRootRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d("Arrived.", "hereOnline");
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Log.d("Arrived.", "here2On");

                   if(child.getKey().equals("SynchronousGames")){

                       for(DataSnapshot finalChild : child.getChildren()){
                           if(finalChild.getKey().equals(userKey)){
                               Log.d("putting", "state");
                               putState(finalChild.getValue().toString());

                           }
                       }

                    //   putState(child.getValue().toString());
                   }

                }

            }




            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void triggerOtherPlayer(){


        mRootRef = FirebaseDatabase.getInstance().getReference();
        mRootRef.child("GameData").child("msgForOpponent").setValue("yes");

    }

*/

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        v = (TextView) getActivity().findViewById(R.id.counter_view);
        v1 = (TextView) getActivity().findViewById(R.id.score_view);
        e = (TextView) getActivity().findViewById(R.id.scroggle_text_view);
        muteMusic = (ImageButton) getActivity().findViewById((R.id.mute));
        muteMusic.setImageLevel(1);
        muteMusic.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                  muteClicked =!muteClicked;
                if(muteClicked==false){
                    muteMusic.setImageLevel(1);
                }else{
                    muteMusic.setImageLevel(0);
                }
                if(ScroggleMultiplayerActivity.mMediaPlayer.isPlaying()){
                ScroggleMultiplayerActivity.mMediaPlayer.pause();}
                else{
                    ScroggleMultiplayerActivity.mMediaPlayer.start();}
                }

        });

      //  pause = (ImageButton) getActivity().findViewById(R.id.pause);
      //  pause.setOnClickListener(new View.OnClickListener(){

                               //      @Override
                               //      public void onClick(View v) {
                               //    pausePressed();
                               //      }
                               //  });

        doneView= (ImageButton)getActivity().findViewById(R.id.done);
        doneView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                done = true;
              donePressed();
            }
        });
    }




    private void noVisibility(){
        for(int i = 0;i<9;i++){
            for(int j = 0; j<9;j++){
                TileMultiplayer tile = mSmallTiles[i][j];
                View v = tile.getView();
                v.setVisibility(View.INVISIBLE);
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        setAvailableAccordingToLargeTileOwner();



        if(muteClicked){
         ScroggleMultiplayerActivity.mMediaPlayer.pause();
            muteMusic.setImageLevel(0);
        }
        if(!gameOver){
       // getCounter();
            }
    }

    private void RunAnimation(TextView v)
    {
        Animation a = AnimationUtils.loadAnimation(getActivity(), R.anim.text_animator);
        a.reset();
        //TextView tv = (TextView) findViewById(R.id.firstTextView);
        v.clearAnimation();
        v.startAnimation(a);
    }


    private void getCounter(){

        mHandler = new Handler();
        mHandler.postDelayed(mRunnable, 1000);


    }

    private void beep() {
        try {
       //     ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
         //   toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 200);
        }catch (RuntimeException e){
           // e.printStackTrace();
        }
    }




    private Runnable mRunnable = new Runnable() {

        @Override
        public void run() {
            t--;
            v.setText("Time left: "+String.valueOf(t)+"  ");
            v1.setText("Score: "+String.valueOf(currentScore)+"  ");
            if(t<11){
                beep();

                try {
                    RunAnimation(v);
                }catch (NullPointerException e){

                }

            }
            //while(t!=0) {


            if(t==0){


                    v.setText("");

                        gameOver = true;
                        mHandler.removeCallbacks(mRunnable);
                        clearAvailable();
                       // Intent i = new Intent(getActivity(), ScroggleStatusAssignment5.class);
                       // getActivity().startActivity(i);


            }else {
                mHandler.postDelayed(mRunnable, 1000);
            }
                }


    };

    private void clearAvailableForLargeTile(){
        mAvailableForLargeTile.clear();
    }

    private void addAllTilesForLargeTiles(){
        for(int large = 0; large<9;large++){
            for(int small= 0; small<9; small++){
                TileMultiplayer tile = mSmallTiles[large][small];
mAvailableForLargeTile.add(tile);
            }
        }

    }

    private void addAvailableForLargeTile(TileMultiplayer tile){
        mAvailableForLargeTile.add(tile);
    }

    private boolean isAvailableForLargeTIle(TileMultiplayer tile){
        if(mAvailableForLargeTile.contains(tile)){
            return true;
        }else{
            return false;
        }
    }


    private void clearAvailable() {
        mAvailable.clear();
    }

    private void addAvailable(TileMultiplayer tile) {
//        tile.animate();
        mAvailable.add(tile);
    }



    private void setAdjacencyList(){

            int array[] = {2, 5, 6, 7, 8};
            adjacencyList.add(array);
            int array1[] = {6, 7, 8};
            adjacencyList.add(array1);

            int array2[] = {0, 3, 6, 7, 8};
            adjacencyList.add(array2);
            int array3[] = {2, 5, 8};
            adjacencyList.add(array3);
             int array4[] = {};
            adjacencyList.add(array4);
             int array5[] = {0, 3, 6};
            adjacencyList.add(array5);
             int array6[] = {0, 1, 2, 5, 8};
            adjacencyList.add(array6);
             int array7[] = {0, 1, 2};
            adjacencyList.add(array7);
        int array8[] = {0, 1, 2, 3, 6};
        adjacencyList.add(array8);

    }

    public boolean isAvailable(TileMultiplayer tile) {
        return mAvailable.contains(tile);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =
                inflater.inflate(R.layout.large_board_scroggle, container, false);

        initViews(rootView);
        loadScores();
       updateAllTiles();
        getCounter();
        saveGameDataOnFireBase();
        putGameState();


        return rootView;
    }


    private void loadScores(){

        score.put("A", 1);
        score.put("B", 3);
        score.put("C", 3);
        score.put("D", 2);
        score.put("E", 1);
        score.put("F", 4);
        score.put("G", 2);
        score.put("H", 4);
        score.put("I", 1);
        score.put("J", 8);
        score.put("K", 5);
        score.put("L", 1);
        score.put("M", 3);
        score.put("N", 1);
        score.put("O", 1);
        score.put("P", 3);
        score.put("Q", 10);
        score.put("R", 1);
        score.put("S", 1);
        score.put("T", 1);
        score.put("U", 3);
        score.put("V", 4);
        score.put("W", 4);
        score.put("X", 8);
        score.put("Y", 4);
        score.put("Z", 10);
    }

    private String chooseRandomWord(){
        char stringInMaking[] = new char[9];
        Random randomGenerator = new Random();
        int stopperNumber = randomGenerator.nextInt(60121);

        long stopper = MainActivity.nineWordsCopy.get(stopperNumber);

        long firstChar = stopper>>>40;
        stringInMaking[0] = MainActivity.reverseletterMap.get(firstChar);
        long secondCharProgresss = stopper<<24;
        long secondChar = secondCharProgresss>>>59;
        stringInMaking[1] = MainActivity.reverseletterMap.get(secondChar);
        long thirdCharProgress = stopper<<29;
        long thirdChar = thirdCharProgress>>>59;
        stringInMaking[2] = MainActivity.reverseletterMap.get(thirdChar);
        long fourthCharProgress = stopper<<34;
        long fourthChar = fourthCharProgress>>>59;
        stringInMaking[3] = MainActivity.reverseletterMap.get(fourthChar);
        long fifthCharProgress = stopper<<39;
        long fifthChar = fifthCharProgress>>>59;
        stringInMaking[4] = MainActivity.reverseletterMap.get(fifthChar);
        long sixthCharProgress = stopper<<44;
        long sixthChar = sixthCharProgress>>>59;
        stringInMaking[5] = MainActivity.reverseletterMap.get(sixthChar);
        long seventhCharProgress = stopper<<49;
        long seventhChar = seventhCharProgress>>>59;
        stringInMaking[6] = MainActivity.reverseletterMap.get(seventhChar);
        long eightCharProgress = stopper<<54;
        long eightChar = eightCharProgress>>>59;
        stringInMaking[7] = MainActivity.reverseletterMap.get(eightChar);
        long ninthCharProgress = stopper<<59;
        long ninthChar = ninthCharProgress>>>59;
        stringInMaking[8] = MainActivity.reverseletterMap.get(ninthChar);
        String returnValue = new String(stringInMaking);
        return returnValue;

    }


    private String[] generateRandomWords(){
        int i=0;

        String newRandomWord;
        newRandomWord = chooseRandomWord();
        do {

            nineNineLetterWords[i] = newRandomWord;
            newRandomWord = chooseRandomWord();
            if(!checkIfValidRandomWord(newRandomWord)){
                newRandomWord = chooseRandomWord();
                continue;
            }
            i++;
        }
        while (i<9);
        return nineNineLetterWords;
    }


    private void setLargeTileOwner(int largeTile){

        if(!LargeTileOwner.containsKey(largeTile)){
        LargeTileOwner.put(largeTile, mAuth.getCurrentUser().getDisplayName().toString());}else{
            repeatClick = true;
        }

    }


    private void initViews(View rootView) {

        mEntireBoard.setView(rootView);
        for (int large = 0; large < 9; large++) {
            View outer = rootView.findViewById(mLargeIds[large]);
            mLargeTiles[large].setView(outer);

            for (int small = 0; small < 9; small++) {
                Button inner = (Button) outer.findViewById
                        (mSmallIds[small]);
                final int fLarge = large;
                final int fSmall = small;
                final TileMultiplayer smallTile = mSmallTiles[large][small];
                smallTile.setView(inner);

               // smallTile.updateDrawableState('a'); //Update here...........................................


                // ...


                inner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        smallTile.animate();
                        totalClicks++;

                       setAvailableAccordingToLargeTileOwner();

                            entryCheck = isAvailable(smallTile) && (!gameOver) && isAvailableForLargeTIle(smallTile);

                        if (entryCheck) {
                           //(getActivity()).startThinking();
                            mSoundPool.play(mSoundX, mVolume, mVolume, 1, 0, 1f);
                            setLargeTileOwner(fLarge);

                            for(int i = 0;i<9;i++){
                                TileMultiplayer tile = mSmallTiles[fLarge][i];
                                if(user== Tile.Owner.X) {
                                    if(tile.getOwner()!= TileMultiplayer.Owner.CLICKED){
                                    tile.setOwner(TileMultiplayer.Owner.X);}
                                }else{
                                    if(tile.getOwner()!= TileMultiplayer.Owner.CLICKED){
                                    tile.setOwner(TileMultiplayer.Owner.O);}

                                }
                                tile.updateDrawableState('a', 0);
                            }

                            makeMove(fLarge, fSmall); //makes the move and sets available the corresponding tile


                            doTransactionForGameState();   //does getstate putstate
                            if(!repeatClick){
                           doTransactionForLargeTileOwner();}
                            repeatClick = false;

                            touchedLargeTile =fLarge;
                            touchedSmallTiles[fSmall] = fSmall+1;
                            getButtonText(smallTile);

                            //think();
                            setCorrectStates(fLarge);

                        } else {
                            mSoundPool.play(mSoundMiss, mVolume, mVolume, 1, 0, 1f);
                        }
                    }
                });
                // ...

            }
        }


    }


    private String getLargeTileOwnerString(){
        StringBuilder largeTileOwnerStringBuilder = new StringBuilder();
        for(int i = 0; i<9;i++) {

if(LargeTileOwner.containsKey(i)) {
    largeTileOwnerStringBuilder.append(i);
    largeTileOwnerStringBuilder.append(',');
    largeTileOwnerStringBuilder.append(LargeTileOwner.get(i));
    largeTileOwnerStringBuilder.append(',');
}
}
        return largeTileOwnerStringBuilder.toString();
    }

    private void doTransactionForLargeTileOwner(){


        mRootRef
                .child("SynchronousGames")
                .child(gameID)
                .child("largeTileOwnerList")
                .runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {

                        gi.largeTileOwnerList = getLargeTileOwnerString();
                        String currentData ="";
                        if(mutableData.getValue()!=null){
                         currentData  = mutableData.getValue().toString();}

                        mutableData.setValue(currentData+getLargeTileOwnerString());

                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean b,
                                           DataSnapshot dataSnapshot) {

                    }
                });
    }

    private void setCorrectStates(int large){
        for(int l = 0; l<9 ;l++){
            for(int small =0;small<9;small++){

                if(l!=large){

                   TileMultiplayer tile = mSmallTiles[l][small];
                    if(tile.getOwner().equals(TileMultiplayer.Owner.NOTCLICKED)){
                       // tile.setOwner(TileMultiplayer.Owner.FREEZED);

                     }

                }
                TileMultiplayer tile = mSmallTiles[large][small];
                if(LargeTileOwner.containsKey(large)){
                    tile.updateDrawableState('a', 0);

                }

            }
        }
    }

    private void setAvailableAccordingToLargeTileOwner(){

        //Log.d(gi.toString(), "check");

        mRootRef =  FirebaseDatabase.getInstance().getReference();

        DatabaseReference r = mRootRef.child("SynchronousGames").child(gameID);

        r.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                Log.d("comes here", "??");


                for(DataSnapshot d : dataSnapshot.getChildren()){
  //                  Log.d("comes here", "???");

    if(d.getKey().equals("largeTileOwnerList")){
       final String list = d.getValue().toString();

        String[] fields = list.split(",");
        //int index = 0;
        for(int index = 0; index<fields.length;index++){

            int tileNumber = Integer.parseInt(fields[index++]);
            String userName = fields[index];
            for(int largetiles = 0; largetiles<9;largetiles++) {
                for (int smaltiles = 0; smaltiles < 9; smaltiles++) {

                    TileMultiplayer tile = mSmallTiles[largetiles][smaltiles];
                    if (largetiles == tileNumber) {
                        if (userName.equals(mAuth.getCurrentUser().getDisplayName().toString())) {
                            //  tile1.setOwner(TileMultiplayer.Owner.CLICKED);
                            if (!mAvailableForLargeTile.contains(tile)) {
                                mAvailableForLargeTile.add(tile);
                            }
                            // tile1.updateDrawableState('a', 0);
                        } else {

                            if (mAvailableForLargeTile.contains(tile)) {
                                mAvailableForLargeTile.remove(tile);
                            }
                        }

                        tile.updateDrawableState('a', 0);

                    }
                    else{

                       // tile.setOwner(TileMultiplayer.Owner.NOTCLICKED);
                        if (!mAvailableForLargeTile.contains(tile)) {
                            mAvailableForLargeTile.add(tile);

                        }
                      //  mAvailable.add(tile);

                        tile.updateDrawableState('a', 0);

                    }
                }
            }
        }


    }                 }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





    }






    private void getButtonText(TileMultiplayer smallTile){
        Button temp = (Button) smallTile.mView;
            enteredStringSroggle += temp.getText();
    }


    private void donePressed() {

        //specially for first large tile touchedLargeTile = 0;

        checkUnPressed();

        try {
            DictionaryAssignment3.result.setText("");
            DictionaryAssignment3.mytext.setText(enteredStringSroggle);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (((enteredStringSroggle + "\n").equalsIgnoreCase(DictionaryAssignment3.result.getText().toString())) && (!wordsDetectedByUser.containsValue(enteredStringSroggle))) {
            //Entering text to the screen



            wordsDetectedByUser.put(hashKey, enteredStringSroggle);
            e.append(enteredStringSroggle + " ");
            hashKey++;

            //Clearing off redundant buttons
                for (int i = 0; i < 9; i++) {
                    TileMultiplayer tile = mSmallTiles[touchedLargeTile][i];
                    if (tile.getOwner() != TileMultiplayer.Owner.CLICKED) {
                        //  if (!phaseTwo) {
                        tile.updateDrawableState(' ', 1);
                        // }
                    } else {

                        switch (enteredStringSroggle.length()) {
                            case 9:
                                updateScore(((Button) mSmallTiles[touchedLargeTile][i].getView()).getText().toString(), 50);
                                break;
                            case 5:
                            case 6:
                                updateScore(((Button) mSmallTiles[touchedLargeTile][i].getView()).getText().toString(), 5);
                                break;
                            case 7:
                                updateScore(((Button) mSmallTiles[touchedLargeTile][i].getView()).getText().toString(), 20);
                                break;
                            case 8:
                                updateScore(((Button) mSmallTiles[touchedLargeTile][i].getView()).getText().toString(), 30);
                                break;

                            default:
                                updateScore(((Button) mSmallTiles[touchedLargeTile][i].getView()).getText().toString(), 1);

                        }
                    }

                }

                setAvailableFromLastMove(touchedLargeTile, 0);


                DoneTiles.add(touchedLargeTile);

                DictionaryAssignment3.result.setText("");


            }  else {

            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    TileMultiplayer tile = mSmallTiles[i][j];
                    if (tile.getOwner() == TileMultiplayer.Owner.CLICKED) {
                        if(!DoneTiles.contains(i)){popup = true;}
                        atLeastOneClicked = true;
                    }
                    if (atLeastOneClicked&&popup) break;
                }
            }
            if (atLeastOneClicked) {
                atLeastOneClicked = false;
                if(popup) {
                    popup = false;
                    e = (TextView) getActivity().findViewById(R.id.scroggle_text_view);
                    // e.a ppend(" ");
                    TileMultiplayer tile = mLargeTiles[touchedLargeTile];
                    builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Not a Valid Word !");
                    builder.setCancelable(false);
                    builder.setPositiveButton(R.string.ok_label,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                    mDialog = builder.show();

                    tile.animate();

                    for (int i = 0; i < 9; i++) {
                        TileMultiplayer tiles = mSmallTiles[touchedLargeTile][i];
                        tiles.setOwner(TileMultiplayer.Owner.NOTCLICKED);
                        tiles.updateDrawableState('a', 0);
                        addAvailable(tiles);}
                }



                DictionaryAssignment3.result.setText("");
                enteredStringSroggle = "";

            }

        }


            if (touchedLargeTile == 0) {
                for (int i = 0; i < 9; i++) {
                    TileMultiplayer tiles = mSmallTiles[touchedLargeTile][i];
                    if ((tiles.getOwner() == TileMultiplayer.Owner.NOTCLICKED)&&(((Button)tiles.getView()).getText().charAt(0)!=' ')) {

                        addAvailable(tiles);
                    }
                }

            }

    for (int x = 0; x < touchedSmallTiles.length; x++) {
        touchedSmallTiles[x] = 0;
    }
    touchedLargeTile = 0;
        DictionaryAssignment3.result.setText("");

        enteredStringSroggle = "";

doTransactionForGameState();
    }



    private void updateScore(String x, int bonus){

        currentScore += (score.get(x))*bonus;

    }
    private void checkUnPressed(){

        for(int j = 0; j<9; j++) {

            if (touchedSmallTiles[j]==0) {
                TileMultiplayer demo = mSmallTiles[touchedLargeTile][j];

                mAvailable.remove(demo);
                //demo.setOwner(T9leAssignment5.Owner.NOTCLICKED);
                demo.updateDrawableState('a', 0);
            }

        }


    }
    private Boolean checkIfValidRandomWord(String x){
        for(int i = 0;i<nineNineLetterWords.length;i++){
            if(nineNineLetterWords[i]==x) {return false;}
        }
        return true;
    }

    private void makeMove(int large, int small) {
        mLastLarge = large;
        mLastSmall = small;
        TileMultiplayer smallTile = mSmallTiles[large][small];
        TileMultiplayer largeTile = mLargeTiles[large];
        smallTile.setOwner(mPlayer);

        done = false;
        setAvailableFromLastMove(large, small); //changed from small to large
        smallTile.updateDrawableState('a', 0);



}


    public void initGame() {

        mEntireBoard = new TileMultiplayer(this);
        // Create all the tiles
        for (int large = 0; large < 9; large++) {
            mLargeTiles[large] = new TileMultiplayer(this);
            for (int small = 0; small < 9; small++) {
                mSmallTiles[large][small] = new TileMultiplayer(this);
            }
            mLargeTiles[large].setSubTiles(mSmallTiles[large]);
        }
        mEntireBoard.setSubTiles(mLargeTiles);

        // If the player moves first, set which spots are available
        mLastSmall = -1;
        mLastLarge = -1;
        setAvailableFromLastMove(mLastLarge, mLastSmall);
    }
//changed small to large
    private void setAvailableFromLastMove(int large, int smallx) {
        clearAvailable();
        // Make all the tiles at the destination available
        if (large != -1) {


            for (int i = 0; i < 9; i++) {
                for (int dest = 0; dest < 9; dest++) {

                        if (!done) {
                            if (i == large) {
                                TileMultiplayer tile = mSmallTiles[large][dest];
                                if ((tile.getOwner() == TileMultiplayer.Owner.NOTCLICKED))
                                    addAvailable(tile);

                                switch (smallx) {
                                    case 0:
                                        int a[] = adjacencyList.get(0);

                                        for (int x : a) {
                                            TileMultiplayer tile1 = mSmallTiles[large][x];
                                            //if(mAvailable.contains(tile1)) {
                                            mAvailable.remove(tile1);
                                            //}
                                        }
                                        break;
                                    case 1:
                                        int a1[] = adjacencyList.get(1);

                                        for (int x : a1) {
                                            TileMultiplayer tile2 = mSmallTiles[large][x];
                                            // if(mAvailable.contains(tile2)) {
                                            mAvailable.remove(tile2);
                                            //}
                                        }
                                        break;
                                    case 2:
                                        int a2[] = adjacencyList.get(2);
                                        for (int x : a2) {
                                            TileMultiplayer tile3 = mSmallTiles[large][x];
                                            // if(mAvailable.contains(tile3)) {
                                            mAvailable.remove(tile3);
                                            // }
                                        }
                                        break;
                                    case 3:
                                        int a3[] = adjacencyList.get(3);
                                        for (int x : a3) {
                                            TileMultiplayer tile4 = mSmallTiles[large][x];
                                            //if(mAvailable.contains(tile4)) {
                                            mAvailable.remove(tile4);
                                            // }
                                        }
                                        break;
                                    case 4:
                                        int a4[] = adjacencyList.get(4);
                                        for (int x : a4) {
                                            TileMultiplayer tile5 = mSmallTiles[large][x];
                                            //if(mAvailable.contains(tile5)) {
                                            mAvailable.remove(tile5);//}

                                        }
                                        break;
                                    case 5:
                                        int a5[] = adjacencyList.get(5);
                                        for (int x : a5) {
                                            TileMultiplayer tile6 = mSmallTiles[large][x];
                                            //if(mAvailable.contains(tile6)) {
                                            mAvailable.remove(tile6);//}

                                        }
                                        break;
                                    case 6:
                                        int a6[] = adjacencyList.get(6);
                                        for (int x : a6) {
                                            TileMultiplayer tile7 = mSmallTiles[large][x];
                                            //if(mAvailable.contains(tile7)) {
                                            mAvailable.remove(tile7);//}

                                        }
                                        break;
                                    case 7:
                                        int a7[] = adjacencyList.get(7);
                                        for (int x : a7) {
                                            TileMultiplayer tile8 = mSmallTiles[large][x];
                                            // if(mAvailable.contains(tile8)) {
                                            mAvailable.remove(tile8);//}

                                        }
                                        break;
                                    case 8:
                                        int a8[] = adjacencyList.get(8);
                                        for (int x : a8) {
                                            TileMultiplayer tile9 = mSmallTiles[large][x];
                                            //if(mAvailable.contains(tile9)) {
                                            mAvailable.remove(tile9);//}

                                        }
                                        break;
                                }

                            } else {
                                if (DoneTiles.contains(i)) {
                                    continue;
                                }


                                        TileMultiplayer tile = mSmallTiles[i][dest];
                                       // if(tile.getOwner().equals(TileMultiplayer.Owner.NOTCLICKED)){
                                        //     tile.setOwner(TileMultiplayer.Owner.FREEZED);

                                       // }

                               // TileMultiplayer tile = mSmallTiles[i][dest];
                               // tile.setOwner(TileMultiplayer.Owner.FREEZED);
                             //  mAvailable.add(tile);
                                tile.updateDrawableState('a', 0);
                            }
                        } else { //OnDOnePressed
                            if (DoneTiles.contains(i)) {
                                continue;
                            }

                            //  Log.d("Comes ", "Hereeee");
                            if (i != large) {//Correct answer
                                TileMultiplayer tile = mSmallTiles[i][dest];

                                if(tile.getOwner()== TileMultiplayer.Owner.CLICKED){
                                    continue;
                                }else {

                                    tile.setOwner(TileMultiplayer.Owner.NOTCLICKED);
                                    addAvailable(tile);
                                    tile.updateDrawableState('a', 0);
                                    //done =false;
                                }
                            }
                        }



                }
            }
        }
        // If there were none available, make all squares available
        if (mAvailable.isEmpty()&&large==-1) {
            setAllAvailable();
        }
    }



    private void setAllAvailable() {
        for (int large = 0; large < 9; large++) {
            for (int small = 0; small < 9; small++) {
                TileMultiplayer tile = mSmallTiles[large][small];
                if (tile.getOwner() == TileMultiplayer.Owner.NOTCLICKED)
                    addAvailable(tile);
            }
        }

    }

    private char[][] generateCharArrays(String [] randomStrings){
        char[][] randomStringsCharArray = new char[9][];
        for(int a = 0;a<9;a++) {
            randomStringsCharArray[a]= randomStrings[a].toCharArray();
        }
        return randomStringsCharArray;
    }

    private int choosePatternNumber(){

        Random r = new Random();
        int num = r.nextInt(8);
        return num;
    }

    private void fixSmallBoards(char [] finalSequenceOfCharacters, int finalPattern, int large){

        switch(finalPattern){

            case 0:
                mSmallTiles[large][0].updateDrawableState((char) (finalSequenceOfCharacters[0]-32), 1);
                mSmallTiles[large][1].updateDrawableState((char) (finalSequenceOfCharacters[1]-32), 1);
                mSmallTiles[large][2].updateDrawableState((char) (finalSequenceOfCharacters[8]-32), 1);
                mSmallTiles[large][3].updateDrawableState((char) (finalSequenceOfCharacters[3]-32), 1);
                mSmallTiles[large][4].updateDrawableState((char) (finalSequenceOfCharacters[2]-32), 1);
                mSmallTiles[large][5].updateDrawableState((char) (finalSequenceOfCharacters[7]-32), 1);
                mSmallTiles[large][6].updateDrawableState((char) (finalSequenceOfCharacters[4]-32), 1);
                mSmallTiles[large][7].updateDrawableState((char) (finalSequenceOfCharacters[5]-32), 1);
                mSmallTiles[large][8].updateDrawableState((char) (finalSequenceOfCharacters[6]-32), 1);
                break;
            case 1:
                mSmallTiles[large][0].updateDrawableState((char) (finalSequenceOfCharacters[0]-32), 1);
                mSmallTiles[large][1].updateDrawableState((char) (finalSequenceOfCharacters[1]-32), 1);
                mSmallTiles[large][2].updateDrawableState((char) (finalSequenceOfCharacters[3]-32), 1);
                mSmallTiles[large][3].updateDrawableState((char) (finalSequenceOfCharacters[8]-32), 1);
                mSmallTiles[large][4].updateDrawableState((char) (finalSequenceOfCharacters[2]-32), 1);
                mSmallTiles[large][5].updateDrawableState((char) (finalSequenceOfCharacters[4]-32), 1);
                mSmallTiles[large][6].updateDrawableState((char) (finalSequenceOfCharacters[7]-32), 1);
                mSmallTiles[large][7].updateDrawableState((char) (finalSequenceOfCharacters[6]-32), 1);
                mSmallTiles[large][8].updateDrawableState((char) (finalSequenceOfCharacters[5]-32), 1);
                break;

            case 2:
                mSmallTiles[large][0].updateDrawableState((char) (finalSequenceOfCharacters[2]-32), 1);
                mSmallTiles[large][1].updateDrawableState((char) (finalSequenceOfCharacters[3]-32), 1);
                mSmallTiles[large][2].updateDrawableState((char) (finalSequenceOfCharacters[4]-32), 1);
                mSmallTiles[large][3].updateDrawableState((char) (finalSequenceOfCharacters[1]-32), 1);
                mSmallTiles[large][4].updateDrawableState((char) (finalSequenceOfCharacters[6]-32), 1);
                mSmallTiles[large][5].updateDrawableState((char) (finalSequenceOfCharacters[5]-32), 1);
                mSmallTiles[large][6].updateDrawableState((char) (finalSequenceOfCharacters[0]-32), 1);
                mSmallTiles[large][7].updateDrawableState((char) (finalSequenceOfCharacters[7]-32), 1);
                mSmallTiles[large][8].updateDrawableState((char) (finalSequenceOfCharacters[8]-32), 1);
                break;

            case 3:
                mSmallTiles[large][0].updateDrawableState((char) (finalSequenceOfCharacters[7]-32), 1);
                mSmallTiles[large][1].updateDrawableState((char) (finalSequenceOfCharacters[8]-32), 1);
                mSmallTiles[large][2].updateDrawableState((char) (finalSequenceOfCharacters[1]-32), 1);
                mSmallTiles[large][3].updateDrawableState((char) (finalSequenceOfCharacters[6]-32), 1);
                mSmallTiles[large][4].updateDrawableState((char) (finalSequenceOfCharacters[0]-32), 1);
                mSmallTiles[large][5].updateDrawableState((char) (finalSequenceOfCharacters[2]-32), 1);
                mSmallTiles[large][6].updateDrawableState((char) (finalSequenceOfCharacters[5]-32), 1);
                mSmallTiles[large][7].updateDrawableState((char) (finalSequenceOfCharacters[4]-32), 1);
                mSmallTiles[large][8].updateDrawableState((char) (finalSequenceOfCharacters[3]-32), 1);
                break;

            case 4:
                mSmallTiles[large][0].updateDrawableState((char) (finalSequenceOfCharacters[3]-32), 1);
                mSmallTiles[large][1].updateDrawableState((char) (finalSequenceOfCharacters[1]-32), 1);
                mSmallTiles[large][2].updateDrawableState((char) (finalSequenceOfCharacters[0]-32), 1);
                mSmallTiles[large][3].updateDrawableState((char) (finalSequenceOfCharacters[4]-32), 1);
                mSmallTiles[large][4].updateDrawableState((char) (finalSequenceOfCharacters[2]-32), 1);
                mSmallTiles[large][5].updateDrawableState((char) (finalSequenceOfCharacters[8]-32), 1);
                mSmallTiles[large][6].updateDrawableState((char) (finalSequenceOfCharacters[5]-32), 1);
                mSmallTiles[large][7].updateDrawableState((char) (finalSequenceOfCharacters[6]-32), 1);
                mSmallTiles[large][8].updateDrawableState((char) (finalSequenceOfCharacters[7]-32), 1);
                break;

            case 5:
                mSmallTiles[large][0].updateDrawableState((char) (finalSequenceOfCharacters[0]-32), 1);
                mSmallTiles[large][1].updateDrawableState((char) (finalSequenceOfCharacters[1]-32), 1);
                mSmallTiles[large][2].updateDrawableState((char) (finalSequenceOfCharacters[2]-32), 1);
                mSmallTiles[large][3].updateDrawableState((char) (finalSequenceOfCharacters[4]-32), 1);
                mSmallTiles[large][4].updateDrawableState((char) (finalSequenceOfCharacters[3]-32), 1);
                mSmallTiles[large][5].updateDrawableState((char) (finalSequenceOfCharacters[8]-32), 1);
                mSmallTiles[large][6].updateDrawableState((char) (finalSequenceOfCharacters[5]-32), 1);
                mSmallTiles[large][7].updateDrawableState((char) (finalSequenceOfCharacters[6]-32), 1);
                mSmallTiles[large][8].updateDrawableState((char) (finalSequenceOfCharacters[7]-32), 1);
                break;

            case 6:

                mSmallTiles[large][0].updateDrawableState((char) (finalSequenceOfCharacters[6]-32), 1);
                mSmallTiles[large][1].updateDrawableState((char) (finalSequenceOfCharacters[5]-32), 1);
                mSmallTiles[large][2].updateDrawableState((char) (finalSequenceOfCharacters[0]-32), 1);
                mSmallTiles[large][3].updateDrawableState((char) (finalSequenceOfCharacters[7]-32), 1);
                mSmallTiles[large][4].updateDrawableState((char) (finalSequenceOfCharacters[4]-32), 1);
                mSmallTiles[large][5].updateDrawableState((char) (finalSequenceOfCharacters[1]-32), 1);
                mSmallTiles[large][6].updateDrawableState((char) (finalSequenceOfCharacters[8]-32), 1);
                mSmallTiles[large][7].updateDrawableState((char) (finalSequenceOfCharacters[3]-32), 1);
                mSmallTiles[large][8].updateDrawableState((char) (finalSequenceOfCharacters[2]-32), 1);
                break;

            case 7:

                mSmallTiles[large][0].updateDrawableState((char) (finalSequenceOfCharacters[4]-32), 1);
                mSmallTiles[large][1].updateDrawableState((char) (finalSequenceOfCharacters[3]-32), 1);
                mSmallTiles[large][2].updateDrawableState((char) (finalSequenceOfCharacters[2]-32), 1);
                mSmallTiles[large][3].updateDrawableState((char) (finalSequenceOfCharacters[5]-32), 1);
                mSmallTiles[large][4].updateDrawableState((char) (finalSequenceOfCharacters[0]-32), 1);
                mSmallTiles[large][5].updateDrawableState((char) (finalSequenceOfCharacters[1]-32), 1);
                mSmallTiles[large][6].updateDrawableState((char) (finalSequenceOfCharacters[6]-32), 1);
                mSmallTiles[large][7].updateDrawableState((char) (finalSequenceOfCharacters[7]-32), 1);
                mSmallTiles[large][8].updateDrawableState((char) (finalSequenceOfCharacters[8]-32), 1);
                break;

            case 8:

                mSmallTiles[large][0].updateDrawableState((char) (finalSequenceOfCharacters[8]-32), 1);
                mSmallTiles[large][1].updateDrawableState((char) (finalSequenceOfCharacters[7]-32), 1);
                mSmallTiles[large][2].updateDrawableState((char) (finalSequenceOfCharacters[6]-32), 1);
                mSmallTiles[large][3].updateDrawableState((char) (finalSequenceOfCharacters[0]-32), 1);
                mSmallTiles[large][4].updateDrawableState((char) (finalSequenceOfCharacters[2]-32), 1);
                mSmallTiles[large][5].updateDrawableState((char) (finalSequenceOfCharacters[5]-32), 1);
                mSmallTiles[large][6].updateDrawableState((char) (finalSequenceOfCharacters[1]-32), 1);
                mSmallTiles[large][7].updateDrawableState((char) (finalSequenceOfCharacters[3]-32), 1);
                mSmallTiles[large][8].updateDrawableState((char) (finalSequenceOfCharacters[4]-32), 1);
                break;
        }
    }


    private void updateAllTiles() {
        String[] randomStrings = generateRandomWords();
        char[][] randomStringsCharArray = generateCharArrays(randomStrings);
        mEntireBoard.updateDrawableState('a', 0);
        for (int large = 0; large < 9; large++) {
            int pattern = choosePatternNumber();

            fixSmallBoards(randomStringsCharArray[large], pattern, large);
             TileMultiplayer tile = mLargeTiles[large];
            tile.setOwner(TileMultiplayer.Owner.NOTCLICKED);
            DoneTiles.clear();

             tile.updateDrawableState('a', 0);
           //  for (int small = 0; small < 9; small++) {

            //mSmallTiles[large][small].updateDrawableState('a');
            //}

        }
    }

private void setAvailableAccordingToGamePhase(int smallx, int large, HashSet<Integer> DoneTiles){
    for(int i =0; i<9;i++){
        for(int j = 0; j<9;j++){
            TileMultiplayer tile = mSmallTiles[i][j];
            if((tile.getOwner()== TileMultiplayer.Owner.X)||(tile.getOwner()== TileMultiplayer.Owner.O)){
                if(!DoneTiles.contains(i)){
                    mAvailable.add(tile);}else
                {
                    mAvailable.remove(tile);

                }
                //  mAvailable.remove(tile);
            }


              if(i==large) {
                  switch (smallx) {
                      case 0:
                          int a[] = adjacencyList.get(0);

                          for (int x : a) {
                              TileMultiplayer tile1 = mSmallTiles[large][x];
                              //if(mAvailable.contains(tile1)) {
                              mAvailable.remove(tile1);
                              //}
                          }
                          break;
                      case 1:
                          int a1[] = adjacencyList.get(1);

                          for (int x : a1) {
                              TileMultiplayer tile2 = mSmallTiles[large][x];
                              // if(mAvailable.contains(tile2)) {
                              mAvailable.remove(tile2);
                              //}
                          }
                          break;
                      case 2:
                          int a2[] = adjacencyList.get(2);
                          for (int x : a2) {
                              TileMultiplayer tile3 = mSmallTiles[large][x];
                              // if(mAvailable.contains(tile3)) {
                              mAvailable.remove(tile3);
                              // }
                          }
                          break;
                      case 3:
                          int a3[] = adjacencyList.get(3);
                          for (int x : a3) {
                              TileMultiplayer tile4 = mSmallTiles[large][x];
                              //if(mAvailable.contains(tile4)) {
                              mAvailable.remove(tile4);
                              // }
                          }
                          break;
                      case 4:
                          int a4[] = adjacencyList.get(4);
                          for (int x : a4) {
                              TileMultiplayer tile5 = mSmallTiles[large][x];
                              //if(mAvailable.contains(tile5)) {
                              mAvailable.remove(tile5);//}

                          }
                          break;
                      case 5:
                          int a5[] = adjacencyList.get(5);
                          for (int x : a5) {
                              TileMultiplayer tile6 = mSmallTiles[large][x];
                              //if(mAvailable.contains(tile6)) {
                              mAvailable.remove(tile6);//}

                          }
                          break;
                      case 6:
                          int a6[] = adjacencyList.get(6);
                          for (int x : a6) {
                              TileMultiplayer tile7 = mSmallTiles[large][x];
                              //if(mAvailable.contains(tile7)) {
                              mAvailable.remove(tile7);//}


                          }
                          break;
                      case 7:
                          int a7[] = adjacencyList.get(7);
                          for (int x : a7) {
                              TileMultiplayer tile8 = mSmallTiles[large][x];
                              // if(mAvailable.contains(tile8)) {
                              mAvailable.remove(tile8);//}
                             // tile8.updateDrawableState(' ', 0)

                          }
                          break;
                      case 8:
                          int a8[] = adjacencyList.get(8);
                          for (int x : a8) {
                              TileMultiplayer tile9 = mSmallTiles[large][x];
                              //if(mAvailable.contains(tile9)) {
                              mAvailable.remove(tile9);//}
                          //    tile9.updateDrawableState(' ', 0)

                          }
                          break;
                  }

              }
                if(DoneTiles.size()==9){
                    mAvailable.clear();
                }







        }
    }
}


    /** Create a string containing the state of the game. */



    public String getState() {

        StringBuilder builder = new StringBuilder();

        builder.append(muteClicked);
        builder.append(',');
        builder.append(gameOver);
        builder.append(',');
        builder.append(wordsDetectedByUser.size());
        builder.append(',');
        for(int i =0;i<wordsDetectedByUser.size();i++)
        { builder.append(wordsDetectedByUser.get(i));
        builder.append(',');}
        builder.append(notValidWord);
        builder.append(',');

        Object a[] = DoneTiles.toArray();
        builder.append(a.length);
        builder.append(',');
        for(int i=0;i<a.length;i++) {
            builder.append(a[i].toString());
            builder.append(',');
        }
        builder.append(mLastLarge);
        builder.append(',');
        builder.append(mLastSmall);
        builder.append(',');
        for (int large = 0; large < 9; large++) {
            for (int small = 0; small < 9; small++) {
                builder.append(mSmallTiles[large][small].getOwner().name());
                builder.append(',');
                builder.append((((Button)mSmallTiles[large][small].getView()).getText()).toString());
                builder.append(',');
                //Log.d(DoneTiles);
            }
        }
                return builder.toString();
    }

    /** Restore the state of the game from the given string. */

    public void putState(String gameData) {
        try {
            String[] fields = gameData.split(",");

            int index = 0;

            muteClicked = Boolean.parseBoolean(fields[index++]);

            gameOver = Boolean.parseBoolean(fields[index++]);


            int size = Integer.parseInt(fields[index++]);
            e = (TextView) getActivity().findViewById(R.id.scroggle_text_view);

            e.setText("");

            for (int i = 0; i < size; i++) {

                wordsDetectedByUser.put(i, fields[index++]);

                e.append(wordsDetectedByUser.get(i) + " ");

            }
            notValidWord = Boolean.parseBoolean(fields[index++]);


            int length = Integer.parseInt((fields[index++]));
            int a[] = new int[length];
            for (int i = 0; i < length; i++) {
                a[i] = Integer.parseInt(fields[index++]);
                DoneTiles.add(a[i]);
            }

            mLastLarge = Integer.parseInt(fields[index++]);
            mLastSmall = Integer.parseInt(fields[index++]);
            for (int large = 0; large < 9; large++) {
                for (int small = 0; small < 9; small++) {
                    TileMultiplayer.Owner owner = TileMultiplayer.Owner.valueOf(fields[index++]);
                    mSmallTiles[large][small].setOwner(owner);
                    mSmallTiles[large][small].updateDrawableState(fields[index++].charAt(0), 1);

                }
            }


            setAvailableAccordingToGamePhase(mLastSmall, mLastLarge, DoneTiles);
           // updateTiles();
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }


    private void updateTiles(){

        for (int large = 0; large < 9; large++) {
            for (int small = 0; small < 9; small++) {
Log.d("Updating", "drawable state");
                TileMultiplayer tile = mSmallTiles[large][small];
                tile.updateDrawableState(' ', 0);




            }
        }
    }


    class RemainingDictionary implements Runnable {


        InputStream is6;
        InputStream is7;
        InputStream is8;
        InputStream is17;

        DataInputStream din6;
        DataInputStream din7;
        DataInputStream din17;

        BufferedReader br;
        InputStreamReader inReader;

        @Override
        public void run() {
            createStreams();

            setfifteenfile();
            setsixteentotwentyfile();
            settwentyonetotwentyfivefile();
            set25abovewordfile();
        }

        private void setfifteenfile(){
            for (int data = 0; data <11256; data++) {
                Long d = null;
                try {
                    d = din17.readLong();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                MainActivity.fifteenWords.put(d, d);

            }

            try {
                din17.close();
                is17.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }




        private void setsixteentotwentyfile() {

            for (int data = 1; data <= 14447; data++) {
                Long d = null;
                try {
                    d = din6.readLong();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                MainActivity.sixteentotwentyWords.put(d, d);


            }

            try {
                din6.close();
                is6.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        private void settwentyonetotwentyfivefile() {

            for (int data = 1; data <= 416; data++) {
                Long d = null;
                try {
                    d = din7.readLong();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                MainActivity.twentyonetotwentyfiveWords.put(d, d);


            }

            try {
                din7.close();
                is7.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        private void set25abovewordfile() {
            String line;
            for (int data = 1; data <= 23; data++) {
                try {
                    if ((line = br.readLine()) != null) {
                        MainActivity.stringWords.put(line, line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        private void createStreams() {
            is6 = getResources().openRawResource(R.raw.sixteentotwentywords);
            is7 = getResources().openRawResource(R.raw.twentyonetotwentyfivewords);
            is8 = getResources().openRawResource(R.raw.twentyfiveabovewords);
            is17 = getResources().openRawResource(R.raw.fifteenwords);


            inReader = new InputStreamReader(is8);
            br = new BufferedReader(inReader);

            din6 = new DataInputStream(is6);
            din7 = new DataInputStream(is7);
            din17 = new DataInputStream(is17);

        }

    }

    class myThread implements Runnable{


        @Override
        public void run() {
            Intent intent1 = new Intent(getActivity(), DictionaryAssignment3.class);
            intent1.putExtra("NewCallingActivity", ScroggleMultiplayerActivity.class.toString());
            startActivity(intent1);
        }
    }




}

