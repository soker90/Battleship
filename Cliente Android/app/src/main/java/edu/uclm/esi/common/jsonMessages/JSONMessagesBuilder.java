package edu.uclm.esi.common.jsonMessages;

import com.maco.tresenraya.jsonMessages.GameListMessage;
import com.maco.tresenraya.jsonMessages.HundirLaFlotaBoardMessage;
import com.maco.tresenraya.jsonMessages.HundirLaFlotaWaitingMessage;
import com.maco.tresenraya.jsonMessages.TresEnRayaBoardMessage;
import com.maco.tresenraya.jsonMessages.TresEnRayaWaitingMessage;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONMessagesBuilder {
	public static JSONMessage build(JSONObject jso) throws JSONException {
		if (jso.get("type").equals(ErrorMessage.class.getSimpleName()))
			return new ErrorMessage(jso);
		if (jso.get("type").equals(LoginMessage.class.getSimpleName()))
			return new LoginMessage(jso);
		if (jso.get("type").equals(LoginMessageAnnouncement.class.getSimpleName()))
			return new LoginMessageAnnouncement(jso);
		if (jso.get("type").equals(LogoutMessageAnnouncement.class.getSimpleName()))
			return new LogoutMessageAnnouncement(jso);
		if (jso.get("type").equals(OKMessage.class.getSimpleName()))
			return new OKMessage(jso);
		if (jso.get("type").equals(RegisterMessage.class.getSimpleName()))
			return new RegisterMessage(jso);
		if (jso.get("type").equals(GameListMessage.class.getSimpleName()))
			return new GameListMessage(jso.getString("games"));
		if (jso.get("type").equals(TresEnRayaBoardMessage.class.getSimpleName()))
			return new TresEnRayaBoardMessage(jso);
		if (jso.get("type").equals(TresEnRayaWaitingMessage.class.getSimpleName()))
			return new TresEnRayaWaitingMessage(jso.getString("text"));
		if (jso.get("type").equals(HundirLaFlotaBoardMessage.class.getSimpleName()))
			return new HundirLaFlotaBoardMessage(jso);
		if (jso.get("type").equals(HundirLaFlotaWaitingMessage.class.getSimpleName()))
			return new HundirLaFlotaWaitingMessage(jso.getString("text"));
		return null;
	}
}
