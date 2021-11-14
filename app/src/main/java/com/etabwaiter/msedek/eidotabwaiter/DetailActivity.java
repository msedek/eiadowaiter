package com.etabwaiter.msedek.eidotabwaiter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.etabwaiter.msedek.eidotabwaiter.Adapter.itemsDetailAdapter;
import com.etabwaiter.msedek.eidotabwaiter.Models.Empleado;
import com.etabwaiter.msedek.eidotabwaiter.Models.Guarnicion;
import com.etabwaiter.msedek.eidotabwaiter.Models.Mesa;
import com.etabwaiter.msedek.eidotabwaiter.Models.Pedido;
import com.etabwaiter.msedek.eidotabwaiter.Models.Receta;
import com.etabwaiter.msedek.eidotabwaiter.SQlite.DBHelper;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class DetailActivity extends AppCompatActivity {

  Integer posiscroll;

  TextView txtPhoto;
  TextView txtPlatoDetail;
  TextView txtPrecioDetail;
  LinearLayout recyclerParent;
  LinearLayout imageParent;
  ImageView imgItemDetail;
  TextView addBtn1;
  Button addBtn2;
  Button buttonDec;
  Button buttonInc;
  TextView quantity;
  EditText edtNotas;
  CheckBox directo;
  String recatego;

  DBHelper myDB;

  boolean toogleDetail;
  boolean toogleView;

  boolean fromOrder;

  boolean ingreActive;
  boolean acompActive;
  boolean adicActive;
  boolean termActive;
  boolean tempActive;
  boolean endulActive;
  boolean lactActive;

  itemsDetailAdapter adpItem;
  RecyclerView recyclerViewItemDetail;
  RecyclerView.LayoutManager recyclerViewLayoutManager;
  Receta receta;
  Mesa mesa;
  TextView description;

  StringBuilder orderIngre;

  ArrayList<String> ingredientes;
  ArrayList<Guarnicion> acompanantes;
  ArrayList<String> terminos;
  ArrayList<String> temperaturas;
  ArrayList<String> endulzantes;
  ArrayList<String> lacteos;

  boolean alergias;
  boolean firstTime;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);

    txtPhoto =  findViewById(R.id.txtPhoto);
    txtPlatoDetail =  findViewById(R.id.txtPlatoDetail);
    txtPrecioDetail =  findViewById(R.id.txtPrecioDetail);
    recyclerViewItemDetail = findViewById(R.id.recyclerItemDetail);
    recyclerParent = findViewById(R.id.recyclerParent);
    imageParent = findViewById(R.id.imageParent);
    imgItemDetail = findViewById(R.id.imgItemDetail);
    addBtn1 = findViewById(R.id.addBtn1);
    addBtn2 = findViewById(R.id.addBtn2);
    buttonDec = findViewById(R.id.buttonDec);
    buttonInc = findViewById(R.id.buttonInc);
    quantity = findViewById(R.id.quantity);
    description = findViewById(R.id.txtDescription);
    edtNotas = findViewById(R.id.edtNotas);
    directo = findViewById(R.id.directo);

    fromOrder = false;
    alergias = true;
    firstTime = true;
    recatego ="";

    iniSetup();
    buttons();
  }

  @SuppressLint({"SetTextI18n", "DefaultLocale"})
  private void getData() {
    Intent intent = getIntent();
    receta = (Receta) Objects.requireNonNull(intent.getExtras()).getSerializable("receta");
    mesa = (Mesa) Objects.requireNonNull(intent.getExtras()).getSerializable("mesa");

    if (receta.getCategoria_receta().toLowerCase().contains("entradas") ||
            receta.getCategoria_receta().toLowerCase().contains("ensaladas") ||
            receta.getCategoria_receta().toLowerCase().contains("piqueos") ||
            receta.getCategoria_receta().toLowerCase().contains("sopas") ||
            receta.getCategoria_receta().toLowerCase().contains("entradas") ||
            receta.getCategoria_receta().toLowerCase().contains("postresCarta")) {
      directo.setVisibility(View.VISIBLE);
    }

    if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
      File sdcard = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
      File d1 = new File(sdcard, receta.getFoto_movil());
      Uri uri = Uri.fromFile(d1);
      Glide.with(getApplicationContext())
              .load(uri)
              .into(imgItemDetail);
    }

    assert receta != null;
    txtPlatoDetail.setText(receta.getNombre().toUpperCase());
    txtPrecioDetail.setText("S/ " + String.format("%.2f", (receta.getPrecio_receta() * 1.28)));

    setItemAdapter();
  }

  private void iniSetup() {
    myDB = DBHelper.GetDBHelper(this);

    toogleDetail = true;
    toogleView = true;

    posiscroll = 0;

    Typeface tfCamera = Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/Font Awesome 5 Free-Solid-900.otf");
    txtPhoto.setTypeface(tfCamera);
    txtPhoto.setText("\uf030");

    getData();
  }

  private void buttons() {

    edtNotas.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
          try  {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
          } catch (Exception e) {
            Log.i("Close Keyboard", e.toString());
          }
        }
      }
    });

    addBtn1.setOnClickListener(new View.OnClickListener() {
      @SuppressLint("SetTextI18n")
      @Override
      public void onClick(View v) {

        try {
          InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
          imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
          Log.i("Close Keyboard", e.toString());
        }

        Empleado empleado = myDB.getEmpleado();

        if (!empleado.getFondoId().equals("")) {



          if (firstTime) {
            for (int i = 0; i < adpItem.holders.size(); i++) {
              if (adpItem.holders.get(i).ingredientes != null) {
                alergias = false;
                break;
              }
            }
            firstTime = false;
          }

          if (alergias && !firstTime) {
            orderIngre = new StringBuilder(adpItem.receta.getNombre() + "_x(" + quantity.getText().toString() + ")");

            ingreActive = false;
            acompActive = false;
            adicActive = false;
            termActive = false;
            tempActive = false;
            endulActive = false;
            lactActive = false;

            ArrayList<String> skuGuarni = new ArrayList<>();

            int ingre = 0;
            int acomp = 0;
            int term = 0;
            int temp = 0;
            int endul = 0;
            int lact = 0;

            boolean proceedTerm = true;
            boolean proceedTemp = true;
            boolean proceedEndul = true;
            boolean proceedLact = true;

            for (int i = 0; i < adpItem.holders.size(); i++) {
              if (adpItem.holders.get(i).ingredientes != null) {
                if (adpItem.holders.get(i).ingredientes.size() > 0) {
                  ingre = i;
                  ingreActive = true;
                }
              }
              if (adpItem.holders.get(i).acompanantes != null) {
                if (adpItem.holders.get(i).acompanantes.size() > 0) {
                  acomp = i;
                  acompActive = true;
                }
              }
              if (adpItem.holders.get(i).terminos != null) {
                proceedTerm = false;
                if (adpItem.holders.get(i).terminos.size() > 0) {
                  term = i;
                  termActive = true;
                }
              }
              if (adpItem.holders.get(i).temperaturas != null) {
                proceedTemp = false;
                if (adpItem.holders.get(i).temperaturas.size() > 0) {
                  temp = i;
                  tempActive = true;
                }
              }
              if (adpItem.holders.get(i).endulzantes != null) {
                proceedEndul = false;
                if (adpItem.holders.get(i).endulzantes.size() > 0) {
                  endul = i;
                  endulActive = true;
                  proceedEndul = false;
                }
              }
              if (adpItem.holders.get(i).lacteos != null) {
                proceedLact = false;
                if (adpItem.holders.get(i).lacteos.size() > 0) {
                  lact = i;
                  lactActive = true;
                }
              }
            }

            if (ingreActive) {
              ingredientes = adpItem.holders.get(ingre).ingredientes;
              for (int i = 0; i < ingredientes.size(); i++) {
                String ingrediente = ingredientes.get(i);
                if (i == 0 && i < ingredientes.size() - 1) {
                  orderIngre.append(" .s(").append(ingrediente).append(",");
                } else if (i == 0 && i == ingredientes.size() - 1) {
                  orderIngre.append(" .s(").append(ingrediente).append(") ");
                } else if (i > 0 && i < ingredientes.size() - 1) {
                  orderIngre.append(" ").append(ingrediente).append(",");
                } else if (i > 0 && i == ingredientes.size() - 1) {
                  orderIngre.append(" ").append(ingrediente).append(") ");
                }
              }
            }

            if (acompActive) {
              acompanantes = adpItem.holders.get(acomp).acompanantes;
              for (int i = 0; i < acompanantes.size(); i++) {
                skuGuarni.add(acompanantes.get(i).getSku());
                String acompanante = acompanantes.get(i).getName();
                if (i == 0 && i < acompanantes.size() - 1) {
                  orderIngre.append(" .ac(").append(acompanante).append(",");
                } else if (i == 0 && i == acompanantes.size() - 1) {
                  orderIngre.append(" .ac(").append(acompanante).append(") ");
                } else if (i > 0 && i < acompanantes.size() - 1) {
                  orderIngre.append(" ").append(acompanante).append(",");
                } else if (i > 0 && i == acompanantes.size() - 1) {
                  orderIngre.append(" ").append(acompanante).append(") ");
                }
              }
            }

            if (termActive) {
              terminos = adpItem.holders.get(term).terminos;
              for (int i = 0; i < terminos.size(); i++) {
                String termino = terminos.get(i);
                if (i == 0 && i < terminos.size() - 1) {
                  orderIngre.append(" .t(").append(termino).append(",");
                } else if (i == 0 && i == terminos.size() - 1) {
                  orderIngre.append(" .t(").append(termino).append(") ");
                } else if (i > 0 && i < terminos.size() - 1) {
                  orderIngre.append(" ").append(termino).append(",");
                } else if (i > 0 && i == terminos.size() - 1) {
                  orderIngre.append(" ").append(termino).append(") ");
                }
              }
            }

            if (orderIngre.toString().contains(".t(")) {
              proceedTerm = true;
            }

            if (tempActive) {
              proceedTemp = false;
              temperaturas = adpItem.holders.get(temp).temperaturas;
              for (int i = 0; i < temperaturas.size(); i++) {
                String temperatura = temperaturas.get(i);
                if (i == 0 && i < temperaturas.size() - 1) {
                  orderIngre.append(" .tem(").append(temperatura).append(",");
                } else if (i == 0 && i == temperaturas.size() - 1) {
                  orderIngre.append(" .tem(").append(temperatura).append(") ");
                } else if (i > 0 && i < temperaturas.size() - 1) {
                  orderIngre.append(" ").append(temperatura).append(",");
                } else if (i > 0 && i == temperaturas.size() - 1) {
                  orderIngre.append(" ").append(temperatura).append(") ");
                }
              }
            }

            if (orderIngre.toString().contains(".tem(")) {
              proceedTemp = true;
            }

            if (endulActive) {
              endulzantes = adpItem.holders.get(endul).endulzantes;
              for (int i = 0; i < endulzantes.size(); i++) {
                String endulzante = endulzantes.get(i);
                if (i == 0 && i < endulzantes.size() - 1) {
                  orderIngre.append(" .end(").append(endulzante).append(",");
                } else if (i == 0 && i == endulzantes.size() - 1) {
                  orderIngre.append(" .end(").append(endulzante).append(") ");
                } else if (i > 0 && i < endulzantes.size() - 1) {
                  orderIngre.append(" ").append(endulzante).append(",");
                } else if (i > 0 && i == endulzantes.size() - 1) {
                  orderIngre.append(" ").append(endulzante).append(") ");
                }
              }
            }

            if (orderIngre.toString().contains(".end(")) {
              proceedEndul = true;
            }

            if (lactActive) {
              proceedLact = false;
              lacteos = adpItem.holders.get(lact).lacteos;
              for (int i = 0; i < lacteos.size(); i++) {
                String lacteo = lacteos.get(i);
                if (i == 0 && i < lacteos.size() - 1) {
                  orderIngre.append(" .lact(").append(lacteo).append(",");
                } else if (i == 0 && i == lacteos.size() - 1) {
                  orderIngre.append(" .lact(").append(lacteo).append(") ");
                } else if (i > 0 && i < lacteos.size() - 1) {
                  orderIngre.append(" ").append(lacteo).append(",");
                } else if (i > 0 && i == lacteos.size() - 1) {
                  orderIngre.append(" ").append(lacteo).append(") ");
                }
              }
            }

            if (orderIngre.toString().contains(".lact(")) {
              proceedLact = true;
            }


            String nota = edtNotas.getText().toString();

            if (proceedTerm && proceedTemp && proceedEndul && proceedLact) {
              String date = new SimpleDateFormat("yyyyMMddHHmmssSS", Locale.getDefault()).format(new Date());
              Pedido pedido = new Pedido();

              if (recatego.contains("DIRECTOS")) receta.setCategoria_receta(recatego);

              StringBuilder withRecipe = orderIngre.append("-")
                      .append(receta.getCategoria_receta())
                      .append("-").append(receta.getSku())
                      .append("*").append(receta.getItem_id())
                      .append("*").append(receta.getTax_id())
                      .append("*").append(receta.getTax_name())
                      .append("*").append(receta.getTax_percentage())
                      .append("*").append(receta.getPrecio_receta());
              if (skuGuarni.size() > 0) {
                for (int i = 0; i < skuGuarni.size(); i++) {
                  withRecipe.append("#").append(skuGuarni.get(i));
                }
              }
              if (!nota.trim().equals("")) {
                withRecipe.append("-").append("nota: ").append(nota);
              }
              //TODO CALCULAR PRECIO Y ENVIAR CON PEDIDO
              pedido.setPedido(withRecipe);
              pedido.setDate(date);
              pedido.setId(date);
//              String position = myDB.listPedidos().size() + "";
              Log.i("EL PEDIDO", String.valueOf(withRecipe));
              myDB.addPedido(pedido, date);
              onBackPressed();
            } else {
              if (!proceedTerm) {
                showMessage("SELECCIONE TERMINO");
              } else if (!proceedTemp) {
                showMessage("SELECCIONE TEMPERATURA");
              } else if (!proceedEndul) {
                showMessage("SELECCIONE ENDULZANTE");
              } else if (!proceedLact) {
                showMessage("SELECCIONE LACTEO");
              }
            }
          } else {
            showMessage("DEBE PREGUNTAR POR LAS ALERGIAS");
            alergias = true;
          }
        } else {
          finish();
        }
      }
    });

    addBtn2.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        addBtn1.performClick();
      }
    });

    directo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
          recatego = "DIRECTOS";
          showMessage("CAMBIO EL PLATO DE CATEGORIA A DIRECTOS");
        } else {
          recatego = "";
        }
      }
    });

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

  @SuppressLint("ClickableViewAccessibility")
  private void setItemAdapter() {
    adpItem = new itemsDetailAdapter(this, receta, description);
    recyclerViewLayoutManager = new GridLayoutManager(getApplicationContext(), 2,GridLayoutManager.HORIZONTAL,false);
    recyclerViewItemDetail.setLayoutManager(recyclerViewLayoutManager);
    recyclerViewItemDetail.addItemDecoration(new DetailActivity.SpacesItemDecoration(20));
    recyclerViewItemDetail.setAdapter(adpItem);

    txtPhoto.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(toogleView) {
          imgItemDetail.setVisibility(View.VISIBLE);
          imageParent.setVisibility(View.VISIBLE);
          recyclerParent.setVisibility(View.GONE);
          toogleView = false;
        } else {
          imageParent.setVisibility(View.GONE);
          imgItemDetail.setVisibility(View.GONE);
          recyclerParent.setVisibility(View.VISIBLE);
          toogleView = true;
        }
      }
    });

    buttonInc.setOnClickListener(new View.OnClickListener() {
      @SuppressLint("SetTextI18n")
      @Override
      public void onClick(View v) {
        Integer cantPlatos =  Integer.parseInt(quantity.getText().toString());
        cantPlatos++;
        quantity.setText(cantPlatos + "");
      }
    });

    buttonDec.setOnClickListener(new View.OnClickListener() {
      @SuppressLint("SetTextI18n")
      @Override
      public void onClick(View v) {
        Integer cantPlatos =  Integer.parseInt(quantity.getText().toString());
        if(cantPlatos > 1) {
          cantPlatos--;
          quantity.setText(cantPlatos + "");
        }
      }
    });
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
  public void onBackPressed() {
    super.onBackPressed();
    finish();
  }

  @Override
  protected void onResume() {
    super.onResume();
    Empleado empleado = myDB.getEmpleado();
    if(empleado.getFondoId().equals("")) {
      finish();
    }
  }
}
