package edu.uclm.esi.common.server.rmi;


import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.SQLException;

import edu.uclm.esi.common.jsonMessages.JSONMessage;
import edu.uclm.esi.common.server.domain.Manager;
import edu.uclm.esi.common.server.domain.User;

public class ServidorRMI extends UnicastRemoteObject implements IServidorRMI {
	
	public static void main(String[]args){
		try {
			ServidorRMI servidor=new ServidorRMI();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected ServidorRMI() throws RemoteException,MalformedURLException {
		super();
		// TODO Auto-generated constructor stub
		LocateRegistry.createRegistry(2995);
		try{
			Naming.bind("rmi://127.0.0.1:2995/juegosEnGrupo", this);
		}
		catch (AlreadyBoundException ex){
			Naming.rebind("rmi://127.0.0.1:2995/juegosEnGrupo", this);
			
		}
	}

	@Override
	public void login(String email, String pwd) throws SQLException, IOException {
		// TODO Auto-generated method stub
		Connection bd=User.identify(email, pwd);
		User user=new User(bd, email, JSONMessage.USER_RMI);
		Manager manager=Manager.get();
		manager.add(user, null);
		
	}

	@Override
	public void register(String email, String pwd1, String pwd2)
			throws RemoteException {
		// TODO Auto-generated method stub
		
	}

}
