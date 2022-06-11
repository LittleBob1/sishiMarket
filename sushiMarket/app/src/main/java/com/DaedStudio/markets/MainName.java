package com.DaedStudio.markets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class MainName extends AppCompatActivity {

    private TextView mainName;

    private FirebaseAuth mAuth;

    private String email;
    private String password;
    private String passwordConfirmText;

    private EditText emailText;
    private EditText nameText;
    private EditText passwordText;
    private EditText passwordConfirm;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private Button signBtn;
    private Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_name);

        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View decorView = getWindow().getDecorView(); //скрыть панель навигации
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        mAuth = FirebaseAuth.getInstance();

        mainName = (TextView) findViewById(R.id.textView8);
        emailText = (EditText) findViewById(R.id.editTextTextEmailAddress);
        nameText = (EditText) findViewById(R.id.editTextTextPersonName);
        passwordText = (EditText) findViewById(R.id.editTextTextPassword);
        passwordConfirm = (EditText) findViewById(R.id.editTextTextPassword2);
        signBtn = (Button) findViewById(R.id.button55);
        registerBtn = (Button) findViewById(R.id.button54);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("AllUsers");

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            walkToCreate();
        }

    }

    public void registration(){
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("AllUsers");
        DatabaseReference userNameRef = rootRef.child(nameText.getText().toString());
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()) {
                    isNoName();
                }else{
                    Toast.makeText(MainName.this, R.string.nameExist, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("TAG", databaseError.getMessage()); //Don't ignore errors!
            }
        };
        userNameRef.addListenerForSingleValueEvent(eventListener);
    }

    public void isNoName(){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(nameText.getText().toString()).build();
                            user.updateProfile(profileUpdates);
                            myRef = database.getReference("AllUsers").child(nameText.getText().toString());
                            ArrayList<Object> fire = new ArrayList<Object>();
                            fire.add(0);
                            fire.add(0);
                            fire.add(0);
                            fire.add(0);
                            myRef.setValue(fire);
                            Toast.makeText(MainName.this, R.string.regSucess, Toast.LENGTH_LONG).show();
                            walkToCreate();
                        } else {
                            Toast.makeText(MainName.this, R.string.regError, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void signIn(){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(MainName.this, R.string.logSucess, Toast.LENGTH_LONG).show();
                            walkToCreate();
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainName.this, R.string.logFail, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void signButton(View view){
        if(signBtn.getText() != "Зарегистрироваться") {
            mainName.setText("Вход");
            nameText.setVisibility(View.INVISIBLE);
            passwordConfirm.setVisibility(View.INVISIBLE);
            signBtn.setText("Зарегистрироваться");
            registerBtn.setText("Войти");
        }else{
            mainName.setText("Регистрация");
            nameText.setVisibility(View.VISIBLE);
            passwordConfirm.setVisibility(View.VISIBLE);
            signBtn.setText("Войти");
            registerBtn.setText("Зарегистрироваться");
        }
    }

    public void registerButton(View view) {
        email = emailText.getText().toString();
        password = passwordText.getText().toString();
        passwordConfirmText = passwordConfirm.getText().toString();
        if (signBtn.getText() != "Зарегистрироваться") {
            if (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.length() > 6 && nameText.getText().toString().length() > 3) {
                if (password.equals(passwordConfirmText)) {
                    registration();
                } else {
                    Toast.makeText(MainName.this, R.string.pasNotMac, Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(MainName.this, R.string.validData, Toast.LENGTH_LONG).show();
            }
        } else {
            if (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.length() > 6) {
                signIn();
            } else {
                Toast.makeText(MainName.this, R.string.validTwo, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void walkToCreate(){
        Intent cr;
        cr = new Intent(this, MainActivity.class);
        startActivity(cr);
        finish();
    }
}