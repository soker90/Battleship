package com.maco.juegosEnGrupo.server.actions;

import org.json.JSONException;
import org.json.JSONObject;

import com.maco.juegosEnGrupo.server.dominio.Game;
import com.maco.juegosEnGrupo.server.dominio.HundirLaFota;
import com.opensymphony.xwork2.ActionContext;

import edu.uclm.esi.common.jsonMessages.ErrorMessage;
import edu.uclm.esi.common.jsonMessages.JSONMessage;
import edu.uclm.esi.common.jsonMessages.OKMessage;
import edu.uclm.esi.common.server.actions.JSONAction;
import edu.uclm.esi.common.server.domain.Manager;
import edu.uclm.esi.common.server.domain.User;

@SuppressWarnings("serial")
public class SendBarcos extends JSONAction {
	private int idUser;
	private int idGame;
	private int idMatch;
	private JSONObject jsoBarcos;
	
	@Override
	public String postExecute() {
		try {
			Manager manager=Manager.get();
			User user=manager.findUserById(this.idUser);
			if (user==null)
				throw new Exception("Usuario no autenticado");
			Game g=manager.findGameById(idGame);
			HundirLaFota match=(HundirLaFota)g.findMatchById(idMatch, idUser);
			match.colocar(user, this.jsoBarcos);
			return SUCCESS;
		} catch (Exception e) {
			this.exception=e;
			ActionContext.getContext().getSession().put("exception", e);
			return ERROR;
		}
	}

	@Override
	public String getResultado() {
		JSONMessage jso;
		if (this.exception!=null)
			jso=new ErrorMessage(this.exception.getMessage());
		else
			jso=new OKMessage();
		return jso.toJSONObject().toString();
	}

	@Override
	public void setCommand(String cmd) {
		try {
			this.jsoBarcos = new JSONObject(cmd);
		} catch (JSONException e) {
			this.exception=e;
		}
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	public void setIdGame(int idGame) {
		this.idGame = idGame;
	}

	public void setIdMatch(int idMatch) {
		this.idMatch = idMatch;
	}
}