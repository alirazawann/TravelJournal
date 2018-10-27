package com.example.aliia.traveljournal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class SignUp extends AppCompatActivity {

    private EditText inputemail,inputpassword;
    private Button signup;


    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();


        inputemail=(EditText) findViewById(R.id.editText4);
        inputpassword=(EditText) findViewById(R.id.editText5) ;
        signup=(Button) findViewById(R.id.button5);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


              String email=inputemail.getText().toString().trim();
              String password=inputpassword.getText().toString().trim();


              if(TextUtils.isEmpty(email))
              {
                  Toast.makeText(getApplicationContext(),"Enter Email",Toast.LENGTH_SHORT).show();
                  return;
              }
              if(TextUtils.isEmpty(password))
              {
                  Toast.makeText(getApplicationContext(),"Enter Password",Toast.LENGTH_SHORT).show();
                  return;
              }

              if(password.length()<6)
              {
                  Toast.makeText(getApplicationContext(),"Enter 6 char's atleast",Toast.LENGTH_SHORT).show();
                  return;
              }

                final ProgressDialog progressDialog=new ProgressDialog(SignUp.this);
                progressDialog.setTitle("Signing Up");
                progressDialog.setMessage("Loading...");
                progressDialog.show();
              mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                  @Override
                  public void onComplete(@NonNull Task<AuthResult> task) {

                      if(task.isSuccessful())
                      {
                          Toast.makeText(getApplicationContext(),"User Registered Successfully",Toast.LENGTH_SHORT).show();

                          Intent intent=new Intent(SignUp.this,profile.class);
                          progressDialog.dismiss();
                          startActivity(intent);

                      }
                      else {

                              if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                  Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();
                                  progressDialog.dismiss();
                              }
                              else
                              {
                                  Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                  progressDialog.dismiss();

                              }
                      }

                  }
              });


            }
        });



    }

}
