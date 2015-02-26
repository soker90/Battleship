package com.maco.tresenraya;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.maco.tresenraya.domain.TresEnRaya;
import com.maco.tresenraya.jsonMessages.TresEnRayaBoardMessage;
import com.maco.tresenraya.jsonMessages.TresEnRayaMatchReadyMessage;
import com.maco.tresenraya.jsonMessages.TresEnRayaMovement;
import com.maco.tresenraya.jsonMessages.TresEnRayaWaitingMessage;

import edu.uclm.esi.common.androidClient.dialogs.Dialogs;
import edu.uclm.esi.common.androidClient.domain.Store;
import edu.uclm.esi.common.androidClient.http.Proxy;
import edu.uclm.esi.common.jsonMessages.ErrorMessage;
import edu.uclm.esi.common.jsonMessages.JSONMessage;
import edu.uclm.esi.common.jsonMessages.JSONParameter;

public class TresEnRayaActivity extends ActionBarActivity {
	private TresEnRaya match;
	private TextView tvPlayer;
	private TextView tvMessage;
	private TextView tvOpponent;
	private Button[] btns;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Store.get().setCurrentContext(this);
		setContentView(R.layout.activity_tres_en_raya);

		this.tvPlayer=(TextView) this.findViewById(R.id.textViewTresEnRayaPlayer);
		this.tvMessage=(TextView) this.findViewById(R.id.textViewMessage);
		this.tvOpponent=(TextView) this.findViewById(R.id.textViewOpponent);
		this.btns=new Button[9];
		int cont=0;
		for (int row=0; row<3; row++) {
			for (int col=0; col<3; col++) {
				int resId = this.getResources().getIdentifier("button"+cont, "id", "com.maco.tresenraya");
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
							Dialogs.showOneButtonDialog(TresEnRayaActivity.this, "Attention", "Wait for the opponent", "OK");
						} else if (!match.getUserWithTurn().equals(Store.get().getUser().getEmail())) {
							Dialogs.showOneButtonDialog(TresEnRayaActivity.this, "Attention", "It's not your turn", "OK");
						} else {
							JSONObject jso=(JSONObject) v.getTag();
							TresEnRayaMovement mov;
							try {
								mov = new TresEnRayaMovement(jso.getInt("row"), jso.getInt("col"));
								match.put(mov);
							} catch (JSONException e) {}
						}
					}
				});
				cont++;
			}
		}
		
		this.match=new TresEnRaya(this);
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
			if (jsm.getType().equals(TresEnRayaBoardMessage.class.getSimpleName())) {
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
		TresEnRayaBoardMessage board=(TresEnRayaBoardMessage) jsm;
		if (this.match==null)
			this.match=new TresEnRaya(this);
		this.match.load(board);
		int cont=0;
		for (int row=0; row<3; row++) {
			for (int col=0; col<3; col++) {
				int resId=this.getResources().getIdentifier("button"+cont, "id", "com.maco.tresenraya");
				this.btns[cont]=(Button) findViewById(resId);
				this.btns[cont].setText(match.get(row, col));
				cont++;
			}
		}
	}

	public void loadMessage(TresEnRayaWaitingMessage wm) {
		this.tvMessage.setText(wm.getText());
	}

	public void loadReadyMessage(TresEnRayaMatchReadyMessage rm) {
		String player1=rm.getPlayer1();
		String player2=rm.getPlayer2();
		String opponent;
		if (Store.get().getUser().getEmail().equals(player1))
			opponent=player2;
		else
			opponent=player1;
		this.tvOpponent.setText("Playing against " + opponent);
	}
}
