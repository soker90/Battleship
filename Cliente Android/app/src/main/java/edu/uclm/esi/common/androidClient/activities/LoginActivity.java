package edu.uclm.esi.common.androidClient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.maco.tresenraya.GameListActivity;
import com.maco.tresenraya.R;

import edu.uclm.esi.common.androidClient.domain.Store;
import edu.uclm.esi.common.androidClient.domain.User;

public class LoginActivity extends ActionBarActivity {
	private EditText etLogin;
	private EditText etPwd;
	private TextView tvErrorMsg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Store.get().setCurrentContext(this);
		setContentView(R.layout.activity_login);
		
		this.etLogin=(EditText) this.findViewById(R.id.editTextLogin);
		this.etPwd=(EditText) this.findViewById(R.id.editTextPwd);
		this.tvErrorMsg=(TextView) this.findViewById(R.id.textViewErrorMsg);
	}

	public void login(View view) {
		try {
			this.tvErrorMsg.setVisibility(View.GONE);
			this.tvErrorMsg.setText("");
			User user=User.identify(this.etLogin.getText().toString(), this.etPwd.getText().toString());
			Toast.makeText(this, "Welcome, " + user.getEmail(), Toast.LENGTH_LONG).show();
			Intent i=new Intent(this, GameListActivity.class);
			startActivity(i);
		} catch (Exception e) {
			this.tvErrorMsg.setVisibility(View.VISIBLE);
			this.tvErrorMsg.setText(e.getMessage());
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
}
