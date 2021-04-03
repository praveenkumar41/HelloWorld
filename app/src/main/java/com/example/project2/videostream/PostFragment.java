package com.example.project2.videostream;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.RequestManager;
import com.example.project2.Adapter.Story_View_Adapter;
import com.example.project2.AddPostActivity;
import com.example.project2.BottomNavigationViewHelper;
import com.example.project2.Friends;
import com.example.project2.FriendsAdapter;
import com.example.project2.PostViewer_Activity;
import com.example.project2.Post_write_something;
import com.example.project2.R;
import com.example.project2.Startapp;
import com.example.project2.Stories;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.mancj.slideup.SlideUp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class PostFragment extends Fragment implements View.OnKeyListener{

    SpaceNavigationView navigationView;
    //  private VideoPlayerRecyclerView mRecyclerView;

    FloatingActionButton add_post;

    List<String> mfollowing;

    FirebaseUser firebaseUser;
    DatabaseReference mref;
    private static final int AUDIO = 0;
    private static final int AUDIO1 = 220;


    ShimmerFrameLayout sh1;

    private Story_View_Adapter story_view_adapter;
    private List<Stories> storyList;

    private StorageReference mStorageRef;
    DatabaseReference mmref;
    int count = 0;
    private static final int STORAGE_REQUEST = 10;
    private static final int CAMERA_REQUEST = 11;
    NestedScrollView nested;

    Uri imagepick;
    String[] camerapermission;
    String[] storagepermission;


    ActionBar mActionbar;

    RecyclerView mRecyclerView,roundrv;
    ArrayList<MediaObject> mediaObjects;
    ArrayList<MediaObject1> mediaObjects1;


    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (keyEvent.getAction() == KeyEvent.ACTION_UP)
        {
            if (i == KeyEvent.KEYCODE_BACK) {
                getActivity().finish();
                System.runFinalizersOnExit(true);
                return true;
            }
        }
        return false;
    }


    private enum VolumeState {ON, OFF};


    private ImageView thumbnail, volumeControl;
    private ProgressBar progressBar;
    private View viewHolderParent;
    private FrameLayout frameLayout;
    private PlayerView videoSurfaceView;
    private SimpleExoPlayer videoPlayer;

    // vars
     private int videoSurfaceDefaultHeight = 0;
    private int screenDefaultHeight = 0;
    private Context context;
    private int playPosition = -1;
    private boolean isVideoViewAdded;
    private RequestManager requestManager;

    VideoPlayerRecyclerAdapter.ViewHolder viewHolder;
    List<String> mfriendsforfriends;
    Activity activity;
    private VolumeState volumeState;
     List<String> mfollow;
    Toolbar toolbar;
    public PostFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View itemview= inflater.inflate(R.layout.fragment_post, container, false);


        add_post=itemview.findViewById(R.id.add_post);
        nested=itemview.findViewById(R.id.nested);
        roundrv = itemview.findViewById(R.id.roundrv);
        add_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), AddPostActivity.class);
                startActivity(intent);
            }
        });

        storagepermission=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        mStorageRef = FirebaseStorage.getInstance().getReference();

        mRecyclerView = itemview.findViewById(R.id.recycler_view);

        checkfollowing();


        mRecyclerView.setLayoutManager( new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(8);
        mRecyclerView.addItemDecoration(itemDecorator);

       // RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecorator(ContextCompat.getDrawable(getContext(), R.drawable.divider));
       // mRecyclerView.addItemDecoration(dividerItemDecoration);


        FirebaseDynamicLinks.getInstance().getDynamicLink(getActivity().getIntent()).addOnSuccessListener(new OnSuccessListener<PendingDynamicLinkData>() {
            @Override
            public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {

                if(pendingDynamicLinkData!=null)
                {
                    Uri deeplink=pendingDynamicLinkData.getLink();

                    Log.e("Main","refer"+" "+deeplink.toString());
                    String referlink=deeplink.toString();

                    try {

                       referlink=referlink.substring(referlink.lastIndexOf("=")+1);
                        Log.e("substring","h"+" "+referlink);

                        final String postid=referlink.substring(0,referlink.indexOf("__"));
                        final String publisher=referlink.substring(referlink.indexOf("__")+1);

                        Log.e("postid","---"+postid);
                        Log.e("publisherid","---"+publisher);

                        DatabaseReference mref=FirebaseDatabase.getInstance().getReference("posts").child(postid);
                        mref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                String bit=dataSnapshot.child("bitmap").getValue(String.class);
                                String descs=dataSnapshot.child("description").getValue(String.class);
                                String image=dataSnapshot.child("imageurl").getValue(String.class);
                                String type=dataSnapshot.child("type").getValue(String.class);

                                Intent intent=new Intent(getContext(),PostViewer_Activity.class);
                                intent.putExtra("bitmap",bit);
                                intent.putExtra("description",descs);
                                intent.putExtra("imageurl",image);
                                intent.putExtra("postid",postid);
                                intent.putExtra("publisher",publisher);
                                intent.putExtra("cardtype",type);
                                startActivity(intent);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


        checkfollowing();
        roundrv.setHasFixedSize(true);
        LinearLayoutManager LinearLayoutManager=new LinearLayoutManager(getContext(), androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL,false);
        roundrv.setLayoutManager(LinearLayoutManager);

        storyList=new ArrayList<>();
        story_view_adapter=new Story_View_Adapter(getContext(),storyList);
        roundrv.setAdapter(story_view_adapter);
        checkfollowingforstories();

        return itemview;
    }

    private void checkfollowing()
    {
        mfollow = new ArrayList<>();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();


        DatabaseReference mref=FirebaseDatabase.getInstance().getReference("follow_count").child(firebaseUser.getUid()).child("following");
        mref.keepSynced(true);
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mfollow.clear();

                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    mfollow.add(snapshot.getKey());
                }
                initRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }


    private void initRecyclerView()
    {
        mediaObjects1 = new ArrayList<>();
        mediaObjects=new ArrayList<>();

                DatabaseReference reference11 = FirebaseDatabase.getInstance().getReference().child("posts");
                reference11.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        mediaObjects1.clear();
                        mediaObjects.clear();
                        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

                        for (DataSnapshot Snapshot : dataSnapshot.getChildren())
                        {
                            MediaObject1 mediaObject = Snapshot.getValue(MediaObject1.class);

                                for(String id: mfollow)
                                {

                                        if(mediaObject.getPublisher().equals(id))
                                        {
                                            mediaObjects1.add(mediaObject);
                                        }
                                }
                        }

                        Collections.reverse(mediaObjects1);
                        ImagedisplayAdapter adapter = new ImagedisplayAdapter(getContext(), mediaObjects1);
                        adapter.notifyDataSetChanged();
                        mRecyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError)
                    {

                    }
                });
    }

    private void checkfollowingforstories()
    {

        mfollowing = new ArrayList<>();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference mref=FirebaseDatabase.getInstance().getReference("follow_count").child(firebaseUser.getUid()).child("following");
        mref.keepSynced(true);
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mfollowing.clear();

                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    mfollowing.add(snapshot.getKey());
                }

               readStory();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readStory()
    {
        DatabaseReference mref=FirebaseDatabase.getInstance().getReference().child("story");
        mref.keepSynced(true);
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long timecurrent=System.currentTimeMillis();

                storyList.clear();
                storyList.add(new Stories("",0,0,"",FirebaseAuth.getInstance().getCurrentUser().getUid(),"","","","","","",""));


                for(String id:mfollowing)
                {
                    int countstory=0;
                    Stories stories=null;

                    for(DataSnapshot snapshot:dataSnapshot.child(id).getChildren())
                    {
                        stories=snapshot.getValue(Stories.class);
                        assert stories != null;
                        if(timecurrent>stories.getTimestart() && timecurrent<stories.getTimeend())
                        {
                            countstory++;
                        }
                    }

                    if(countstory>0)
                    {
                        storyList.add(stories);
                    }
                }

                story_view_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public class DividerItemDecorator extends RecyclerView.ItemDecoration {
        private Drawable mDivider;

        public DividerItemDecorator(Drawable divider) {
            mDivider = divider;
        }

        @Override
        public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
            int dividerLeft = parent.getPaddingLeft();
            int dividerRight = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount - 1; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int dividerTop = child.getBottom() + params.bottomMargin;
                int dividerBottom = dividerTop + mDivider.getIntrinsicHeight();

                mDivider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom);
                mDivider.draw(canvas);
            }
        }
    }
}
