package com.maco.tresenraya.jsonMessages;


import edu.uclm.esi.common.jsonMessages.JSONMessage;
import edu.uclm.esi.common.jsonMessages.JSONable;

public class HundirLaFlotaAbandonar extends JSONMessage {
	@JSONable
	private int user, game, match;


	public HundirLaFlotaAbandonar(int user, int game, int match) {
		super(false);
		this.user = user;
		this.game = game;
		this.match = match;
	}

	public int getUser() {
		return this.user;
	}
	public int getGame() {
		return this.game;
	}
	public int getMath() {
		return this.match;
	}

}