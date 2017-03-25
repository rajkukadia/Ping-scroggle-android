
package edu.neu.madcourse.raj__kukadia.assignment7;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.neu.madcourse.raj__kukadia.R;
import edu.neu.madcourse.raj__kukadia.assignment5.ScroggleAssignment5;

public class ControlFragmentMultiplayer extends Fragment {
    private DatabaseReference mRootRef;
    private FirebaseAuth mAuth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =
                inflater.inflate(R.layout.fragment_control_scroggle_multiplayer, container, false);
        View main = rootView.findViewById(R.id.button_main_multiplayer);
    //    View restart = rootView.findViewById(R.id.button_restart);
        mRootRef = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();


        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
confirm();

            }
        });
      //  restart.setOnClickListener(new View.OnClickListener() {
         ///   @Override
           // public void onClick(View view) {
       //         ((ScroggleMultiplayerActivity) getActivity()).restartGame();
      //      }
     //   });
        return rootView;
    }

    private void confirm(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        mRootRef.child("SynchronousGames").child(ScroggleMultiplayerFragment.gameID).removeValue();
                        getActivity().finish();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you want to quit?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }


}
