package com.maco.hundirlaflota.jsonMessages;

import edu.uclm.esi.common.jsonMessages.JSONMessage;
import edu.uclm.esi.common.jsonMessages.JSONable;

public class HundirLaFlotaWaitingMessage extends JSONMessage {
	@JSONable
	private String text;
	
	public HundirLaFlotaWaitingMessage(String text) {
		super(false);
		this.text=text;
	}

	public String getText() {
		return this.text;
	}

}
