package com.example.app_bateaux;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MenuActivity extends Activity {

    private String user="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        user = (String)this.getIntent().getExtras().get("User");

        TextView textView = (TextView)findViewById(R.id.textViewUser);
        textView.setText(user);
    }
}
