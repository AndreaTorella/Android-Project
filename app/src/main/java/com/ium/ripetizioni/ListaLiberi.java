package com.ium.ripetizioni;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListaLiberi extends AppCompatActivity {

    List<String> listviewTitle =  new ArrayList<>();
    List<String> id_pren =  new ArrayList<>();
    List<Integer> listviewImage =  new ArrayList<>();
    List<String> listviewShortDescription =  new ArrayList<>();

    int contaElem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        final String nomeCorso = intent.getExtras().getString("nome_corso");
        final int idCorso = intent.getExtras().getInt("id_corso");
        setContentView(R.layout.activity_listaimg);
        caricamentoLista(idCorso);
        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < contaElem; i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("listview_title", listviewTitle.get(i));
            hm.put("listview_discription", listviewShortDescription.get(i));
            //hm.put("listview_image", Integer.toString(listviewImage.get(i)));
            hm.put("id_pren", id_pren.get(i));
            aList.add(hm);
        }

        String[] from = {"listview_image", "listview_title", "listview_discription"};
        int[] to = {R.id.listview_image, R.id.listview_item_title, R.id.listview_item_short_description};

        SimpleAdapter simpleAdapter = new SimpleAdapter(getBaseContext(), aList, R.layout.activity_lista_liberi, from, to);
        ListView androidListView = (ListView) findViewById(R.id.list_view);
        androidListView.setAdapter(simpleAdapter);
        androidListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adattatore, final View componente, int pos, long id) {
                final HashMap<String, String> item = (HashMap<String, String>) adattatore.getItemAtPosition(pos);
                final String value = item.get("listview_title");
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                final String name = preferences.getString("ID", "");
                if (name.equalsIgnoreCase("")) {
                    new AlertDialog.Builder(ListaLiberi.this)
                            .setTitle("Prenotazione")
                            .setMessage("Devi prima effettuare il login! Procedere ora?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
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
                    new AlertDialog.Builder(ListaLiberi.this)
                            .setTitle("Prenotazione")
                            .setMessage("Vuoi confermare la prenotazione?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    GestioneDB db = new GestioneDB(getApplicationContext());
                                    db.open();
                                    db.effettuaPrenotazione(name, item.get("id_pren"));
                                    db.close();
                                    //Intent intent = new Intent(getApplicationContext(), ListaLiberi.class);
                                    //intent.putExtra("nome_corso", nomeCorso);
                                    //startActivity(intent);
                                    Toast.makeText(getApplicationContext(), "Prenotazione effettuata!", Toast.LENGTH_SHORT).show();
                                    finish();
                                    startActivity(getIntent());
                                }
                            })
                            .setNegativeButton(android.R.string.no, null).show();
                }
            }
        });
    }

    public void caricamentoLista(int idCorso) {
        GestioneDB db = new GestioneDB(this);
        db.open();
        Cursor c;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final String idUtente = preferences.getString("ID", "");
        if (idUtente.equalsIgnoreCase("")) c = db.getLiberi(idCorso);
        else c = db.getLiberiLoggato(idCorso, idUtente);
        contaElem = c.getCount();

        int i = 0;
        if (c.moveToFirst()) {
            do {
                /*String[] words = c.getString(0).split("\\s+");
                if (words[1].equals("Sara") || words[1].equals("Rossella"))
                    listviewTitle[i] = "Professoressa " + c.getString(0);
                else
                    listviewTitle[i] = "Professor " + c.getString(0);
*/
                listviewTitle.add(i, "Docente: " + c.getString(0));
                listviewShortDescription.add(i, "Giorno: " + c.getString((1)) + "      Ora: " + c.getString((2)));
                //listviewImage[i] = getResources().getIdentifier(words[0].toLowerCase(), "drawable", getPackageName());
                id_pren.add(i, c.getString(3));
                i++;
            } while (c.moveToNext());
        } else {
            setEmptyLayout();
            //TODO non ci sono ripetizioni libere
            //Toast.makeText(getApplicationContext(), "Impossibile aprire il corso", Toast.LENGTH_LONG).show();
            //onBackPressed();
        }
        db.close();
    }

    private void setEmptyLayout() {
        RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.emptyLayout);
        View content = getLayoutInflater()
                .inflate(R.layout.content_empty_lista_liberi, mainLayout, false);
        mainLayout.addView(content);

    }


}