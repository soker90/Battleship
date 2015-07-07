package com.maco.tresenraya;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.maco.tresenraya.domain.HundirLaFlota;
import com.maco.tresenraya.jsonMessages.HundirLaFlotaBoardMessage;
import com.maco.tresenraya.jsonMessages.HundirLaFlotaMatchReadyMessage;
import com.maco.tresenraya.jsonMessages.HundirLaFlotaMovement;
import com.maco.tresenraya.jsonMessages.HundirLaFlotaWaitingMessage;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uclm.esi.common.androidClient.dialogs.Dialogs;
import edu.uclm.esi.common.androidClient.domain.Store;
import edu.uclm.esi.common.androidClient.http.Proxy;
import edu.uclm.esi.common.jsonMessages.ErrorMessage;
import edu.uclm.esi.common.jsonMessages.JSONMessage;
import edu.uclm.esi.common.jsonMessages.JSONParameter;
import edu.uclm.esi.common.jsonMessages.OKMessage;

public class HundirLaFlotaActivity extends ActionBarActivity {
	private HundirLaFlota match;
	private TextView tvPlayer;
	private TextView tvMessage;
	private TextView tvOpponent;
	private Button[] btns;
	private Button btnAbandonar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Store.get().setCurrentContext(this);
		setContentView(R.layout.activity_hundir_la_flota);

		this.tvPlayer=(TextView) this.findViewById(R.id.textViewTresEnRayaPlayer);
		this.tvMessage=(TextView) this.findViewById(R.id.textViewMessage);
		this.tvOpponent=(TextView) this.findViewById(R.id.textViewOpponent);
		this.btns=new Button[25];
		int cont=0;
		for (int row=0; row<5; row++) {
			for (int col=0; col<5; col++) {
				int resId = this.getResources().getIdentifier("c"+cont, "id", "com.maco.tresenraya");
				this.btns[cont]=(Button) findViewById(resId);
				JSONObject tag=new JSONObject();
				try {
					tag.put("row", row);
					tag.put("col", col);
				} catch (JSONException e) {}
				this.btns[cont].setTag(tag);
				this.btns[cont].setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (match.getOpponent()==null) {
							Dialogs.showOneButtonDialog(HundirLaFlotaActivity.this, "Attention", "Wait for the opponent", "OK");
						} else if (!match.getUserWithTurn().equals(Store.get().getUser().getEmail())) {
							Dialogs.showOneButtonDialog(HundirLaFlotaActivity.this, "Attention", "It's not your turn", "OK");
						} else {
							JSONObject jso=(JSONObject) v.getTag();
							HundirLaFlotaMovement mov;
							try {
								mov = new HundirLaFlotaMovement(jso.getInt("row"), jso.getInt("col"));
								match.put(mov);
							} catch (JSONException e) {}
						}
					}
				});
				cont++;
			}
		}

		this.btnAbandonar = (Button) this.findViewById(R.id.btnAbandonar);
		this.btnAbandonar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				abandonarPartida();
			}
		});


		this.match=new HundirLaFlota(this);
		loadMatch();
	}

	private void loadMatch() {
		Store store=Store.get();
		this.tvPlayer.setText(store.getUser().getEmail());
		JSONParameter jspIdUser=new JSONParameter("idUser", ""+store.getUser().getId());
		JSONParameter jspIdGame=new JSONParameter("idGame", ""+store.getIdGame());
		JSONParameter jspIdMatch=new JSONParameter("idMatch", ""+store.getIdMatch());
		try {
			JSONMessage jsm=Proxy.get().postJSONOrderWithResponse("GetBoard.action", jspIdUser, jspIdGame, jspIdMatch);
			if (jsm.getType().equals(HundirLaFlotaBoardMessage.class.getSimpleName())) {
				loadBoard(jsm);
			} else {
				ErrorMessage em=(ErrorMessage) jsm;
				Toast.makeText(this, em.getText(), Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

	public void loadBoard(JSONMessage jsm) throws JSONException {
		HundirLaFlotaBoardMessage board=(HundirLaFlotaBoardMessage) jsm;
		if (this.match==null)
			this.match=new HundirLaFlota(this);
		this.match.load(board);
		int cont=0;
		for (int row=0; row<5; row++) {
			for (int col=0; col<5; col++) {
				int resId=this.getResources().getIdentifier("c"+cont, "id", "com.maco.tresenraya");
				this.btns[cont]=(Button) findViewById(resId);
				String text = match.get(row, col);
				if(text.equals("X"))
					this.btns[cont].setText("");
				else
					this.btns[cont].setText(text);
				cont++;
			}
		}
	}

	public void loadMessage(HundirLaFlotaWaitingMessage wm) {
		this.tvMessage.setText(wm.getText());
	}

	public void loadReadyMessage(HundirLaFlotaMatchReadyMessage rm) {
		String player1=rm.getPlayer1();
		String player2=rm.getPlayer2();
		String opponent;
		if (Store.get().getUser().getEmail().equals(player1))
			opponent=player2;
		else
			opponent=player1;
		this.tvOpponent.setText("Playing against " + opponent);
	}

	private void abandonarPartida(){
		Store store=Store.get();
		JSONParameter jspIdUser=new JSONParameter("idUser", ""+store.getUser().getId());
		JSONParameter jspIdMatch=new JSONParameter("idMatch", ""+store.getIdMatch());
		JSONParameter jspIdGame=new JSONParameter("idGame", ""+store.getIdGame());
		try {
			JSONMessage jsm=Proxy.get().postJSONOrderWithResponse("AbandonarJuego.action", jspIdUser,jspIdGame, jspIdMatch);
			if (jsm.getType().equals(OKMessage.class.getSimpleName())) {
				Toast.makeText(this, "Has abandonado la partida", Toast.LENGTH_LONG).show();
			} else {
				ErrorMessage em=(ErrorMessage) jsm;
				Toast.makeText(this, em.getText(), Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		} finally {
			Intent i=new Intent(this, GameListActivity.class);
			startActivity(i);
		}


	}
}
