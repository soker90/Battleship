package edu.uclm.esi.common.androidClient.http;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import edu.uclm.esi.common.jsonMessages.JSONMessage;

public class Proxy {	
	public static String PREFIX = "--", LINE_END = "\r\n"; 

	private static Proxy yo;
	private String urlServer;

	private Proxy() {
		this.urlServer = "172.19.183.87:8080";
	}

	public static Proxy get() {
		if (yo==null)
			yo=new Proxy();
		return yo;
	}
	
	public void postJSONOrderWithNoResponse(String resource, JSONObject jso) throws JSONException {
		PostJSONDataWithNoResponse pd=new PostJSONDataWithNoResponse(resource);
		pd.execute(jso);
	}
	
	public JSONMessage postJSONOrderWithResponse(String resource) throws JSONException, InterruptedException, ExecutionException {
		PostJSONDataWithResponse pd=new PostJSONDataWithResponse(resource);
		pd.execute();
		JSONMessage resp = pd.get();		
		return resp;
	}
	
	public JSONMessage postJSONOrderWithResponse(String resource, JSONMessage... jso) throws JSONException, InterruptedException, ExecutionException {
		PostJSONDataWithResponse pd=new PostJSONDataWithResponse(resource);
		pd.execute(jso);
		JSONMessage resp = pd.get();		
		return resp;
	}

	public String getUrlServer() {
		return urlServer;
	}



}
