package com.therap.javafest.utext;

import greendroid.app.GDActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.therap.javafest.utext.backup.CloudBackUp;

public class RegisterActivity extends GDActivity implements OnClickListener {

	private TextView etMsg;
	private Button bRegister;
	private EditText etFullName, etEmail, etPassword;
	private CloudBackUp cb;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarContentView(R.layout.activity_register);
		Init();
		renderView();
	}

	private void Init() {
		cb = new CloudBackUp();
	}

	private void renderView() {
		bRegister = (Button) findViewById(R.id.bRegister);
		bRegister.setOnClickListener(this);

		etFullName = (EditText) findViewById(R.id.etFullName);
		etEmail = (EditText) findViewById(R.id.etEmail);
		etPassword = (EditText) findViewById(R.id.etPassword);
		etMsg = (TextView) findViewById(R.id.tvMsg);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.bRegister:
			int status = cb.register(etFullName.getText().toString(), etEmail
					.getText().toString(), etPassword.getText().toString());
			if (status == 1) {
				etMsg.setText("Registration Complete!");
			} else if (status == 2) {
				etMsg.setText("Please Fill All The Fields Correctly!");
			} else if (status == 3) {
				etMsg.setText("This Email Address All Ready Taken!");
			} else {
				etMsg.setText("Something Wrong! Please Try Again.");
			}
			break;
		}
	}
}
