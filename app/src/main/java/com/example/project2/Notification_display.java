package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class Notification_display extends Fragment implements View.OnKeyListener {

    Activity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.notification_display, container, false);

        return v;

    }
        @Override
        public boolean onKey (View view,int i, KeyEvent keyEvent){
            if (keyEvent.getAction() == KeyEvent.ACTION_UP) {
                if (i == KeyEvent.KEYCODE_BACK) {

                    Intent intent = new Intent(getContext(), Startapp.class);
                    startActivity(intent);
                    getActivity().finish();
                    return true;
                }
            }
            return false;
        }

    }
