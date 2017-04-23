package edu.neu.madcourse.raj__kukadia.ping;


import android.view.View;
import android.widget.AdapterView;


public abstract class DoubleClickListener implements AdapterView.OnItemClickListener {

    private static final long DOUBLE_CLICK_TIME_DELTA = 300;//milliseconds

    long lastClickTime = 0;


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        long clickTime = System.currentTimeMillis();
        if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA){
            onDoubleClick(view, parent, position);
        } else {
            onSingleClick(view, parent, position);
        }
        lastClickTime = clickTime;
    }

    public abstract void onSingleClick(View v, AdapterView<?> parent, int position);
    public abstract void onDoubleClick(View v, AdapterView<?> parent, int position);
}




