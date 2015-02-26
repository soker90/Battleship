package edu.uclm.esi.common.androidClient.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.maco.tresenraya.R;

import edu.uclm.esi.common.androidClient.domain.Store;
import edu.uclm.esi.common.androidClient.domain.User;

public class CreateAccountActivity extends ActionBarActivity {
	private EditText etEmail, etPwd1, etPwd2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Store.get().setCurrentContext(this);
		setContentView(R.layout.activity_create_account);
		
		this.etEmail=(EditText) this.findViewById(R.id.editTextEmail);
		this.etPwd1=(EditText) this.findViewById(R.id.editTextPwd1);
		this.etPwd2=(EditText) this.findViewById(R.id.editTextPwd2);
	}

	public void register(View view) {
		try {
			User.insert(this.etEmail.getText().toString(), this.etPwd1.getText().toString(), this.etPwd2.getText().toString());
			Toast.makeText(this, "Account created", Toast.LENGTH_LONG).show();
		}
		catch (Exception e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
}
