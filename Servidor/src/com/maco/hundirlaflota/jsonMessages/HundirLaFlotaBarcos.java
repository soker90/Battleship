package com.maco.hundirlaflota.jsonMessages;

import edu.uclm.esi.common.jsonMessages.JSONMessage;
import edu.uclm.esi.common.jsonMessages.JSONable;

public class HundirLaFlotaBarcos extends JSONMessage {
	@JSONable
	private char[][] squares;

	public HundirLaFlotaBarcos(char[][] squares) {
		super(true);
		this.squares = squares;
	}
	
	public char[][] getSquares() {
		return squares;
	}

}
