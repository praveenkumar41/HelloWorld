package com.example.project2;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.project2.videostream.ImagedisplayAdapter;
import com.example.project2.videostream.MediaObject;
import com.example.project2.videostream.MediaObject1;
import com.example.project2.videostream.PostFragment;
import com.example.project2.videostream.Post_image_write_something;
import com.example.project2.videostream.VerticalSpacingItemDecorator;
import com.example.project2.videostream.VideoPlayerRecyclerAdapter;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.internal.NavigationMenuView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.luseen.spacenavigation.SpaceNavigationView;


import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.bluetooth.BluetoothClass.Service.AUDIO;

public class Startapp extends AppCompatActivity {

    SpaceNavigationView navigationView;
    BottomNavigationView bottom_navi;
    FragmentManager fragmentManager;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.runFinalizersOnExit(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startapp);

        bottom_navi = findViewById(R.id.bottom_navi);

      //  BottomNavigationViewHelper.disableShiftMode(bottom_navi);

        if (savedInstanceState == null) {
            bottom_navi.setSelectedItemId(R.id.home1);
              fragmentManager = getSupportFragmentManager();
            PostFragment postFragment = new PostFragment();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, postFragment).commit();
        }

        bottom_navi.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {

                Fragment fragment=null;

                switch (item.getItemId())
                {
                    case R.id.home1:

                         fragment=new PostFragment();
                         break;

                    case R.id.search1:
                        fragment=new userfragment();
                        break;

                    case R.id.messages1:
                        fragment=new Chatfragment();
                        break;

                    case R.id.notification1:
                        fragment=new Notification_display();
                        break;

                    case R.id.profile1:
                        fragment=new fragment_profile();
                        break;
                }

                if (fragment != null)
                {
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
                }

                return true;
            }

        });
    }
}