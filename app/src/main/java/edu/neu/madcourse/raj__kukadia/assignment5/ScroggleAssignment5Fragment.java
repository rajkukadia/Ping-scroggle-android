
package edu.neu.madcourse.raj__kukadia.assignment5;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import edu.neu.madcourse.raj__kukadia.DictionaryAssignment3;
import edu.neu.madcourse.raj__kukadia.MainActivity;
import edu.neu.madcourse.raj__kukadia.R;
import edu.neu.madcourse.raj__kukadia.assignment5.ScroggleStatusAssignment5;



public class ScroggleAssignment5Fragment extends Fragment {
    static private int mLargeIds[] = {R.id.largescroggle1, R.id.largescroggle2, R.id.largescroggle3,
            R.id.largescroggle4, R.id.largescroggle5, R.id.largescroggle6, R.id.largescroggle7, R.id.largescroggle8,
            R.id.largescroggle9,};
    static private int mSmallIds[] = {R.id.smallscroggle1, R.id.smallscroggle2, R.id.smallscroggle3,
            R.id.smallscroggle4, R.id.smallscroggle5, R.id.smallscroggle6, R.id.smallscroggle7, R.id.smallscroggle8,
            R.id.smallscroggle9,};
    private Handler mHandler = new Handler();
    private Handler m1Handler = new Handler();

    private TileAssignment5 mEntireBoard = new TileAssignment5(this);
    private TileAssignment5 mLargeTiles[] = new TileAssignment5[9];
    private TileAssignment5 mSmallTiles[][] = new TileAssignment5[9][9];
    private TileAssignment5.Owner mPlayer = TileAssignment5.Owner.CLICKED;
    private Set<TileAssignment5> mAvailable = new HashSet<TileAssignment5>();
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



    private String[] nineNineLetterWords = new String[9];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Retain this fragment across configuration changes.
        setRetainInstance(true);
        initGame();
        setAdjacencyList();




        mSoundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        mSoundX = mSoundPool.load(getActivity(), R.raw.sergenious_movex, 1);
        mSoundO = mSoundPool.load(getActivity(), R.raw.sergenious_moveo, 1);
        mSoundMiss = mSoundPool.load(getActivity(), R.raw.erkanozan_miss, 1);
        mSoundRewind = mSoundPool.load(getActivity(), R.raw.joanne_rewind, 1);
    }

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
                if(ScroggleAssignment5.mMediaPlayer.isPlaying()){
                ScroggleAssignment5.mMediaPlayer.pause();}
                else{
                    ScroggleAssignment5.mMediaPlayer.start();}
                }

        });

        pause = (ImageButton) getActivity().findViewById(R.id.pause);
        pause.setOnClickListener(new View.OnClickListener(){

                                     @Override
                                     public void onClick(View v) {
                                   pausePressed();
                                     }
                                 });

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
                TileAssignment5 tile = mSmallTiles[i][j];
                View v = tile.getView();
                v.setVisibility(View.INVISIBLE);
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if(muteClicked){
         ScroggleAssignment5.mMediaPlayer.pause();
            muteMusic.setImageLevel(0);
        }
        if(!gameOver){
       getCounter();
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

    private void pausePressed(){

        if(ScroggleAssignment5.mMediaPlayer.isPlaying()) {
            ScroggleAssignment5.mMediaPlayer.pause();
        }
        noVisibility();
        mHandler.removeCallbacks(mRunnable);
        builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Game Paused !");
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.resume_label,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(!gameOver){
                        mHandler.postDelayed(mRunnable, 1000);}
                        if((!ScroggleAssignment5.mMediaPlayer.isPlaying())&&!muteClicked){

                        ScroggleAssignment5.mMediaPlayer.start();}
                        for (int k = 0; k < 9; k++) {
                            for (int j = 0; j < 9; j++) {
                                TileAssignment5 tile = mSmallTiles[k][j];
                                View v = tile.getView();
                                v.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
        mDialog = builder.show();
        //make the large tile available again
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

/*
    private Runnable m1Runnable = new Runnable() {

        @Override
        public void run() {
          counterForRepeatedWord++;
            v.setText("Not using this again !");
            v1.setText("");

                if(counterForRepeatedWord==4){
                    counterForRepeatedWord=0;
                 m1Handler.removeCallbacks(m1Runnable);
                 mHandler.postDelayed(mRunnable, 1000);
                }else {

                    m1Handler.postDelayed(m1Runnable, 1000);
                }
            }

    };

*/
    private Runnable mRunnable = new Runnable() {

        @Override
        public void run() {
            t--;
            v.setText("Time left: "+String.valueOf(t)+"  ");
            v1.setText("Score: "+String.valueOf(currentScore)+"  ");
            if(t<11){
                beep();
            RunAnimation(v);}
            //while(t!=0) {


            if(t==0){
                if(!phaseTwo) {

                    v.setText("");
                    if(DoneTiles.size()!=0){
                        //for(int i = 0; i<3;i++){
                            v1.setText("Phase two begins..");
                            RunAnimation(v1);

                       // }
                    setPhasetwo();}else{
                        gameOver = true;
                        mHandler.removeCallbacks(mRunnable);
                        clearAvailable();
                        Intent i = new Intent(getActivity(), ScroggleStatusAssignment5.class);
                        getActivity().startActivity(i);
                    }
                }else{
                    gameOver = true;
                    mHandler.removeCallbacks(mRunnable);
                    //mEntireBoard.getView().setVisibility(View.INVISIBLE);
                  clearAvailable();
                    phaseTwo=false;
                    Intent i = new Intent(getActivity(), ScroggleStatusAssignment5.class);
                    getActivity().startActivity(i);
                }
            }else {
                mHandler.postDelayed(mRunnable, 1000);
            }
                }


    };
    private void clearAvailable() {
        mAvailable.clear();
    }

    private void addAvailable(TileAssignment5 tile) {
//        tile.animate();
        mAvailable.add(tile);
    }


    private void setPhasetwo(){
        t=90;
        atLeastOneClicked =false;
        getCounter();
      //
        phaseTwo = true;
        //done = false;
            for(int i = 0;i<9;i++){
                for(int j = 0;j<9;j++) {

                    if (DoneTiles.contains(i)) {
                        TileAssignment5 tile = mSmallTiles[i][j];
                        tile.setOwner(TileAssignment5.Owner.NOTCLICKED);
                        addAvailable(tile);
                        tile.updateDrawableState('a', 0);
                    }else{
                        TileAssignment5 tile = mSmallTiles[i][j];
                        tile.setOwner(TileAssignment5.Owner.NOTCLICKED);
                        //((Button)tile.getView()).setText("");
                        tile.updateDrawableState(' ', 1);
                    }

                    TileAssignment5 tile = mSmallTiles[i][j];
                    if(((Button)mSmallTiles[i][j].getView()).getText().toString().charAt(0)==' '){
                       // Log.d("Yes ", "it came");
                        if(mAvailable.contains(tile)){
                            mAvailable.remove(tile);
                        }

                    }
                    else{
                        if(!mAvailable.contains(tile)){
                        addAvailable(tile);}
                    }

                }
            }


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

    public boolean isAvailable(TileAssignment5 tile) {
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
                final TileAssignment5 smallTile = mSmallTiles[large][small];
                smallTile.setView(inner);

               // smallTile.updateDrawableState('a'); //Update here...........................................


                // ...


                inner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        smallTile.animate();
                        totalClicks++;

                        // ...

                        if (isAvailable(smallTile)&&(!gameOver)) {
                           //(getActivity()).startThinking();
                            mSoundPool.play(mSoundX, mVolume, mVolume, 1, 0, 1f);

                            makeMove(fLarge, fSmall); //makes the move and sets available the corresponding tile

                            touchedLargeTile =fLarge;
                            touchedSmallTiles[fSmall] = fSmall+1;
                            getButtonText(smallTile);


                            //think();
                        } else {
                            mSoundPool.play(mSoundMiss, mVolume, mVolume, 1, 0, 1f);
                        }
                    }
                });
                // ...

            }
        }


    }



    private void getButtonText(TileAssignment5 smallTile){
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



            //  if(!wordsDetectedByUser.containsValue(enteredStringSroggle)) {
            wordsDetectedByUser.put(hashKey, enteredStringSroggle);
            e.append(enteredStringSroggle + " ");
            hashKey++;
            //}else
            //{
            // Thread showWordAlreadyDetected = new Thread(new WordcantAccept());
            //notValidWord = true;
            //mHandler.removeCallbacks(mRunnable);
            // m1Handler.postDelayed(m1Runnable, 300);
            //  }
            if (!phaseTwo) {
                //Clearing off redundant buttons
                for (int i = 0; i < 9; i++) {
                    TileAssignment5 tile = mSmallTiles[touchedLargeTile][i];
                    if (tile.getOwner() != TileAssignment5.Owner.CLICKED) {
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


            } else {

                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        TileAssignment5 tile = mSmallTiles[i][j];
                        if (tile.getOwner() == TileAssignment5.Owner.CLICKED) {

                            switch (enteredStringSroggle.length()) {
                                case 9:
                                    updateScore(((Button) mSmallTiles[i][j].getView()).getText().toString(), 50);
                                    break;
                                case 5:
                                case 6:
                                    updateScore(((Button) mSmallTiles[i][j].getView()).getText().toString(), 5);
                                    break;
                                case 7:
                                    updateScore(((Button) mSmallTiles[i][j].getView()).getText().toString(), 20);
                                    break;
                                case 8:
                                    updateScore(((Button) mSmallTiles[i][j].getView()).getText().toString(), 30);
                                    break;

                                default:
                                    updateScore(((Button) mSmallTiles[i][j].getView()).getText().toString(), 1);

                            }
                            DictionaryAssignment3.result.setText("");

                        }
                    }
                }

            }
        } else {
            // canShowDialogBox= true;
            //    if (!notValidWord ) {
            //  canShowDialogBox =false;

            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    TileAssignment5 tile = mSmallTiles[i][j];
                    if (tile.getOwner() == TileAssignment5.Owner.CLICKED) {
                        if(!DoneTiles.contains(i)){popup = true;}
                        atLeastOneClicked = true;
                    }
                    if (atLeastOneClicked&&popup) break;
                }
            }
            if (atLeastOneClicked) {
                atLeastOneClicked = false;
                if(!phaseTwo){
                if(popup) {
                    popup = false;
                    e = (TextView) getActivity().findViewById(R.id.scroggle_text_view);
                    // e.a ppend(" ");
                    TileAssignment5 tile = mLargeTiles[touchedLargeTile];
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

                    // for(int i =0;i<3;i++){
                    tile.animate();

                    for (int i = 0; i < 9; i++) {
                        TileAssignment5 tiles = mSmallTiles[touchedLargeTile][i];
                        tiles.setOwner(TileAssignment5.Owner.NOTCLICKED);
                        tiles.updateDrawableState('a', 0);
                        addAvailable(tiles);}
                }}
                else{
                    e = (TextView) getActivity().findViewById(R.id.scroggle_text_view);
                    // e.a ppend(" ");
                    TileAssignment5 tile = mLargeTiles[touchedLargeTile];
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

                    // for(int i =0;i<3;i++){
                   // tile.animate();
                    for (int i = 0; i < 9; i++) {
                        for (int j = 0; j < 9; j++) {
                            TileAssignment5 tiles = mSmallTiles[i][j];
                            if (tiles.getOwner() == TileAssignment5.Owner.CLICKED) {
                                tiles.setOwner(TileAssignment5.Owner.NOTCLICKED);
                            }
                            tiles.updateDrawableState('a', 0);


                        }
                    }

                }

                    //try {
                    //Thread.sleep(500);
                    //} catch (InterruptedException e1) {
                    //  e1.printStackTrace();
                    //}}


                /*else {
                    for (int i = 0; i < 9; i++) {
                        for (int j = 0; j < 9; j++) {
                            TieAssignment5 tiles = mSmallTiles[i][j];
                            if (tiles.getOwner() == ileAssignment5.Owner.CLICKED) {
                                tiles.setOwner(ileAssignment5.Owner.NOTCLICKED);
                            }
                            tiles.updateDrawableState('a', 0);


                        }
                    }
                }
*/

                DictionaryAssignment3.result.setText("");
                enteredStringSroggle = "";
                // done = false;
                // for (int x = 0; x < touchedSmallTiles.length; x++) {
                //   touchedSmallTiles[x] = 0;
                //}
                //touchedLargeTile = 0;

                //   }
            }

        }
        if(phaseTwo){

            for(int i = 0; i<9 ; i++) {
                for (int dest = 0; dest < 9; dest++) {
                    TileAssignment5 tile = mSmallTiles[i][dest];
                    if (tile.getOwner() == TileAssignment5.Owner.CLICKED) {
                        tile.setOwner(TileAssignment5.Owner.NOTCLICKED);
                        if (mAvailable.contains(tile)) {
                            mAvailable.remove(tile);
                        }
                        tile.updateDrawableState(' ', 1);
                    } else {
                        if(((Button)mSmallTiles[i][dest].getView()).getText().charAt(0)==' '){
                            mAvailable.remove(tile);
                        }else{
                        addAvailable(tile);}
                        tile.setOwner(TileAssignment5.Owner.NOTCLICKED);
                        tile.updateDrawableState('a', 0);
                    }
                }
            }

        }

        //if same word is entered again
   /*     if(notValidWord){
            notValidWord = false;
            for(int i = 0; i<9 ; i++) {
                for (int dest = 0; dest < 9; dest++) {
                    ileAssignment5 tile = mSmallTiles[i][dest];
                    if((tile.getOwner()== ileAssignment5.Owner.CLICKED)||(tile.getOwner()== ileAssignment5.Owner.FREEZED)){
                        tile.setOwner(ileAssignment5.Owner.NOTCLICKED);
                        tile.updateDrawableState('a', 0);
                    }
                    if(((Button)tile.getView()).getText().charAt(0)==' '){
                        mAvailable.remove(tile);
                    }else {
                        addAvailable(tile);
                    }
                }
            }
       }*/
    // done = false;

        if (!phaseTwo) {
            if (touchedLargeTile == 0) {
                for (int i = 0; i < 9; i++) {
                    TileAssignment5 tiles = mSmallTiles[touchedLargeTile][i];
                    if ((tiles.getOwner() == TileAssignment5.Owner.NOTCLICKED)&&(((Button)tiles.getView()).getText().charAt(0)!=' ')) {

                        addAvailable(tiles);
                    }
                }

            }
        }
    for (int x = 0; x < touchedSmallTiles.length; x++) {
        touchedSmallTiles[x] = 0;
    }
    touchedLargeTile = 0;
        DictionaryAssignment3.result.setText("");

        enteredStringSroggle = "";
    }



    private void updateScore(String x, int bonus){

        currentScore += (score.get(x))*bonus;

    }
    private void checkUnPressed(){

        for(int j = 0; j<9; j++) {

            if (touchedSmallTiles[j]==0) {
                TileAssignment5 demo = mSmallTiles[touchedLargeTile][j];

                mAvailable.remove(demo);
                //demo.setOwner(T9leAssignment5.Owner.NOTCLICKED);
                demo.updateDrawableState('a', 0);
            }

        }
/*
            Log.d(String.valueOf(touchedLargeTile), " Large tile");
        for(int x:touchedSmallTiles){
            Log.d(String.valueOf(x), " Small Tiles");
        }
*/

    }

    private Boolean checkIfValidRandomWord(String x){
        for(int i = 0;i<nineNineLetterWords.length;i++){
            if(nineNineLetterWords[i]==x) {return false;}
        }
        return true;
    }

    private void think() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getActivity() == null) return;
                if (mEntireBoard.getOwner() == TileAssignment5.Owner.CLICKED) {
                    int move[] = new int[2];
                    pickMove(move);
                    if (move[0] != -1 && move[1] != -1) {
                        switchTurns();
                        mSoundPool.play(mSoundO, mVolume, mVolume,
                                1, 0, 1f);
                        makeMove(move[0], move[1]);
                        switchTurns();
                    }
                }
                ((ScroggleAssignment5) getActivity()).stopThinking();
            }
        }, 1000);
    }

    private void pickMove(int move[]) {
        TileAssignment5.Owner opponent = mPlayer == TileAssignment5.Owner.X ? TileAssignment5.Owner.O : TileAssignment5
                .Owner.X;
        int bestLarge = -1;
        int bestSmall = -1;
        int bestValue = Integer.MAX_VALUE;
        for (int large = 0; large < 9; large++) {
            for (int small = 0; small < 9; small++) {
                TileAssignment5 smallTile = mSmallTiles[large][small];
                if (isAvailable(smallTile)) {
                    // Try the move and get the score
                    TileAssignment5 newBoard = mEntireBoard.deepCopy();
                    newBoard.getSubTiles()[large].getSubTiles()[small]
                            .setOwner(opponent);
                    int value = newBoard.evaluate();
                    Log.d("UT3",
                            "Moving to " + large + ", " + small + " gives value " +
                                    "" + value
                    );
                    if (value < bestValue) {
                        bestLarge = large;
                        bestSmall = small;
                        bestValue = value;
                    }
                }
            }
        }
        move[0] = bestLarge;
        move[1] = bestSmall;
        Log.d("UT3", "Best move is " + bestLarge + ", " + bestSmall);
    }

    private void switchTurns() {
        mPlayer = mPlayer == TileAssignment5.Owner.X ? TileAssignment5.Owner.O : TileAssignment5
                .Owner.X;
    }

    private void makeMove(int large, int small) {
        mLastLarge = large;
        mLastSmall = small;
        TileAssignment5 smallTile = mSmallTiles[large][small];
        TileAssignment5 largeTile = mLargeTiles[large];
        smallTile.setOwner(mPlayer);
        //setAvailableFromLastMove(small);
        if(!phaseTwo){
        done = false;}
        setAvailableFromLastMove(large, small); //changed from small to large
        smallTile.updateDrawableState('a', 0);

        /*
        TileAsignment5.Owner oldWinner = largeTile.getOwner();
        ileAssignment5.Owner winner = largeTile.findWinner();
        if (winner != oldWinner) {
            largeTile.animate();
            largeTile.setOwner(winner);
        }
        winner = mEntireBoard.findWinner();
        mEntireBoard.setOwner(winner);
        updateAllTiles();
        if (winner != ileAssignment5.Owner.NOTCLICKED) {
            (getActivity()).reportWinner(winner);
        }
        */
    }

    public void restartGame() {
        mSoundPool.play(mSoundRewind, mVolume, mVolume, 1, 0, 1f);
        // ...
        mHandler.removeCallbacks(mRunnable);
        //mHandler.postDelayed(mRunnable, 1000);
        initGame();
        gameOver = false;
        done = false;
        //if(e!=null){
        e.setText("");
        //}
        popup =false;
        atLeastOneClicked =false;
        currentScore =0;
        initViews(getView());
        t=90;
        enteredStringSroggle="";
        notValidWord=false;
      //  canShowDialogBox =false;
        phaseTwo=false;
        hashKey=0;
        wordsDetectedByUser.clear();
        mHandler.postDelayed(mRunnable, 1000);

        updateAllTiles();
    }

    public void initGame() {
      //
        //
        // phaseTwo = false;
        mEntireBoard = new TileAssignment5(this);
        // Create all the tiles
        for (int large = 0; large < 9; large++) {
            mLargeTiles[large] = new TileAssignment5(this);
            for (int small = 0; small < 9; small++) {
                mSmallTiles[large][small] = new TileAssignment5(this);
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
                    if (!phaseTwo) {
                        if (!done) {
                            if (i == large) {
                                TileAssignment5 tile = mSmallTiles[large][dest];
                                if ((tile.getOwner() == TileAssignment5.Owner.NOTCLICKED))
                                    addAvailable(tile);

                                switch (smallx) {
                                    case 0:
                                        int a[] = adjacencyList.get(0);

                                        for (int x : a) {
                                            TileAssignment5 tile1 = mSmallTiles[large][x];
                                            //if(mAvailable.contains(tile1)) {
                                            mAvailable.remove(tile1);
                                            //}
                                        }
                                        break;
                                    case 1:
                                        int a1[] = adjacencyList.get(1);

                                        for (int x : a1) {
                                            TileAssignment5 tile2 = mSmallTiles[large][x];
                                            // if(mAvailable.contains(tile2)) {
                                            mAvailable.remove(tile2);
                                            //}
                                        }
                                        break;
                                    case 2:
                                        int a2[] = adjacencyList.get(2);
                                        for (int x : a2) {
                                            TileAssignment5 tile3 = mSmallTiles[large][x];
                                            // if(mAvailable.contains(tile3)) {
                                            mAvailable.remove(tile3);
                                            // }
                                        }
                                        break;
                                    case 3:
                                        int a3[] = adjacencyList.get(3);
                                        for (int x : a3) {
                                            TileAssignment5 tile4 = mSmallTiles[large][x];
                                            //if(mAvailable.contains(tile4)) {
                                            mAvailable.remove(tile4);
                                            // }
                                        }
                                        break;
                                    case 4:
                                        int a4[] = adjacencyList.get(4);
                                        for (int x : a4) {
                                            TileAssignment5 tile5 = mSmallTiles[large][x];
                                            //if(mAvailable.contains(tile5)) {
                                            mAvailable.remove(tile5);//}

                                        }
                                        break;
                                    case 5:
                                        int a5[] = adjacencyList.get(5);
                                        for (int x : a5) {
                                            TileAssignment5 tile6 = mSmallTiles[large][x];
                                            //if(mAvailable.contains(tile6)) {
                                            mAvailable.remove(tile6);//}

                                        }
                                        break;
                                    case 6:
                                        int a6[] = adjacencyList.get(6);
                                        for (int x : a6) {
                                            TileAssignment5 tile7 = mSmallTiles[large][x];
                                            //if(mAvailable.contains(tile7)) {
                                            mAvailable.remove(tile7);//}

                                        }
                                        break;
                                    case 7:
                                        int a7[] = adjacencyList.get(7);
                                        for (int x : a7) {
                                            TileAssignment5 tile8 = mSmallTiles[large][x];
                                            // if(mAvailable.contains(tile8)) {
                                            mAvailable.remove(tile8);//}

                                        }
                                        break;
                                    case 8:
                                        int a8[] = adjacencyList.get(8);
                                        for (int x : a8) {
                                            TileAssignment5 tile9 = mSmallTiles[large][x];
                                            //if(mAvailable.contains(tile9)) {
                                            mAvailable.remove(tile9);//}

                                        }
                                        break;
                                }

                            } else {
                                if (DoneTiles.contains(i)) {
                                    continue;
                                }
                                TileAssignment5 tile = mSmallTiles[i][dest];
                                tile.setOwner(TileAssignment5.Owner.FREEZED);
                                tile.updateDrawableState('a', 0);
                            }
                        } else { //OnDOnePressed
                            if (DoneTiles.contains(i)) {
                                continue;
                            }

                            //  Log.d("Comes ", "Hereeee");
                            if (i != large) {//Correct answer
                                TileAssignment5 tile = mSmallTiles[i][dest];
                                tile.setOwner(TileAssignment5.Owner.NOTCLICKED);
                                addAvailable(tile);
                                tile.updateDrawableState('a', 0);
                                //done =false;
                            }
                        }


                    }else {
/*
                        ileAssignment5 thistile = mSmallTiles[i][dest];
                        if(((Button)thistile.getView()).getText().charAt(0)==' '){
                            mAvailable.remove(thistile);
                            thistile.updateDrawableState('a', 0);
                        }
*/


                        if (i == large) {
                                if (dest == smallx) {
                                    TileAssignment5 tile1 = mSmallTiles[large][dest];
                                    tile1.setOwner(TileAssignment5.Owner.CLICKED);
                                    if (mAvailable.contains(tile1)) {
                                        mAvailable.remove(tile1);
                                    }
                                    tile1.updateDrawableState('a', 0);

                                } else {
                                    TileAssignment5 tile2 = mSmallTiles[large][dest];
                                    if (!(tile2.getOwner() == TileAssignment5.Owner.CLICKED)) {

                                        tile2.setOwner(TileAssignment5.Owner.FREEZED);
                                    }
                                    if (mAvailable.contains(tile2)) {
                                        mAvailable.remove(tile2);
                                    }
                                    tile2.updateDrawableState('a', 0);
                            }


                            } else {


                                TileAssignment5 tile3 = mSmallTiles[i][dest];
                                if (!(tile3.getOwner() == TileAssignment5.Owner.CLICKED)) {
                                    tile3.setOwner(TileAssignment5.Owner.NOTCLICKED);
                                }
                          //  if(((((Button)mSmallTiles[i][dest].getView()).getText().toString().equals(null))||((Button)mSmallTiles[i][dest].getView()).getText().toString().charAt(0)==' ')||(((Button)mSmallTiles[i][dest].getView()).getText().toString().equals(""))){

                                if ((!mAvailable.contains(tile3))&&(tile3.getView().toString().charAt(0)!=' ')){
                                    mAvailable.add(tile3);
                                }


                                tile3.updateDrawableState('a', 0);



                            TileAssignment5 tile = mSmallTiles[i][dest];
                            if(((Button)mSmallTiles[i][dest].getView()).getText().toString().charAt(0)==' '){
                                // Log.d("Yes ", "it came");
                                if(mAvailable.contains(tile)){
                                    mAvailable.remove(tile);
                                }

                            }
                            else{
                                if(!mAvailable.contains(tile)){
                                    addAvailable(tile);}
                            }









                            /*






                            ileAssignment5 tile = mSmallTiles[i][dest];
                            ileAssignment5 tile = mSmallTiles[i][dest];
                            try{
                                if(((((Button)mSmallTiles[i][dest].getView()).getText().toString().equals(null))||((Button)mSmallTiles[i][dest].getView()).getText().toString().charAt(0)==' ')||(((Button)mSmallTiles[i][dest].getView()).getText().toString().equals(""))){
                                    // Log.d("Yes ", "it came");
                                    if(mAvailable.contains(tile)){
                                        mAvailable.remove(tile);
                                    }
                                }
                                else{
                                    if(!mAvailable.contains(tile)){
                                        addAvailable(tile);}
                                }}catch (ArrayIndexOutOfBoundsException e){


                            }catch ( StringIndexOutOfBoundsException e){

                            }

*/
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

    private void setPhaseTwoLogic() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                TileAssignment5 tile = mSmallTiles[i][j];
                if (((Button) mSmallTiles[i][j].getView()).getText().toString().charAt(0) == ' ') {
                    // Log.d("Yes ", "it came");
                    if (mAvailable.contains(tile)) {
                        mAvailable.remove(tile);
                    }

                } else {
                    if (!mAvailable.contains(tile)) {
                        addAvailable(tile);
                    }

                }
            }
        }
    }

    private void setAllAvailable() {
        for (int large = 0; large < 9; large++) {
            for (int small = 0; small < 9; small++) {
                TileAssignment5 tile = mSmallTiles[large][small];
                if (tile.getOwner() == TileAssignment5.Owner.NOTCLICKED)
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
             TileAssignment5 tile = mLargeTiles[large];
            tile.setOwner(TileAssignment5.Owner.NOTCLICKED);
            DoneTiles.clear();

             tile.updateDrawableState('a', 0);
           //  for (int small = 0; small < 9; small++) {

            //mSmallTiles[large][small].updateDrawableState('a');
            //}

        }
    }

private void setAvailableAccordingToGamePhase(boolean phaseTwo, int smallx, int large, HashSet<Integer> DoneTiles){
    for(int i =0; i<9;i++){
        for(int j = 0; j<9;j++){
            TileAssignment5 tile = mSmallTiles[i][j];

            if(phaseTwo){

                if(((Button)tile.getView()).getText().charAt(0)==' '){
                     mAvailable.remove(tile);

                }
                if(tile.getOwner()==TileAssignment5.Owner.FREEZED){
                   mAvailable.remove(tile);
                }



    }
    else{

        if(tile.getOwner()== TileAssignment5.Owner.FREEZED){
            mAvailable.remove(tile);
        }

              if(i==large) {
                  switch (smallx) {
                      case 0:
                          int a[] = adjacencyList.get(0);

                          for (int x : a) {
                              TileAssignment5 tile1 = mSmallTiles[large][x];
                              //if(mAvailable.contains(tile1)) {
                              mAvailable.remove(tile1);
                              //}
                          }
                          break;
                      case 1:
                          int a1[] = adjacencyList.get(1);

                          for (int x : a1) {
                              TileAssignment5 tile2 = mSmallTiles[large][x];
                              // if(mAvailable.contains(tile2)) {
                              mAvailable.remove(tile2);
                              //}
                          }
                          break;
                      case 2:
                          int a2[] = adjacencyList.get(2);
                          for (int x : a2) {
                              TileAssignment5 tile3 = mSmallTiles[large][x];
                              // if(mAvailable.contains(tile3)) {
                              mAvailable.remove(tile3);
                              // }
                          }
                          break;
                      case 3:
                          int a3[] = adjacencyList.get(3);
                          for (int x : a3) {
                              TileAssignment5 tile4 = mSmallTiles[large][x];
                              //if(mAvailable.contains(tile4)) {
                              mAvailable.remove(tile4);
                              // }
                          }
                          break;
                      case 4:
                          int a4[] = adjacencyList.get(4);
                          for (int x : a4) {
                              TileAssignment5 tile5 = mSmallTiles[large][x];
                              //if(mAvailable.contains(tile5)) {
                              mAvailable.remove(tile5);//}

                          }
                          break;
                      case 5:
                          int a5[] = adjacencyList.get(5);
                          for (int x : a5) {
                              TileAssignment5 tile6 = mSmallTiles[large][x];
                              //if(mAvailable.contains(tile6)) {
                              mAvailable.remove(tile6);//}

                          }
                          break;
                      case 6:
                          int a6[] = adjacencyList.get(6);
                          for (int x : a6) {
                              TileAssignment5 tile7 = mSmallTiles[large][x];
                              //if(mAvailable.contains(tile7)) {
                              mAvailable.remove(tile7);//}

                          }
                          break;
                      case 7:
                          int a7[] = adjacencyList.get(7);
                          for (int x : a7) {
                              TileAssignment5 tile8 = mSmallTiles[large][x];
                              // if(mAvailable.contains(tile8)) {
                              mAvailable.remove(tile8);//}

                          }
                          break;
                      case 8:
                          int a8[] = adjacencyList.get(8);
                          for (int x : a8) {
                              TileAssignment5 tile9 = mSmallTiles[large][x];
                              //if(mAvailable.contains(tile9)) {
                              mAvailable.remove(tile9);//}

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
}


    /** Create a string containing the state of the game. */
    public String getState() {
        StringBuilder builder = new StringBuilder();
        //builder.append(muteMusic.getBackground().getLevel());
        //builder.append(',');
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
     //   m1Handler.removeCallbacks(m1Runnable);
        mHandler.removeCallbacks(mRunnable);
        builder.append(phaseTwo);
        builder.append(',');
        builder.append(currentScore); //storing current score
        builder.append(',');
        builder.append(t); //storing timer state
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
        String[] fields = gameData.split(",");
        //setPhaseTwoLogic();
        int index = 0;
       // Object n = (Object)fields[index++];
        //e=(TextView)n;

        //int availabletilessize = Integer.parseInt((fields[index++]));
        //for(int i =0;i<availabletilessize;i++){
          //  mAvailable.add(fields[index++]);
        //}
      //   muteMusic = (Button)getActivity().findViewById((R.id.mute));
       // int level = Integer.parseInt(fields[index++]);
        //muteMusic.getBackground().setLevel(level);
        muteClicked = Boolean.parseBoolean(fields[index++]);
      //  if(muteClicked){

           // .mMediaPlayer.pause();
     //   }
        gameOver = Boolean.parseBoolean(fields[index++]);


        int size = Integer.parseInt(fields[index++]);
        e = (TextView) getActivity().findViewById(R.id.scroggle_text_view);

        e.setText("");

        for(int i = 0; i<size; i++){

            wordsDetectedByUser.put(i, fields[index++]);

            e.append(wordsDetectedByUser.get(i)+" ");

        }
        notValidWord =Boolean.parseBoolean(fields[index++]);
        phaseTwo =Boolean.parseBoolean(fields[index++]);


        currentScore = Integer.parseInt(fields[index++]);
       t = Integer.parseInt(fields[index++]);
        int length = Integer.parseInt((fields[index++]));
        int a[ ]= new int[length];
        for(int i=0;i<length;i++){
          a[i]=Integer.parseInt(fields[index++]);
          DoneTiles.add(a[i]);
        }

        mLastLarge = Integer.parseInt(fields[index++]);
        mLastSmall = Integer.parseInt(fields[index++]);
        for (int large = 0; large < 9; large++) {
            for (int small = 0; small < 9; small++) {
                TileAssignment5.Owner owner = TileAssignment5.Owner.valueOf(fields[index++]);
                mSmallTiles[large][small].setOwner(owner);
                mSmallTiles[large][small].updateDrawableState(fields[index++].charAt(0), 1);
                //Log.d(DoneTiles.toString(), "checkkk");
                // mSmallTiles[large][small].updateDrawableState('a', 0);
            }
        }
      //  setAvailableFromLastMove(mLastLarge, mLastSmall);
      //  updateAllTiles();
       setAvailableAccordingToGamePhase(phaseTwo, mLastSmall, mLastLarge, DoneTiles);
    }

}

