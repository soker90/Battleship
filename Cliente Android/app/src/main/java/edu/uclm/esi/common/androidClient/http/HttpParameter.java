package edu.uclm.esi.common.androidClient.http;

public class HttpParameter {
	private String parName, parValue;
	
	public HttpParameter(String parName, String parValue) {
		super();
		this.parName = parName;
		this.parValue = parValue;
	}
	
	private String escape(String s) {
		return s.replaceAll("\n", "%0A");
	}

	@Override
	public String toString() {
		return parName + "=" + escape(parValue);
	}
}
