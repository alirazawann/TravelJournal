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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class wishlist extends AppCompatActivity implements ImageAdapter.OnItemClickListener,GestureDetector.OnGestureListener{

    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;

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
        setContentView(R.layout.activity_wishlist);

        //gentureObject =new GestureDetectorCompat(timeline.this,new LearnGesture());

        mRecyclerView=findViewById(R.id.recycler_view6);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mProgressCircle=findViewById(R.id.progress_circle6);

        mUploads=new ArrayList<>();

        mAdapter =new ImageAdapter(wishlist.this,mUploads);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemCickListene(wishlist.this);
        mStorage=FirebaseStorage.getInstance();
        mDatabaseRef= FirebaseDatabase.getInstance().getReference("wishlistimages/"+ FirebaseAuth.getInstance().getCurrentUser().getUid().toString());

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
                Toast.makeText(wishlist.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();
                mProgressCircle.setVisibility(View.INVISIBLE);

            }

        });
        detector=new GestureDetectorCompat(this,this);
    }


    @Override
    public void OnItemClick(int position) {
        Toast.makeText(wishlist.this,"Normal click at position",Toast.LENGTH_LONG).show();
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
                Toast.makeText(wishlist.this,"Item deleted",Toast.LENGTH_SHORT).show();
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
        Intent intent=new Intent(wishlist.this,viewprofile.class);
        startActivity(intent);
    }
    public void mainfunc(View view)
    {
        Intent intent=new Intent(wishlist.this,home.class);
        startActivity(intent);

    }
    public void gotoaddphoto(View view)
    {
        Intent intent=new Intent(wishlist.this,uploadimages.class);
        startActivity(intent);
    }
    public void incityfunc(View view)
    {
        Intent intent=new Intent(wishlist.this,timeline.class);
        startActivity(intent);
    }

    public void outcityfunc(View view)
    {
        Intent intent=new Intent(wishlist.this,outcity.class);
        startActivity(intent);
    }
    public void outstatefunc(View view)
    {
        Intent intent=new Intent(wishlist.this,outstate.class);
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

        Intent intent=new Intent(wishlist.this,outcity.class);
        startActivity(intent);
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        detector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
    public void gotoincity(View view)
    {

    }
    public void gotooutcity(View view)
    {
        Intent intent=new Intent(wishlist.this,outcity.class);
        startActivity(intent);
    }
    public void gotoforeign(View view)
    {
        Intent intent=new Intent(wishlist.this,outstate.class);
        startActivity(intent);
    }
    public void vfunc(View view)
    {
        Intent intent=new Intent(wishlist.this,videosview.class);
        startActivity(intent);
    }

    public void vfuncwishlist(View view)
    {
        Intent intent=new Intent(wishlist.this,videowishlist.class);
        startActivity(intent);
    }
    public void wishlistfunc(View view)
    {
        Intent intent=new Intent(wishlist.this,wishlist.class);
        startActivity(intent);
    }

}
