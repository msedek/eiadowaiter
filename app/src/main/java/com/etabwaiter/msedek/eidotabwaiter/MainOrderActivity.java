package com.etabwaiter.msedek.eidotabwaiter;

import android.content.Intent;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.etabwaiter.msedek.eidotabwaiter.Adapter.itemsAdapter;
import com.etabwaiter.msedek.eidotabwaiter.Interfaz.endPoints;
import com.etabwaiter.msedek.eidotabwaiter.Models.Comanda;
import com.etabwaiter.msedek.eidotabwaiter.Models.Empleado;
import com.etabwaiter.msedek.eidotabwaiter.Models.Marchar;
import com.etabwaiter.msedek.eidotabwaiter.Models.Mesa;
import com.etabwaiter.msedek.eidotabwaiter.Models.Pedido;
import com.etabwaiter.msedek.eidotabwaiter.Models.Receta;
import com.etabwaiter.msedek.eidotabwaiter.SQlite.DBHelper;
import com.etabwaiter.msedek.eidotabwaiter.Utils.Constants;
import com.github.nkzawa.socketio.client.IO;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainOrderActivity extends AppCompatActivity {

  com.github.nkzawa.socketio.client.Socket mSocket;

  private ArrayList<Receta> myRecetas;
  //  private ArrayList<Mensaje> myMessages;
  private Mesa mesa;

  LinearLayout overlay;
  ArrayList<String> orders;
  ArrayList<String> ordersNotSent;

  Button btnOrder;
  Button btnCheck;
  Button btnMarchar;

  DBHelper myDB;

  //  RecyclerView recyclerViewSms;
  RecyclerView recyclerArticles;
  //  smsAdapter adpSmsOrder;
  itemsAdapter adpPlatos;

  TextView txtArrow;
  TextView textCategory;
  TextView numMesa;

  ArrayList<Receta> search;

  EditText pax;
  boolean showPax;

  EditText searchBox;
  ScrollView scrollOrder;
  LinearLayout orderContent;

  ArrayList<String> ingredientes;
  ArrayList<String> acompanantes;
  ArrayList<String> adicionales;
  ArrayList<String> terminos;

//  LinearLayout recyclerParentOrder;

  RecyclerView.LayoutManager recyclerViewLayoutManager;

  boolean toogleOrder;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.orderlist);
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    iniOrder();
    buttons();
    setItemsAdapter();
  }

  private void iniOrder() {
//    recyclerViewSms = findViewById(R.id.messageRecyclerOrder);
    recyclerArticles = findViewById(R.id.recyclerArticles);
    overlay = findViewById(R.id.overlay);
    txtArrow = findViewById(R.id.txtArrow);
    scrollOrder = findViewById(R.id.scrollOrder);
    orderContent = findViewById(R.id.orderContentLinear);
    btnOrder = findViewById(R.id.buttonOrder);
    btnCheck = findViewById(R.id.buttonCheck);
    btnMarchar = findViewById(R.id.btnMarchar);
//    recyclerParentOrder = findViewById(R.id.recyclerParentOrder);
    textCategory = findViewById(R.id.textCategory);
    numMesa = findViewById(R.id.numMesa);
    pax = findViewById(R.id.pax);
    searchBox = findViewById(R.id.searchBox);

    myDB = DBHelper.GetDBHelper(this);
    myRecetas = myDB.listRecetas();
    toogleOrder = true;
    search = new ArrayList<>();

    showPax = true;

    Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/Font Awesome 5 Free-Solid-900.otf");
    txtArrow.setTypeface(tf);
    txtArrow.setText("\uf35b");
  }

  private void buttons() {

    txtArrow.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(toogleOrder) {
          LinearLayout.LayoutParams paramOverlay = new LinearLayout.LayoutParams(
                  LinearLayout.LayoutParams.MATCH_PARENT,
                  LinearLayout.LayoutParams.MATCH_PARENT
          );
          overlay.setLayoutParams(paramOverlay);

          txtArrow.setRotationX(180);
          toogleOrder = false;
        } else {
          toogleOrder = true;
          LinearLayout.LayoutParams paramOverlay = new LinearLayout.LayoutParams(
                  LinearLayout.LayoutParams.MATCH_PARENT,
                  154
          );
          overlay.setLayoutParams(paramOverlay);
          txtArrow.setRotationX(0);
        }
      }
    });

    btnOrder.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Empleado empleado = myDB.getEmpleado();
        if(!empleado.getFondoId().equals("")) {
          if (mesa.getPax() > 0) {
            createComanda(empleado.getFondoId());
            if (myDB.listPedidos().size() > 0) {
              onBackPressed();
            }
          } else {

            int myPax = !pax.getText().toString().equals("") ? Integer.parseInt(pax.getText().toString()) : 0;

            if(myPax == 0) {
              showMessage("Coloque PAX mayor a 0");
            } else {
              createComanda(empleado.getFondoId());
              if (myDB.listPedidos().size() > 0) {
                onBackPressed();
              }
            }
          }
        } else {
          finish();
        }
      }
    });

    btnCheck.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mSocket.emit(Constants.PRE_CUENTA, mesa.get_id());
        finish();
      }
    });

    btnMarchar.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        showMessage("PEDIDO MARCHADO");
        Marchar marchar = new Marchar();
        marchar.setCommand("MARCHAR ORDEN");
        marchar.setMesa(mesa.getNumeroMesa());
        marcharPedido(marchar);
      }
    });

    searchBox.addTextChangedListener(new TextWatcher() {

      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        search.clear();
        for (Receta receta: myRecetas) {
          if(receta.getNombre().toLowerCase().contains(s)) search.add(receta);
        }
      }

      @Override
      public void afterTextChanged(Editable s) {
        adpPlatos.myPlatos.clear();
        adpPlatos.myPlatos.addAll(search);
        adpPlatos.notifyDataSetChanged();
        myRecetas = myDB.listRecetas();
      }
    });

    searchBox.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
          try  {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
          } catch (Exception e) {
            Log.i("Close Keyboard", e.toString());
          }
        }
      }
    });

    pax.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View v, boolean hasFocus) {
        if(!hasFocus) {
          try  {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
          } catch (Exception e) {
            Log.i("Close Keyboard", e.toString());
          }
        }
      }
    });

  }

  private void marcharPedido(Marchar marchar) {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(getApplicationContext().getString(R.string.iptab))
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    final endPoints request = retrofit.create(endPoints.class);

    Call<Marchar> call = request.sendMarchar(marchar);

    call.enqueue(new Callback<Marchar>() {
      @Override
      public void onResponse(@NonNull Call<Marchar> call, @NonNull Response<Marchar> response) {
        if(response.code() == 200) {
          showMessage("Pedido Marchado");
        } else {
          showMessage("Ocurió un problema al marchar pedido");
        }
      }
      @Override
      public void onFailure(@NonNull Call<Marchar> call, @NonNull Throwable t) {
        Log.d("Error Sending marchado", t.getMessage());
      }
    });
  }

  private void getData() {
    Intent intent = getIntent();
    String myMesa  = (Objects.requireNonNull(intent.getExtras())).getString("mesa");
    assert mesa != null;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(getString(R.string.iptab))
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    endPoints request = retrofit.create(endPoints.class);
    Call<Mesa> call = request.getMesaById(myMesa);
    call.enqueue(new Callback<Mesa>() {
      @Override
      public void onResponse(@NonNull Call<Mesa> call, @NonNull Response<Mesa> response) {
        assert response.body() != null;
        myDB.updateMesa(response.body());
        mesa = response.body();

        if (mesa.getPax() > 0) showPax = false;
        if(!showPax) {
          pax.setVisibility(View.INVISIBLE);
        }

        numMesa.setText(mesa.getNumeroMesa());

        orderContent.removeAllViews();
        orders = new ArrayList<>();
        ordersNotSent = new ArrayList<>();
        ingredientes = new ArrayList<>();
        acompanantes = new ArrayList<>();
        adicionales = new ArrayList<>();
        terminos = new ArrayList<>();
        ArrayList<String> nombrePlatos;
        ArrayList<String> nombrePlatosNotSent;

        ArrayList<Pedido> notGone = myDB.listPedidos();

        for (int i = 0; i < notGone.size(); i++) {
          Pedido pedido = notGone.get(i);
          ordersNotSent.add(pedido.getPedido().toString());
        }

        for (int i = 0; i < mesa.getOrders().size(); i++) {
          if(!mesa.getOrders().get(i).isCobrado()) {
            String order = mesa.getOrders().get(i).getOrder();
            orders.add(order);
          }
        }

        if(ordersNotSent.size()>0) {
          nombrePlatosNotSent = createOrder(ordersNotSent);
          createView(nombrePlatosNotSent, true);
        }

        if(orders.size() > 0) {
          nombrePlatos = createOrder(orders);
          createView(nombrePlatos, false);
        }

        scrollOrder.post(new Runnable() {
          @Override
          public void run() {
            scrollOrder.fullScroll(ScrollView.FOCUS_DOWN);
          }
        });

      }
      @Override
      public void onFailure(@NonNull Call<Mesa> call, @NonNull Throwable t) {
        Log.i("Error from MesaID: " , t.getMessage());
        showMessage("No hay Conexion con el servidor");
      }
    });
  }

  private void setItemsAdapter() {
    adpPlatos = new itemsAdapter(getApplicationContext(), myRecetas, mesa, textCategory);
    recyclerViewLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
    recyclerArticles.setLayoutManager(recyclerViewLayoutManager);
    recyclerArticles.addItemDecoration(new MainOrderActivity.SpacesItemDecoration(5));
    recyclerArticles.setAdapter(adpPlatos);
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

  private ArrayList<String> createOrder(ArrayList<String> orders){
    ArrayList<String> nombrePlatos = new ArrayList<>();
    for (int i = 0; i < orders.size(); i++) {
      String order = orders.get(i);
      String[] splitS = order.split("-");
      nombrePlatos.add(splitS[0]);
    }
    return nombrePlatos;
  }

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

  private void createView(ArrayList<String> nombrePlatos, boolean state) {
    for (String nombrePlato: nombrePlatos) {
      final CheckBox checkBox = new CheckBox(getApplicationContext());
      LinearLayout.LayoutParams paramc = new LinearLayout.LayoutParams(
              LinearLayout.LayoutParams.MATCH_PARENT,
              ViewGroup.LayoutParams.MATCH_PARENT);

      LinearLayout.LayoutParams paLin = new LinearLayout.LayoutParams(
              LinearLayout.LayoutParams.WRAP_CONTENT,
              LinearLayout.LayoutParams.WRAP_CONTENT);


      checkBox.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.letras));
      checkBox.setScaleX(1.5f);
      checkBox.setScaleY(1.5f);

      checkBox.setEnabled(state);
      paramc.gravity = Gravity.FILL;
      checkBox.setSingleLine(false);

      checkBox.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
      checkBox.setButtonDrawable(getApplicationContext().getDrawable(R.drawable.ic_delete_forever_black_24dp));
      if(!state)    checkBox.setButtonDrawable(getApplicationContext().getDrawable(R.drawable.ic_delete_forever_disabled));
      int marginTop = 0;

      scrollOrder.setPadding(15, 0, 0, 0);
      orderContent.setPadding(0, 0, 0, 0);
      if(nombrePlatos.indexOf(nombrePlato) == 0) marginTop = 5;
      paLin.setMargins(0,marginTop,0,0);
      paramc.setMargins(0,marginTop,10,0);
      checkBox.setHeight(10);

      final LinearLayout linearLayout = new LinearLayout(this);

      paLin.height = 50;
      linearLayout.setLayoutParams(paLin);
      linearLayout.setOrientation(LinearLayout.HORIZONTAL);

      TextView textView = new TextView(this);
      textView.setText(nombrePlato);
      textView.setLayoutParams(paramc);
      textView.setTextSize(18);
      textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
      textView.setTypeface(textView.getTypeface(), Typeface.BOLD_ITALIC);

      checkBox.setLayoutParams(paramc);
      checkBox.setChecked(true);
      checkBox.setTag(orderContent.getChildCount());
      textView.setTag(orderContent.getChildCount());

      checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
          ArrayList<Pedido> pedidos = myDB.listPedidos();
          boolean finish = false;
          for (int i = 0; i < pedidos.size(); i++) {
            if(finish) break;
            String pedido = pedidos.get(i).getPedido().toString();
            String sqId = pedidos.get(i).getId();

            for (int j = 0; j < orderContent.getChildCount(); j++) {

              LinearLayout lyout  = (LinearLayout) orderContent.getChildAt(j);

              TextView tview = (TextView) lyout.getChildAt(1);
              String[] parts = pedido.split("-");
              String part1 = parts[0];
              String fromtv = tview.getText().toString();
              if(fromtv.equals(part1) && tview.getTag()==checkBox.getTag()) {
                myDB.deletePedido(sqId);
                orderContent.removeView(lyout);
                finish = true;
                break;
              }
            }
          }
        }
      });

      linearLayout.addView(checkBox);
      linearLayout.addView(textView);
      orderContent.addView(linearLayout);
    }
  }

