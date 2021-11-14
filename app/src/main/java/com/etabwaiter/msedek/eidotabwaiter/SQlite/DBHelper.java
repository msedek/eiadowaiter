package com.etabwaiter.msedek.eidotabwaiter.SQlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.etabwaiter.msedek.eidotabwaiter.Models.Mensaje;
import com.etabwaiter.msedek.eidotabwaiter.Models.Empleado;
import com.etabwaiter.msedek.eidotabwaiter.Models.Mesa;
import com.etabwaiter.msedek.eidotabwaiter.Models.Pedido;
import com.etabwaiter.msedek.eidotabwaiter.Models.Receta;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

  private static final String DATABASE_NAME = "waiteryacho01.db";
  private static final int DATABASE_VERSION = 3;
  private String DB_PATH;

  public static DBHelper GetDBHelper(Context context) {
    DBHelper dbHelper = new DBHelper(context.getApplicationContext());
    if (!dbHelper.isDataBaseExist()) {
      dbHelper.deleteAllReceta();
//      dbHelper.deleteAllComanda();
//      dbHelper.deleteAllCuenta();
      dbHelper.deleteAllSms();
      dbHelper.deleteAllMesa();
//      dbHelper.deleteAllPosition();
      dbHelper.deleteAllPedido();
      dbHelper.deleteAllEmpleado();
      dbHelper.createDataBase();
    }
    return dbHelper;
  }

  @SuppressLint("SdCardPath")
  private DBHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
    DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
  }

  private void createDataBase() {
    boolean isExist = isDataBaseExist();
    if (!isExist) {
      this.getReadableDatabase();
      onCreate(this.getWritableDatabase());
    }
  }

  private boolean isDataBaseExist() {
    File file = new File(DB_PATH + DATABASE_NAME);
    return file.exists();
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    // Escribir la estructura de la bd: Tablas, ...
    db.execSQL(" CREATE TABLE plato  (_id TEXT primary key, jreceta  TEXT); ");
//    db.execSQL(" CREATE TABLE cuenta (_id INTEGER primary key, jcuenta TEXT); ");
    db.execSQL(" CREATE TABLE sms (_id TEXT primary key, jsms TEXT); ");
    db.execSQL(" CREATE TABLE mesa (_id TEXT primary key, jmesa TEXT); ");
//    db.execSQL(" CREATE TABLE comanda (_id TEXT primary key, jcomanda TEXT); ");
//    db.execSQL(" CREATE TABLE position (_id TEXT primary key, jposition TEXT); ");
    db.execSQL(" CREATE TABLE pedido (_id TEXT primary key, jpedido TEXT); ");
    db.execSQL(" CREATE TABLE empleado (_id TEXT primary key, jempleado TEXT); ");
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // Escribir las modificaciones en la bd.
    db.execSQL("DROP TABLE IF EXISTS plato;");
//    db.execSQL("DROP TABLE IF EXISTS cuenta;");
    db.execSQL("DROP TABLE IF EXISTS sms;");
    db.execSQL("DROP TABLE IF EXISTS mesa;");
//    db.execSQL("DROP TABLE IF EXISTS comanda;");
//    db.execSQL("DROP TABLE IF EXISTS position;");
    db.execSQL("DROP TABLE IF EXISTS pedido;");
    db.execSQL("DROP TABLE IF EXISTS empleado;");
    onCreate(db);
  }

  /* IMPLEMENTACIÓN: MÉTODOS CRUD */
  private static final String TABLE_NAME_RECETA = "plato";
  //  private static final String TABLE_NAME_CUENTA = "cuenta";
  private static final String TABLE_NAME_SMS = "sms";
  private static final String TABLE_NAME_MESA = "mesa";
  //  private static final String TABLE_NAME_COMANDA = "comanda";
