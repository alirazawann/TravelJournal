package com.example.aliia.traveljournal;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public class profile extends AppCompatActivity {

    private static final int CHOOSE_IMAGE = 101;
    ImageView inputimage;
    EditText inputname,inputgender,inputcity,inputcountry,inputage,inputphone,inputemail;
    Button update;
  // private String image;
    String downloadURL="";

    private UploadTask uploadTask;

    Uri filepath;
    String profileImageUrl;
    String currentUserid;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef,userRef1;
    private StorageReference storageReference;
    FirebaseStorage storage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();


        mAuth=FirebaseAuth.getInstance();
        currentUserid=mAuth.getCurrentUser().getUid();
        usersRef=FirebaseDatabase.getInstance().getReference().child("DP").child(currentUserid);
        //userRef1=FirebaseDatabase.getInstance().getReference().child("DP").child(currentUserid);





        inputimage=(ImageView) findViewById(R.id.imageView);
        inputname=(EditText) findViewById(R.id.editText);
        inputgender=(EditText) findViewById(R.id.editText9);
        inputcity=(EditText) findViewById(R.id.editText6);
        inputcountry=(EditText) findViewById(R.id.editText7);
        inputage=(EditText) findViewById(R.id.editText8);
        inputemail=(EditText) findViewById(R.id.editText10);
        inputphone=(EditText) findViewById(R.id.editText11);
        update=(Button) findViewById(R.id.button7);











        inputimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageChooser();

            }
        });

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {
                    String image=dataSnapshot.child("Profile images").getValue().toString();


                    Log.d("Result",image);
                    Picasso.get().load(image).placeholder(R.drawable.c).fit()
                            .centerCrop().into(inputimage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        usersRef=FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserid);

        usersRef.addValueEventListener(new ValueEventListener() {
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

                    inputname.setText(n,TextView.BufferType.EDITABLE);
                    inputage.setText(a,TextView.BufferType.EDITABLE);
                    inputemail.setText(e,TextView.BufferType.EDITABLE);
                    inputphone.setText(p,TextView.BufferType.EDITABLE);
                    inputcountry.setText(co,TextView.BufferType.EDITABLE);
                    inputcity.setText(ci,TextView.BufferType.EDITABLE);
                    inputgender.setText(g,TextView.BufferType.EDITABLE);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                final ProgressDialog progressDialog=new ProgressDialog(profile.this);
                progressDialog.setTitle("Loading data");
                progressDialog.setMessage("Loading...");
                progressDialog.show();
                update();
                updateImage();
                progressDialog.dismiss();



            }
        });


    }

    private void updateImage() {


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filepath = data.getData();
            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);

                inputimage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }


            if (filepath != null) {
                final StorageReference ref = storageReference.child("images/" + currentUserid.toString());
                uploadTask = ref.putFile(filepath);
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            // throw task.getException();
                            throw Objects.requireNonNull(task.getException());
                        }
                        return ref.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {

                            Uri downloadUri = task.getResult();
                            String miUrlOk = downloadUri.toString();


                            usersRef=FirebaseDatabase.getInstance().getReference().child("DP").child(currentUserid);
                            usersRef.child("Profile images").setValue(miUrlOk)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()) {
                                                Intent intent = new Intent(profile.this, profile.class);
                                                //startActivity(intent);
                                                Toast.makeText(profile.this, "image stored in DB", Toast.LENGTH_LONG).show();

                                            } else {
                                                String message = task.getException().getMessage();
                                                Toast.makeText(profile.this, "Error in Stroring image in DB" + message, Toast.LENGTH_LONG).show();

                                            }
                                        }
                                    });


                        }
                    }


                });


            }


        }
    }
    private void update() {

        String displayname = inputname.getText().toString().trim();
        String displayphone = inputphone.getText().toString().trim();
        String displayage = inputage.getText().toString().trim();
        String displayemail = inputemail.getText().toString().trim();
        String displaycity = inputcity.getText().toString().trim();
        String displaycountry = inputcountry.getText().toString().trim();
        String displaygender = inputgender.getText().toString().trim();



        if (!TextUtils.isEmpty(displayname) && !TextUtils.isEmpty(displayphone) && !TextUtils.isEmpty(displayage) && !TextUtils.isEmpty(displayemail) && !TextUtils.isEmpty(displaycity) && !TextUtils.isEmpty(displaycountry) && !TextUtils.isEmpty(displaygender)  )
        {

            /*
            if(image==null)
            {
                image="";
            }*/
            User u=new User(displayname,displayemail,displaycity,displaycountry,displayphone,displaygender,displayage);

            FirebaseDatabase.getInstance().getReference("Users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .setValue(u).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(profile.this,"added",Toast.LENGTH_SHORT).show();

                    }
                    else
                    {

                    }
                }
            });


        }
        else
        {
            Toast.makeText(this,"Not added",Toast.LENGTH_SHORT).show();

        }






    }




    private void showImageChooser()
    {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select image"),CHOOSE_IMAGE);

    }

}
