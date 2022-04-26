package com.ium.ripetizioni;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ium.ripetizioni.fragment.LoginFragment;
import com.ium.ripetizioni.fragment.SignUpFragment;

public class LoginSignup extends AppCompatActivity implements View.OnClickListener {

    String provenienza;
    String nomeCorso;
    int idCorso;
    TextView loginText, signUpBtn;
    Button sign_button;
    EditText nomeEditText;
    EditText cognomeEditText;
    EditText emailEditText;
    EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            provenienza = extras.getString("provenienza");
            nomeCorso = extras.getString("nome_corso");
            idCorso = extras.getInt("id_corso");
        }
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
        transaction.replace(R.id.fragment_container, new LoginFragment());
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
            setFragment(new LoginFragment());
        }

        if (v == signUpBtn) {
            sign_button.setText(R.string.signup);
            sign_button.setTag(R.string.signup);
            setFragment(new SignUpFragment());
        }

        if (v == sign_button) {
            Object tag = v.getTag();
            if (tag.equals(R.string.login)) {
                Context context = this;
                emailEditText = findViewById(R.id.login_email);
                passwordEditText = findViewById(R.id.login_password);
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                if (!checkValidFieldsLogin())
                    return;


                RequestQueue queue = Volley.newRequestQueue(this);
                String url = "http://10.0.2.2:8080/IUM_TWEB_Project_war_exploded/mainServlet?action=" + "requestroleandroid" +
                        "&username=" + email + "&password=" + password;

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.equals("found")) {
                                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("Email", email);
                                    editor.putString("Password", password);
                                    editor.apply();
                                    Toast.makeText(getApplicationContext(), "Login effettuato!", Toast.LENGTH_SHORT).show();
                                    goToActivity();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Impossibile effettuare il login, response: " +response, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //textView.setText("That didn't work!");
                    }
                });
                queue.add(stringRequest);

            } /*else if (tag.equals(R.string.signup)) {
                GestioneDB db = new GestioneDB(this);
                db.open();
                nomeEditText = findViewById(R.id.nome);
                cognomeEditText = findViewById(R.id.cognome);
                emailEditText = findViewById(R.id.email);
                passwordEditText = findViewById(R.id.password);

                if (!checkValidFieldsSignup())
                    return;

                String nome = nomeEditText.getText().toString();
                String cognome = cognomeEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                long id = db.inserisciUtente(nome, cognome, email, password);
                if (id != -1) {
                    Toast.makeText(getApplicationContext(), "Registrazione effettuata!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Impossibile registrarsi!", Toast.LENGTH_SHORT).show();
                }
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = preferences.edit();
                Cursor c = db.getUtente(email);
                editor.putString("Email", email);
                editor.putString("ID", c.getString(0));
                editor.apply();
                db.close();
                goToActivity();
            }
            */
        }

    }

    private void goToActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        if (provenienza != null && provenienza.equals("prenotazioni")) {
            intent = new Intent(this, ListaPrenotazioni.class);
        } else if (provenienza != null && provenienza.equals("listaliberi")) {
            intent = new Intent(this, ListaRipetizioni.class);
            intent.putExtra("nome_corso", nomeCorso);
            intent.putExtra("id_corso", idCorso);
        } else if (provenienza != null && provenienza.equals("listacorsi")) {
            intent = new Intent(this, ListaCorsi.class);
        }
        finish();

        this.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (provenienza != null && provenienza.equals("listaliberi")) {
            Intent intent = new Intent(getApplicationContext(), ListaRipetizioni.class);
            intent.putExtra("nome_corso", nomeCorso);
            intent.putExtra("id_corso", idCorso);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
        super.onBackPressed();
    }

    private boolean checkValidFieldsLogin() {
        boolean result = true;
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (email.length() <= 0) {
            emailEditText.setError(getString(R.string.mandatory));
            result = false;
        }
        if (password.length() <= 0) {
            passwordEditText.setError(getString(R.string.mandatory));
            result = false;
        }

        return result;
    }

    private boolean checkValidFieldsSignup() {
        boolean result = true;
        String nome = nomeEditText.getText().toString();
        String cognome = cognomeEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (nome.length() <= 0) {
            nomeEditText.setError(getString(R.string.mandatory));
            result = false;
        }
        if (cognome.length() <= 0) {
            cognomeEditText.setError(getString(R.string.mandatory));
            result = false;
        }
        if (email.length() <= 0) {
            emailEditText.setError(getString(R.string.mandatory));
            result = false;
        }
        if (password.length() <= 0) {
            passwordEditText.setError(getString(R.string.mandatory));
            result = false;
        }

        return result;
    }

}
