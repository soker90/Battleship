package edu.uclm.esi.common.androidClient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.maco.tresenraya.HundirLaFlotaActivity;
import com.maco.tresenraya.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ColocalBarcosActivity extends ActionBarActivity {

    private TextView tvPlayer;
    private TextView tvMessage;
    private Button[] btns;
    private char[][] squares;
    private int[] longitud;
    private int ncasillas;
    private int nbarco;
    public char X='X', O='O', WHITE = ' ';
    private int NBARCOS = 3;
    private  Button btnSend;
    ArrayList<String> rowTemp;
    ArrayList<String> colTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcos);
        squares = new char[5][5];
        longitud = new int[]{2, 3, 3};
        this.ncasillas = 0;
        rowTemp = new ArrayList<>();
        colTemp = new ArrayList<>();
        nbarco = 0;

        this.tvPlayer=(TextView) this.findViewById(R.id.textViewTresEnRayaPlayer);
        this.tvMessage=(TextView) this.findViewById(R.id.textViewMessage);
        tvPlayer.setText("Elige las casillas del barco");
        tvMessage.setText("Barco de longitud "+longitud[0]);
        this.btns=new Button[25];
        int resIS = this.getResources().getIdentifier("btnSend", "id", "com.maco.tresenraya");
        this.btnSend = (Button) findViewById(resIS);
        int cont=0;
        for (int row=0; row<5; row++)
            for (int col = 0; col < 5; col++) {
                int resId = this.getResources().getIdentifier("d" + cont, "id", "com.maco.tresenraya");
                this.btns[cont] = (Button) findViewById(resId);
                JSONObject tag = new JSONObject();
                try {
                    tag.put("row", row);
                    tag.put("col", col);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                this.btns[cont].setTag(tag);
                squares[row][col] = ' ';
                this.btns[cont].setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (ncasillas < longitud[nbarco]) {
                            try {
                                JSONObject jso = (JSONObject) v.getTag();
                                ((Button) v).setText("X");
                                ncasillas++;
                                tvMessage.setText("Barco de longitud " + longitud[nbarco]);
                                rowTemp.add(String.valueOf(jso.getInt("row")));
                                colTemp.add(String.valueOf(jso.getInt("col")));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        else{
                            tvMessage.setText("Ya están todas las casillas elegidas");
                        }
                    }
                });
                cont++;
            }
        this.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                String row = "";
                String col = "";
                ArrayList<Integer> s = new ArrayList<Integer>();
                for (Object i : rowTemp.toArray()) {
                    row += i.toString();
                }

               for (Object i : colTemp.toArray()) {
                    col += i.toString();
                }

                addBoat(row.toCharArray(),col.toCharArray());

            }
        });

    }

    protected void addBoat(char[] row, char[] col){
        if(nbarco < NBARCOS) {
            if (validate(row, col)) {
                String rowAux;
                String colAux;
                for (int i = 0; i < row.length; i++) {
                    rowAux = "";
                    rowAux += row[i];
                    colAux = "";
                    colAux += col[i];
                    squares[Integer.parseInt(rowAux)][Integer.parseInt(colAux)] = X;
                }

                if(nbarco < NBARCOS) {
                    if (ncasillas == longitud[nbarco])
                        nbarco++;
                    if(nbarco < NBARCOS)
                        tvMessage.setText("Barco de longitud " + longitud[nbarco]);
                    else {
                        tvMessage.setText("Se acabó");
                        Intent i=new Intent(this, HundirLaFlotaActivity.class);
                        startActivity(i);
                        //sendBarcos();
                    }
                }
            } else {
                tvMessage.setText("Error! Barco de longitud " + longitud[nbarco]);
            }
            borrarCasillas();
        }

    }

    /******************************************************************
     * Valida que el barco este bien situado, que se encuentre en una *
     * unica columna o unica fila                                     *
     ******************************************************************/

    protected boolean validate(char[] row, char[] col){
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
            ok = validateRepeat(row, col);
        return ok;
    }

    /*********************************************************************
     * En caso de que alguna de las casillas este ocupada por otro barco *
     * devuelve true                                                     *
     *********************************************************************/

    private boolean validateRepeat(char[] row, char[] col){
        String rowAux;
        String colAux;
        for (int i = 0; i < row.length; i++){
                rowAux = "";
                rowAux += row[i];
                colAux = "";
                colAux += col[i];
                if(squares[Integer.parseInt(rowAux)][Integer.parseInt(colAux)] == X){
                    return false;
                }
            }
        return true;
    }

    private void borrarCasillas(){
        ncasillas = 0;
        rowTemp = new ArrayList<>();
        colTemp = new ArrayList<>();

        for (int i = 0; i < 25;i++)
            this.btns[i].setText("");

    }

    /**************
    //No funciona
     **************/
    /*
    private void sendBarcos(){
        Store store=Store.get();
        JSONParameter jspIdUser=new JSONParameter("idUser", ""+ store.getUser().getId());
        JSONParameter jspIdGame=new JSONParameter("idGame", ""+store.getIdGame());
        JSONParameter jspIdMatch=new JSONParameter("idMatch", ""+store.getIdMatch());
        HundirLaFlotaBarcos boat;
        boat = new HundirLaFlotaBarcos(squares);
        JSONParameter jspBoat = new JSONParameter("squares", ""+boat);
        try {
            JSONMessage jsm= Proxy.get().postJSONOrderWithResponse("SendBarcos.action", jspIdUser, jspIdGame, jspIdMatch, jspBoat);
            if (!jsm.getType().equals(OKMessage.class.getSimpleName())) {
                ErrorMessage em=(ErrorMessage) jsm;
                tvMessage.setText(em.getText());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
