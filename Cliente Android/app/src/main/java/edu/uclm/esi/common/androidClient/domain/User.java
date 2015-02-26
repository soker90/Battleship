package edu.uclm.esi.common.androidClient.domain;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uclm.esi.common.androidClient.http.Proxy;
import edu.uclm.esi.common.jsonMessages.ErrorMessage;
import edu.uclm.esi.common.jsonMessages.JSONMessage;
import edu.uclm.esi.common.jsonMessages.LoginMessage;
import edu.uclm.esi.common.jsonMessages.LoginWithGoogleMessage;
import edu.uclm.esi.common.jsonMessages.OKMessage;
import edu.uclm.esi.common.jsonMessages.RegisterMessage;

public class User {
	private int id;
	private String email;
	private java.sql.Date fechaDeAlta;

	public User() {
	}

	public User(int id, String email) throws Exception{
		this();
		this.id=id;
		this.email=email;
	}

	public User(String string, String email) {
		this();
	}

	public static User identify(String email, String pwd) throws Exception {
		LoginMessage lm=new LoginMessage(email, pwd, JSONMessage.USER_ANDROID);
		JSONMessage jsm=Proxy.get().postJSONOrderWithResponse("Login.action", lm);
		if (jsm.getType().equals(OKMessage.class.getSimpleName())) {
			OKMessage okm=(OKMessage) jsm;
			int id=okm.getAdditionalInfo().getInt(0);
			User user=new User(id, email);
			Store.get().setUser(user);
			return user;
		}
		ErrorMessage erm=(ErrorMessage) jsm;
		throw new Exception(erm.getText());
	}

	public static User identifyWithGoogle(String email) throws Exception {
		LoginWithGoogleMessage lm=new LoginWithGoogleMessage(email);
		JSONMessage jsm=Proxy.get().postJSONOrderWithResponse("LoginWithGoogle.action", lm);
		if (jsm.getType().equals(OKMessage.class.getSimpleName())) {
			OKMessage okm=(OKMessage) jsm;
			int id=okm.getAdditionalInfo().getInt(0);
			User user=new User(id, email);
			Store.get().setUser(user);
			return user;
		}
		ErrorMessage erm=(ErrorMessage) jsm;
		throw new Exception(erm.getText());	
	}

	public static void insert(String email, String pwd1, String pwd2) throws Exception {
		RegisterMessage rm=new RegisterMessage(email, pwd1, pwd2);
		JSONMessage jsm=Proxy.get().postJSONOrderWithResponse("Register.action", rm);
		String type=jsm.getType();
		if (type.equals(ErrorMessage.class.getSimpleName())) {
			ErrorMessage erm=(ErrorMessage) jsm;
			throw new Exception(erm.getText());
		}
	}

	public String getEmail() {
		return email;
	}

	public java.sql.Date getFechaDeAlta() {
		return fechaDeAlta;
	}

	public void setFechaDeAlta(java.sql.Date fechaDeAlta) {
		this.fechaDeAlta = fechaDeAlta;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public JSONObject toJSONParameter() throws JSONException {
		JSONObject jso=new JSONObject();
		jso.put("parName", "id");
		jso.put("parValue", ""+ this.id);
		return jso;
	}
}