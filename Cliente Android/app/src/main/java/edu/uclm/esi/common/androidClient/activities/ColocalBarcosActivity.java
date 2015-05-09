package edu.uclm.esi.common.androidClient.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.maco.tresenraya.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ColocalBarcosActivity extends ActionBarActivity {

    private TextView tvPlayer;
    private TextView tvMessage;
    private Button[] btns;
    private char[][] squares;
    private int[] longitud;
    private int nbarcos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcos);
        squares = new char[5][5];
        longitud = new int[]{2, 3, 3};
        this.nbarcos = 0;

        this.tvPlayer=(TextView) this.findViewById(R.id.textViewTresEnRayaPlayer);
        this.tvMessage=(TextView) this.findViewById(R.id.textViewMessage);
        tvPlayer.setText("Elige las casillas del barco");
        //tvMessage.setText("Barco de longitud "+longitud[cont]);
        this.btns=new Button[25];
        int cont=0;
        for (int row=0; row<5; row++) {
            for (int col=0; col<5; col++) {
                int resId = this.getResources().getIdentifier("d"+cont, "id", "com.maco.tresenraya");
                this.btns[cont]=(Button) findViewById(resId);
                JSONObject tag=new JSONObject();
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
                        if(nbarcos <3) {
                            try {
                                JSONObject jso = (JSONObject) v.getTag();
                                squares[jso.getInt("row")][jso.getInt("col")] = 'X';
                                ((Button) v).setText("X");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                cont++;
            }
        }
    }
}
