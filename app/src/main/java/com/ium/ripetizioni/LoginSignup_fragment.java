package com.ium.ripetizioni;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;


import com.ium.ripetizioni.fragment.SignInFragment;
import com.ium.ripetizioni.fragment.SignUpFragment;

public class LoginSignup_fragment extends AppCompatActivity implements View.OnClickListener {
    private FragmentPagerAdapter adapterViewPager;

    String richiamo = null;
    String corso = null;
    private TextView loginText, signUpBtn;
    private Button sign_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras == null) richiamo = null;
        else richiamo = extras.getString("provenienza");
        if (extras == null) corso = null;
        else corso = extras.getString("corso");
        setContentView(R.layout.activity_login);

        loginText = findViewById(R.id.login);
        signUpBtn = findViewById(R.id.signup);
        sign_button = findViewById(R.id.sign_button);
        sign_button.setOnClickListener(this);
        signUpBtn.setOnClickListener(this);
        loginText.setOnClickListener(this);
        sign_button.setText(R.string.login);
        sign_button.setTag(R.string.login);


        FragmentTransaction transaction;
        transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new SignInFragment());
        transaction.commit();
    }

    private void setFragment(Fragment fragment) {
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    @Override
    public void onClick(View v) {
        if (v == loginText) {
            sign_button.setText(R.string.login);
            sign_button.setTag(R.string.login);
            setFragment(new SignInFragment());
        }

        if (v == signUpBtn) {
            sign_button.setText(R.string.signup);
            sign_button.setTag(R.string.signup);
            setFragment(new SignUpFragment());
        }

        if (v == sign_button) {
            Object tag = v.getTag();
            if (tag.equals(R.string.login)) {
                GestioneDB db = new GestioneDB(this);
                db.open();
                EditText edit = findViewById(R.id.signin_email);
                EditText edit1 = findViewById(R.id.signin_password);
                String email = edit.getText().toString();
                String password = edit1.getText().toString();
                Cursor c = db.getUtenti();
                boolean debug = true;
                if (c.moveToFirst()) {
                    do {
                        if (c.getString(3).equals(email) && c.getString(4).equals(password)) {
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("Email", c.getString(3));
                            editor.putString("ID", c.getString(0));
                            editor.apply();
                            Toast.makeText(getApplicationContext(), "Login effettuato!", Toast.LENGTH_SHORT).show();
                            debug = false;
                            db.close();
                            Intent intent = new Intent(this, MainActivity.class);
                            if (richiamo != null && richiamo.equals("prenotazioni")) {
                                //intent = new Intent(this, ListaPrenotazioni.class);
                            } else if (richiamo != null && richiamo.equals("listaliberi")) {
                                //intent = new Intent(this, ListaLiberi.class);
                                //intent.putExtra("corso", corso);
                            }
                            //this.startActivity(intent);
                        }
                    } while (c.moveToNext());
                    if (debug)
                        Toast.makeText(getApplicationContext(), "L'utente non esiste", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Impossibile effettuare il login", Toast.LENGTH_SHORT).show();
                }
                db.close();
            } else if (tag.equals(R.string.signup)) {
                GestioneDB db = new GestioneDB(this);
                db.open();
                EditText edit1 = findViewById(R.id.nome);
                EditText edit2 = findViewById(R.id.cognome);
                EditText edit3 = findViewById(R.id.email);
                EditText edit4 = findViewById(R.id.password);
                String nome = edit1.getText().toString();
                String cognome = edit2.getText().toString();
                String email = edit3.getText().toString();
                String password = edit4.getText().toString();
                long id = db.inserisciUtente(nome, cognome, email, password);
                if (id != -1) {
                    Toast.makeText(getApplicationContext(), "Registrazione effettuata!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Impossibile registrarsi!", Toast.LENGTH_SHORT).show();
                }
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = preferences.edit();
                Cursor c = db.getUtente(email);
                editor.putString("Email", email);
                editor.putString("ID", c.getString(0));
                editor.apply();
                db.close();
                Intent intent = null;
                /*if (richiamo == null)
                    intent = new Intent(this, MainActivity.class);
                else if (richiamo.equals("prenotazioni"))
                    intent = new Intent(this, ListaPrenotazioni.class);
                else if (richiamo.equals("listaliberi")) {
                    intent = new Intent(this, ListaLiberi.class);
                    intent.putExtra("corso", corso);
                }
                this.startActivity(intent);*/
            }
        }
    }
}
