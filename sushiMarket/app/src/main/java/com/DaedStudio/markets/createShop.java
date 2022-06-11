package com.DaedStudio.markets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.lights.Light;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.annotations.Nullable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class createShop extends AppCompatActivity {

    private String shop;

    private FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference myRef;

    private TextView textShop;
    private TextView full;
    private CheckBox box;
    private EditText fio;
    private EditText numb;

    private int sale = 0;
    private int ghg = 0;

    SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_shop);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        Objects.requireNonNull(getSupportActionBar()).hide();
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
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            shop = extras.getString("allRol");
        }

        mSettings = getSharedPreferences("history", Context.MODE_PRIVATE);

        textShop = (TextView) findViewById(R.id.shop);
        full = (TextView)findViewById(R.id.full);
        box = (CheckBox)findViewById(R.id.checkBox);
        fio = (EditText) findViewById(R.id.fio);
        numb = (EditText) findViewById(R.id.numb);
        textShop.setText("Ваш заказ:\n" + shop);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("AllUsers").child(user.getDisplayName());

        Object g = MainActivity.total;
        full.setText("Итого: " + g.toString());

        Log.e("sdf", myRef.toString());

        Query myQuery = myRef;
        myQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(ghg == 3) {
                    Log.e("sdf", user.getDisplayName());
                    box.setText("Использовать " + dataSnapshot.getValue().toString() + " баллов");
                    sale = Integer.parseInt(dataSnapshot.getValue().toString());
                }
                ghg++;
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(box.isChecked()){
                    if(MainActivity.total <= sale){
                        full.setText("Итого: 0");
                    }else {
                        MainActivity.total -= sale;
                        Object g = MainActivity.total;
                        full.setText("Итого: " + g.toString());
                    }
                }
                if(!box.isChecked()){
                    if(MainActivity.total <= sale){
                        Object g = MainActivity.total;
                        full.setText("Итого: " + g.toString());
                    }else {
                        MainActivity.total += sale;
                        Object g = MainActivity.total;
                        full.setText("Итого: " + g.toString());
                    }
                }

            }
        });

    }


    public void trueBuy(View view){
        if(fio.getText().length() > 3 && numb.getText().length() > 3 && MainActivity.total > 0){

            if(box.isChecked()){
                if(MainActivity.total < sale){
                    sale -= MainActivity.total;
                }else {
                    sale = 0;
                }
            }
            sale += MainActivity.total / 100 * 10;
            ArrayList<Object> fire = new ArrayList<Object>();
            fire.add(0);
            fire.add(0);
            fire.add(0);
            fire.add(sale);
            myRef.setValue(fire);

            SharedPreferences.Editor editor = mSettings.edit();
            String s = "";
            Date currentDate = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy - HH:mm", Locale.getDefault());
            String dateText = dateFormat.format(currentDate);
            if(mSettings.contains("hist")) {
                s = mSettings.getString("hist", "");
                s = s + "Ваш заказ от\n" + dateText + "\n№ " + Math.random() * 100000 + "\n" + shop + "\n\nИтого: " + MainActivity.total + " руб." + "\n\n\n";
            }else{
                s = "Ваш заказ от\n" + dateText + "\n№ " + Math.random() * 100000 + "\n" + shop + "\n\nИтого: " + MainActivity.total + " руб." + "\n\n\n";
            }
            editor.putString("hist", s);
            editor.apply();
            Toast.makeText(this, "Ожидайте", Toast.LENGTH_SHORT).show();
            MainActivity.total = 0;
            Intent cr = new Intent(this, MainActivity.class);
            cr.putExtra("fff", 0);
            startActivity(cr);
            finish();
        }else{
            Toast.makeText(this, "Введите данные или выберите товары!", Toast.LENGTH_SHORT).show();
        }
    }

    public void cancel(View view){
        MainActivity.total = 0;
        Intent cr = new Intent(this, MainActivity.class);
        cr.putExtra("fff", 0);
        startActivity(cr);
        finish();
    }
}