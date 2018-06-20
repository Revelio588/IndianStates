package com.indian.states.capitals.indianstates;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private Button register;
    private EditText user;
    private EditText pass;
    private EditText confpass;
    private EditText contact;
    private EditText email;
    private EditText state;
    private FirebaseAuth mAuth;
    private DatabaseReference mdb;
    private ProgressBar regProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {





        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        register= findViewById(R.id.reg_id);
        user= findViewById(R.id.user_id);
        pass= findViewById(R.id.pass_id);
        confpass= findViewById(R.id.confirmpass_id);
        contact= findViewById(R.id.contact_id);
        email= findViewById(R.id.email_id);
        state= findViewById(R.id.state_id);
        mAuth = FirebaseAuth.getInstance();
        mdb= FirebaseDatabase.getInstance().getReference();
        regProgress = findViewById(R.id.reg_progress);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add intent
                register_user();

            }
        });
    }
    private void register_user() {
        final String username,password,contact_no,emailid,states;
        username=user.getText().toString().trim();
        password=pass.getText().toString().trim();
        contact_no=contact.getText().toString().trim();
        emailid=email.getText().toString().trim();
        states=state.getText().toString().trim();

        if(!Patterns.EMAIL_ADDRESS.matcher(emailid).matches()){
            email.setError("Enter valid email address");
            email.requestFocus();
            return;
        }

        if(emailid.isEmpty()){
            email.setError("Email required");
            email.requestFocus();
            return;
        }
        if(password.isEmpty()){
            pass.setError("Password required");
            pass.requestFocus();
            return;
        }
        if(password.length()<6){
            pass.setError("Min password length should be 6");
            pass.requestFocus();
            return;
        }
        if(!password.equals(confpass.getText().toString())) {
            confpass.setError("Passwords don't match");
            confpass.requestFocus();
            return;
        }

        regProgress.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(emailid, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(getApplicationContext(), "Authentication successful.",
                                    Toast.LENGTH_SHORT).show();

                            HashMap<String,String> dmap=new HashMap<String, String>();

                            dmap.put("name",username);
                            dmap.put("password",password);
                            dmap.put("contact",contact_no);
                            dmap.put("email",emailid);
                            dmap.put("state",states);

                            mdb.push().setValue(dmap);

                            //finish();
                            /*
                            Intent intent=new Intent(getApplicationContext(),Profile.class);
                            intent.putExtra("name",username);
                            intent.putExtra("contact",contact_no);
                            intent.putExtra("email",emailid);
                            intent.putExtra("state",states);
                            startActivity(intent);*/
                            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(getApplicationContext(), "Authentication failed***",
                                    Toast.LENGTH_SHORT).show();
                            }

                        regProgress.setVisibility(View.INVISIBLE);
                    }
                });
    }


}
