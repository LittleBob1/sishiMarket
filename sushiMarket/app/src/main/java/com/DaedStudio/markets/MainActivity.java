package com.DaedStudio.markets;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public static int total;
    private TextView totalPrice;
    private static MainActivity mInstanceActivity;
    private ArrayList<Product> products;
    private ArrayList<Product> dop;
    private ArrayList<Product> removeProd;
    private ListView productList;
    private ProductAdapter adapter;
    private Button shop;
    private Button buy;
private Button news;

    private FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference myRef;

    private String[] sitting = {"Меню", "История заказов", "Роллы", "Бургеры", "Другое"};

    private String sales = "";

    private Spinner spin;

    SharedPreferences mSettings;

    private boolean isShop = false;

    @SuppressLint("ResourceAsColor")
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
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        totalPrice = findViewById(R.id.totalPrice);

        mInstanceActivity = this;

        mSettings = getSharedPreferences("history", Context.MODE_PRIVATE);

        removeProd = new ArrayList<Product>();
        products = new ArrayList<Product>();
        dop = new ArrayList<Product>();
        news = (Button)findViewById(R.id.newSS);
        shop = (Button) findViewById(R.id.button);
        buy = (Button) findViewById(R.id.button2);
        spin = (Spinner) findViewById(R.id.spinner);
        spinSett();

        if(products.size() == 0){
            products.add(new Product("Филадельфия", "шт.", "59 руб.", R.drawable.fila));
            products.add(new Product("Калифорния", "шт.", "39 руб.", R.drawable.kalif));
            products.add(new Product("Сэнсэй ролл", "шт.", "29 руб.", R.drawable.sensey));
            products.add(new Product("Горячие роллы", "шт.", "59 руб.", R.drawable.hotroll));
            products.add(new Product("Агиро ролл", "шт.", "99 руб.", R.drawable.agiro));
            products.add(new Product("Киото ролл", "шт.", "89 руб.", R.drawable.kioto));
            products.add(new Product("Ясай маки", "шт.", "49 руб.", R.drawable.ysay));
            products.add(new Product("Сет \"AllRolls\" 1 кг.", "шт.", "1099 руб.", R.drawable.allrol));
            products.add(new Product("Черная мамба", "шт", "409 руб.", R.drawable.black));
            products.add(new Product("Сырочек", "шт", "599 руб.", R.drawable.ches));
            products.add(new Product("Сытный", "шт", "399 руб.", R.drawable.bad));
            products.add(new Product("Классический", "шт", "499 руб.", R.drawable.burg));

            products.add(new Product("Картошка фри", "шт,", "150 руб.", R.drawable.potate));
            products.add(new Product("Крылышки", "шт,", "49 руб.", R.drawable.kril));
            products.add(new Product("Кока-кола", "шт,", "79 руб.", R.drawable.kola));
            products.add(new Product("Вода", "шт,", "69 руб.", R.drawable.water));
            products.add(new Product("Сырный соус", "шт,", "49 руб.", R.drawable.cheese));
            products.add(new Product("Кетчуп", "шт,", "49 руб.", R.drawable.ketchup));
            products.add(new Product("Булка", "шт,", "39 руб.", R.drawable.bulka));

            dop.add(new Product("Филадельфия", "шт.", "59 руб.", R.drawable.fila));
            dop.add(new Product("Калифорния", "шт.", "39 руб.", R.drawable.kalif));
            dop.add(new Product("Сэнсэй ролл", "шт.", "29 руб.", R.drawable.sensey));
            dop.add(new Product("Горячие роллы", "шт.", "59 руб.", R.drawable.hotroll));
            dop.add(new Product("Агиро ролл", "шт.", "99 руб.", R.drawable.agiro));
            dop.add(new Product("Киото ролл", "шт.", "89 руб.", R.drawable.kioto));
            dop.add(new Product("Ясай маки", "шт.", "49 руб.", R.drawable.ysay));
            dop.add(new Product("Сет \"AllRolls\" 1 кг.", "шт.", "1099 руб.", R.drawable.allrol));
            dop.add(new Product("Черная мамба", "шт", "409 руб.", R.drawable.black));
            dop.add(new Product("Сырочек", "шт", "599 руб.", R.drawable.ches));
            dop.add(new Product("Сытный", "шт", "399 руб.", R.drawable.bad));
            dop.add(new Product("Классический", "шт", "499 руб.", R.drawable.burg));

            dop.add(new Product("Картошка фри", "шт,", "150 руб.", R.drawable.potate));
            dop.add(new Product("Крылышки", "шт,", "49 руб.", R.drawable.kril));
            dop.add(new Product("Кока-кола", "шт,", "79 руб.", R.drawable.kola));
            dop.add(new Product("Вода", "шт,", "69 руб.", R.drawable.water));
            dop.add(new Product("Сырный соус", "шт,", "49 руб.", R.drawable.cheese));
            dop.add(new Product("Кетчуп", "шт,", "49 руб.", R.drawable.ketchup));
            dop.add(new Product("Булка", "шт,", "39 руб.", R.drawable.bulka));
        }

        productList = findViewById(R.id.productList);
        adapter = new ProductAdapter(this, R.layout.list_item, products);
        productList.setAdapter(adapter);
        for(int i = 0; i< products.size(); i++){
            Product product = products.get(i);
            adapter.remove(product);
            removeProd.add(product);
            i--;
        }

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Sales");

        Query my = myRef;
        my.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                sales += "-" + snapshot.getValue().toString() + "\n\n";
                news.setBackgroundColor(Color.RED);
                Bundle b = getIntent().getExtras();
                if(b != null){
                    news.setBackgroundColor(R.color.blackFive);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }

    public void setTotal(){
        totalPrice = findViewById(R.id.totalPrice);
        Object s = total;
        totalPrice.setText("Итого: " + s.toString() + " руб.");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mInstanceActivity = null;
    }
    public static MainActivity getmInstanceActivity() {
        return mInstanceActivity;
    }

    public void shopBtn(View view) {
        isShop = !isShop;
        boolean isRepeat = false;

        if(isShop) {
            adapter.clear();
            for (int i = 0; i < removeProd.size(); i++) {
                Product product = removeProd.get(i);
                if (product.getCount() > 0) {
                    adapter.add(product);
                }
            }
            shop.setText("Обратно");
            buy.setVisibility(View.VISIBLE);
            spin.setVisibility(View.INVISIBLE);
        }else{
            removeProd.clear();
            spin.setSelection(0);
            for(int i = 0; i < adapter.getCount(); i++){
                Product product = adapter.getItem(i);
                    removeProd.add(product);
                    adapter.remove(product);
                    i--;
            }
            for(int i = 0; i < dop.size(); i++){
                Product product = dop.get(i);
                for(int j = 0; j < removeProd.size(); j++){
                    Product pr = removeProd.get(j);
                    if (product.getName().equals(pr.getName())) {
                        isRepeat = true;
                    }
                }

                if(isRepeat == false){
                    removeProd.add(product);
                }
                isRepeat = false;
            }
            shop.setText("Корзина");
            spin.setVisibility(View.VISIBLE);
            buy.setVisibility(View.INVISIBLE);
        }


    }
    public void buySusBtn(View view){
        String s = "";
        for(int i = 0; i < adapter.getCount(); i++) {
            Product product = adapter.getItem(i);
            s = s + "\n" + product.getName() + " " + product.getCount() + "шт. " + product.getPrice();
        }
        Intent cr;
        cr = new Intent(this, createShop.class);
        cr.putExtra("allRol", s);
        startActivity(cr);
    }

    private void spinSett(){
        ArrayAdapter<String> adaapter;
        adaapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sitting);
        adaapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adaapter);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) parent.getItemAtPosition(position);
                if(item.equals("История заказов")){
                    spin.setSelection(0);
                    fsff();
                }
                if(item.equals("Роллы")){
                    rolls();
                }
                if(item.equals("Бургеры")){
                    burgers();
                }
                if(item.equals("Другое")){
                    others();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){
                spin.setSelection(0);
            }
        };
        spin.setOnItemSelectedListener(itemSelectedListener);
    }

    public void fsff(){
        Intent cr = new Intent(this, histAct.class);
        startActivity(cr);
    }

    public void rolls(){
        for(int i = 0; i < adapter.getCount(); i++){
            Product product = adapter.getItem(i);
            adapter.remove(product);
            i--;
        }
        for(int i = 0; i < removeProd.size(); i++){
            Product product = removeProd.get(i);
            if(product.getUnit().equals("шт.")){
                adapter.add(product);
            }
        }
    }

    public void burgers(){
        for(int i = 0; i < adapter.getCount(); i++){
            Product product = adapter.getItem(i);
            //removeProd.add(product);
            adapter.remove(product);
            i--;
        }
        for(int i = 0; i < removeProd.size(); i++){
            Product product = removeProd.get(i);
            if(product.getUnit().equals("шт")){
                adapter.add(product);
            }
        }
    }

    public void others(){
        for(int i = 0; i < adapter.getCount(); i++){
            Product product = adapter.getItem(i);
            //removeProd.add(product);
            adapter.remove(product);
            i--;
        }
        for(int i = 0; i < removeProd.size(); i++){
            Product product = removeProd.get(i);
            if(product.getUnit().equals("шт,")){
                adapter.add(product);
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    public void news(View view){
        news.setBackgroundColor(R.color.blackFive);
        Intent intent = new Intent(this, newsAct.class);
        intent.putExtra("sale", sales);
        startActivity(intent);
    }
}