package com.example.aliia.traveljournal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class outstate extends AppCompatActivity implements ImageAdapter.OnItemClickListener{

    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;


    private ProgressBar mProgressCircle;

    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;

    private List<Upload> mUploads;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outstate);

        mRecyclerView=findViewById(R.id.recycler_view2);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mProgressCircle=findViewById(R.id.progress_circle2);

        mUploads=new ArrayList<>();

        mAdapter =new ImageAdapter(outstate.this,mUploads);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemCickListene(outstate.this);

        mStorage=FirebaseStorage.getInstance();
        mDatabaseRef= FirebaseDatabase.getInstance().getReference("outofstateuploads/"+ FirebaseAuth.getInstance().getCurrentUser().getUid().toString());

        mDBListener=mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mUploads.clear();
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren())
                {
                    Upload upload=postSnapshot.getValue(Upload.class);
                    upload.setKey(postSnapshot.getKey());

                    mUploads.add(upload);
                }

                mAdapter.notifyDataSetChanged();

                mProgressCircle.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(outstate.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();
                mProgressCircle.setVisibility(View.INVISIBLE);

            }

        });
    }

    @Override
    public void OnItemClick(int position) {
        Toast.makeText(outstate.this,"Normal click at position",Toast.LENGTH_LONG).show();
    }


    @Override
    public void onDeleteClick(int position) {

        Upload selectedItem=mUploads.get(position);
        final String selectdKey=selectedItem.getKey();
        StorageReference imageRef=mStorage.getReferenceFromUrl(selectedItem.getImageUrl());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mDatabaseRef.child(selectdKey).removeValue();
                Toast.makeText(outstate.this,"Item deleted",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }

    public void gotoprofile(View view)
    {
        Intent intent=new Intent(outstate.this,viewprofile.class);
        startActivity(intent);
    }
    public void gotoaddphoto(View view)
    {
        Intent intent=new Intent(outstate.this,uploadimages.class);
        startActivity(intent);
    }
    public void incityfunc(View view)
    {
        Intent intent=new Intent(outstate.this,timeline.class);
        startActivity(intent);
    }

    public void outcityfunc(View view)
    {
        Intent intent=new Intent(outstate.this,outcity.class);
        startActivity(intent);
    }
    public void outstatefunc(View view)
    {
        Intent intent=new Intent(outstate.this,outstate.class);
        startActivity(intent);
    }

    public void gotoincityfunc1(View view)
    {
        Intent intent=new Intent(outstate.this,timeline.class);
        startActivity(intent);
    }
    public void gotooutcityfunc1(View view)
    {

        Intent intent=new Intent(outstate.this,outcity.class);
        startActivity(intent);
    }
    public void gotoforeignfunc1(View view)
    {

    }
    public void mainfunc(View view)
    {
        Intent intent=new Intent(outstate.this,home.class);
        startActivity(intent);
    }
    public void vfunc(View view)
    {
        Intent intent=new Intent(outstate.this,outstatevideos.class);
        startActivity(intent);
    }
    public void wishlistfunc(View view)
    {
        Intent intent=new Intent(outstate.this,wishlist.class);
        startActivity(intent);
    }
}
