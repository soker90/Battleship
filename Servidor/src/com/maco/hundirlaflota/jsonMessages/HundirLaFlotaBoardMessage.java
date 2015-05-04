package com.maco.hundirlaflota.jsonMessages;

import java.util.StringTokenizer;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uclm.esi.common.jsonMessages.JSONMessage;
import edu.uclm.esi.common.jsonMessages.JSONable;

public class HundirLaFlotaBoardMessage extends JSONMessage {
	@JSONable
	private String squares1;
	@JSONable
	private String squares2;
	@JSONable
	private String player1;
	@JSONable
	private String player2;
	@JSONable
	private String userWithTurn;

	public HundirLaFlotaBoardMessage(String board) throws JSONException {
		super(false);
		StringTokenizer st=new StringTokenizer(board, "#");
		this.squares1=st.nextToken();
		this.player1=st.nextToken();
		if (st.hasMoreTokens()) {
			this.squares2=st.nextToken();
			this.player2=st.nextToken();
			userWithTurn=st.nextToken();
		}
	}
	
	public HundirLaFlotaBoardMessage(JSONObject jso) throws JSONException {
		super(false);
		this.squares1=jso.getString("squares1");
		this.player1=jso.getString("player1");
		if (jso.optString("player2").length()>0) {
			this.squares2=jso.getString("squares2");
			this.player2=jso.getString("player2");
			this.userWithTurn=jso.getString("userWithTurn");
		}
	}

	public String getSquares1() {
		return squares1;
	}
	
	public String getSquares2() {
		return squares2;
	}
	
	public String getPlayer1() {
		return player1;
	}
	
	public String getPlayer2() {
		return player2;
	}
	
	public String getUserWithTurn() {
		return userWithTurn;
	}
}
