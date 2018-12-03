package com.example.aliia.traveljournal;

import android.content.Intent;
import android.gesture.Gesture;
import android.support.annotation.NonNull;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class home extends AppCompatActivity implements ImageAdapter.OnItemClickListener{

    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;
    private ProgressBar mProgressCircle;
    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef,databaseReference;
    private ValueEventListener mDBListener;
    public List<String> req,req2,req3;
     DatabaseReference rootRef;
     DatabaseReference usersdRef;
     int j;


    private List<Upload> mUploads;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        j=0;
        mRecyclerView=findViewById(R.id.recycler_view3);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mProgressCircle=findViewById(R.id.progress_circle3);

        mUploads=new ArrayList<>();

        mAdapter =new ImageAdapter(home.this,mUploads);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemCickListene(home.this);
        mStorage=FirebaseStorage.getInstance();

        databaseReference=FirebaseDatabase.getInstance().getReference();

        req= new ArrayList<>();
        req2= new ArrayList<>();
        req3= new ArrayList<>();





         rootRef = FirebaseDatabase.getInstance().getReference();
        // usersdRef = rootRef.child("uploads");
       //  mDatabaseRef= FirebaseDatabase.getInstance().getReference("uploads/");//+ FirebaseAuth.getInstance().getCurrentUser().getUid().toString());

        mUploads.clear();



                usersdRef = rootRef.child("uploads");



            /*if(j==1)
            {
                usersdRef = rootRef.child("Outofcityuploads");

            }
            if(j==2)
            {
                usersdRef = rootRef.child("outofstateuploads");

            }*/
            usersdRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        if (ds.exists()) {

                            String name = ds.getKey();


                                req.add(name);
                                Log.d("Image TAG req 1", name);

                           /* if(j==1) {
                                req2.add(name);
                                Log.d("Image TAG req 2", name);

                            }
                            if(j==2) {
                                req3.add(name);
                                Log.d("Image TAG req 3", name);


                            }*/
                        }

                    }



                            for (int i = 0; i < req.size(); i++) {

                                mDatabaseRef = rootRef.child("uploads/" + req.get(i) + "/");
                                mDatabaseRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        Log.d("Inside req 1", "inner");
                                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                            Upload upload = postSnapshot.getValue(Upload.class);
                                            upload.setKey(postSnapshot.getKey());
                                            mUploads.add(upload);

                                        }
                                        mAdapter.notifyDataSetChanged();

                                        mProgressCircle.setVisibility(View.INVISIBLE);


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

        usersdRef = rootRef.child("Outofcityuploads");

        usersdRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    if (ds.exists()) {

                        String name = ds.getKey();


                        req2.add(name);

                           /*



                            if(j==2) {
                                req3.add(name);
                                Log.d("Image TAG req 3", name);


                            }*/
                    }

                }



                for (int i = 0; i < req2.size(); i++) {

                    mDatabaseRef = rootRef.child("Outofcityuploads/" + req2.get(i) + "/");
                    mDatabaseRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                Upload upload = postSnapshot.getValue(Upload.class);
                                upload.setKey(postSnapshot.getKey());
                                mUploads.add(upload);

                            }
                            mAdapter.notifyDataSetChanged();

                            mProgressCircle.setVisibility(View.INVISIBLE);


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                        /*if(j==1)
                        {
                            for (int i = 0; i < req2.size(); i++) {
                                mDatabaseRef = rootRef.child("Outofcityuploads/" + req2.get(i) + "/");
                                mDatabaseRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        Log.d("Inside req 2", "inner");
                                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                            Upload upload = postSnapshot.getValue(Upload.class);
                                            upload.setKey(postSnapshot.getKey());

                                            mUploads.add(upload);
                                        }
                                        mAdapter.notifyDataSetChanged();

                                        mProgressCircle.setVisibility(View.INVISIBLE);


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }
                        if(j==2)
                        {
                            for (int i = 0; i < req3.size(); i++) {
                                mDatabaseRef = rootRef.child("outofstateuploads/" + req3.get(i) + "/");
                                mDatabaseRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        Log.d("Inside req 3", "inner");
                                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                            Upload upload = postSnapshot.getValue(Upload.class);
                                            upload.setKey(postSnapshot.getKey());

                                            mUploads.add(upload);
                                        }
                                        mAdapter.notifyDataSetChanged();

                                        mProgressCircle.setVisibility(View.INVISIBLE);

                                    }


                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }*/







            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        usersdRef = rootRef.child("outofstateuploads");

        usersdRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    if (ds.exists()) {

                        String name = ds.getKey();


                        req3.add(name);

                           /* if(j==1) {
                                req2.add(name);
                                Log.d("Image TAG req 2", name);

                            }
                            if(j==2) {
                                req3.add(name);
                                Log.d("Image TAG req 3", name);


                            }*/
                    }

                }



                for (int i = 0; i < req3.size(); i++) {

                    mDatabaseRef = rootRef.child("outofstateuploads/" + req3.get(i) + "/");
                    mDatabaseRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                Upload upload = postSnapshot.getValue(Upload.class);
                                upload.setKey(postSnapshot.getKey());
                                mUploads.add(upload);

                            }
                            mAdapter.notifyDataSetChanged();

                            mProgressCircle.setVisibility(View.INVISIBLE);


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }









            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        /*
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()) {

                    if (ds.exists()) {

                        String name = ds.getKey();
                        Log.d("Image TAG", name);
                        req.add(name);

                    }

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        usersdRef.addListenerForSingleValueEvent(eventListener);
*/






