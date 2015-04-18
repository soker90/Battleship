package com.maco.tresenraya.jsonMessages;

import edu.uclm.esi.common.jsonMessages.JSONMessage;
import edu.uclm.esi.common.jsonMessages.JSONable;

public class HundirLaFlotaMatchReadyMessage extends JSONMessage {
	@JSONable
	private String player1;
	@JSONable
	private String player2;

	public HundirLaFlotaMatchReadyMessage(String player1, String player2) {
		super(false);
		this.player1=player1;
		this.player2=player2;
	}

	public String getPlayer1() {
		return this.player1;
	}
	
	public String getPlayer2() {
		return player2;
	}
}
