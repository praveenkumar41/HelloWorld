package com.example.project2.Newsignin;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project2.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class FirstPage_Activity extends AppCompatActivity {

    TextInputEditText name,dob;
    DatePickerDialog.OnDateSetListener mDatesetlistener;
    Button btn;

    TextInputLayout edit,edit1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firstpage);

        name=findViewById(R.id.name);
        dob=findViewById(R.id.dob);
        btn=findViewById(R.id.button);

        edit=findViewById(R.id.edit);
        edit1=findViewById(R.id.edit1);



        dob.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b)
                {
                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    int Month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dialog = new DatePickerDialog(FirstPage_Activity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDatesetlistener, year, Month, day);

                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                    dob.clearFocus();

                }
            }
        });

        mDatesetlistener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int Month, int day)
            {
                Month = Month + 1;
                String s1 = "OnDateSet: mm/dd/yyy: " + Month + "/" + day + "/" + year;

                String DOF = Month + "-" + day + "-" + year;
                dob.setText(DOF);
            }

        };



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!TextUtils.isEmpty(edit.getEditText().getText().toString()) && !TextUtils.isEmpty(edit1.getEditText().getText().toString()))
                {
                    Intent intent=new Intent(getApplicationContext(),SecondPage_Activity.class);
                    intent.putExtra("username",edit.getEditText().getText().toString());
                    intent.putExtra("dob",edit1.getEditText().getText().toString());
                    startActivity(intent);
                }
            }
        });
    }
}
