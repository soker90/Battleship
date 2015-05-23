package com.maco.hundirlaflota.jsonMessages;

import java.lang.reflect.Field;
import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uclm.esi.common.jsonMessages.JSONMessage;
import edu.uclm.esi.common.jsonMessages.JSONable;

public class HundirLaFlotaBarcos extends JSONMessage {

	private char[][] squares;

	public HundirLaFlotaBarcos(char[][] squares) {
		super(true);
		this.squares = squares;
	}

	public char[][] getSquares() {
		return squares;
	}

	public JSONObject toJSONObject() {

		JSONObject result=new JSONObject();
		try {
			result.put("squares", squares);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			result.put("type", this.getClass().getSimpleName());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}


}
