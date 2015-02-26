package edu.uclm.esi.common.androidClient.sockets;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import org.json.JSONException;
import org.json.JSONObject;

import com.maco.tresenraya.TresEnRayaActivity;
import com.maco.tresenraya.jsonMessages.TresEnRayaBoardMessage;
import com.maco.tresenraya.jsonMessages.TresEnRayaMatchReadyMessage;
import com.maco.tresenraya.jsonMessages.TresEnRayaWaitingMessage;

import android.widget.Toast;
import edu.uclm.esi.common.androidClient.domain.Store;
import edu.uclm.esi.common.jsonMessages.ErrorMessage;
import edu.uclm.esi.common.jsonMessages.JSONMessage;
import edu.uclm.esi.common.jsonMessages.JSONMessagesBuilder;
import edu.uclm.esi.common.jsonMessages.LoginMessageAnnouncement;

public class SocketListener extends Thread {
	private static SocketListener yo;
	
	private ServerSocket socket;
	private int port;
	private boolean started;

	private SocketListener() throws UnknownHostException, IOException {
		this.port=4000;
		this.socket=new ServerSocket(port);
	}

	public static SocketListener get() throws UnknownHostException, IOException {
		if (yo==null)
			yo=new SocketListener();
		return yo;
	}
	
	@Override
	public void run() {
		while (true) {
			this.started=true;
			try {
				Socket remote=socket.accept();
				DataInputStream in = new DataInputStream(remote.getInputStream());
				try {
					final String msg=in.readUTF();
					dealWithMessage(msg); 
					remote.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			catch (IOException e) {
				Toast.makeText(Store.get().getCurrentContext(), e.getMessage(), Toast.LENGTH_LONG).show();
			}
		}
	}

	private void dealWithMessage(String msg) throws JSONException {
		String text=null;
		JSONMessage jsm=JSONMessagesBuilder.build(new JSONObject(msg));
		if (jsm.getType().equals(LoginMessageAnnouncement.class.getSimpleName())) {
			LoginMessageAnnouncement lma=(LoginMessageAnnouncement) jsm;
			text=lma.getEmail() + " has just arrived";
			sendToast(text);
			return;
		} 
		if (jsm.getType().equals(TresEnRayaWaitingMessage.class.getSimpleName())) {
			final TresEnRayaWaitingMessage wm=(TresEnRayaWaitingMessage) jsm;
			final TresEnRayaActivity activity=(TresEnRayaActivity) Store.get().getCurrentContext();
			activity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					activity.loadMessage(wm);
				}
			});
			return;
		}
		if (jsm.getType().equals(TresEnRayaMatchReadyMessage.class.getSimpleName())) {
			final TresEnRayaMatchReadyMessage rm=(TresEnRayaMatchReadyMessage) jsm;
			final TresEnRayaActivity activity=(TresEnRayaActivity) Store.get().getCurrentContext();
			activity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					activity.loadReadyMessage(rm);
				}
			});
			return;
		}
		if (jsm.getType().equals(TresEnRayaBoardMessage.class.getSimpleName())) {
			final TresEnRayaBoardMessage board=(TresEnRayaBoardMessage) jsm;
			final TresEnRayaActivity activity=(TresEnRayaActivity) Store.get().getCurrentContext();
			activity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					 try {
						activity.loadBoard(board);
					} catch (JSONException e) {
						sendToast(e.getMessage());
					}
				}
			});
			return;
		} 
		if (jsm.getType().equals(ErrorMessage.class.getSimpleName())) {
			ErrorMessage em=(ErrorMessage) jsm;
			text=em.getText();
			sendToast(text);
			return;
		}
	}

	private void sendToast(final String text2) {
		((android.app.Activity) Store.get().getCurrentContext()).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(Store.get().getCurrentContext(), text2, Toast.LENGTH_LONG).show();
			}
		});
	}

	public void close() throws IOException {
		socket.close();
	}
	
	public void startListening() {
		if (!started)
			start();
	}
}
