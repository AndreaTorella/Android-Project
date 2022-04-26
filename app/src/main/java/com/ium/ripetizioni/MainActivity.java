package com.ium.ripetizioni;

import android.app.AlertDialog;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.corsi) {
            Intent intent = new Intent(this, ListaCorsi.class);
            intent.putExtra("provenienza", "homepage");
            this.startActivity(intent);
            finish();
        } else if (id == R.id.prenotazioni) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String name = preferences.getString("Email", "");
            if (name.equalsIgnoreCase("")) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Prenotazioni effettuate")
                        .setMessage("Devi prima effettuare il login.\nProcedere ora?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent intent2 = new Intent(getApplicationContext(), LoginSignup.class);
                                intent2.putExtra("provenienza", "prenotazioni");
                                startActivity(intent2);
                                finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();

            } else {
                Intent intent2 = new Intent(this, ListaPrenotazioni.class);
                startActivity(intent2);
                finish();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String name = preferences.getString("Email", "");

        if (name.equalsIgnoreCase("")) {
            Intent intent = new Intent(this, LoginSignup.class);
            this.startActivity(intent);
        } else {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("Email", "");
            editor.putString("ID", "");
            editor.apply();
            Toast.makeText(getApplicationContext(), "Logout effettuato!", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    /* Menu a tendina
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }*/
}
