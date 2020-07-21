package com.smile.atozdelivery.controller;

import android.content.Context;
import android.content.SharedPreferences;

public class Control {

    public Context context;
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;

    public Control(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    public void adduid(String uid){
        editor.putString("uid",uid).apply();
    }

    public String getuid(){
        return sharedPreferences.getString("uid","none");
    }

    public void addName(String name){
        editor.putString("name",name).apply();
    }

    public String getname(){
        return sharedPreferences.getString("name",null);
    }

    public void addcno(String cno){
        editor.putString("cno",cno).apply();
    }

    public String getcno(){
        return sharedPreferences.getString("cno","none");
    }

    public void addsts(String sts){
        editor.putString("log",sts).apply();
    }

    public String getsts(){
        return sharedPreferences.getString("log","none");
    }

    public void addwork(){
        editor.putString("work","login").apply();
    }

    public String getwork(){
        return sharedPreferences.getString("work",null);
    }

    public void logoutwork(){
        editor.putString("work","logout").apply();
    }

    public void clearmemory(){
        editor.clear().apply();
    }

}
