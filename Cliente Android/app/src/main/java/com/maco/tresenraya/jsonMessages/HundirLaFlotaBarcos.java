package com.maco.tresenraya.jsonMessages;

import edu.uclm.esi.common.jsonMessages.JSONMessage;

public class HundirLaFlotaBarcos extends JSONMessage {

	private Object squares;

	public HundirLaFlotaBarcos(Object squares) {
		super(true);
		this.squares = squares;
	}

	public Object getSquares() {
		return squares;
	}


}
