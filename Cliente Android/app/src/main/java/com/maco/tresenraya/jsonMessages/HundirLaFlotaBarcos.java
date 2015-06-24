package com.maco.tresenraya.jsonMessages;

import edu.uclm.esi.common.jsonMessages.JSONMessage;
import edu.uclm.esi.common.jsonMessages.JSONable;

public class HundirLaFlotaBarcos extends JSONMessage {

	@JSONable
	private String squares;

	public HundirLaFlotaBarcos(String squares) {
		super(true);
		this.squares = squares;
	}

	public String getSquares() {
		return squares;
	}


}
