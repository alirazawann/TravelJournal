package com.example.aliia.traveljournal;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class uploadimages extends AppCompatActivity {




    private UploadTask uploadTask;

    private static final int PICK_IMAGE_REQUEST=1;
    private Button mButtonChooseImage;
    private Button mButtonChooseVideo;
    private Button mButtonUpload;
    private TextView mTextViewShowUploads;
    private EditText mEditTextFileName;
    private ImageView mImageView;
    private VideoView mVideoView;
    private ProgressBar mProgressBar;
            RadioGroup radioGroup;
            RadioButton radioButton;

    private Uri mImageUri;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadimages);

        radioGroup= findViewById(R.id.rGroup);

        mButtonChooseImage=findViewById(R.id.button11);
        mButtonUpload=findViewById(R.id.button12);
        mTextViewShowUploads=findViewById(R.id.textView39);
        mEditTextFileName=findViewById(R.id.editText12);
        mImageView=findViewById(R.id.imageView);
        mProgressBar=findViewById(R.id.progress_bar);

        //mStorageRef= FirebaseStorage.getInstance().getReference("uploads");
        //mDatabaseRef= FirebaseDatabase.getInstance().getReference("uploads");


        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openFileChooser();
            }
        });


        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadfile();

            }
        });
        mTextViewShowUploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagesActivity();

            }
        });

    }

    private String getFileExtensio(Uri uri)
    {
        ContentResolver cR= getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));

    }
    private void uploadfile() {
        int radioId=radioGroup.getCheckedRadioButtonId();
        radioButton=findViewById(radioId);
        final ProgressDialog progressDialog=new ProgressDialog(uploadimages.this);
        progressDialog.setTitle("Uploading");
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        if(mImageUri!=null)
        {
            if(radioButton.getText().equals("Within city trips"))
            {
                mStorageRef= FirebaseStorage.getInstance().getReference("uploads");
                mDatabaseRef= FirebaseDatabase.getInstance().getReference("uploads");

            }
            else if(radioButton.getText().equals("Out of city trips"))
            {
                mStorageRef= FirebaseStorage.getInstance().getReference("Outofcityuploads");
                mDatabaseRef= FirebaseDatabase.getInstance().getReference("Outofcityuploads");
            }
            else if(radioButton.getText().equals("Out of state trips"))
            {
                mStorageRef= FirebaseStorage.getInstance().getReference("Outofstateuploads");
                mDatabaseRef= FirebaseDatabase.getInstance().getReference("Outofstateuploads");
            }
            else
            {
                mStorageRef= FirebaseStorage.getInstance().getReference("wishlistimages");
                mDatabaseRef= FirebaseDatabase.getInstance().getReference("wishlistimages");
            }

            final StorageReference fileRefence=mStorageRef.child(System.currentTimeMillis()+"."+getFileExtensio(mImageUri));
            //final String fileconext=fileRefence.toString();
            //final StorageReference ref=mStorageRef.child("uploads");
            uploadTask=fileRefence.putFile(mImageUri);
            Task<Uri> urlTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful())
                    {
                       // throw task.getException();
                        throw Objects.requireNonNull(task.getException());
                    }
                    return fileRefence.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful())
                    {

                        Uri downloadUri=task.getResult();
                        String miUrlOk=downloadUri.toString();
                        Upload upload=new Upload(mEditTextFileName.getText().toString().trim(),miUrlOk);






                        if(radioButton.getText().equals("Within city trips"))
                        {
                            mStorageRef= FirebaseStorage.getInstance().getReference("uploads");
                            mDatabaseRef= FirebaseDatabase.getInstance().getReference("uploads/"+FirebaseAuth.getInstance().getCurrentUser().getUid().toString());

                        }
                        else if(radioButton.getText().equals("Out of city trips"))
                        {
                            mStorageRef= FirebaseStorage.getInstance().getReference("Outofcityuploads");
                            mDatabaseRef= FirebaseDatabase.getInstance().getReference("Outofcityuploads/"+FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
                        }
                        else if(radioButton.getText().equals("Out of state trips"))
                        {
                            mStorageRef= FirebaseStorage.getInstance().getReference("Outofstateuploads");
                            mDatabaseRef= FirebaseDatabase.getInstance().getReference("outofstateuploads/"+FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
                        }

                        else
                        {
                            mStorageRef= FirebaseStorage.getInstance().getReference("wishlistimages");
                            mDatabaseRef= FirebaseDatabase.getInstance().getReference("wishlistimages/"+FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
                        }




                        //mDatabaseRef= FirebaseDatabase.getInstance().getReference("uploads/"+FirebaseAuth.getInstance().getCurrentUser().getUid().toString());

                        String uploadId=mDatabaseRef.push().getKey();
                        mDatabaseRef.child(uploadId).setValue(upload).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                Toast.makeText(uploadimages.this,"added",Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();

                            }
                        });
                    }
                    else
                    {

                        progressDialog.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(uploadimages.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            });

        }
        else{
            Toast.makeText(this,"No File Selected",Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }

    }

    private void openFileChooser()
    {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== PICK_IMAGE_REQUEST && resultCode== RESULT_OK && data!=null && data.getData()!=null)
        {
            mImageUri=data.getData();

            Picasso.get().load(mImageUri).into(mImageView);

        }
    }
    private void openImagesActivity()
    {
        Intent intent=new Intent(uploadimages.this,timeline.class);
        startActivity(intent);
    }
    public void checkbutton(View view)
    {

        int radioId=radioGroup.getCheckedRadioButtonId();
        radioButton=findViewById(radioId);
    }
    public void funcuploadingvideo(View view)
    {
        Intent intent=new Intent(uploadimages.this,uploadvideos.class);
        startActivity(intent);
    }
}
