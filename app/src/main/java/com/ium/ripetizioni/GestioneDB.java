package com.ium.ripetizioni;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class GestioneDB {

    static final String KEY_RIGAID = "id";
    static final String KEY_NOME = "nome";
    static final String KEY_DESCRIZIONE = "descrizione";
    static final String KEY_COGNOME = "cognome";
    static final String KEY_EMAIL = "email";
    static final String KEY_PASSWORD = "password";
    static final String TABELLA_UTENTE = "utente";
    static final String TABELLA_LIBERE = "libere";
    static final String TABELLA_DOCENZA = "docenza";
    static final String TABELLA_CORSO = "corso";
    static final String DATABASE_NOME = "TestDB";
    static final int DATABASE_VERSIONE = 1;

    static final String CREATE_UTENTE = "CREATE TABLE utente " +
            "(id integer primary key autoincrement, " +
            "nome text not null, " +
            "cognome text not null, " +
            "email text not null unique, " +
            "password text not null);";
    static final String CREATE_CORSO = "CREATE TABLE corso " +
            "(id integer primary key autoincrement, " +
            "nome text not null, " +
            "descrizione text not null);";
    static final String CREATE_LIBERE = "CREATE TABLE libere " +
            "(id integer primary key autoincrement, " +
            "id_docenza integer not null, " +
            "giorno text not null," +
            "ora text not null);";
    static final String CREATE_DOCENZA = "CREATE TABLE docenza " +
            "(id integer primary key autoincrement, " +
            "id_corso integer not null, " +
            "nome_prof text not null);";
    static final String CREATE_PRENOTATE = "CREATE TABLE prenotate " +
            "(id_utente integer, " +
            "id_prenotazione integer, " +
            "stato text," +
            "PRIMARY KEY (id_utente, id_prenotazione));";

    static final String INSERT_UTENTE = "INSERT INTO utente (nome, cognome, email, password) VALUES " +
            "(\"agostino\", \"messsina\", \"agostino@mail.com\", \"agostino\");";
    static final String INSERT_LIBERE = "INSERT INTO libere (id_docenza, giorno, ora) VALUES" +
            "(1, 1, 1)," +
            "(2, 2, 2)," +
            "(3, 3, 3)," +
            "(1, 2, 1);";
    static final String INSERT_DOCENZA = "INSERT INTO docenza (id_corso, nome_prof) VALUES " +
            "('1', \"Baroglio\"), " +
            "('2', \"Bondo\"), " +
            "('3', \"Pozzato\"), " +
            "('3', \"Schifanella\"), " +
            "('4', \"Botta\"), " +
            "('6', \"Pensa\"), " +
            "('7', \"Varllo\"), " +
            "('5', \"Adlaii\");";
    static final String INSERT_PRENOTATE = "INSERT INTO prenotate (id_utente, id_prenotazione) VALUES " +
            "(\"1\", \"1\")," +
            "(\"1\", \"2\")," +
            "(\"1\", \"3\");";
    static final String INSERT_CORSO = "INSERT INTO corso (nome, descrizione) VALUES" +
            "(\"Sistemi Operativi\", \"L’insegnamento fornisce una conoscenza di base dell'architettura interna e del funzionamento dei moderni sistemi operativi.\"), " +
            "(\"Sicurezza\", \"Il corso si propone di fornire agli studenti gli strumenti crittografici e tecnici utilizzati per garantire la sicurezza di reti e calcolatori.\"), " +
            "(\"Algoritmi\", \"L’insegnamento ha lo scopo di introdurre i concetti e le tecniche fondamentali per l’analisi e la progettazione di algoritmi.\"), " +
            "(\"Tecnologie Web\", \"Obiettivi: Imparare diversi linguaggi e tecnologie per lo sviluppo Web client-side, quali HTML5, CSS, JavaScript, JQuery.\"), " +
            "(\"Ricerca Operativa\", \"Il corso si propone di fornire agli studenti nozioni generali di calcolo matriciale, algebra e geometria.\"), " +
            "(\"Database\", \"L'insegnamento è un'introduzione alle basi di dati e ai sistemi di gestione delle medesime (SGBD).\"), " +
            "(\"Analisi\", \"L'insegnamento ha lo scopo di presentare le nozioni di base su funzioni, grafici e loro trasformazioni.\");";

    final Context context;
    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public GestioneDB(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NOME, null, DATABASE_VERSIONE);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_DOCENZA);
                db.execSQL(CREATE_UTENTE);
                db.execSQL(CREATE_CORSO);
                db.execSQL(CREATE_LIBERE);
                db.execSQL(CREATE_PRENOTATE);

                //Inserting sample data
                db.execSQL(INSERT_UTENTE);
                db.execSQL(INSERT_DOCENZA);
                db.execSQL(INSERT_LIBERE);
                //db.execSQL(INSERT_PRENOTATE);
                db.execSQL(INSERT_CORSO);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(DatabaseHelper.class.getName(), "Aggiornamento database dalla versione " + oldVersion + " alla "
                    + newVersion + ". I dati esistenti verranno eliminati.");
            onCreate(db);
        }
    }


    public GestioneDB open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        DBHelper.close();
    }

    public long inserisciUtente(String nome, String cognome, String email, String password) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NOME, nome);
        initialValues.put(KEY_COGNOME, cognome);
        initialValues.put(KEY_EMAIL, email);
        initialValues.put(KEY_PASSWORD, password);
        return db.insert(TABELLA_UTENTE, null, initialValues);
    }

    public Cursor getUtenti() {
        return db.query(TABELLA_UTENTE, new String[]{KEY_RIGAID, KEY_NOME, KEY_COGNOME, KEY_EMAIL, KEY_PASSWORD}, null, null, null, null, null);
    }

    public Cursor getUtente(String email) {
        String sql = "SELECT id FROM utente WHERE email='" + email + "'";
        Cursor c = db.rawQuery(sql, null);
        c.moveToNext();
        return c;
    }


  /*public Cursor getUtente(String email) throws SQLException
  {
    Cursor mCursore = db.query(true, TABELLA_UTENTE, new String[] {KEY_RIGAID, KEY_NOME, KEY_COGNOME, KEY_EMAIL, KEY_PASSWORD}, KEY_EMAIL + "= 'email'", null, null, null, null, null);
    if (mCursore != null) {
      mCursore.moveToFirst();
    }
    return mCursore;
  }*/

    public long inserisciCorso(String nomeCorso, String descrizione) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NOME, nomeCorso);
        initialValues.put(KEY_DESCRIZIONE, descrizione);
        return db.insert(TABELLA_CORSO, null, initialValues);
    }

    public long inserisciDocenza(String nomeCorso, String nomeProf) {
        ContentValues initialValues = new ContentValues();
        initialValues.put("id_corso", nomeCorso);
        initialValues.put("nome_prof", nomeProf);
        return db.insert(TABELLA_DOCENZA, null, initialValues);
    }

    public Cursor getDocenze() {
        return db.query("docenza", new String[]{KEY_RIGAID, "id_corso", "nome_prof"}, null, null, null, null, null);
    }

    public long inserisciLibere(int idDocenza, String giorno, String ora) {
        ContentValues initialValues = new ContentValues();
        initialValues.put("id_docenza", idDocenza);
        initialValues.put("giorno", giorno);
        initialValues.put("ora", ora);
        return db.insert(TABELLA_LIBERE, null, initialValues);
    }

    public Cursor getLiberi(int idCorso) {
        String sql = "SELECT docenza.nome_prof, libere.giorno, libere.ora, libere.id " +
                "FROM docenza INNER JOIN libere ON docenza.id = libere.id_docenza " +
                "WHERE docenza.id_corso ='" + idCorso + "' " +
                "AND libere.id NOT IN ( " +
                "SELECT id_prenotazione " +
                "FROM prenotate) " +
                "ORDER BY giorno";
        return db.rawQuery(sql, null);
    }

    public Cursor getLiberiLoggato(int idCorso, String idUtente) {
        String sql = "SELECT docenza.nome_prof, l1.giorno, l1.ora, l1.id " +
                "FROM docenza INNER JOIN libere l1 ON docenza.id = l1.id_docenza " +
                "WHERE docenza.id_corso LIKE '" + idCorso + "' " +
                "AND l1.id NOT IN ( " +
                "SELECT id_prenotazione " +
                "FROM prenotate) " +
                "AND NOT EXISTS (SELECT 1 " +
                "FROM (SELECT l2.giorno, l2.ora " +
                "FROM libere l2 INNER JOIN prenotate ON l2.id = prenotate.id_prenotazione " +
                "WHERE prenotate.id_utente='" + idUtente + "') AS l2 " +
                "WHERE l1.giorno=l2.giorno " +
                "AND l1.ora=l2.ora)" +
                "ORDER BY giorno";
        return db.rawQuery(sql, null);
    }

    public Cursor getTutteLibere() {
        String sql = "SELECT * " +
                "FROM libere";
        return db.rawQuery(sql, null);
    }

    public Cursor getPrenotati(String id_utente) {
        /*String sql = "SELECT docenza.nome_prof, libere.giorno, libere.ora, prenotate.stato, libere.id " +
                "FROM docenza INNER JOIN libere ON docenza.id = libere.id_docenza " +
                "WHERE libere.id IN ( " +
                "SELECT id_prenotazione " +
                "FROM prenotate " +
                "WHERE id_utente ='" + id_utente + "' )" +
                "ORDER BY giorno";*/
        String sql = "SELECT docenza.nome_prof, libere.giorno, libere.ora, prenotate.stato, libere.id " +
                "FROM docenza JOIN libere ON docenza.id = libere.id_docenza JOIN prenotate ON id_utente ='\" + id_utente + \"'";
        return db.rawQuery(sql, null);
    }

    public void effettuaPrenotazione(String email, String id_pren) {
        ContentValues initialValues = new ContentValues();
        initialValues.put("id_utente", email);
        initialValues.put("id_prenotazione", id_pren);
        db.insert("prenotate", null, initialValues);
    }

    public Cursor getCorsi() {
        return db.query(TABELLA_CORSO, new String[]{KEY_RIGAID, KEY_NOME, KEY_DESCRIZIONE}, null, null, null, null, null);
    }

    public Cursor getNCorsi() {
        String sql = "SELECT COUNT(*) FROM corso ";
        return db.rawQuery(sql, null);
    }

    public void cancellaPrenotazione(String id) {
        String sql = "UPDATE prenotate SET stato = 'D' WHERE id_prenotazione=" + id;
        db.execSQL(sql);
    }

    public void cancella() {
        db.delete("libere", null, null);
    }

    public void crea() {
        db.execSQL("DROP TABLE libere");
        db.execSQL(CREATE_LIBERE);
    }

}