//  private static final String TABLE_NAME_POSITION = "position";
  private static final String TABLE_NAME_PEDIDO = "pedido";
  private static final String TABLE_NAME_EMPLEADO = "empleado";

  public boolean addReceta(Receta plato) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    Gson gson = new Gson();
    String json = gson.toJson(plato);
    contentValues.put("_id", plato.get_id());
    contentValues.put("jreceta", json);
    db.insert(TABLE_NAME_RECETA, null, contentValues);
    return true;
  }

//  public boolean addPosition(String position) {
//    SQLiteDatabase db = this.getWritableDatabase();
//    ContentValues contentValues = new ContentValues();
//    Gson gson = new Gson();
//    String json = gson.toJson(position);
//    contentValues.put("_id", "0");
//    contentValues.put("jposition", json);
//    db.insert(TABLE_NAME_POSITION, null, contentValues);
//    return true;
//  }

  public boolean addPedido(Pedido pedido, String position) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    Gson gson = new Gson();
    String json = gson.toJson(pedido);
    contentValues.put("_id", position);
    contentValues.put("jpedido", json);
    db.insert(TABLE_NAME_PEDIDO, null, contentValues);
    return true;
  }

  public boolean addEmpleado(Empleado empleado) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    Gson gson = new Gson();
    String json = gson.toJson(empleado);
    contentValues.put("_id", empleado.get_id());
    contentValues.put("jempleado", json);
    db.insert(TABLE_NAME_EMPLEADO, null, contentValues);
    return true;
  }

//  public boolean addComanda(Comanda comanda, String pos) {
//    SQLiteDatabase db = this.getWritableDatabase();
//    ContentValues contentValues = new ContentValues();
//    Gson gson = new Gson();
//    String json = gson.toJson(comanda);
//    contentValues.put("_id", pos);
//    contentValues.put("jcomanda", json);
//    db.insert(TABLE_NAME_COMANDA, null, contentValues);
//    return true;
//  }

  public boolean addSms(Mensaje sms) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    Gson gson = new Gson();
    String json = gson.toJson(sms);
    contentValues.put("_id", sms.get_id());
    contentValues.put("jsms", json);
    db.insert(TABLE_NAME_SMS, null, contentValues);
    return true;
  }

  public boolean addMesa(Mesa mesa) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    Gson gson = new Gson();
    String json = gson.toJson(mesa);
    contentValues.put("_id", mesa.get_id());
    contentValues.put("jmesa", json);
    db.insert(TABLE_NAME_MESA, null, contentValues);
    return true;
  }

  public boolean updateMesa(Mesa mesa) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    Gson gson = new Gson();
    String json = gson.toJson(mesa);
    contentValues.put("jmesa", json);
    db.update(TABLE_NAME_MESA, contentValues, "_id=?", new String[]{mesa.get_id()});
    return true;
  }

  public boolean updateEmpleado(Empleado empleado) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    Gson gson = new Gson();
    String json = gson.toJson(empleado);
    contentValues.put("jempleado", json);
    db.update(TABLE_NAME_EMPLEADO, contentValues, "_id=?", new String[]{empleado.get_id()});
    return true;
  }

  public boolean deleteSms(String id) {
    SQLiteDatabase db = this.getWritableDatabase();
    db.delete(TABLE_NAME_SMS, "_id = ?",
            new String[]{ id });
    return true;
  }

  public boolean deletePedido(String id) {
    Log.i("delete DBHELPER", id);
    SQLiteDatabase db = this.getWritableDatabase();
    db.delete(TABLE_NAME_PEDIDO, "_id = ?",
            new String[]{ id });
    return true;
  }

//  public boolean deleteComanda(String id) {
//    SQLiteDatabase db = this.getWritableDatabase();
//    db.delete(TABLE_NAME_COMANDA, "_id = ?",
//            new String[]{ id });
//    return true;
//  }

