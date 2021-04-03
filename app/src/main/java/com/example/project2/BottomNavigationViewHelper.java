package com.example.project2;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.example.project2.videostream.PostFragment;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.lang.reflect.Field;

public class BottomNavigationViewHelper
{
    /*
    private static final String TAG="BottonNavigationViewEx";

    public static void setupBottomNavigationView(BottomNavigationViewEx bottomNavigationViewEx)
    {
     //   bottomNavigationViewEx.enableAnimation(false);
      //  bottomNavigationViewEx.enableItemShiftingMode(false);
      //  bottomNavigationViewEx.enableShiftingMode(false);
      //  bottomNavigationViewEx.setTextVisibility(false);
    }

    public static void enablenavigation(final Context context, BottomNavigationViewEx viewEx)
    {
        viewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.home1:
                        Intent intent=new Intent(context, PostFragment.class);
                        context.startActivity(intent);
                        break;

                    case R.id.search1:
                        Intent intent1=new Intent(context,userfragment.class);
                        context.startActivity(intent1);
                        break;

                    case R.id.messages1:
                        Intent intent2=new Intent(context,Chatfragment.class);
                        context.startActivity(intent2);
                        break;

                    case R.id.notification1:
                        Intent intent3=new Intent(context,Notification_display.class);
                        context.startActivity(intent3);
                        break;

                    case R.id.profile1:
                        Intent intent4=new Intent(context,fragment_profile.class);
                        context.startActivity(intent4);
                        break;
                }
                return false;
            }
        });
    }

     */
       public static void disableShiftMode(BottomNavigationView view) {
            BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
            try {
                Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
                shiftingMode.setAccessible(true);
                shiftingMode.setBoolean(menuView, false);
                shiftingMode.setAccessible(false);
                for (int i = 0; i < menuView.getChildCount(); i++) {
                    BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                    //noinspection RestrictedApi
                    item.setShifting(false);
                    // set once again checked value, so view will be updated
                    //noinspection RestrictedApi
                    item.setChecked(item.getItemData().isChecked());
                }
            } catch (NoSuchFieldException e) {
                Log.e("BNVHelper", "Unable to get shift mode field", e);
            } catch (IllegalAccessException e) {
                Log.e("BNVHelper", "Unable to change value of shift mode", e);
            }
        }
    }

