
package com.example.project2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class fragment_profile extends Fragment implements View.OnKeyListener {

    Activity activity;

    ImageButton imagebuttonpopmenu, backarrow, userprofileeditprofilebutton, loverbutton;
    TextView userprofilename, userprofilemailtext, userprofiledobtext, userprofileeditprofilename;

    ImageView userprofilemail, userprofildob, userprofilechangephoto;
    CircleImageView userprofileimage;
    private DatabaseReference firebaseDatabase;
    FirebaseUser firebaseUser;
    private static final int AUDIO = 0;
    private StorageReference mStorageRef;

    TextView followercount, followingcount, follower, following;
    TextView save, video, photo, post;
    int c;

   // PagerviewAdapter pagerviewAdapter;
    //ViewPager viewPager;


    ViewPagerAdapter viewPagerAdapter21;
    TabLayout viewPagerTab;

    int following_count = 0, followers_count = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_fragment_profile, container, false);

        viewPagerAdapter21 = new fragment_profile.ViewPagerAdapter(getFragmentManager());

        ViewPager viewPager = v.findViewById(R.id.viewpager);
        viewPager.setAdapter(viewPagerAdapter21);

        viewPagerTab = v.findViewById(R.id.viewpagertab);
        viewPagerTab.setupWithViewPager(viewPager);


        imagebuttonpopmenu = v.findViewById(R.id.imagebuttonpopmenu);
        backarrow = v.findViewById(R.id.backarrow);
        save = v.findViewById(R.id.save);
        video = v.findViewById(R.id.video);
        photo = v.findViewById(R.id.photo);
        post = v.findViewById(R.id.post);


        mStorageRef = FirebaseStorage.getInstance().getReference();

        followercount = v.findViewById(R.id.followercount);
        followingcount = v.findViewById(R.id.followingcount);

        follower = v.findViewById(R.id.follower);
        following = v.findViewById(R.id.following);

        follower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), FollowersActivity.class);
                startActivity(intent);
//                activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), FollowingActivity.class);
                startActivity(intent);
//                activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });


        userprofileimage = v.findViewById(R.id.userprofileimage);
        userprofilechangephoto = v.findViewById(R.id.userprofilechangephoto);

        userprofileeditprofilename = v.findViewById(R.id.userprofileeditprofilename);

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
        firebaseDatabase.keepSynced(true);

        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String image = dataSnapshot.child("IMAGEURL").getValue(String.class);
                String name = dataSnapshot.child("USERNAME").getValue(String.class);

                userprofileeditprofilename.setText(name);

                Picasso.with(getContext()).load(image).placeholder(R.drawable.pro).into(userprofileimage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




/*
        userprofileeditprofilebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(getContext(),Usereditprofilepage.class);
                startActivity(intent);
            }
        });
*/


        imagebuttonpopmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popup = new PopupMenu(getContext(), imagebuttonpopmenu);
                popup.getMenuInflater().inflate(R.menu.logout, popup.getMenu());


                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.menu1:
                                FirebaseAuth.getInstance().signOut();

                                Calendar calendar = Calendar.getInstance();
                                SimpleDateFormat currentdate = new SimpleDateFormat("MMM dd, YYYY");
                                final String savecurrentdate = currentdate.format(calendar.getTime());

                                SimpleDateFormat currenttime = new SimpleDateFormat("hh:mm a");
                                final String savecurrenttime = currenttime.format(calendar.getTime());


                                DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child("user_activity").child(firebaseUser.getUid()).push();

                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("activity", "signout");
                                hashMap.put("userid", firebaseUser.getUid());
                                hashMap.put("pushid", mref.getKey());
                                hashMap.put("date", savecurrentdate);
                                hashMap.put("time", savecurrenttime);
                                mref.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Intent intent = new Intent(getContext(), Intoapp.class);
                                            startActivity(intent);
                                            getActivity().finish();
                                        }
                                    }
                                });

                                return true;

                            case R.id.menu2:
                                Intent intent1 = new Intent(getContext(), BlockedListFragment.class);
                                startActivity(intent1);
                                return true;

                            case R.id.menu3:
                                Intent intent2 = new Intent(getContext(), SettingActivity.class);
                                startActivity(intent2);
                                return true;
                        }
                        return false;
                    }
                });

                popup.show();
            }
        });

        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), Startapp.class);
                startActivity(intent);
                getActivity().finish();

            }
        });

        getFollowers();
        getFollowing();

        return v;
    }


    private void getFollowing() {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("follow_count").child(firebaseUser.getUid()).child("following");
        ref.keepSynced(true);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    following_count++;
                }

                String c = Integer.toString(following_count);
                followingcount.setText(c);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getFollowers() {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("follow_count").child(firebaseUser.getUid()).child("followers");
        ref.keepSynced(true);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    followers_count++;
                }

                String c = Integer.toString(followers_count);
                followercount.setText(c);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
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


    public class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            if (position == 0) {

                fragment = new postdisplay();
            } else if (position == 1) {
                fragment = new Photosdisplay();
            }
            else if(position==2)
            {
                fragment=new videosdisplay();
            }
            else if(position==3)
            {
                fragment=new savepostdisplay();
            }

            return fragment;
        }


        @Override
        public int getCount() {
            return 4;
        }
    }

}





