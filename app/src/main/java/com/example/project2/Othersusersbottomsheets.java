package com.example.project2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Othersusersbottomsheets extends BottomSheetDialogFragment {

    private BottomsheetListener mlistener;
    private FirebaseUser firebaseUser;
    private DatabaseReference mref,mref1,mref11;
    CardView report,block,unfriend,sharedphotos,sharedvideos;
    Othersusersbottomsheets context=this;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottomsheet, container, false);

        report=view.findViewById(R.id.report);
        block=view.findViewById(R.id.block);
        unfriend=view.findViewById(R.id.unfriend);
        sharedphotos=view.findViewById(R.id.sharedphotos);
        sharedvideos=view.findViewById(R.id.sharedvideos);

       // TextView sharedvideostext=view.findViewById(R.id.sharedvideostext);

        final String userid=getArguments().getString("USERID");

      //  sharedvideostext.setText(userid);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        mref= FirebaseDatabase.getInstance().getReference().child("Friends");
        mref.keepSynced(true);

        mref11= FirebaseDatabase.getInstance().getReference().child("Friends");
        mref11.keepSynced(true);

        mref1= FirebaseDatabase.getInstance().getReference("users").child(userid);
        mref1.keepSynced(true);

        block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder alertdialog =new AlertDialog.Builder(view.getRootView().getContext());
                alertdialog.setTitle("Block User");
                alertdialog.setIcon(R.drawable.ic_block_black_24dp);
                alertdialog.setMessage("Do you want to block this user");

                alertdialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {

                        mref.child(firebaseUser.getUid()).child(userid).child("blockeduser").setValue(userid).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                mref.child(userid).child(firebaseUser.getUid()).child("blockeduser").setValue(userid).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getContext(), "User Blocked", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });


                            }
                        });

                    }
                });

                alertdialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                alertdialog.create();
                alertdialog.show();

            }
        });


        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),Report_on_user.class);
                intent.putExtra("userid",userid);
                startActivity(intent);
            }
        });






        unfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mref11.child(firebaseUser.getUid()).child(userid).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mref11.child(userid).child(firebaseUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getContext(), "Unfriended", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                });
            }
        });


        return view;
    }


    public interface BottomsheetListener{
        void onbuttonclicked(String text);
    }

 /*   @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            mlistener= (BottomsheetListener) context;

        }
        catch (ClassCastException e){
            throw  new ClassCastException(context.toString()+"Must implement bottomsheetlistener");
        }

    }

  */
}