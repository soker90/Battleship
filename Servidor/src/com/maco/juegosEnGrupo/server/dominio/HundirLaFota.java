package com.maco.juegosEnGrupo.server.dominio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import com.maco.hundirlaflota.jsonMessages.HundirLaFlotaBoardMessage;
import com.maco.hundirlaflota.jsonMessages.HundirLaFlotaMovement;
import com.maco.hundirlaflota.jsonMessages.HundirLaFlotaWaitingMessage;

import edu.uclm.esi.common.jsonMessages.ErrorMessage;
import edu.uclm.esi.common.jsonMessages.JSONMessage;
import edu.uclm.esi.common.server.domain.User;
import edu.uclm.esi.common.server.sockets.Notifier;

public class HundirLaFota extends Match{
	
	public static int HUNDIR_LA_FLOTA = 2;
	public static char X='X', O='O', WHITE = ' ';
	private ArrayList<char[][]> squares;
	private ArrayList<char[][]> boats;
	private User userWithTurn;
	
	public HundirLaFota(Game game) {
		super(game);
		squares.add(new char[10][10]);
		squares.add(new char[10][10]);
		boats.add(new char[10][10]);
		boats.add(new char[10][10]);
		for (int row=0; row<10; row++)
			for (int col=0; col<10; col++)
			{
				squares.get(0)[row][col]=WHITE;
				squares.get(1)[row][col]=WHITE;
				boats.get(0)[row][col]=O;
				boats.get(1)[row][col]=O;
			}
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
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	protected boolean isTheTurnOf(User user) {
		return this.userWithTurn.equals(user);
	}
	@Override
	protected void postMove(User user, JSONObject jsoMovement) throws Exception {
		int userActivo;
		if (!jsoMovement.get("type").equals(HundirLaFlotaMovement.class.getSimpleName())) {
			throw new Exception("Unexpected type of movement");
		}
		int row=jsoMovement.getInt("row");
		int col=jsoMovement.getInt("col");
		JSONMessage result=null;
		
		if (this.userWithTurn.equals(this.players.get(0))) {
			userActivo = 0;
		}else{
			userActivo =1;
		}
		
		if (this.squares.get(userActivo)[row][col]!=WHITE) {
			result=new ErrorMessage("Square busy");
			Notifier.get().post(user, result);
		} else if (!this.isTheTurnOf(user)) {
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
				//Si es el primer jugador se compara con el square del jugador uno
				this.squares.get(0)[row][col]=X;
				this.userWithTurn=this.players.get(1);
			} else {
				this.squares.get(1)[row][col]=O;
				this.userWithTurn=this.players.get(0);
			}
			result=new HundirLaFlotaBoardMessage(this.toString());
			Notifier.get().post(this.players, result);
		}
		
	}
	
	protected void addBoat(int player,int[] row, int[] col){
		if(validate(row, col, player)){
			for (int i = 0; i < col.length; i++) {
				boats.get(player)[row[i]][col[i]] = X;
			}
		}

	}
	
	/******************************************************************
	 * Valida que el barco este bien situado, que se encuentre en una *
	 * unica columna o unica fila                                     *
	 ******************************************************************/
	
	protected boolean validate(int row[], int[] col,int player){
		boolean ok = false;
		//Si esta en la misma fila
		for (int i = 0; i < row.length-1; i++) {
			if(row[i] == row[i+1]){
				if(col[i]+1 == col[i+1])
					ok = true;
				else{
					ok=false;
					break;
				}
			}else{
				ok = false;
				break;
			}
		}
		//Si esta en la misma columna
		if(!ok){
			for (int i = 0; i < col.length-1; i++) {
				if(col[i] == col[i+1]){
					if(row[i] + 1 == row[i+1])
						ok = true;
					else{
						ok = false;
						break;
					}
				} else {
					ok = false;
					break;
				}
			}
		}
		if(ok)
			ok = validateNoRepeat(row, col, player);
		return ok;
	}
	
	/*********************************************************************
	 * En caso de que alguna de las casillas este ocupada por otro barco *
	 * devuelve true                                                     *
	 *********************************************************************/
	
	private boolean validateNoRepeat(int[] row, int[] col,int player){
		for (int i = 0; i < col.length; i++) {
			if(boats.get(player)[row[i]][col[i]] == X){
				return false;
			}
		}
		return true;
	}

}