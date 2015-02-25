package edu.uclm.esi.common.server.actions;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

import edu.uclm.esi.common.server.sockets.Notifier;

@SuppressWarnings("serial")
public abstract class JSONAction extends ActionSupport {
	protected Exception exception=null;
	protected String ip;
	
	public JSONAction() {
		try {
			ServletActionContext.getResponse().setHeader("Access-Control-Allow-Origin", "*");
			ServletActionContext.getResponse().setCharacterEncoding("UTF-8");  
			Notifier notifier=Notifier.get();
			notifier.startListening();
			HttpServletRequest request = ServletActionContext.getRequest();
			this.ip=request.getRemoteAddr();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public final String execute() {
		if (this.exception!=null) {
			return ERROR;
		}
		return postExecute();
	}

	protected abstract String postExecute();

	public abstract void setCommand(String cmd);

	public abstract String getResultado();
}
