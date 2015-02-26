package com.maco.tresenraya;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.maco.tresenraya.domain.TresEnRaya;
import com.maco.tresenraya.jsonMessages.GameListMessage;

import edu.uclm.esi.common.androidClient.dialogs.Dialogs;
import edu.uclm.esi.common.androidClient.dialogs.IDialogListener;
import edu.uclm.esi.common.androidClient.domain.Store;
import edu.uclm.esi.common.androidClient.domain.User;
import edu.uclm.esi.common.androidClient.http.Proxy;
import edu.uclm.esi.common.jsonMessages.ErrorMessage;
import edu.uclm.esi.common.jsonMessages.JSONMessage;
import edu.uclm.esi.common.jsonMessages.JSONParameter;
import edu.uclm.esi.common.jsonMessages.OKMessage;

public class GameListActivity extends ActionBarActivity implements IDialogListener {
	private LinearLayout gameList;
	private Button btnSelectedGameId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Store.get().setCurrentContext(this);
		setContentView(R.layout.activity_game_list);
		this.gameList=(LinearLayout) this.findViewById(R.id.LinearLayoutGameList);
		
		Proxy proxy=Proxy.get();
		try {
			JSONMessage jsm=proxy.postJSONOrderWithResponse("GameList.action");
			if (jsm.getType().equals(GameListMessage.class.getSimpleName())) {
				GameListMessage glm=(GameListMessage) jsm;
				JSONArray jsa=glm.getGames();
				for (int i=0; i<jsa.length(); i++) {
					JSONObject jGame=jsa.getJSONObject(i);
					final Button btnGame=new Button(this);
					btnGame.setTag(jGame.getInt("id"));
					btnGame.setText(jGame.getString("name"));
					btnGame.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							btnSelectedGameId=(Button) v;
							Dialogs.showTwoButtonsDialog(GameListActivity.this, "OK", "Do you wanna join "  + btnGame.getText() + "?", "Yes", "No");
						}
					});
					LinearLayout ll=new LinearLayout(this);
					ll.addView(btnGame);
					this.gameList.addView(ll);
				}	
			} else {
				ErrorMessage erm=(ErrorMessage) jsm;
				throw new Exception(erm.getText());
			}

		} catch (Exception e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public Context getContext() {
		return this;
	}

	@Override
	public void setSelectedButton(int button) {
		if (button==IDialogListener.YES) {
			Proxy proxy=Proxy.get();
			try {
				Store store=Store.get();
				User user=store.getUser();
				
				JSONParameter jspIdGame=new JSONParameter("idGame", btnSelectedGameId.getTag().toString());
				JSONParameter jspIdUSer=new JSONParameter("idUser", ""+ user.getId());

				JSONMessage jso=proxy.postJSONOrderWithResponse("JoinGame.action", jspIdGame, jspIdUSer);
				if (jso.getType().equals(OKMessage.class.getSimpleName())) {
					OKMessage okm=(OKMessage) jso;
					store.setGame((Integer) btnSelectedGameId.getTag());
					store.setMatch(okm.getAdditionalInfo().getInt(0));
					if (store.getIdGame()==TresEnRaya.TRES_EN_RAYA) {
						Intent i=new Intent(this, TresEnRayaActivity.class);
						startActivity(i);
					} else {
						Toast.makeText(this, "That game is not implemented", Toast.LENGTH_LONG).show();
					}
				} else {
					ErrorMessage em=(ErrorMessage) jso;
					Toast.makeText(this, em.getText(), Toast.LENGTH_LONG).show();
				}
			}
			catch (Exception e) {
				Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
			}
		} else {
			btnSelectedGameId=null;
		}
	}


}
