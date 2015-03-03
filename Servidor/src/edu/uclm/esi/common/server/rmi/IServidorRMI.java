package edu.uclm.esi.common.server.rmi;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

public interface IServidorRMI extends Remote {
	void login(String email,String pwd) throws RemoteException, SQLException, IOException;
	void register(String email,String pwd1, String pwd2) throws RemoteException;
}
