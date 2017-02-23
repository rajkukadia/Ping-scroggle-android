
package edu.neu.madcourse.raj__kukadia;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.Random;

public class TileAssignment5 {

   public enum Owner {
      X, O /* letter O */, NEITHER, BOTH, CLICKED, NOTCLICKED, FREEZED
   }

   // These levels are defined in the drawable definitions
   private static final int LEVEL_X = 0;
   private static final int LEVEL_O = 1; // letter O
   private static final int LEVEL_BLANK = 2;
   private static final int LEVEL_CLICKED = 1;
   private static final int LEVEL_NOTCLICKED = 0;
   private static final int LEVEL_FREEZED = 2;

   private static final int LEVEL_TIE = 3;

   Random r;
   private static final String LEVEL_A = "A";
   private static final String LEVEL_B = "B";
   private static final String LEVEL_C = "C";
   private static final String LEVEL_D = "D";
   private static final String LEVEL_E = "E";
   private static final String LEVEL_F = "F";
   private static final String LEVEL_G = "G";
   private static final String LEVEL_H = "H";
   private static final String LEVEL_I = "I";
   //private static final int LEVEL_ = 1;

   //private final InstructionsActivityAssignment5 iGame;

   private final ScroggleAssignment5Fragment mGame;
   private Owner mOwner = Owner.NOTCLICKED;
   public View mView;
   private TileAssignment5 mSubTiles[];

   //public TileAssignment5(InstructionsActivityAssignment5 game) {
     // this.iGame = game;
//   }


   public TileAssignment5(ScroggleAssignment5Fragment game) {
      this.mGame = game;
   }

   public TileAssignment5 deepCopy() {
      TileAssignment5 tile = new TileAssignment5(mGame);
      tile.setOwner(getOwner());
      if (getSubTiles() != null) {
         TileAssignment5 newTiles[] = new TileAssignment5[9];
         TileAssignment5 oldTiles[] = getSubTiles();
         for (int child = 0; child < 9; child++) {
            newTiles[child] = oldTiles[child].deepCopy();
         }
         tile.setSubTiles(newTiles);
      }
      return tile;
   }

   public View getView() {
      return mView;
   }

   public void setView(View view) {
      this.mView = view;
   }

   public Owner getOwner() {
      return mOwner;
   }

   public void setOwner(Owner owner) {
      this.mOwner = owner;
   }

   public TileAssignment5[] getSubTiles() {
      return mSubTiles;
   }

   public void setSubTiles(TileAssignment5[] subTiles) {
      this.mSubTiles = subTiles;
   }

   public void updateDrawableState(char a, int x) {
      if (mView == null) return;
       int level = getLevel();
      if (mView.getBackground() != null) {
        // Log.d("Here it coomes ...", "aaa");
      mView.getBackground().setLevel(level);
      }
      if (mView instanceof Button) {

         //Random randomGenerator;
         //randomGenerator = new Random();
         //int stopNumber = randomGenerator.nextInt(8)+1;
        // mView.getBackground().setLevel(1);
        if(x==1) {
           Button b = ((Button) mView);
           b.setText(String.valueOf(a));
        }
        // b.getBackground().setLevel(level);

         //spreadWords sw = new spreadWords(mView);
         //sw.execute();

         //Log.d("it came", "here");
      }
   }

   private int getLevel() {
      int level = LEVEL_BLANK;

      switch (mOwner) {

         case CLICKED:
            level = LEVEL_CLICKED;
            break;

         case NOTCLICKED:
            level = LEVEL_NOTCLICKED;
            break;
         case FREEZED:
            level = LEVEL_FREEZED;
            break;


         /*
         case 1:
            level = LEVEL_A;
            break;
         case 2:
            level = LEVEL_B;
            break;
         case 3:
            level = LEVEL_C;
            break;
         case 4:
            level = LEVEL_D;
            break;
         case 5:
            level = LEVEL_E;
            break;
         case 6:
            level = LEVEL_F;
            break;
         case 7:
            level = LEVEL_G;
            break;
         case 8:
            level = LEVEL_H;
            break;
         case 9:
            level = LEVEL_I;
            break;
  */
         //case X:
         // level = LEVEL_X;
         //break;
         // case O: // letter O
         //  level = LEVEL_O;
         //  break;
         //case BOTH:
         // level = LEVEL_TIE;
         //break;
         //case NEITHER:
         // level = mGame.isAvailable(this) ? LEVEL_AVAILABLE : LEVEL_BLANK;
         //break;
      }
      return level;
   }

