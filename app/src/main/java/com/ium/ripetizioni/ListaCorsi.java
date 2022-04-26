package com.ium.ripetizioni;

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
import com.ium.ripetizioni.dto.Corso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListaCorsi extends AppCompatActivity {

    Map<String, Integer> listviewTitle = new HashMap<>();
    //List<String> listviewShortDescription = new ArrayList<>();
    private String provenienza;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listaimg);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            provenienza = extras.getString("provenienza");
        }

        caricamentoLista();

    }

    private void caricamentoLista() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://10.0.2.2:8080/IUM_TWEB_Project_war_exploded/mainServlet?action=requestallcorsiandroid";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //textView.setText("Response is: " + response);
                        Gson gson = new Gson();
                        Type listType = new TypeToken<ArrayList<Corso>>() {
                        }.getType();
                        ArrayList<Corso> corsi = gson.fromJson(response, listType);
                        //Toast.makeText(getApplicationContext(), "corsi: " + corsi, Toast.LENGTH_LONG).show();
                        for (int i = 0; i < corsi.size(); i++) {
                            listviewTitle.put(corsi.get(i).getTitolo(), corsi.get(i).getID());
                        }

                        List<HashMap<String, String>> adapterList = new ArrayList<>();

                        for (String title : listviewTitle.keySet()) {
                            HashMap<String, String> hm = new HashMap<String, String>();
                            hm.put("listview_title", title);
                            hm.put("listview_id", "IDCorso: " + listviewTitle.get(title).toString());
                            hm.put("listview_image", Integer.toString(getResources()
                                    .getIdentifier("arrow", "drawable", "com.ium.ripetizioni")));
                            adapterList.add(hm);
                        }

                        String[] from = {"listview_image", "listview_title", "listview_id"};
                        int[] to = {R.id.listview_image, R.id.listview_item_title, R.id.listview_item_short_description};

                        SimpleAdapter simpleAdapter = new SimpleAdapter(getBaseContext(), adapterList, R.layout.activity_lista_corsi, from, to);
                        ListView androidListView = findViewById(R.id.list_view);
                        androidListView.setAdapter(simpleAdapter);
                        androidListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adattatore, final View componente, int pos, long id) {
                                HashMap<String, String> item = (HashMap<String, String>) adattatore.getItemAtPosition(pos);
                                String nomeCorso = item.get("listview_title");
                                String[] myArray = item.get("listview_id").split("IDCorso: ");
                                int id_corso = Integer.parseInt(myArray[1]);
                                Intent intent = new Intent(getApplicationContext(), ListaRipetizioni.class);
                                intent.putExtra("nome_corso", nomeCorso);
                                intent.putExtra("id_corso", id_corso);
                                startActivity(intent);
                            }
                        });

                        Toolbar toolbar = findViewById(R.id.toolbar);
                        setSupportActionBar(toolbar);
                        getSupportActionBar().setTitle(null);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", "Error: " + error);
                Toast.makeText(getApplicationContext(), "Errore nella connessione!", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
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
            intent.putExtra("provenienza", "listacorsi");
            this.startActivity(intent);
            finish();
        } else {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("Username", "");
            editor.putString("ID", "");
            editor.apply();
            Toast.makeText(getApplicationContext(), "Logout effettuato!", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (provenienza != null && provenienza.equals("homepage")) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        } else if (provenienza != null && provenienza.equals("prenotazioni")) {
            Intent intent = new Intent(getApplicationContext(), ListaPrenotazioni.class);
            startActivity(intent);
        }
        finish();
        super.onBackPressed();
    }
}