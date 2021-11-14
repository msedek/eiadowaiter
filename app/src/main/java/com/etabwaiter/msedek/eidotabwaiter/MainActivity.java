package com.etabwaiter.msedek.eidotabwaiter;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.hardware.display.DisplayManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.etabwaiter.msedek.eidotabwaiter.Adapter.smsAdapter;
import com.etabwaiter.msedek.eidotabwaiter.Adapter.tablesAdapter;
import com.etabwaiter.msedek.eidotabwaiter.Interfaz.endPoints;
import com.etabwaiter.msedek.eidotabwaiter.Models.Empleado;
import com.etabwaiter.msedek.eidotabwaiter.Models.Mensaje;
import com.etabwaiter.msedek.eidotabwaiter.Models.Mesa;
import com.etabwaiter.msedek.eidotabwaiter.SQlite.DBHelper;
import com.etabwaiter.msedek.eidotabwaiter.Utils.Constants;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.ListIterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

  ArrayList<Mesa> myTables;
  ArrayList<Mensaje> myMensajes;
  Mesa mesa;

  TextView employName;
  TextView employDni;
  TextView textDate;
  Button logButton;

  DBHelper myDB;

  RecyclerView.LayoutManager recyclerViewLayoutManager;
  RecyclerView recyclerViewTables;
  RecyclerView recyclerViewSms;
  tablesAdapter adpTbl;
  smsAdapter adpSms;

  Empleado empleado;

  boolean screenWasOff;

  LinearLayout recyclerParent;


  private Socket mSocket;
  {
    try {
//      mSocket = IO.socket("http://192.168.1.25:3000/"); //DEMO
//      mSocket = IO.socket("http://192.168.1.25:3000/"); //DEVELOPMENT
      mSocket = IO.socket("http://192.168.1.35:3000/"); //HUAYACHO PROD
    } catch (URISyntaxException ignored) {}
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mSocket.connect();
//    mSocket.on(Constants.GET_MESAS, onGetMesas);
    mSocket.on(Constants.UPDATE_MESA, onUpdateMesa);
    mSocket.on(Constants.CLOSE_SHIFT, onCloseShift);
    mSocket.on(Constants.SEND_SMS, onUpdateSms);

    setContentView(R.layout.activity_main);

    employName = findViewById(R.id.employNameTextView);
    employDni = findViewById(R.id.employDniTextView);
    textDate = findViewById(R.id.dateTextView);
    recyclerViewTables = findViewById(R.id.tablesRecycler);
    recyclerViewSms = findViewById(R.id.messageRecycler);
    recyclerParent = findViewById(R.id.recyclerParent);
    logButton = findViewById(R.id.logButtom);

    myDB = DBHelper.GetDBHelper(this);
    myDB.deleteAllPedido();

    myTables = new ArrayList<>();
    myMensajes = new ArrayList<>();

    empleado = myDB.getEmpleado();

    employName.setText(empleado.getVendor_name());
    employDni.setText(empleado.getCf_dni_cliente());

    Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/Font Awesome 5 Free-Solid-900.otf");
    logButton.setTypeface(tf);
    logButton.setText("\uf2f5");
    logButton.setRotationY(180);
    screenWasOff = false;

    Calendar cal = Calendar.getInstance();
    @SuppressLint("SimpleDateFormat") SimpleDateFormat oSimpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    String MyString = oSimpleDateFormat.format(cal.getTime());
    textDate.setText(MyString);


    for (Mensaje men: empleado.getMensajes()) {
      myDB.addSms(men);
    }
    myMensajes.addAll(empleado.getMensajes());
    setMessageAdapter();
    if( empleado.getMensajes().size()>0) {
      Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
      // Vibrate for 500 milliseconds
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        assert v != null;
        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
      } else {
        //deprecated in API 26
        assert v != null;
        v.vibrate(500);
      }
    }

    logButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.myDialog);
        builder.setMessage("Cerrar Sesion?").setPositiveButton("Si", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
      }
    });

    myDB.deleteAllMesa();
    getMesas();
  }

  @Override
  protected void onStart() {
    super.onStart();
//    mSocket.emit(Constants.SEND_MESA);
//    myDB.deleteAllMesa();
//    getMesas();
  }

  private void getMesas() {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(getString(R.string.iptab))
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    endPoints request = retrofit.create(endPoints.class);
    Call<ArrayList<Mesa>> call = request.getJSONMesas();
    call.enqueue(new Callback<ArrayList<Mesa>>() {
      @Override
      public void onResponse(@NonNull Call<ArrayList<Mesa>> call, @NonNull Response<ArrayList<Mesa>> response) {
        if(response.code()==200) {
          myTables = response.body();
//          myDB.deleteAllMesa();
          for (int i = 0; i < myTables.size(); i++) {
            myDB.addMesa(myTables.get(i));
          }
          setTableAdapter();
        }
      }
      @Override
      public void onFailure(@NonNull Call<ArrayList<Mesa>> call, @NonNull Throwable t) {
        Log.i("Error from load Mesa: " , t.getMessage());
      }
    });
  }

  DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialog, int which) {
      switch (which){
        case DialogInterface.BUTTON_POSITIVE:
          Retrofit retrofit = new Retrofit.Builder()
                  .baseUrl(getString(R.string.iptab))
                  .addConverterFactory(GsonConverterFactory.create())
                  .build();
          endPoints request = retrofit.create(endPoints.class);
          Call<Empleado> call = request.logOutEmpleado(empleado.get_id());
          call.enqueue(new Callback<Empleado>() {
            @Override
            public void onResponse(@NonNull Call<Empleado> call, @NonNull Response<Empleado> response) {
              if(response.code()==200) {

                mSocket.off(Constants.SEND_MESA);
                mSocket.off(Constants.PARTIAL_LOG);
                mSocket.off(Constants.GET_MESAS);
                mSocket.disconnect();
                mSocket.close();

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", true);
                startActivity(intent);
                finish();
              }
            }
            @Override
            public void onFailure(@NonNull Call<Empleado> call, @NonNull Throwable t) {
              Log.i("Error from LogEmp: " , t.getMessage());
            }
          });
          break;

        case DialogInterface.BUTTON_NEGATIVE:
          //No button clicked
          break;
      }
    }
  };

  private void setTableAdapter() {
    adpTbl = new tablesAdapter(this, myTables);
    recyclerViewLayoutManager = new GridLayoutManager(getApplicationContext(), 4);
    recyclerViewTables.setLayoutManager(recyclerViewLayoutManager);
    recyclerViewTables.addItemDecoration(new SpacesItemDecoration(14));
    recyclerViewTables.setAdapter(adpTbl);
  }

  private void setMessageAdapter() {
    adpSms = new smsAdapter(this, myMensajes);
    final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
    recyclerViewSms.setLayoutManager(linearLayoutManager);
    recyclerViewSms.setAdapter(adpSms);
  }

  private class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int halfSpace;
    SpacesItemDecoration(int space) {
      this.halfSpace = space / 2;
    }
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

      if (parent.getPaddingLeft() != halfSpace) {
        parent.setPadding(halfSpace, halfSpace, halfSpace, halfSpace);
        parent.setClipToPadding(false);
      }

      outRect.top = halfSpace;
      outRect.bottom = halfSpace;
      outRect.left = halfSpace;
      outRect.right = halfSpace;
    }
  }

  @Override
  protected void onResume() {
    super.onResume();

    KeyguardManager myKM = (KeyguardManager) getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE);
    if( myKM.inKeyguardRestrictedInputMode()) {
      //it is locked
      screenWasOff = true;
    }

    if(screenWasOff) {
//      showMessage("kk refresh");
      screenWasOff = false;
//      showMessage(screenWasOff + "");
      myDB.deleteAllMesa();
      getMesas();
    }

    Empleado empleado = myDB.getEmpleado();
    if(empleado.getFondoId().equals("")) {

      finish();

//      if(myDB.listSms().size() == 0) {
//        for (Mensaje men: empleado.getMensajes()) {
//          myDB.addSms(men);
//        }
//        myMensajes.addAll(empleado.getMensajes());
//        setMessageAdapter();
//        if( empleado.getMensajes().size()>0) {
//          Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
//          // Vibrate for 500 milliseconds
//          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            assert v != null;
//            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
//          } else {
//            //deprecated in API 26
//            assert v != null;
//            v.vibrate(500);
//          }
//        }
//      }

//      ArrayList<Mesa> test = myDB.listMesas();
//
//      if(test.size() == 0) {
//        mSocket.emit(Constants.SEND_MESA);
//      }

    }
  }

  @Override
  public void onBackPressed() {}

