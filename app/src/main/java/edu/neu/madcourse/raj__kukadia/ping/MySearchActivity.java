 package edu.neu.madcourse.raj__kukadia.ping;

 import android.app.Activity;
 import android.os.Bundle;
 import android.support.annotation.Nullable;
 import android.text.Editable;
 import android.text.TextWatcher;

 import android.util.Log;
 import android.view.View;
 import android.widget.AdapterView;
 import android.widget.ArrayAdapter;
 import android.widget.EditText;
 import android.widget.ListView;

 import java.util.ArrayList;
 import java.util.Arrays;
 import java.util.Iterator;

 import edu.neu.madcourse.raj__kukadia.R;

 public class MySearchActivity extends Activity {

     private ListView lv;
     private EditText e;
     private ArrayAdapter<String> adapter;
     ArrayList<String> activityList;

     @Override
     protected void onCreate(@Nullable Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_my_search_ping);

         e = (EditText) findViewById(R.id.search_bar);
         lv = (ListView) findViewById(R.id.activity_list);


         initList();

         e.addTextChangedListener(new TextWatcher() {
             @Override
             public void beforeTextChanged(CharSequence s, int start, int count, int after) {

             }

             @Override
             public void onTextChanged(CharSequence s, int start, int before, int count) {
                 initList();
                     searchItem(s.toString());

             }

             @Override
             public void afterTextChanged(Editable s) {

             }
         });
     }

     private void initList(){
         activityList = new ArrayList<>();
         activityList.addAll(Arrays.asList(getResources().getStringArray(R.array.activity_array)));
         adapter = new ArrayAdapter<String>(MySearchActivity.this, android.R.layout.simple_list_item_1, activityList);
         lv.setAdapter(adapter);
         lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(activityList.get(position).toString(), "check");
             }
         });
     }

     private void searchItem(String item){
         for(Iterator<String>iterator=activityList.iterator();iterator.hasNext();){
             {
                 String value = iterator.next();
                 if(!value.contains(item)){
                      iterator.remove();
                 }
             }
         }
         adapter.notifyDataSetChanged();
     }
 }




