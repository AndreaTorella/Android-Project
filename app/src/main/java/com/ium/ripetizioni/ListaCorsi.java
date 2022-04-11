package com.ium.ripetizioni;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ListaCorsi extends AppCompatActivity {

    List<String> listviewTitle = new ArrayList<>();
    List<String> listviewShortDescription = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listaimg);

        caricamentoLista();
        List<HashMap<String, String>> aList = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("listview_title", listviewTitle.get(i));
            hm.put("listview_description", listviewShortDescription.get(i));
            String title = listviewTitle.get(i).toLowerCase(Locale.ROOT).replace(" ", "");
            hm.put("listview_image", Integer.toString(getResources()
                    .getIdentifier(title, "drawable", "com.ium.ripetizioni")));

            aList.add(hm);
        }

        String[] from = {"listview_image", "listview_title", "listview_description"};
        int[] to = {R.id.listview_image, R.id.listview_item_title, R.id.listview_item_short_description};

        SimpleAdapter simpleAdapter = new SimpleAdapter(getBaseContext(), aList, R.layout.activity_lista_corsi, from, to);
        ListView androidListView = (ListView) findViewById(R.id.list_view);
        androidListView.setAdapter(simpleAdapter);
        androidListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adattatore, final View componente, int pos, long id) {
                HashMap<String, String> item = (HashMap<String, String>) adattatore.getItemAtPosition(pos);
                String nomeCorso = item.get("listview_title");
                Intent intent = new Intent(getApplicationContext(), ListaLiberi.class);
                intent.putExtra("nome_corso", nomeCorso);
                intent.putExtra("id_corso", (int) id + 1);
                startActivity(intent);
            }
        });
    }

    private void caricamentoLista() {
        GestioneDB db = new GestioneDB(this);
        db.open();
        Cursor c = db.getCorsi();
        if (c.moveToFirst()) {
            do {
                listviewTitle.add(c.getString(1));
                listviewShortDescription.add(c.getString(2));
            } while (c.moveToNext());
        }

        db.close();
    }


}