package com.maco.juegosEnGrupo.server.dominio;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import org.apache.tomcat.util.bcel.classfile.Constant;
import org.json.JSONException;
import org.json.JSONObject;

import com.maco.hundirlaflota.jsonMessages.HundirLaFlotaBoardMessage;
import com.maco.hundirlaflota.jsonMessages.HundirLaFlotaMovement;
import com.maco.hundirlaflota.jsonMessages.HundirLaFlotaWaitingMessage;

import edu.uclm.esi.common.jsonMessages.ErrorMessage;
import edu.uclm.esi.common.jsonMessages.JSONMessage;
import edu.uclm.esi.common.server.domain.User;
import edu.uclm.esi.common.server.persistence.Broker;
import edu.uclm.esi.common.server.persistence.DAOUser;
import edu.uclm.esi.common.server.sockets.Notifier;

public class HundirLaFota extends Match{
	
	public static int HUNDIR_LA_FLOTA = 2;
	public static char X='X', O='O', WHITE = ' ', T='T';
	private ArrayList<char[][]> squares;
	private User userWithTurn;
	private int[] cont;
	private final int CABOATS = 4;
	
	public HundirLaFota(Game game) {
		super(game);
		squares = new ArrayList<char[][]>();
		squares.add(new char[5][5]);
		squares.add(new char[5][5]);
		for (int row=0; row<5; row++)
			for (int col=0; col<5; col++)
			{
				squares.get(0)[row][col]=WHITE;
				squares.get(1)[row][col]=WHITE;
			}
		
		//Testeo, quitar luego
		squares.get(0)[0][0] = X;
		squares.get(0)[0][1] = X;
		squares.get(0)[2][0] = X;
		squares.get(0)[2][1] = X;
		squares.get(1)[1][0] = X;
		squares.get(1)[1][1] = X;
		squares.get(1)[3][0] = X;
		squares.get(1)[3][1] = X;
		////////
		cont = new int[2];
		cont[0] = 0;
		cont[1] = 0;
	}

	@Override
	protected void postAddUser(User user) {
		if (this.players.size()==2) {
			Random dado=new Random();
			JSONMessage jsTurn=new HundirLaFlotaWaitingMessage("Match ready. You have the turn.");
			JSONMessage jsNoTurn=new HundirLaFlotaWaitingMessage("Match ready. Wait for the opponent to move.");
			if (dado.nextBoolean()) {
				this.userWithTurn=this.players.get(0);
				try {
					Notifier.get().post(this.players.get(0), jsTurn);
					Notifier.get().post(this.players.get(1), jsNoTurn);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			} else {
				this.userWithTurn=this.players.get(1);
				try {
					Notifier.get().post(this.players.get(1), jsTurn);
					Notifier.get().post(this.players.get(0), jsNoTurn);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				JSONMessage jsBoard=new HundirLaFlotaBoardMessage(this.toString());
				Notifier.get().post(this.players.get(0), jsBoard);
				Notifier.get().post(this.players.get(1), jsBoard);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			JSONMessage jsm=new HundirLaFlotaWaitingMessage("Waiting for one more player");
			try {
				Notifier.get().post(this.players.get(0), jsm);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	@Override
	public String toString() {
		String r="";
		for (int row=0; row<5; row++)
			for (int col=0; col<5; col++){
				r+=this.squares.get(0)[row][col];
				
			}
		r+="#" + this.players.get(0).getEmail() + "#";
		if (this.players.size()==2) {
			for(int row=0;row <5;row++)
				for(int col=0;col<5;col++)
					r+=this.squares.get(1)[row][col];
			r+="#"+this.players.get(1).getEmail() + "#";
			r+=this.userWithTurn.getEmail();
		}
		return r;
	}
	@Override
	protected boolean isTheTurnOf(User user) {
		return this.userWithTurn.equals(user);
	}
	@Override
	protected void postMove(User user, JSONObject jsoMovement) throws Exception {
		if (!jsoMovement.get("type").equals(HundirLaFlotaMovement.class.getSimpleName())) {
			throw new Exception("Unexpected type of movement");
		}
		int row=jsoMovement.getInt("row");
		int col=jsoMovement.getInt("col");
		JSONMessage result=null;
		
		if (!this.isTheTurnOf(user)) {
			result=new ErrorMessage("It's not your turn");
			Notifier.get().post(user, result);
		} 
		updateBoard(row, col, result);
		
	}
	@Override
	protected void updateBoard(int row, int col, JSONMessage result)
			throws JSONException, IOException {
		if (result==null) {
			if (this.userWithTurn.equals(this.players.get(0))) {
				ejecutarAtaque(1, row, col);
				this.userWithTurn=this.players.get(1);
			} else {
				ejecutarAtaque(0, row, col);
				this.userWithTurn=this.players.get(0);
			}
			
			result=new HundirLaFlotaBoardMessage(this.toString());
			Notifier.get().post(this.players, result);
		}
		
	}
	
	
	
	private void ejecutarAtaque(int player,int row, int col){
		if(squares.get(player)[row][col] == WHITE)
			squares.get(player)[row][col] = O;
		else if(squares.get(player)[row][col] == X)
		{
			squares.get(player)[row][col] = T;
			cont[player]++;
				
		}
			if(cont[player] == CABOATS){
				try{
					DAOUser.insertRanking(String.valueOf(userWithTurn.getId()),String.valueOf(game.getId()));
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		
		try {
			DAOUser.insertMovemment(String.valueOf(player), String.valueOf(this.game.getId())
					, String.valueOf(row), String.valueOf(col));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
