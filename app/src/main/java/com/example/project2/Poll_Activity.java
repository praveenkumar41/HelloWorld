package com.example.project2;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Poll_Activity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout layoutList;
    Button buttonAdd;
    ImageView buttonSubmitList,discard;
    EditText question;

    List<String> teamList = new ArrayList<>();
    ArrayList<polloptions> cricketersList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll);

        layoutList = findViewById(R.id.layout_list);
        buttonAdd = findViewById(R.id.button_add);
        buttonSubmitList = findViewById(R.id.button_submit_list);

        question=findViewById(R.id.question);

        discard=findViewById(R.id.discard);

        buttonAdd.setOnClickListener(this);
        buttonSubmitList.setOnClickListener(this);

        discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.button_add:

                addView();
                break;

            case R.id.button_submit_list:

                if(checkIfValidAndRead())
                {

                    if(!TextUtils.isEmpty(question.getText().toString()))
                    {

                        String myid= FirebaseAuth.getInstance().getCurrentUser().getUid();

                        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("story").child(myid);
                        reference.keepSynced(true);
                        String storyid=reference.push().getKey();
                        long timeend=System.currentTimeMillis()+86400000;

                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("imageurl",question.getText().toString());
                        for(int i=0;i<cricketersList.size();i++)
                        {
                            String s=Integer.toString(i);
                            hashMap.put(""+s,cricketersList.get(i));
                        }
                        String count=Integer.toString(cricketersList.size());

                        hashMap.put("options_count",count);
                        hashMap.put("timestart", ServerValue.TIMESTAMP);
                        hashMap.put("timeend",timeend);
                        hashMap.put("storyid",storyid);
                        hashMap.put("userid",myid);
                        hashMap.put("type","poll");

                        reference.child(storyid).setValue(hashMap);

                        finish();
                    }
                    else
                    {
                        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }
    }

    private boolean checkIfValidAndRead() {
        cricketersList.clear();
        boolean result = true;

        for(int i=0;i<layoutList.getChildCount();i++){

            View cricketerView = layoutList.getChildAt(i);

            EditText editTextName = (EditText)cricketerView.findViewById(R.id.edit_cricketer_name);
            //   AppCompatSpinner spinnerTeam = (AppCompatSpinner)cricketerView.findViewById(R.id.spinner_team);

            polloptions cricketer = new polloptions();

            if(!editTextName.getText().toString().equals("")){
                cricketer.setCricketerName(editTextName.getText().toString());
            }else {
                result = false;
                break;
            }

           /* if(spinnerTeam.getSelectedItemPosition()!=0){
                cricketer.setTeamName(teamList.get(spinnerTeam.getSelectedItemPosition()));
            }else {
                result = false;
                break;
            }
*/

            cricketersList.add(cricketer);

        }

        if(cricketersList.size()==0){
            result = false;
            Toast.makeText(this, "Add Cricketers First!", Toast.LENGTH_SHORT).show();
        }else if(!result){
            Toast.makeText(this, "Enter All Details Correctly!", Toast.LENGTH_SHORT).show();
        }


        return result;
    }

    private void addView() {

        final View cricketerView = getLayoutInflater().inflate(R.layout.row_add,null,false);

        EditText editText = (EditText)cricketerView.findViewById(R.id.edit_cricketer_name);
        //      AppCompatSpinner spinnerTeam = (AppCompatSpinner)cricketerView.findViewById(R.id.spinner_team);
        ImageView imageClose = (ImageView)cricketerView.findViewById(R.id.image_remove);

        //  ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,teamList);
        //    spinnerTeam.setAdapter(arrayAdapter);

        imageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeView(cricketerView);
            }
        });

        layoutList.addView(cricketerView);

    }

    private void removeView(View view){

        layoutList.removeView(view);

    }
}