package edu.uclm.esi.common.androidClient.domain;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

public class Store {
	private static Store yo;
	
	private User user;
	private int idGame;
	private int idMatch;
	private Context ctx;
	
	private Store() {
	}
	
	public static Store get() {
		if (yo==null)
			yo=new Store();
		return yo;
	}
	
	public void setUser(User user) {
		this.user=user;
	}
	
	public User getUser() {
		return this.user;
	}

	public void setGame(int idGame) {
		this.idGame=idGame;
	}
	
	public int getIdGame() {
		return idGame;
	}

	public void setMatch(int idMatch) {
		this.idMatch=idMatch;
	}
	
	public int getIdMatch() {
		return idMatch;
	}
	
	public Context getCurrentContext() {
		return this.ctx;
	}
	
	public void setCurrentContext(Context ctx) {
		this.ctx=ctx;
		String an=((Activity) ctx).getClass().getSimpleName();
		Toast.makeText(this.ctx, "Context changed to: " + an, Toast.LENGTH_LONG).show();
	}
}
