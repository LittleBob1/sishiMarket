package com.DaedStudio.markets;

import android.graphics.drawable.Drawable;

import androidx.annotation.DrawableRes;

public class Product {
    private String name;
    private String price;
    private int count;
    private String unit;
    private int draw;

    Product(String name, String unit, String price, int draw){
        this.name = name;
        this.price = price;
        this.count = 0;
        this.unit = unit;
        this.draw = draw;
    }
    public String getUnit() {
        return this.unit;
    }
    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public String getPrice(){
        return this.price;
    }
    public int getDraw(){return this.draw;}
}
