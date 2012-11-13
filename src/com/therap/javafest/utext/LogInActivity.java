package com.therap.javafest.utext;

import greendroid.app.GDActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.therap.javafest.utext.backup.CloudBackUp;

public class LogInActivity extends GDActivity implements OnClickListener {

	private Button bLogIn;
	private TextView tvMsg;
	private EditText etEmail, etPassword;

	private CloudBackUp cb;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarContentView(R.layout.activity_sign_in);
		Init();
		renderView();
	}

	private void Init() {
		cb = new CloudBackUp();
	}

	private void renderView() {
		tvMsg = (TextView) findViewById(R.id.tvMsg);
		etEmail = (EditText) findViewById(R.id.etEmail);
		etPassword = (EditText) findViewById(R.id.etPassword);
		bLogIn = (Button) findViewById(R.id.bLogIn);
		bLogIn.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		int status;
		switch (view.getId()) {
		case R.id.bLogIn:
			status = cb.login(etEmail.getText().toString(), etPassword
					.getText().toString());
			if (status == 0) {
				tvMsg.setText("Invalid Email & Password Combination!");
			} else {
				startActivity(new Intent(this, DatabaseBackUpActivity.class));
				finish();
			}
			break;
		}
	}
}