/*
        databaseReference.child("uploads").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                req.clear();
                if(dataSnapshot.exists()) {

                    Log.d("Success","success");

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        String requests = postSnapshot.getKey();
                        req.add(requests);
                    }
                }
                else
                {
                    Log.d("Error","error");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


*/

/*
        mDBListener=mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mUploads.clear();


                        for (DataSnapshot postsnapshot : dataSnapshot.getChildren()) {

                            Upload upload = postsnapshot.getValue(Upload.class);
                            upload.setKey(postsnapshot.getKey());

                            mUploads.add(upload);
                        }


              /*  for (DataSnapshot snapshot: dataSnapshot.getChildren())
                {
                    for (DataSnapshot postSnapshot: snapshot.getChildren())
                    {
                        Upload upload=postSnapshot.getValue(Upload.class);
                        upload.setKey(postSnapshot.getKey());

                        mUploads.add(upload);
                    }

                }



                mAdapter.notifyDataSetChanged();

                mProgressCircle.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(home.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();
                mProgressCircle.setVisibility(View.INVISIBLE);

            }

        });*/
    }


    @Override
    public void OnItemClick(int position) {
        Toast.makeText(home.this,"Normal click at position",Toast.LENGTH_LONG).show();
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
                Toast.makeText(home.this,"Item deleted",Toast.LENGTH_SHORT).show();
            }
        });

    }






    public void gotoprofile(View view)
    {
        Intent intent=new Intent(home.this,viewprofile.class);
        startActivity(intent);
    }
    public void mainfunc(View view)
    {

    }
    public void gotoaddphoto(View view)
    {
        Intent intent=new Intent(home.this,uploadimages.class);
        startActivity(intent);
    }
    public void incityfunc(View view)
    {
        Intent intent=new Intent(home.this,timeline.class);
        startActivity(intent);
    }

    public void outcityfunc(View view)
    {
        Intent intent=new Intent(home.this,outcity.class);
        startActivity(intent);
    }
    public void outstatefunc(View view)
    {
        Intent intent=new Intent(home.this,outstate.class);
        startActivity(intent);
    }
    public void hvidfunc(View view)
    {
        Intent intent=new Intent(home.this,homevideo.class);
        startActivity(intent);
    }
    public void wishlistfunc(View view)
    {
        Intent intent=new Intent(home.this,wishlist.class);
        startActivity(intent);
    }


}
