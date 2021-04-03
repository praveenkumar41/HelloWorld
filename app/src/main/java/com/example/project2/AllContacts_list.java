package com.example.project2;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class AllContacts_list extends AppCompatActivity
{

    List<String> listofcontacts,listofemails,listofcontactnames,listofemailnames;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    List<String> allappcontacts,allappemails,listofcontacts1,listofemails1,allappcontacts1,allappemails1;
    int i1=0;
    String cc="";
    List<String> list3;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_list);
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cc=dataSnapshot.child("cc").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

        getContacts();
        getmailids();

        gettingallcontacts();
        gettingallmailids();

        allappemails1=new ArrayList<>(allappemails);
        allappcontacts1=new ArrayList<>(allappcontacts);

        final DatabaseReference mref=FirebaseDatabase.getInstance().getReference().child("User_Contact_List").child(firebaseUser.getUid());
        DatabaseReference mref11=FirebaseDatabase.getInstance().getReference().child("User_Email_List").child(firebaseUser.getUid());

        mref.keepSynced(true);
        mref11.keepSynced(true);

        Collections.sort(listofcontacts);
        listofcontacts1=new ArrayList<>(listofcontacts);
        for (i1 = 0; i1 < listofcontacts.size(); i1++) {

                String con = listofcontacts.get(i1).replace(" ", "");
                String mail2 = con.replace("-", "");
                String mail3 = mail2.replace(")", "");
                String mail4 = mail3.replace("(", "");
                String mail5 = mail4.replace("_", "");

                mref.child(mail5).setValue("false");
        }

            Collections.sort(listofemails);
            listofemails1=new ArrayList<>();

            for (int j1 = 0; j1 < listofemails.size(); j1++)
            {
                if(listofemails.get(j1).contains("@gmail.com"))
                {
                    String mail=listofemails.get(j1).replace("@gmail.com","");
                    String mail1=mail.replace(".","");
                    String mail5=mail1.replace(" ","");
                    listofemails1.add(mail5);
                    mref11.child(mail5).setValue("false");
                }
            }

        list3=new ArrayList<>();

        DatabaseReference mrefry11=FirebaseDatabase.getInstance().getReference().child("ssusers");

        for (String temp : listofemails1) {

            if (allappemails.contains(temp)) {

                list3.add(temp);
            }
        }

        int c=allappemails.size();
        String s=Integer.toString(c);

        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();



    }

    private void getContacts()
    {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {

            listofcontacts = new ArrayList<>();
            listofcontactnames = new ArrayList<>();

            ContentResolver cr = getContentResolver();
            Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            if (cur.getCount() > 0) {
                while (cur.moveToNext()) {
                    String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                    Cursor cur1 = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (cur1.moveToNext()) {
                        //to get the contact names
                        String name = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        Log.e("Name :", name);
                        String con = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        String mail = con.replace(" ", "");
                        String mail1 = mail.replace("-", "");
                        String mail2 = mail1.replace(")", "");
                        String mail3 = mail2.replace("(", "");
                        String mail4 = mail3.replace("_", "");

                        if (name != null) {
                            listofcontactnames.add(name);
                        }

                        if (mail4 != null) {
                            listofcontacts.add(mail4);
                        }
                    }
                    cur1.close();
                }
            }
        }
    }


    private void getmailids()
    {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {

            listofemails = new ArrayList<>();
            listofemailnames = new ArrayList<>();

            ContentResolver cr = getContentResolver();
            Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            if (cur.getCount() > 0) {
                while (cur.moveToNext()) {
                    String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                    Cursor cur1 = cr.query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                            new String[]{id},null);
                    while (cur1.moveToNext()) {
                        //to get the contact names
                        String name = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        Log.e("Name :", name);
                        String email = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        Log.e("Email", email);

                        if (email != null) {
                            listofemails.add(email);
                        }

                        if (name != null) {
                            listofemailnames.add(name);
                        }
                    }

                    cur1.close();
                }
            }
        }
    }


    private void gettingallcontacts()
    {
       allappcontacts = new ArrayList<>();
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference mref=FirebaseDatabase.getInstance().getReference("All_Contacts");
        mref.keepSynced(true);
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                allappcontacts.clear();

                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    private void gettingallmailids()
    {
        allappemails = new ArrayList<>();
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference mref=FirebaseDatabase.getInstance().getReference("All_Emailid");
        mref.keepSynced(true);
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                allappemails.clear();

                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    allappemails.add(snapshot.getValue().toString());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }
}