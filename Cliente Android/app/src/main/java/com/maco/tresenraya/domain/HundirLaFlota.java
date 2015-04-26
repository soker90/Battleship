package com.maco.tresenraya.domain;

import android.widget.Toast;

import com.maco.tresenraya.HundirLaFlotaActivity;
import com.maco.tresenraya.TresEnRayaActivity;
import com.maco.tresenraya.jsonMessages.HundirLaFlotaBoardMessage;
import com.maco.tresenraya.jsonMessages.HundirLaFlotaMovement;
import com.maco.tresenraya.jsonMessages.TresEnRayaBoardMessage;
import com.maco.tresenraya.jsonMessages.TresEnRayaMovement;

import org.json.JSONException;

import edu.uclm.esi.common.androidClient.domain.Store;
import edu.uclm.esi.common.androidClient.http.Proxy;
import edu.uclm.esi.common.jsonMessages.ErrorMessage;
import edu.uclm.esi.common.jsonMessages.JSONMessage;
import edu.uclm.esi.common.jsonMessages.JSONParameter;
import edu.uclm.esi.common.jsonMessages.OKMessage;

public class HundirLaFlota {
	public static int HUNDIR_LA_FLOTA = 2;
	public static char X='X', O='O', WHITE = ' ';

	private char[][] squares;
	private HundirLaFlotaActivity ctx;
	private String opponent;
	private String userWithTurn;

	public HundirLaFlota(HundirLaFlotaActivity ctx) {
		this.ctx=ctx;
		squares=new char[5][5];
	}
	
	public void put(HundirLaFlotaMovement mov) {
		Store store=Store.get();
		JSONParameter jspIdUser=new JSONParameter("idUser", ""+ store.getUser().getId());
		JSONParameter jspIdGame=new JSONParameter("idGame", ""+store.getIdGame());
		JSONParameter jspIdMatch=new JSONParameter("idMatch", ""+store.getIdMatch());
		try {
			JSONMessage jsm=Proxy.get().postJSONOrderWithResponse("SendMovement.action", mov, jspIdUser, jspIdGame, jspIdMatch);
			if (jsm.getType().equals(OKMessage.class.getSimpleName())) {
				Toast.makeText(this.ctx, "Succesfully sent", Toast.LENGTH_LONG).show();
			} else {
				ErrorMessage em=(ErrorMessage) jsm;
				Toast.makeText(this.ctx, em.getText(), Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			Toast.makeText(this.ctx, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
	public String get(int row, int col) {
		return "" + squares[row][col];
	}
	
	public String toString() {
		String r="";
		for (int row=0; row<5; row++)
			for (int col=0; col<5; col++)
				r+=this.squares[row][col];
		return r;
	}

	public void load(HundirLaFlotaBoardMessage board) throws JSONException {
		String squares=board.getSquares();
		int cont=0;
		for (int row=0; row<5; row++)
			for (int col=0; col<5; col++)
				this.squares[row][col]=squares.charAt(cont++);
		if (board.getPlayer2()!=null) {
			if (Store.get().getUser().getEmail().equals(board.getPlayer1()))
				this.opponent=board.getPlayer2();
			else 
				this.opponent=board.getPlayer1();
			this.userWithTurn=board.getUserWithTurn();
		}
	}
	
	public String getOpponent() {
		return opponent;
	}
	
	public String getUserWithTurn() {
		return userWithTurn;
	}
}
