package com.therap.javafest.utext;

import greendroid.app.GDActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class BackUpActivity extends GDActivity implements OnClickListener {
	Button bLogIn, bRegister;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarContentView(R.layout.activity_back_up);
		renderView();
	}

	private void renderView() {
		bLogIn = (Button) findViewById(R.id.bLogIn);
		bLogIn.setOnClickListener(this);
		bRegister = (Button) findViewById(R.id.bRegister);
		bRegister.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.bLogIn:
			startActivity(new Intent(this, LogInActivity.class));
			break;
		case R.id.bRegister:
			startActivity(new Intent(this, RegisterActivity.class));
			break;
		}
	}

}
