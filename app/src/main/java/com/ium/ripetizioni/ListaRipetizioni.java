package com.ium.ripetizioni;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ium.ripetizioni.dto.Ripetizione;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListaRipetizioni extends AppCompatActivity {

    Context context = this;
    Map<Integer, List<String>> mapRipetizioni = new HashMap<>();
    private String nomeCorso;
    private int idCorso;
    //Key:id list[0]: nome_docente list[1]: cognome_docente  list[2]: giorno_ripetizione  list[3]: ora_ripetizione

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        nomeCorso = intent.getExtras().getString("nome_corso");
        idCorso = intent.getExtras().getInt("id_corso");
        setContentView(R.layout.activity_listaimg);
        caricamentoLista(idCorso);


    }

    public void caricamentoLista(int idCorso) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://10.0.2.2:8080/IUM_TWEB_Project_war_exploded/mainServlet?action=" + "requestcorsocatalogo" +
                "&id_corso=" + idCorso;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<ArrayList<Ripetizione>>() {
                        }.getType();
                        ArrayList<Ripetizione> ripetizioni = gson.fromJson(response, listType);
                        if (ripetizioni.size() == 0) {
                            setEmptyLayout();
                        } else {
                            //Toast.makeText(getApplicationContext(), "ripetizioni: " + ripetizioni, Toast.LENGTH_LONG).show();
                            for (int i = 0; i < ripetizioni.size(); i++) {
                                ArrayList<String> arrayList = new ArrayList<>();
                                arrayList.add(ripetizioni.get(i).getDocente().getNome());
                                arrayList.add(ripetizioni.get(i).getDocente().getCognome());
                                arrayList.add(ripetizioni.get(i).getSlot().getGiornoString());
                                arrayList.add(ripetizioni.get(i).getSlot().getOrarioString());
                                mapRipetizioni.put(i, arrayList);
                            }

                            List<HashMap<String, String>> aList = new ArrayList<>();

                            for (int i : mapRipetizioni.keySet()) {
                                HashMap<String, String> hm = new HashMap<>();
                                hm.put("listview_course", "Corso: " + nomeCorso);
                                hm.put("listview_title", "Docente: " + mapRipetizioni.get(i).get(0) + " " + mapRipetizioni.get(i).get(1));
                                hm.put("listview_date", "Giorno: " + mapRipetizioni.get(i).get(2) + "    Ora: " +
                                        mapRipetizioni.get(i).get(3));
                                //hm.put("id_pren", id_prenotazione.get(i));
                                aList.add(hm);
                            }

                            String[] from = {"listview_course", "listview_title", "listview_date"};
                            int[] to = {R.id.listview_item_course, R.id.listview_item_title, R.id.listview_item_short_description};

                            SimpleAdapter simpleAdapter = new SimpleAdapter(getBaseContext(), aList, R.layout.activity_lista_ripetizioni, from, to);
                            ListView androidListView = findViewById(R.id.list_view);
                            androidListView.setAdapter(simpleAdapter);
                            androidListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adattatore, final View componente, int pos, long id) {
                                    final HashMap<String, String> item = (HashMap<String, String>) adattatore.getItemAtPosition(pos);
                                    final String value = item.get("listview_title");
                                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                    final String username = preferences.getString("Email", "");
                                    //Toast.makeText(getApplicationContext(), "Username: " + username, Toast.LENGTH_LONG).show();
                                    if (username.equalsIgnoreCase("")) {
                                        new AlertDialog.Builder(ListaRipetizioni.this)
                                                .setTitle("Prenotazione")
                                                .setMessage("Devi prima effettuare il login! Procedere ora?")
                                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int whichButton) {
                                                        Intent intent = new Intent(getApplicationContext(), LoginSignup.class);
                                                        intent.putExtra("provenienza", "listaliberi");
                                                        intent.putExtra("nome_corso", nomeCorso);
                                                        intent.putExtra("id_corso", idCorso);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                })
                                                .setNegativeButton(android.R.string.no, null).show();
                                    } else {
                                        String[] myArray = item.get("listview_date").split("    ");
                                        String[] docenteArray = item.get("listview_title").split("Docente: ");
                                        String[] nomeCognomeDocente = docenteArray[1].split(" ");
                                        String[] token1 = myArray[0].split(": ");
                                        String[] token2 = myArray[1].split(": ");
                                        String giorno = token1[1];
                                        String ora = token2[1];
                                        String nomeDocente = nomeCognomeDocente[0];
                                        String cognomeDocente = nomeCognomeDocente[1];

                                        new AlertDialog.Builder(ListaRipetizioni.this)
                                                .setTitle("Prenotazione")
                                                .setMessage("Vuoi confermare la prenotazione?")
                                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                                    public void onClick(DialogInterface dialog, int whichButton) {
                                                        RequestQueue queue = Volley.newRequestQueue(context);
                                                        String url = "http://10.0.2.2:8080/IUM_TWEB_Project_war_exploded/mainServlet?action=" + "requestinsertprenotazioneandroid" +
                                                                "&corso=" + nomeCorso +
                                                                "&nome=" + nomeDocente +
                                                                "&cognome=" + cognomeDocente +
                                                                "&giorno=" + giorno +
                                                                "&ora=" + ora +
                                                                "&username=" + username;

                                                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                                                new Response.Listener<String>() {
                                                                    @Override
                                                                    public void onResponse(String response) {
                                                                        Log.d("ListaLiberi", "Dentro on_response del server, response: " + response);
                                                                        if (response.equals("done")) {
                                                                            Toast.makeText(getApplicationContext(), "Prenotazione effettuata!", Toast.LENGTH_LONG).show();
                                                                            finish();
                                                                            startActivity(getIntent());
                                                                        }
                                                                    }
                                                                }, new Response.ErrorListener() {
                                                            @Override
                                                            public void onErrorResponse(VolleyError error) {
                                                                //Log.d("Error", "Error: " + error);
                                                                Log.d("Error", "Error: " + error);
                                                                Toast.makeText(getApplicationContext(), "Errore nella connessione!", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });

                                                        queue.add(stringRequest);
                                                    }
                                                })
                                                .setNegativeButton(android.R.string.no, null).show();
                                    }
                                }
                            });

                            Toolbar toolbar = findViewById(R.id.toolbar);
                            setSupportActionBar(toolbar);
                            getSupportActionBar().setTitle(null);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //textView.setText("That didn't work!");
            }
        });
        queue.add(stringRequest);
    }

    private void setEmptyLayout() {
        RelativeLayout mainLayout = findViewById(R.id.emptyLayout);
        View content = getLayoutInflater()
                .inflate(R.layout.content_empty_lista_liberi, mainLayout, false);
        mainLayout.addView(content);
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
            intent.putExtra("nome_corso", nomeCorso);
            intent.putExtra("id_corso", idCorso);
            intent.putExtra("provenienza", "ripetizioni");
            this.startActivity(intent);
            finish();
        } else {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("Email", "");
            editor.putString("ID", "");
            editor.apply();
            Toast.makeText(getApplicationContext(), "Logout effettuato!", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

}