//  private void setMessageAdapter() {
//    adpSmsOrder = new smsAdapter(this, myMessages);
//    final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//    linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//    recyclerViewSms.setLayoutManager(linearLayoutManager);
//    recyclerViewSms.setAdapter(adpSmsOrder);
//  }

  private void createComanda(String fondoId) {
    Comanda comanda = new Comanda();
    comanda.setMesa(mesa.get_id());
    comanda.setNumeroMesa(mesa.getNumeroMesa());
    comanda.setMesaEstado(mesa.getEstado());
    ArrayList<String> orders = new ArrayList<>();

    for (int i = 0; i < myDB.listPedidos().size(); i++) {
      String order = String.valueOf(myDB.listPedidos().get(i).getPedido());
      orders.add(order);
    }

    if(orders.size() > 0) {
      comanda.setOrders(orders);
      Empleado empleado = myDB.getEmpleado();

      empleado.set_id(empleado.get_id()); //TODO TOMAR DE LA BD
      comanda.setEmpleado(empleado);
      comanda.setEnviado(true);
      comanda.setFondoId(fondoId);
//      int myPax = mesa.getPax();
//      comanda.setPax(myPax);
      if(mesa.getPax() == 0) {
        comanda.setPax(Integer.parseInt(pax.getText().toString()));
      } else {
        comanda.setPax(50000);
      }
      sendOrder(comanda);
    } else {
      showMessage("Seleccione Producto");
    }
  }

  public void sendOrder(Comanda comanda) { //TODO ACA VER TODO PARA COPIAR LO DE LOGOUT
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(getApplicationContext().getString(R.string.iptab))
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    final endPoints request = retrofit.create(endPoints.class);

    Call<Object> call = request.addComanda(comanda);

    call.enqueue(new Callback<Object>() {
      @Override
      public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
        if(response.code() == 200) {
          showMessage("Pedido colocado con éxito");
        } else {
          showMessage("Ocurió un problema al colocar el pedido, intente de nuevo");
        }
      }
      @Override
      public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
        Log.d("Error Sending order", t.getMessage());
      }
    });
  }

  @Override
  protected void onResume() {
    super.onResume();
    Empleado empleado = myDB.getEmpleado();
    if(!empleado.getFondoId().equals("")) {
      getData();
//      myMessages = myDB.listSms();
//      setMessageAdapter();
      try {
        mSocket = IO.socket(getApplicationContext().getString(R.string.iptab));
      } catch (URISyntaxException e) {
        Log.i(MainActivity.class.getSimpleName(),e.getMessage());
        e.printStackTrace();
      }
      mSocket.connect();
    } else {
      finish();
    }
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    myDB.deleteAllPedido();
    finish();
  }
}