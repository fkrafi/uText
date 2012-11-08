package com.therap.javafest.utext;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class UserVerificationActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_verification);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_user_verification, menu);
        return true;
    }
}
