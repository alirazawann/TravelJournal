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

public class outcity extends AppCompatActivity implements ImageAdapter.OnItemClickListener{

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
        setContentView(R.layout.activity_outcity);

        mRecyclerView=findViewById(R.id.recycler_view1);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mProgressCircle=findViewById(R.id.progress_circle1);

        mUploads=new ArrayList<>();

        mAdapter =new ImageAdapter(outcity.this,mUploads);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemCickListene(outcity.this);

        mStorage=FirebaseStorage.getInstance();
        mDatabaseRef= FirebaseDatabase.getInstance().getReference("Outofcityuploads/"+ FirebaseAuth.getInstance().getCurrentUser().getUid().toString());

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
                Toast.makeText(outcity.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();
                mProgressCircle.setVisibility(View.INVISIBLE);

            }

        });
    }

    @Override
    public void OnItemClick(int position) {
        Toast.makeText(outcity.this,"Normal click at position",Toast.LENGTH_LONG).show();
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
                Toast.makeText(outcity.this,"Item deleted",Toast.LENGTH_SHORT).show();
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
        Intent intent=new Intent(outcity.this,viewprofile.class);
        startActivity(intent);
    }
    public void gotoaddphoto(View view)
    {
        Intent intent=new Intent(outcity.this,uploadimages.class);
        startActivity(intent);
    }
    public void incityfunc(View view)
    {
        Intent intent=new Intent(outcity.this,timeline.class);
        startActivity(intent);
    }

    public void outcityfunc(View view)
    {
        Intent intent=new Intent(outcity.this,outcity.class);
        startActivity(intent);
    }
    public void outstatefunc(View view)
    {
        Intent intent=new Intent(outcity.this,outstate.class);
        startActivity(intent);
    }
    public void gotoincityfunc2(View view)
    {
        Intent intent=new Intent(outcity.this,timeline.class);
        startActivity(intent);
    }
    public void gotooutcityfunc2(View view)
    {

    }
    public void gotoforeignfunc2(View view)
    {
        Intent intent=new Intent(outcity.this,outstate.class);
        startActivity(intent);
    }
    public void mainfunc(View view)
    {
        Intent intent=new Intent(outcity.this,home.class);
        startActivity(intent);
    }
    public void vfunc(View view)
    {
        Intent intent=new Intent(outcity.this,outcityvideos.class);
        startActivity(intent);
    }
    public void wishlistfunc(View view)
    {
        Intent intent=new Intent(outcity.this,wishlist.class);
        startActivity(intent);
    }

}
