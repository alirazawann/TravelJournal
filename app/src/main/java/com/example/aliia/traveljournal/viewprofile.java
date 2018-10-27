package com.example.aliia.traveljournal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


public class viewprofile extends AppCompatActivity {





    private DatabaseReference UserRef;
    private FirebaseAuth mAuth;
    private String currentUserid;
    TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7;
    ImageView iV;

    String im;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewprofile);

        mAuth=FirebaseAuth.getInstance();
        currentUserid=mAuth.getCurrentUser().getUid();
        UserRef=FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserid);



        iV=(ImageView) findViewById(R.id.imageView2);
        tv1=(TextView) findViewById(R.id.textView8);
        tv2=(TextView) findViewById(R.id.textView11);
        tv3=(TextView) findViewById(R.id.textView12);
        tv4=(TextView) findViewById(R.id.textView13);
        tv5=(TextView) findViewById(R.id.textView14);
        tv6=(TextView) findViewById(R.id.textView15);
        tv7=(TextView) findViewById(R.id.textView23);
        iV=(ImageView) findViewById(R.id.imageView2) ;

        final ProgressDialog progressDialog=new ProgressDialog(viewprofile.this);
        progressDialog.setTitle("Loading data");
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if(dataSnapshot.exists())
                {



                    String n=dataSnapshot.child("name").getValue().toString();
                    String a=dataSnapshot.child("age").getValue().toString();
                    String e=dataSnapshot.child("email").getValue().toString();
                    String p=dataSnapshot.child("phone").getValue().toString();
                    String co=dataSnapshot.child("country").getValue().toString();
                    String ci=dataSnapshot.child("city").getValue().toString();
                    String g=dataSnapshot.child("gender").getValue().toString();

                    tv1.setText(n);
                    tv2.setText(a);
                    tv3.setText(e);
                    tv4.setText(p);
                    tv5.setText(co);
                    tv6.setText(ci);
                    tv7.setText(g);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        UserRef=FirebaseDatabase.getInstance().getReference().child("DP").child(currentUserid);
        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {


                        String image=dataSnapshot.child("Profile images").getValue().toString();


                        Log.d("Result",image);
                        Picasso.get().load(image).placeholder(R.drawable.c).fit()
                                .centerCrop().into(iV);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        progressDialog.dismiss();


    }
    public void signoutfunc(View view)
    {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        FirebaseAuth.getInstance().signOut();


        finish();
        startActivity(new Intent(viewprofile.this,MainActivity.class));
    }
    public void editprofile(View view)
    {
        Intent intent=new Intent(viewprofile.this,profile.class);
        startActivity(intent);
    }

}