   private void countCaptures(int totalX[], int totalO[]) {
      int capturedX, capturedO;
      // Check the horizontal
      for (int row = 0; row < 3; row++) {
         capturedX = capturedO = 0;
         for (int col = 0; col < 3; col++) {
            Owner owner = mSubTiles[3 * row + col].getOwner();
            if (owner == Owner.X || owner == Owner.BOTH) capturedX++;
            if (owner == Owner.O || owner == Owner.BOTH) capturedO++;
         }
         totalX[capturedX]++;
         totalO[capturedO]++;
      }

      // Check the vertical
      for (int col = 0; col < 3; col++) {
         capturedX = capturedO = 0;
         for (int row = 0; row < 3; row++) {
            Owner owner = mSubTiles[3 * row + col].getOwner();
            if (owner == Owner.X || owner == Owner.BOTH) capturedX++;
            if (owner == Owner.O || owner == Owner.BOTH) capturedO++;
         }
         totalX[capturedX]++;
         totalO[capturedO]++;
      }

      // Check the diagonals
      capturedX = capturedO = 0;
      for (int diag = 0; diag < 3; diag++) {
         Owner owner = mSubTiles[3 * diag + diag].getOwner();
         if (owner == Owner.X || owner == Owner.BOTH) capturedX++;
         if (owner == Owner.O || owner == Owner.BOTH) capturedO++;
      }
      totalX[capturedX]++;
      totalO[capturedO]++;
      capturedX = capturedO = 0;
      for (int diag = 0; diag < 3; diag++) {
         Owner owner = mSubTiles[3 * diag + (2 - diag)].getOwner();
         if (owner == Owner.X || owner == Owner.BOTH) capturedX++;
         if (owner == Owner.O || owner == Owner.BOTH) capturedO++;
      }
      totalX[capturedX]++;
      totalO[capturedO]++;
   }

   public Owner findWinner() {
      // If owner already calculated, return it
      if (getOwner() != Owner.NEITHER)
         return getOwner();

      int totalX[] = new int[4];
      int totalO[] = new int[4];
      countCaptures(totalX, totalO);
      if (totalX[3] > 0) return Owner.X;
      if (totalO[3] > 0) return Owner.O;

      // Check for a draw
      int total = 0;
      for (int row = 0; row < 3; row++) {
         for (int col = 0; col < 3; col++) {
            Owner owner = mSubTiles[3 * row + col].getOwner();
            if (owner != Owner.NEITHER) total++;
         }
         if (total == 9) return Owner.BOTH;
      }

      // Neither player has won this tile
      return Owner.NEITHER;
   }

   public int evaluate() {
      switch (getOwner()) {
         case X:
            return 100;
         case O:
            return -100;
         case NEITHER:
            int total = 0;
            if (getSubTiles() != null) {
               for (int tile = 0; tile < 9; tile++) {
                  total += getSubTiles()[tile].evaluate();
               }
               int totalX[] = new int[4];
               int totalO[] = new int[4];
               countCaptures(totalX, totalO);
               total = total * 100 + totalX[1] + 2 * totalX[2] + 8 *
                       totalX[3] - totalO[1] - 2 * totalO[2] - 8 * totalO[3];
            }
            return total;
      }
      return 0;
   }

   public void animate() {
      Animator anim = AnimatorInflater.loadAnimator(mGame.getActivity(),
              R.animator.tictactoe);
      if (getView() != null) {
         anim.setTarget(getView());
         anim.start();
      }
   }
}


/*
   class spreadWords extends AsyncTask<Void, Void, Void>{
      private long MyCharacter;
      private View mView;
      spreadWords(View v){
         mView = v;
      }

      @Override
      protected Void doInBackground(Void... params) {
         char[] stringInMaking = null;
         Random randomGenerator;
         randomGenerator = new Random();

         long stopNumber = randomGenerator.nextInt(60120);

         Log.d(Long.toString(stopNumber), "");
         Log.d(Integer.toString(MainActivity.nineWords.size())," HashMao size");

         for (long l = 0; l <MainActivity.nineWords.size(); l++) {

            long randomLong = MainActivity.nineWords.get(l);

            long firstChar = randomLong>>>40;
           stringInMaking[0] = MainActivity.reverseletterMap.get((byte)firstChar);
            long secondCharProgresss = randomLong<<24;
            long secondChar = secondCharProgresss>>>59;
            stringInMaking[1] = MainActivity.reverseletterMap.get((byte)secondChar);
            long thirdCharProgress = randomLong<<29;
            long thirdChar = thirdCharProgress>>>59;
            stringInMaking[2] = MainActivity.reverseletterMap.get((byte)thirdChar);
            long fourthCharProgress = randomLong<<34;
            long fourthChar = fourthCharProgress>>>59;
            stringInMaking[3] = MainActivity.reverseletterMap.get((byte)fourthChar);
            long fifthCharProgress = randomLong<<39;
            long fifthChar = fifthCharProgress>>>59;
            stringInMaking[4] = MainActivity.reverseletterMap.get((byte)fifthChar);
            long sixthCharProgress = randomLong<<44;
            long sixthChar = sixthCharProgress>>>59;
            stringInMaking[5] = MainActivity.reverseletterMap.get((byte)sixthChar);
            long seventhCharProgress = randomLong<<49;
            long seventhChar = seventhCharProgress>>>59;
            stringInMaking[6] = MainActivity.reverseletterMap.get((byte)seventhChar);
            long eightCharProgress = randomLong<<54;
            long eightChar = eightCharProgress>>>59;
            stringInMaking[7] = MainActivity.reverseletterMap.get((byte)eightChar);
            long ninthCharProgress = randomLong<<59;
            long ninthChar = ninthCharProgress>>>59;
            stringInMaking[8] = MainActivity.reverseletterMap.get((byte)ninthChar);

            String newString = new String(stringInMaking);
            Log.d("it prints: ", newString);

         if(l==stopNumber) {
            publishProgress(newString);
            break;
         }

         }





         return null;
      }

      private void publishProgress(String newString) {

         Button b = ((Button) mView);
         b.setText(newString);
      }
   }




}
*/