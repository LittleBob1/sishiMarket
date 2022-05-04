package com.DaedStudio.markets;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public static int total;
    TextView totalPrice;
    private static MainActivity mInstanceActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        totalPrice = findViewById(R.id.totalPrice);

        mInstanceActivity = this;


        ArrayList<Product> products = new ArrayList<Product>();
        if(products.size()==0){
            products.add(new Product("Филадельфия", "шт.", "59 руб.", R.drawable.fila));
            products.add(new Product("Калифорния", "шт.", "39 руб.", R.drawable.kalif));
            products.add(new Product("Сэнсэй ролл", "шт.", "29 руб.", R.drawable.sensey));
            products.add(new Product("Горячие\nроллы", "шт.", "59 руб.", R.drawable.hotroll));
            products.add(new Product("Агиро ролл", "шт.", "99 руб.", R.drawable.agiro));
            products.add(new Product("Киото ролл", "шт.", "89 руб.", R.drawable.kioto));
            products.add(new Product("Ясай маки", "шт.", "49 руб.", R.drawable.ysay));
            products.add(new Product("Сет \"AllRolls\"\n1 кг.", "шт.", "1099 руб.", R.drawable.allrol));
        }

        ListView productList = findViewById(R.id.productList);
        ProductAdapter adapter = new ProductAdapter(this, R.layout.list_item, products);
        productList.setAdapter(adapter);
    }

    public void setTotal(){
        totalPrice = findViewById(R.id.totalPrice);
        Object s = total;
        totalPrice.setText("Цена: " + s.toString() + " руб.");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mInstanceActivity = null;
    }
    public static MainActivity getmInstanceActivity() {
        return mInstanceActivity;
    }

    public void shopBtn(View view){

    }
}