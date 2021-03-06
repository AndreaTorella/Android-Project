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
import com.ium.ripetizioni.dto.Prenotazione;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListaPrenotazioni extends AppCompatActivity {

    private final List<String> listviewTeacher = new ArrayList<>();
    private final List<String> listviewCourse = new ArrayList<>();
    private final List<String> idPren = new ArrayList<>();
    private final List<String> listviewDate = new ArrayList<>();
    private final List<String> listviewStatus = new ArrayList<>();
    private final int contaElem = 0;
    Map<Integer, List<String>> mapPrenotazioni = new HashMap<>();
    Context context = this;
    //Key:id  list[0]: titolo_corso  list[1]: nome_docente list[2]: cognome_docente  list[3]: giorno_ripetizione  list[4]: ora_ripetizione list[5]: stato

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_listaimg);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        caricamentoLista(preferences.getString("Email", ""));


    }

    public void caricamentoLista(String username) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://10.0.2.2:8080/IUM_TWEB_Project_war_exploded/mainServlet?action=" + "requestpersonalprenotazioniandroid" +
                "&username=" + username;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("ListaPrenotazioni", "Dentro onResponde di caricamentoLista");
                        Gson gson = new Gson();
                        Type listType = new TypeToken<ArrayList<Prenotazione>>() {
                        }.getType();
                        ArrayList<Prenotazione> prenotazioni = gson.fromJson(response, listType);

                        if (prenotazioni.size() == 0) {
                            setEmptyLayout();
                        } else {
                            /*Key:id  list[0]: titolo_corso  list[1]: nome_docente list[2]: cognome_docente
                            list[3]: giorno_ripetizione  list[4]: ora_ripetizione list[5]: stato
                             */

                            for (int i = 0; i < prenotazioni.size(); i++) {
                                ArrayList<String> arrayList = new ArrayList<>();
                                arrayList.add(prenotazioni.get(i).getCorso().getTitolo());
                                arrayList.add(prenotazioni.get(i).getDocente().getNome());
                                arrayList.add(prenotazioni.get(i).getDocente().getCognome());
                                arrayList.add(prenotazioni.get(i).getSlot().getGiornoString());
                                arrayList.add(prenotazioni.get(i).getSlot().getOrarioString());
                                arrayList.add(String.valueOf(prenotazioni.get(i).getStato()));
                                mapPrenotazioni.put(i, arrayList);
                            }
                            List<HashMap<String, String>> adapterList = new ArrayList<>();

                            for (int i : mapPrenotazioni.keySet()) {
                                HashMap<String, String> hm = new HashMap<>();
                                hm.put("listview_title", "Docente: " + mapPrenotazioni.get(i).get(1) + " " + mapPrenotazioni.get(i).get(2));
                                hm.put("listview_course", "Corso: " + mapPrenotazioni.get(i).get(0));
                                hm.put("listview_date", "Giorno: " + mapPrenotazioni.get(i).get(3) + "      Ora: " + mapPrenotazioni.get(i).get(4));
                                String stato = getFullStatus(mapPrenotazioni.get(i).get(5));
                                hm.put("listview_status", "Stato: " + stato);
                                //hm.put("id_pren", idPren.get(i));
                                adapterList.add(hm);
                            }

                            String[] from = {"listview_course", "listview_title", "listview_date", "listview_status"};
                            int[] to = {R.id.listview_item_course, R.id.listview_item_title, R.id.listview_item_short_description, R.id.listview_item_status};

                            SimpleAdapter simpleAdapter = new SimpleAdapter(getBaseContext(), adapterList, R.layout.activity_lista_prenotazioni, from, to);
                            ListView androidListView = findViewById(R.id.list_view);
                            androidListView.setAdapter(simpleAdapter);
                            androidListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adattatore, final View componente, int pos, long id) {
                                    final HashMap<String, String> item = (HashMap<String, String>) adattatore.getItemAtPosition(pos);
                                    final String value = item.get("listview_title");
                                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                    final String username = preferences.getString("Email", "");
                                    if (username.equalsIgnoreCase("")) {
                                        Intent intent = new Intent(getApplicationContext(), LoginSignup.class);
                                        startActivity(intent);
                                    } else {
                                        //TODO richiamare API per rimozione prenotazione
                                        String[] myArray = item.get("listview_date").split("    ");
                                        String[] docenteArray = item.get("listview_title").split("Docente: ");
                                        String[] nomeCognomeDocente = docenteArray[1].split(" ");
                                        String[] token1 = myArray[0].split(": ");
                                        String[] token2 = myArray[1].split(": ");
                                        String[] corsoArray = item.get("listview_course").split("Corso: ");
                                        String giorno = token1[1];
                                        String ora = token2[1];
                                        String nomeDocente = nomeCognomeDocente[0];
                                        String cognomeDocente = nomeCognomeDocente[1];
                                        String titoloCorso = corsoArray[1];

                                        new AlertDialog.Builder(ListaPrenotazioni.this)
                                                .setTitle("Prenotazione")
                                                .setMessage("Vuoi eliminare la prenotazione?")
                                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int whichButton) {
                                                        RequestQueue queue = Volley.newRequestQueue(context);
                                                        String url = "http://10.0.2.2:8080/IUM_TWEB_Project_war_exploded/mainServlet?action=" + "requestdropprenotazioneandroid" +
                                                                "&corso=" + titoloCorso +
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
                                                                            Intent intent = new Intent(getApplicationContext(), ListaPrenotazioni.class);
                                                                            startActivity(intent);
                                                                            finish();
                                                                            Toast.makeText(getApplicationContext(), "Prenotazione eliminata!", Toast.LENGTH_SHORT).show();
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

            }
        });

        queue.add(stringRequest);

    }

    private String getFullStatus(String string) {
        switch (string) {
            case "1":
                return "Prenotata";
            case "2":
                return "Effettuata";
            case "3":
                return "Disdetta";
        }

        return "";
    }

    private void setEmptyLayout() {
        RelativeLayout mainLayout = findViewById(R.id.emptyLayout);
        View content = getLayoutInflater()
                .inflate(R.layout.content_empty_lista_prenotazioni, mainLayout, false);
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
            intent.putExtra("provenienza", "prenotazioni");
            this.startActivity(intent);
            finish();
        } else {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("Email", "");
            editor.putString("ID", "");
            editor.apply();
            Toast.makeText(getApplicationContext(), "Logout effettuato!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            this.startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    public void gotoCorsi(View view) {
        Intent intent = new Intent(this, ListaCorsi.class);
        intent.putExtra("provenienza", "prenotazioni");
        this.startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();

        super.onBackPressed();
    }
}