//  private Emitter.Listener onGetMesas= new Emitter.Listener() {
//    @Override
//    public void call(final Object... args) {
//      MainActivity.this.runOnUiThread(new Runnable() {
//        @Override
//        public void run() {
//          JSONArray mesas;
//          myDB.deleteAllMesa();
//          try {
//            String data = args[0].toString();
//            mesas = new JSONArray(data);
//            for (int i = 0; i < mesas.length();i++) {
//              JSONObject myMesa = mesas.getJSONObject(i);
//              JsonParser parser = new JsonParser();
//              JsonElement mJson =  parser.parse(String.valueOf(myMesa));
//              Gson gson = new Gson();
//              Mesa mesa = gson.fromJson(mJson, Mesa.class);
//              myTables.add(mesa);
//              myDB.addMesa(mesa);
//            }
//
//
//            Collections.sort(myTables, new Comparator<Mesa>() {
//              public int compare(Mesa o1, Mesa o2) {
//                return o1.getNumeroMesa().compareTo(o2.getNumeroMesa());
//              }
//            });
//
//            setTableAdapter();
//          } catch (JSONException e) {
//            e.printStackTrace();
//          }
//        }
//      });
//    }
//  };

  private Emitter.Listener onUpdateMesa= new Emitter.Listener() {
    @Override
    public void call(final Object... args) {
      MainActivity.this.runOnUiThread(new Runnable() {
        @Override
        public void run() {
          JSONObject myMesa;
          try {
            String data = args[0].toString();
            myMesa = new JSONObject(data);
            JsonParser parser = new JsonParser();
            JsonElement mJson =  parser.parse(String.valueOf(myMesa));
            Gson gson = new Gson();
            Mesa mesa = gson.fromJson(mJson, Mesa.class);
            ListIterator<Mesa> iterator = adpTbl.myTables.listIterator();
            int itera = -1;
            while (iterator.hasNext()) {
              Mesa next = iterator.next();
              itera = itera+ 1;
              if (next.getNumeroMesa().equals(mesa.getNumeroMesa())) {
                //Replace element
                iterator.set(mesa);
                myTables.set(itera, mesa);
              }
            }
            adpTbl.notifyDataSetChanged();
            myDB.updateMesa(mesa);
          } catch (JSONException e) {
            e.printStackTrace();
          }
        }
      });
    }
  };

  private Emitter.Listener onCloseShift= new Emitter.Listener() {
    @Override
    public void call(final Object... args) {
      MainActivity.this.runOnUiThread(new Runnable() {
        @Override
        public void run() {

          Empleado emp = myDB.getEmpleado();

          mSocket.emit(Constants.PARTIAL_LOG, emp.get_id());
          mSocket.off(Constants.SEND_MESA);
          mSocket.off(Constants.PARTIAL_LOG);
          mSocket.off(Constants.GET_MESAS);
          mSocket.off(Constants.PRE_CUENTA);
          mSocket.disconnect();
          mSocket.close();

          Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
          intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
          intent.putExtra("EXIT", true);
          startActivity(intent);
          finish();
        }
      });
    }
  };

  /**
   * Is the screen of the device on.
   * @param context the context
   * @return true when (at least one) screen is on
   */
  public boolean isScreenOn(Context context) {
    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
      DisplayManager dm = (DisplayManager) context.getSystemService(Context.DISPLAY_SERVICE);
      boolean screenOn = false;
      for (Display display : dm.getDisplays()) {
        if (display.getState() != Display.STATE_OFF) {
          screenOn = true;
        } else {

        }
      }
      return screenOn;
    } else {
      PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
      //noinspection deprecation
      return pm.isScreenOn();
    }
  }

  private Emitter.Listener onUpdateSms= new Emitter.Listener() {
    @Override
    public void call(final Object... args) {
      MainActivity.this.runOnUiThread(new Runnable() {
        @Override
        public void run() {
          JSONObject myEmpleado;
          try {
            String data = args[0].toString();
            myEmpleado = new JSONObject(data);
            JsonParser parser = new JsonParser();
            JsonElement mJson =  parser.parse(String.valueOf(myEmpleado));
            Gson gson = new Gson();
            Empleado empleado = gson.fromJson(mJson, Empleado.class);

            Empleado check = myDB.getEmpleado();

            if(empleado.get_id().equals(check.get_id())) {

//              ArrayList<Mensaje> notPresent= new ArrayList<>(empleado.getMensajes());
//              notPresent.removeAll(adpSms.mensajes);

//              adpSms.mensajes.clear();
              Mensaje mensaje = empleado.getMensajes().get(0);
              myMensajes.add(mensaje);
//              for (int i = 0; i < empleado.getMensajes().size(); i++) {
//                Mensaje mensaje = empleado.getMensajes().get(i);
//              }
//              adpSms.mensajes.addAll(empleado.getMensajes());
              adpSms.notifyItemInserted(adpSms.mensajes.size());
//              adpSms.notifyDataSetChanged();
//              check.getMensajes().addAll(notPresent);
//              myDB.updateEmpleado(check);

              Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
              // Vibrate for 500 milliseconds
              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                assert v != null;
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
              } else {
                //deprecated in API 26
                assert v != null;
                v.vibrate(500);
              }

            }
          } catch (JSONException e) {
            e.printStackTrace();
          }
        }
      });
    }
  };

  private void showMessage(String mensaje) {
    final Toast toast = Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT);
    toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
    toast.show();

    Handler handler = new Handler();
    handler.postDelayed(new Runnable() {
      @Override
      public void run() {
        toast.cancel();
      }
    }, 800);
  }

  @Override
  protected void onPause() {
    super.onPause();


  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mSocket.disconnect();
  }
}
