package com.example.gv;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Gv gv=new Gv(this);
        gv.setMargin(50 ,20, 30, 30);
        setContentView(gv);
    }
}
