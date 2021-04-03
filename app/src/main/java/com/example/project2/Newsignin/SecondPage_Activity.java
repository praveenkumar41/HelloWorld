package com.example.project2.Newsignin;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project2.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SecondPage_Activity extends AppCompatActivity {

    TextInputLayout passbox;
    TextInputEditText password;
    Button button;
    TextView tvPasswordStrength;

    FirebaseUser firebaseUser;
    FirebaseAuth mAuth;
    String name,dob,devicetoken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.secondpage_activity);

        String type=getIntent().getStringExtra("type");
        final String mailid=getIntent().getStringExtra("mailid");
        name=getIntent().getStringExtra("username");
        dob=getIntent().getStringExtra("dob");

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        mAuth = FirebaseAuth.getInstance();

        tvPasswordStrength=findViewById(R.id.tvPasswordStrength);

        passbox=findViewById(R.id.passbox);
        password=findViewById(R.id.password);
        button=findViewById(R.id.button);

        //mail and password aunthentication

        passbox.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                calculatestrength(editable.toString());
            }
        });



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(!TextUtils.isEmpty(passbox.getEditText().getText().toString()))
                {
                    if (tvPasswordStrength.getText().equals("Password Strength : LOW")) {
                        tvPasswordStrength.setTextColor(Color.RED);
                        tvPasswordStrength.setText("password strength must be medium or higher");
                    }
                    else if (tvPasswordStrength.getText().equals("Password Strength : MEDIUM") || tvPasswordStrength.getText().equals("Password Strength : HIGH"))
                    {
                      Intent intent = new Intent(getApplicationContext(), ThirdActivity.class);
                      intent.putExtra("password", passbox.getEditText().getText().toString());
                      intent.putExtra("username", name);
                      intent.putExtra("dob", dob);
                      startActivity(intent);
                   }
                }
            }
        });

    }

    private void calculatestrength(String passwordText) {

        int upperChars = 0, lowerChars = 0, numbers = 0,
                specialChars = 0, otherChars = 0, strengthPoints = 0;
        char c;

        int passwordLength = passwordText.length();

        if (passwordLength ==0)
        {
            tvPasswordStrength.setText("Invalid Password");
            tvPasswordStrength.setTextColor(Color.RED);
            return;
        }

        //If password length is <= 5 set strengthPoints=1
        if (passwordLength <= 5) {
            strengthPoints =1;
        }
        //If password length is >5 and <= 10 set strengthPoints=2
        else if (passwordLength <= 10) {
            strengthPoints = 2;
        }
        //If password length is >10 set strengthPoints=3
        else
            strengthPoints = 3;
        // Loop through the characters of the password
        for (int i = 0; i < passwordLength; i++) {
            c = passwordText.charAt(i);
            // If password contains lowercase letters
            // then increase strengthPoints by 1
            if (c >= 'a' && c <= 'z') {
                if (lowerChars == 0) strengthPoints++;
                lowerChars = 1;
            }
            // If password contains uppercase letters
            // then increase strengthPoints by 1
            else if (c >= 'A' && c <= 'Z') {
                if (upperChars == 0) strengthPoints++;
                upperChars = 1;
            }
            // If password contains numbers
            // then increase strengthPoints by 1
            else if (c >= '0' && c <= '9') {
                if (numbers == 0) strengthPoints++;
                numbers = 1;
            }
            // If password contains _ or @
            // then increase strengthPoints by 1
            else if (c == '_' || c == '@') {
                if (specialChars == 0) strengthPoints += 1;
                specialChars = 1;
            }
            // If password contains any other special chars
            // then increase strengthPoints by 1
            else {
                if (otherChars == 0) strengthPoints += 2;
                otherChars = 1;
            }
        }

        if (strengthPoints <= 3)
        {
            tvPasswordStrength.setText("Password Strength : LOW");
            tvPasswordStrength.setTextColor(Color.RED);
        }
        else if (strengthPoints <= 6) {
            tvPasswordStrength.setText("Password Strength : MEDIUM");
            tvPasswordStrength.setTextColor(Color.YELLOW);
        }
        else if (strengthPoints <= 9){
            tvPasswordStrength.setText("Password Strength : HIGH");
            tvPasswordStrength.setTextColor(Color.GREEN);
        }
    }
}