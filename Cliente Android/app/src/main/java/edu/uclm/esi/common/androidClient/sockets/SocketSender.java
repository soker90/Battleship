package edu.uclm.esi.common.androidClient.sockets;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import edu.uclm.esi.common.jsonMessages.JSONMessage;

public class SocketSender {
	private static SocketSender yo;
	
	private String host;
	private int port;
	
	private SocketSender() {
		this.host="192.168.1.128";
		this.port=3000;
	}
	
	public static SocketSender get() {
		if (yo==null)
			yo=new SocketSender();
		return yo;
	}

	public void send(final JSONMessage msg) throws IOException {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Socket socket=new Socket(host, port);
					OutputStream outToServer = socket.getOutputStream();
			        DataOutputStream out = new DataOutputStream(outToServer);
			        out.writeUTF(msg.toString());
			        out.flush();
			        socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
