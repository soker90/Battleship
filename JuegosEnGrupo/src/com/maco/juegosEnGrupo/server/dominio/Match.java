package com.maco.juegosEnGrupo.server.dominio;

import java.io.IOException;
import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uclm.esi.common.jsonMessages.JSONMessage;
import edu.uclm.esi.common.jsonMessages.OKMessage;
import edu.uclm.esi.common.server.domain.User;
import edu.uclm.esi.common.server.sockets.Notifier;

public abstract class Match {
	protected Vector<User> players;
	protected Game game;
	
	public Match(Game game) {
		this.game=game;
		this.players=new Vector<User>();
	}

	public boolean isComplete() {
		return this.players.size()==game.getPlayersMin();
	}

	public void add(User user) throws Exception {
		if (this.players.contains(user))
			throw new Exception(user.getEmail() + " is already playing this match");
		this.players.add(user);
		postAddUser(user);
	}

	protected abstract void postAddUser(User user);

	public boolean isPlaying(int idUser) {
		for (User player : players)
			if (player.getId()==idUser)
				return true;
		return false;
	}

	public static Match build(Game game) {
		if (game.getId()==1)
			return new TresEnRaya(game);
		if (game.getId()==2)
			return null;
		return null;
	}

	@Override
	public abstract String toString();

	public void move(User user, JSONObject jsoMovement) throws Exception {
		if (!isTheTurnOf(user))
			throw new Exception("It's not your turn");
		postMove(user, jsoMovement);
	}

	protected abstract boolean isTheTurnOf(User user);
	protected abstract void postMove(User user, JSONObject jsoMovement) throws Exception;

	protected abstract void updateBoard(int row, int col, JSONMessage result) throws JSONException, IOException;
}
