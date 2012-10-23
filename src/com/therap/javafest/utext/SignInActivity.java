package com.therap.javafest.utext;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class SignInActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_sign_in, menu);
        return true;
    }
}
