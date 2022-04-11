package com.ium.ripetizioni;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button corsiButton;
    Button prenotazioniButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        corsiButton = findViewById(R.id.corsi);
        corsiButton.setOnClickListener(this);
        prenotazioniButton = findViewById(R.id.prenotazioni);
        prenotazioniButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.corsi:
                if (getNCorsi() > 0) {
                    Intent intent = new Intent(this, ListaCorsi.class);
                    this.startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Non sono presenti corsi", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.prenotazioni:
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String name = preferences.getString("Email", "");
                if (name.equalsIgnoreCase("")) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Prenotazioni effettuate1")
                            .setMessage("Devi prima effettuare il login.\nProcedere ora?")
                            // .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    Intent intent2 = new Intent(getApplicationContext(), LoginSignup.class);
                                    intent2.putExtra("provenienza", "prenotazioni");
                                    startActivity(intent2);
                                }
                            })
                            .setNegativeButton(android.R.string.no, null).show();
                    finish();
                } else {
                    //TODO remove
                    Toast.makeText(getApplicationContext(), "Già loggato", Toast.LENGTH_SHORT).show();
                    preferences.edit().remove("Email").apply();
                    preferences.edit().remove("ID").apply();
                    //Intent intent2 = new Intent(this, ListaPrenotazioni.class);
                    //startActivity(intent2);
                }
                break;

            default:
                break;
        }
    }

    private int getNCorsi() {
        GestioneDB db = new GestioneDB(this);
        db.open();
        Cursor c = db.getNCorsi();
        int result = 0;
        if (c.moveToFirst()) {
            result = c.getInt(0);
        }
        db.close();

        return result;
    }



}
