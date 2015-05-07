package com.maco.tresenraya.domain;

import android.widget.Toast;

import com.maco.tresenraya.HundirLaFlotaActivity;
import com.maco.tresenraya.jsonMessages.HundirLaFlotaBoardMessage;
import com.maco.tresenraya.jsonMessages.HundirLaFlotaMovement;

import org.json.JSONException;

import edu.uclm.esi.common.androidClient.domain.Store;
import edu.uclm.esi.common.androidClient.http.Proxy;
import edu.uclm.esi.common.jsonMessages.ErrorMessage;
import edu.uclm.esi.common.jsonMessages.JSONMessage;
import edu.uclm.esi.common.jsonMessages.JSONParameter;
import edu.uclm.esi.common.jsonMessages.OKMessage;

public class HundirLaFlota {
	public static int HUNDIR_LA_FLOTA = 2;
	public static char X='X', O='O', WHITE = ' ', T = 'T';

	private char[][] squares1;
    private char[][] squares2;
	private HundirLaFlotaActivity ctx;
	private String opponent;
	private String userWithTurn;
	private int player;

	public HundirLaFlota(HundirLaFlotaActivity ctx) {
		this.ctx=ctx;
		squares1=new char[5][5];
        squares2=new char[5][5];
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

	private String get1(int row, int col) {
		return "" + squares1[row][col];
	}

    private String get2(int row, int col) {
        return "" + squares2[row][col];
    }

	public String get(int row, int col) {
		if(player == 1)
			return get2(row, col);
		else
			return get1(row,col);
	}

	private String toString1() {
		String r="";
		for (int row=0; row<5; row++)
			for (int col=0; col<5; col++)
				r+=this.squares1[row][col];
		return r;
	}

    private String toString2() {
        String r="";
        for (int row=0; row<5; row++)
            for (int col=0; col<5; col++)
                r+=this.squares2[row][col];
        return r;
    }

	public String toString() {
		if(player == 1)
			return toString2();
		else
			return toString1();
	}

	public void load(HundirLaFlotaBoardMessage board) throws JSONException {
		String squares1=board.getSquares1();
        String squares2=board.getSquares2();

		int cont=0;
		for (int row=0; row<5; row++)
			for (int col=0; col<5; col++) {
                this.squares1[row][col] = squares1.charAt(cont++);
            }
		if (board.getPlayer2()!=null) {
            cont=0;
            for(int row=0;row<5; row++)
                for(int col=0;col<5;col++)
                    this.squares2[row][col] = squares2.charAt(cont++);
			if (Store.get().getUser().getEmail().equals(board.getPlayer1())){
				this.opponent=board.getPlayer2();
				player = 1;
			}

			else{
				this.opponent=board.getPlayer1();
				player = 2;
			}


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
