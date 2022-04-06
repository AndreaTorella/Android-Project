package com.ium.ripetizioni;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ium.ripetizioni.fragment.SignInFragment;

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String name = preferences.getString("Email", "");
        if (!(name.equalsIgnoreCase(""))) menu.findItem(R.id.account).setTitle("Logout");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        switch (item.getItemId()) {
            case R.id.account:
                String name = preferences.getString("Email", "");
                if (name.equalsIgnoreCase("")) {
                    //Intent intent = new Intent(this, LoginSignup.class);
                    //this.startActivity(intent);
                } else {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("Email", "");
                    editor.putString("ID", "");
                    editor.apply();
                    Toast.makeText(getApplicationContext(), "Logout effettuato!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(this, MainActivity.class);
                    this.startActivity(intent);
                }
                break;
            case R.id.info:
                Toast.makeText(getApplicationContext(), "Ripetizioni - Agostino Messina", Toast.LENGTH_LONG).show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.corsi:
                // Intent intent = new Intent(this, ListaCorsi.class);
                //this.startActivity(intent);
                break;

            case R.id.prenotazioni:
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String name = preferences.getString("Email", "");
                if (name.equalsIgnoreCase("")) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Prenotazioni effettuate")
                            .setMessage("Devi prima effettuare il login!\nProcedere ora?")
                           // .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    Intent intent2 = new Intent(getApplicationContext(), LoginSignup_fragment.class);
                                    intent2.putExtra("provenienza", "prenotazioni");
                                    startActivity(intent2);
                                }
                            })
                            .setNegativeButton(android.R.string.no, null).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Già loggato", Toast.LENGTH_SHORT).show();
                    preferences.edit().remove("Email").apply();
                    //Intent intent2 = new Intent(this, ListaPrenotazioni.class);
                    //startActivity(intent2);
                }
                break;

            default:
                break;
        }

    }

}
