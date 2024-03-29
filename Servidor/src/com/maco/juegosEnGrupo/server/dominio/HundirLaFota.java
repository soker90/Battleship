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

import com.maco.hundirlaflota.jsonMessages.HundirLaFlotaBarcos;
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
	private int ganador;
	private int perdedor;
	private final static int COLOCANDO = 0;
	private final static int ESPERA = 1;
	private final static int JUGANDO = 2;
	private final static int FINALIZADO = 3;
	private int estado;
	
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
		ganador = -1;
		perdedor = -1;
		cont = new int[2];
		cont[0] = 0;
		cont[1] = 0;
		estado = HundirLaFota.COLOCANDO;
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
		if(this.estado != HundirLaFota.JUGANDO){
			if(this.estado == HundirLaFota.ESPERA){
				JSONMessage message = new ErrorMessage("Tu oponente está colocando.");
				Notifier.get().post(user, message);
			}
			return;
		}
		
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
			if(this.ganador == -1){
				updateBoard(row, col, result);
			}
			
		
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
			calcularGanador();
			if(this.ganador != -1){
				JSONMessage message = new ErrorMessage("¡¡Has ganado!!");
				Notifier.get().post(this.players.get(ganador), message);
				message = new ErrorMessage("¡¡Has perdido!!");
				Notifier.get().post(this.players.get(perdedor), message);
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
	
	private boolean calcularGanador(){
		if(this.ganador == -1){
			int cont[] = new int[2];
			cont[0]=0;
			cont[1]=0;
			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < squares.get(i).length; j++) {
					for (int j2 = 0; j2 < squares.get(i).length; j2++) {
						if(squares.get(i)[j][j2] == T)
							cont[i]++;
					}
				}
			}
				if(cont[0] == 8){
					this.ganador = 1;
					this.perdedor = 0;
					return true;
				}
				if(cont[1] == 8){
					this.ganador = 0;
					this.perdedor = 1;
					return true;
				}
			
		}
		return false;
	}
	
	public void colocar(User user, JSONObject jsoBarcos) throws Exception {
		postColocar(user, jsoBarcos);
		switch (estado) {
		case HundirLaFota.COLOCANDO:
			estado = HundirLaFota.ESPERA;
			break;
		case HundirLaFota.ESPERA:
			estado = HundirLaFota.JUGANDO;

		default:
			break;
		}
		
	}
	
	private void postColocar (User user, JSONObject jsoBarcos) throws Exception {
		if (!jsoBarcos.get("type").equals(HundirLaFlotaBarcos.class.getSimpleName())) {
			throw new Exception("Unexpected type of movement");
		}
		String Ssquare=jsoBarcos.getString("squares");
		char[][] square = toCharofString(Ssquare);
		
		if(user == this.players.get(0)){
			this.squares.remove(0);
			this.squares.add(0, square);
		} else {
			this.squares.remove(1);
			this.squares.add(1, square);
				
		}
	}
	
	private char[][] toCharofString(String s){
		char[][] c = new char[5][5];
		int cont = 0;
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				c[i][j] = s.charAt(cont);
				cont++;
			}
		}
		
		return c;
	}
	
	public void notifyAbandonar(User user) throws Exception{
		if(this.players.size() == 2){
			JSONMessage message = new ErrorMessage("Su oponente ha abandonado la partida.");
			Notifier.get().post(this.getOponente(user), message);
		}
	}
	
	private User getOponente(User user) {
		if(this.players.get(0) == user)
			return this.players.get(1);
		else
			return this.players.get(0);
	}

}
