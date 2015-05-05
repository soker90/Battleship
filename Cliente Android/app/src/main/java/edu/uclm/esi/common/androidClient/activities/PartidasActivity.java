package edu.uclm.esi.common.androidClient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.maco.tresenraya.GameListActivity;
import com.maco.tresenraya.HundirLaFlotaActivity;
import com.maco.tresenraya.R;

import edu.uclm.esi.common.androidClient.domain.Store;
import edu.uclm.esi.common.androidClient.domain.User;

public class PartidasActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Store.get().setCurrentContext(this);
		setContentView(R.layout.activity_partidas);
		

	}

	public void login(View view) {

			Intent i=new Intent(this, HundirLaFlotaActivity.class);
			startActivity(i);

	}
	
}
