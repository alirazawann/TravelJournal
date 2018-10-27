package com.example.aliia.traveljournal;

import android.content.Intent;
import android.gesture.Gesture;
import android.net.Uri;
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
import android.widget.VideoView;

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

public class videowishlist extends AppCompatActivity implements VideoAdapter.OnItemClickListener,GestureDetector.OnGestureListener{

    private RecyclerView mRecyclerView;
    private VideoAdapter mAdapter;

    float x1,x2,y1,y2;

    private GestureDetectorCompat detector;

    private ProgressBar mProgressCircle;
    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;

    private List<Upload> mUploads;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videowishlist);

        //gentureObject =new GestureDetectorCompat(timeline.this,new LearnGesture());

        mRecyclerView=findViewById(R.id.recycler_view417);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mProgressCircle=findViewById(R.id.progress_circle);

        mUploads=new ArrayList<>();

        mAdapter =new VideoAdapter(videowishlist.this,mUploads);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemCickListene(videowishlist.this);
        mStorage=FirebaseStorage.getInstance();
        mDatabaseRef= FirebaseDatabase.getInstance().getReference("wishlistvideos/"+ FirebaseAuth.getInstance().getCurrentUser().getUid().toString());

        mDBListener=mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mUploads.clear();
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren())
                {
                    Upload upload=postSnapshot.getValue(Upload.class);
                    upload.setKey(postSnapshot.getKey());

                    mUploads.add(upload);
                    /*Uri u1=Uri.parse("https://firebasestorage.googleapis.com/v0/b/traveljournal-d70b2.appspot.com/o/videouploads%2F1540597826596.mp4?alt=media&token=31f9ef65-ec9b-4c5a-b38c-738cc80fd1c2");
                    v1.setVideoURI(u1);
                    v1.requestFocus();
                    v1.start();
*/


                }

                mAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(videowishlist.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();
                mProgressCircle.setVisibility(View.INVISIBLE);

            }

        });
        detector=new GestureDetectorCompat(this,this);
    }


    @Override
    public void OnItemClick(int position) {
        Toast.makeText(videowishlist.this,"Normal click at position",Toast.LENGTH_LONG).show();
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
                Toast.makeText(videowishlist.this,"Item deleted",Toast.LENGTH_SHORT).show();
            }
        });

    }
/*
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        this.gentureObject.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
    class LearnGesture extends GestureDetector.SimpleOnGestureListener{


        @Override
        public boolean onFling(MotionEvent event1,MotionEvent event2,float velocityX,float velocityY)
        {
            if(event2.getX()> event1.getX())
            {
                Log.d("incity","in City");
                Intent intent=new Intent(timeline.this,outcity.class);
                startActivity(intent);


            } else
            if (event2.getX()<event1.getX()) {



                Log.d("outcity","out City");
                Intent intent=new Intent(timeline.this,outstate.class);
                startActivity(intent);

            }
            return true;
        }

    }
*/


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }

    public void gotoprofile(View view)
    {
        Intent intent=new Intent(videowishlist.this,viewprofile.class);
        startActivity(intent);
    }
    public void mainfunc(View view)
    {
        Intent intent=new Intent(videowishlist.this,home.class);
        startActivity(intent);

    }
    public void gotoaddphoto(View view)
    {
        Intent intent=new Intent(videowishlist.this,uploadimages.class);
        startActivity(intent);
    }
    public void incityfunc(View view)
    {
        Intent intent=new Intent(videowishlist.this,timeline.class);
        startActivity(intent);
    }

    public void outcityfunc(View view)
    {
        Intent intent=new Intent(videowishlist.this,outcity.class);
        startActivity(intent);
    }
    public void outstatefunc(View view)
    {
        Intent intent=new Intent(videowishlist.this,outstate.class);
        startActivity(intent);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        Log.d("incity","in City");
        Intent intent=new Intent(videowishlist.this,outcity.class);
        startActivity(intent);
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        detector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
    public void ocfunc(View view)
    {

    }

    public void icfunc(View view)
    {

        Intent intent=new Intent(videowishlist.this,videosview.class);
        startActivity(intent);
    }
    public void osfunc(View view)
    {
        Intent intent=new Intent(videowishlist.this,outstatevideos.class);
        startActivity(intent);
    }
    public void ifunc(View view)
    {
        Intent intent=new Intent(videowishlist.this,timeline.class);
        startActivity(intent);
    }
    public void addfunc(View view)
    {
        Intent intent=new Intent(videowishlist.this,uploadvideos.class);
        startActivity(intent);
    }
    public void homefunc(View view)
    {
        Intent intent=new Intent(videowishlist.this,home.class);
        startActivity(intent);
    }
    public void galleryfunc(View view)
    {
        Intent intent=new Intent(videowishlist.this,timeline.class);
        startActivity(intent);
    }

    public void wishlistimgfunc(View view)
    {
        Intent intent=new Intent(videowishlist.this,wishlist.class);
        startActivity(intent);
    }
    public void wishlistfunc(View view)
    {
        Intent intent=new Intent(videowishlist.this,wishlist.class);
        startActivity(intent);
    }
}
