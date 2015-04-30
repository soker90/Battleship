package edu.uclm.esi.common.androidClient.activities;

import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.maco.tresenraya.GameListActivity;
import com.maco.tresenraya.R;

import edu.uclm.esi.common.androidClient.domain.Store;
import edu.uclm.esi.common.androidClient.domain.User;
import edu.uclm.esi.common.androidClient.sockets.SocketListener;


public class MainMenu extends ActionBarActivity implements OnClickListener {

	private ConnectionResult connectionResult;
	private SignInButton signinButton;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Store.get().setCurrentContext(this);
		SocketListener socket;
		try {
			socket = SocketListener.get();
			socket.startListening();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        setContentView(R.layout.activity_main_menu);
		this.signinButton=(SignInButton) this.findViewById(R.id.sign_in_button);
		this.signinButton.setOnClickListener(this);
    }

    public void login(View view) {
    	Intent i=new Intent(this, LoginActivity.class);
    	startActivity(i);
    }
    
    public void createAccount(View view) {
    	Intent i=new Intent(this, CreateAccountActivity.class);
    	startActivity(i);
    }


	public void onConnectionFailed(ConnectionResult result) {
		if (result.hasResolution()) {
            try {
                result.startResolutionForResult(this, 9000);
            } catch (SendIntentException e) {

            }
        }
        this.connectionResult = result;
	}


	public void onConnected(Bundle arg0) {
		try {
			Intent i=new Intent(this, GameListActivity.class);
			startActivity(i);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void onDisconnected() {
		Toast.makeText(this, "Unconnected.", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onClick(View v) {

	}
}