//  public boolean addCuenta(Cuentadata cuentadata) {
//    SQLiteDatabase db = this.getWritableDatabase();
//    ContentValues contentValues = new ContentValues();
//    Gson gson = new Gson();
//    String json = gson.toJson(cuentadata);
//    contentValues.put("jcuenta", json);
//    db.insert(TABLE_NAME_CUENTA, null, contentValues);
//    return true;
//  }

  public boolean deleteAllReceta() {
    SQLiteDatabase db = this.getWritableDatabase();
    db.delete(TABLE_NAME_RECETA, "",
            new String[]{  });
    return true;
  }

  public boolean deleteAllMesa() {
    SQLiteDatabase db = this.getWritableDatabase();
    db.delete(TABLE_NAME_MESA, "",
            new String[]{  });
    return true;
  }

//  @SuppressWarnings("WeakerAccess")
//  public boolean deleteAllCuenta() {
//    SQLiteDatabase db = this.getWritableDatabase();
//    db.delete(TABLE_NAME_CUENTA, "",
//            new String[]{ });
//    return true;
//  }

  @SuppressWarnings("WeakerAccess")
  public boolean deleteAllSms() {
    SQLiteDatabase db = this.getWritableDatabase();
    db.delete(TABLE_NAME_SMS, "",
            new String[]{  });
    return true;
  }

//  @SuppressWarnings("WeakerAccess")
//  public boolean deleteAllComanda() {
//    SQLiteDatabase db = this.getWritableDatabase();
//    db.delete(TABLE_NAME_COMANDA, "",
//            new String[]{  });
//    return true;
//  }

//  @SuppressWarnings("WeakerAccess")
//  public boolean deleteAllPosition() {
//    SQLiteDatabase db = this.getWritableDatabase();
//    db.delete(TABLE_NAME_POSITION, "",
//            new String[]{  });
//    return true;
//  }

  @SuppressWarnings("WeakerAccess")
  public boolean deleteAllPedido() {
    SQLiteDatabase db = this.getWritableDatabase();
    db.delete(TABLE_NAME_PEDIDO, "",
            new String[]{  });
    return true;
  }

  @SuppressWarnings("WeakerAccess")
  public boolean deleteAllEmpleado() {
    SQLiteDatabase db = this.getWritableDatabase();
    db.delete(TABLE_NAME_EMPLEADO, "",
            new String[]{  });
    return true;
  }

  public ArrayList<Receta> listRecetas() {
    ArrayList<Receta> lista = new ArrayList<>();
    Receta receta;
    Gson gson = new Gson();
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.rawQuery(" SELECT * FROM " + TABLE_NAME_RECETA, null);
    cursor.moveToFirst();
    try {
      while (!cursor.isAfterLast()) {
        String sacadata = (cursor.getString(cursor.getColumnIndex("jreceta")));
        receta  = gson.fromJson(sacadata, Receta.class);
        lista.add(receta);
        cursor.moveToNext();
      }
    } finally {
      cursor.close();
      db.close();
    }
    return lista;
  }

  public ArrayList<Mesa> listMesas() {
    ArrayList<Mesa> lista = new ArrayList<>();
    Mesa mesa;
    Gson gson = new Gson();
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.rawQuery(" SELECT * FROM " + TABLE_NAME_MESA, null);
    cursor.moveToFirst();
    try {
      while (!cursor.isAfterLast()) {
        String sacadata = (cursor.getString(cursor.getColumnIndex("jmesa")));
        mesa  = gson.fromJson(sacadata, Mesa.class);
        lista.add(mesa);
        cursor.moveToNext();
      }
    } finally {
      cursor.close();
      db.close();
    }
    return lista;
  }

  public ArrayList<Mensaje> listSms() {
    ArrayList<Mensaje> lista = new ArrayList<>();
    Mensaje sms;
    Gson gson = new Gson();
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.rawQuery(" SELECT * FROM " + TABLE_NAME_SMS, null);
    cursor.moveToFirst();
    try {
      while (!cursor.isAfterLast()) {
        String sacadata = (cursor.getString(cursor.getColumnIndex("jsms")));
        sms  = gson.fromJson(sacadata, Mensaje.class);
        lista.add(sms);
        cursor.moveToNext();
      }
    } finally {
      cursor.close();
      db.close();
    }
    return lista;
  }

