package com.DaedStudio.markets;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class createShop extends AppCompatActivity {

    private String shop;

    private TextView textShop;
    private EditText fio;
    private EditText numb;

    SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_shop);

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
        fio = (EditText) findViewById(R.id.fio);
        numb = (EditText) findViewById(R.id.numb);
        textShop.setText("Ваш заказ:\n" + shop + "\n\nИтого: " + MainActivity.total + " руб.");
    }

    public void trueBuy(View view){
        if(fio.getText().length() > 3 && numb.getText().length() > 3 && MainActivity.total > 0){
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
            startActivity(cr);
            finish();
        }else{
            Toast.makeText(this, "Введите данные или выберите товары!", Toast.LENGTH_SHORT).show();
        }
    }

    public void cancel(View view){
        MainActivity.total = 0;
        Intent cr = new Intent(this, MainActivity.class);
        startActivity(cr);
        finish();
    }
}