//  public ArrayList<Cuentadata> listCuenta() {
//    ArrayList<Cuentadata> lista = new ArrayList<>();
//    Cuentadata cuentadata;
//    Gson gson = new Gson();
//    SQLiteDatabase db = this.getReadableDatabase();
//    Cursor cursor = db.rawQuery(" SELECT * FROM " + TABLE_NAME_CUENTA, null);
//    cursor.moveToFirst();
//    try {
//      while (!cursor.isAfterLast()) {
//        String sacadata = (cursor.getString(cursor.getColumnIndex("jcuenta")));
//        cuentadata  = gson.fromJson(sacadata, Cuentadata.class);
//        lista.add(cuentadata);
//        cursor.moveToNext();
//      }
//    } finally {
//      cursor.close();
//      db.close();
//    }
//    return lista;
//  }

//  public ArrayList<Comanda> listComandas() {
//    ArrayList<Comanda> comandas = new ArrayList<>();
//    Comanda comanda;
//    Gson gson = new Gson();
//    SQLiteDatabase db = this.getReadableDatabase();
//    Cursor cursor = db.rawQuery(" SELECT * FROM " + TABLE_NAME_COMANDA, null);
//    cursor.moveToFirst();
//    try {
//      while (!cursor.isAfterLast()) {
//        String sacadata = (cursor.getString(cursor.getColumnIndex("jcomanda")));
//        comanda  = gson.fromJson(sacadata, Comanda.class);
//        comandas.add(comanda);
//        cursor.moveToNext();
//      }
//    } finally {
//      cursor.close();
//      db.close();
//    }
//    return comandas;
//  }

//  public ArrayList<String> listNumeroMesa() {
//    ArrayList<String> nMesas = new ArrayList<>();
//    String nMesa;
//    Gson gson = new Gson();
//    SQLiteDatabase db = this.getReadableDatabase();
//    Cursor cursor = db.rawQuery(" SELECT * FROM " + TABLE_NAME_POSITION, null);
//    cursor.moveToFirst();
//    try {
//      while (!cursor.isAfterLast()) {
//        String sacadata = (cursor.getString(cursor.getColumnIndex("jposition")));
//        nMesa  = gson.fromJson(sacadata, String.class);
//        nMesas.add(nMesa);
//        cursor.moveToNext();
//      }
//    } finally {
//      cursor.close();
//      db.close();
//    }
//    return nMesas;
//  }

  public ArrayList<Pedido> listPedidos() {
    ArrayList<Pedido> Pedidos = new ArrayList<>();
    Pedido pedido;
    Gson gson = new Gson();
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.rawQuery(" SELECT * FROM " + TABLE_NAME_PEDIDO, null);
    cursor.moveToFirst();
    try {
      while (!cursor.isAfterLast()) {
        String sacadata = (cursor.getString(cursor.getColumnIndex("jpedido")));
        pedido  = gson.fromJson(sacadata, Pedido.class);
        Pedidos.add(pedido);
        cursor.moveToNext();
      }
    } finally {
      cursor.close();
      db.close();
    }
    return Pedidos;
  }

  public Empleado getEmpleado() {
    Empleado empleado = new Empleado();
    Gson gson = new Gson();
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.rawQuery(" SELECT * FROM " + TABLE_NAME_EMPLEADO, null);
    cursor.moveToFirst();
    try {
      while (!cursor.isAfterLast()) {
        String sacadata = (cursor.getString(cursor.getColumnIndex("jempleado")));
        empleado  = gson.fromJson(sacadata, Empleado.class);
        cursor.moveToNext();
      }
    } finally {
      cursor.close();
      db.close();
    }
    return empleado;
  }
}