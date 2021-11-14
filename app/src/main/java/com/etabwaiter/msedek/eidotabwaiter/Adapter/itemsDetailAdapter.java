package com.etabwaiter.msedek.eidotabwaiter.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.etabwaiter.msedek.eidotabwaiter.Models.ConfigPosition;
import com.etabwaiter.msedek.eidotabwaiter.Models.Guarnicion;
import com.etabwaiter.msedek.eidotabwaiter.Models.Receta;
import com.etabwaiter.msedek.eidotabwaiter.R;


import java.util.ArrayList;

public class itemsDetailAdapter extends RecyclerView.Adapter<itemsDetailAdapter.itemsAdapterViewHolder> {

  private Context mContext;
  public Receta receta;
  private int recyclerSize;
  public ArrayList<itemsAdapterViewHolder> holders;
  private ArrayList<ConfigPosition> configs;
  private TextView description;

  public itemsDetailAdapter(Context context, final Receta receta, TextView description) {
    this.mContext = context;
    this.receta = receta;
    this.recyclerSize = 0;
    this.description = description;
    this.holders = new ArrayList<>();

    configs = new ArrayList<>();

    if(receta.getIngredientes().size() > 0) {
      ConfigPosition configposition = new ConfigPosition();
      configposition.setCategoria("ingredientes");
      configposition.setPosition(recyclerSize);
      configs.add(configposition);
      recyclerSize++;
    }

    if(receta.getContorno().size() > 0) {
      ConfigPosition configposition = new ConfigPosition();
      configposition.setCategoria("guarnicion");
      configposition.setPosition(recyclerSize);
      configs.add(configposition);
      recyclerSize++;
    }

    if(receta.getCf_cocci_n().size() > 0) {
      ConfigPosition configposition = new ConfigPosition();
      configposition.setCategoria("termino");
      configposition.setPosition(recyclerSize);
      configs.add(configposition);
      recyclerSize++;
    }

    if(receta.getCf_temperatura().size() > 0) {
      ConfigPosition configposition = new ConfigPosition();
      configposition.setCategoria("temperatura");
      configposition.setPosition(recyclerSize);
      configs.add(configposition);
      recyclerSize++;
    }

    if(receta.getEndulzante().size() > 0) {
      ConfigPosition configposition = new ConfigPosition();
      configposition.setCategoria("endulzante");
      configposition.setPosition(recyclerSize);
      configs.add(configposition);
      recyclerSize++;
    }

    if(receta.getCf_lacteos().size() > 0) {
      ConfigPosition configposition = new ConfigPosition();
      configposition.setCategoria("lacteos");
      configposition.setPosition(recyclerSize);
      configs.add(configposition);
      recyclerSize++;
    }
  }

  @NonNull
  @Override
  public itemsDetailAdapter.itemsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new itemsDetailAdapter.itemsAdapterViewHolder(LayoutInflater.from(mContext).inflate(R.layout.itemdetailrow, parent, false));
  }

  @SuppressLint("SetTextI18n")
  @Override
  public void onBindViewHolder(@NonNull final itemsDetailAdapter.itemsAdapterViewHolder holder, final int position) {
    holders.add(position,holder);
    ConstraintLayout.LayoutParams param = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    holder.txtTitle.setLayoutParams(param);
    holder.txtTitle.setTextColor(ContextCompat.getColor(mContext, R.color.letras));
    param.setMargins(35,0,0,0);
    holder.elpadre.removeAllViews();

    String cat = configs.get(position).getCategoria();
    holder.txtTitle.setText(cat.toUpperCase());


    description.setText(receta.getDescripcion());

    if(cat.equals("ingredientes")) {
      holder.ingredientes = new ArrayList<>();
      for (String insumos: receta.getIngredientes()) {

        holder.elpadre.addView(createCheck(insumos,
                holder.txtTitle.getText().toString().toLowerCase(),
                holder.ingredientes,
                null,
                null,
                null,
                null,
                null,
                null,
                0));
      }
//      holder.ingredientes.clear();
    }

    if(cat.equals("guarnicion")) {
      holder.acompanantes = new ArrayList<>();
      holder.cantGuarni = receta.getCf_cant_guarnicion();
      for (Guarnicion sub: receta.getContorno()) {
        holder.elpadre.addView(createCheck(sub.getName(),
                holder.txtTitle.getText().toString().toLowerCase(),
                null,
                holder.acompanantes,
                null,
                null,
                null,
                null,
                holder.elpadre,
                holder.cantGuarni));
      }
//      holder.acompanantes.clear();
    }

    if(cat.equals("termino")) {
      holder.terminos = new ArrayList<>();
      for (String termino: receta.getCf_cocci_n()) {
        holder.elpadre.addView(createCheck(termino,
                holder.txtTitle.getText().toString().toLowerCase(),
                null,
                null,
                holder.terminos,
                null,
                null,
                null,
                holder.elpadre,
                0));
      }
//      holder.terminos.clear();
    }

    if(cat.equals("temperatura")) {
      holder.temperaturas = new ArrayList<>();
      for (String tempe: receta.getCf_temperatura()) {
        holder.elpadre.addView(createCheck(tempe,
                holder.txtTitle.getText().toString().toLowerCase(),
                null,
                null,
                null,
                holder.temperaturas,
                null,
                null,
                holder.elpadre,
                0));
      }
//      holder.temperaturas.clear();
    }

    if(cat.equals("endulzante")) {
      holder.endulzantes = new ArrayList<>();
      for (String endul: receta.getEndulzante()) {
        holder.elpadre.addView(createCheck(endul,
                holder.txtTitle.getText().toString().toLowerCase(),
                null,
                null,
                null,
                null,
                holder.endulzantes,
                null,
                holder.elpadre,
                0));
      }
//      holder.endulzantes.clear();
    }

    if(cat.equals("lacteos")) {
      holder.lacteos = new ArrayList<>();
      for (String lact: receta.getCf_lacteos()) {
        holder.elpadre.addView(createCheck(lact,
                holder.txtTitle.getText().toString().toLowerCase(),
                null,
                null,
                null,
                null,
                null,
                holder.lacteos,
                holder.elpadre,
                0));
      }
//      holder.lacteos.clear();
    }
  }

  @Override
  public int getItemCount() {
    return recyclerSize;
  }

  private void showMessage(String mensaje) {
    final Toast toast = Toast.makeText(mContext, mensaje, Toast.LENGTH_SHORT);
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

  private CheckBox createCheck(final String checkText,
                               final String checkSwitch,
                               final ArrayList<String> ingredientes,
                               final ArrayList<Guarnicion> acompanantes,
                               final ArrayList<String> terminos,
                               final ArrayList<String> temperaturas,
                               final ArrayList<String> endulzantes,
                               final ArrayList<String> lacteos,
                               final LinearLayout elPadre,
                               final int cantGuarniciones) {
    final CheckBox checkBox = new CheckBox(mContext);
    LinearLayout.LayoutParams paramc = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    checkBox.setTextColor(ContextCompat.getColor(mContext, R.color.letras));
    checkBox.setText(String.format("%s%s", checkText.substring(0, 1).toUpperCase(), checkText.substring(1)));
    checkBox.setScaleX(1.5f);
    checkBox.setScaleY(1.5f);
    paramc.gravity = Gravity.CENTER_VERTICAL;
    checkBox.setSingleLine(true);
    checkBox.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
    paramc.setMargins(90,0,0,12);
    checkBox.setHeight(40);
    checkBox.setLayoutParams(paramc);
    checkBox.setChecked(false);
    if (checkSwitch.equals("ingredientes")) {
      checkBox.setChecked(true);
    }

    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
          switch (checkSwitch) {
            case "guarnicion":
              if(acompanantes.size() < cantGuarniciones) {
                Guarnicion guarnicion = new Guarnicion();
                guarnicion.setName(checkText);
                for (Guarnicion guar : receta.getContorno()) {
                  if(guar.getName().equals(checkText)) {
                    guarnicion.setSku(guar.getSku());
                    break;
                  }
                }
                acompanantes.add(guarnicion);
                showMessage("AGREGASTE " + checkText.toUpperCase());
              } else {
                showMessage("NO PUEDES AGREGAR MAS GUARNICIONES");
                CheckBox gua = (CheckBox) buttonView;
                for (int i = 0; i <= elPadre.getChildCount(); i++) {
                  View view = elPadre.getChildAt(i);
                  if (view instanceof CheckBox) {
                    CheckBox chk = (CheckBox) view;
                    String txt =  chk.getText().toString();
                    if(txt.equals(gua.getText().toString())) {
                      chk.setChecked(false);
                      break;
                    }
                  }
                }
                Log.i("PRE BORRADA", acompanantes.toString());
              }
              break;
            case "ingredientes":
              ingredientes.remove(checkText);
              showMessage("AGREGASTE " + checkText.toUpperCase());
              break;
            case "termino":
              CheckBox ter = (CheckBox) buttonView;
              for (int i = 0; i <= elPadre.getChildCount(); i++) {
                View view = elPadre.getChildAt(i);
                if (view instanceof CheckBox) {
                  CheckBox chk = (CheckBox) view;
                  String txt =  chk.getText().toString();
                  if(!txt.equals(ter.getText().toString())) {
                    chk.setChecked(false);
                  }
                }
              }
              terminos.clear();
              terminos.add(checkText);
              showMessage("TERMINO " + checkText.toUpperCase());
              break;
            case "temperatura":
              CheckBox tem = (CheckBox) buttonView;
              for (int i = 0; i <= elPadre.getChildCount(); i++) {
                View view = elPadre.getChildAt(i);
                if (view instanceof CheckBox) {
                  CheckBox chk = (CheckBox) view;
                  String txt =  chk.getText().toString();
                  if(!txt.equals(tem.getText().toString())) {
                    chk.setChecked(false);
                  }
                }
              }
              temperaturas.clear();
              temperaturas.add(checkText);
              showMessage("TEMPERATURA " + checkText.toUpperCase());
              break;
            case "endulzante":
              CheckBox end = (CheckBox) buttonView;
              for (int i = 0; i <= elPadre.getChildCount(); i++) {
                View view = elPadre.getChildAt(i);
                if (view instanceof CheckBox) {
                  CheckBox chk = (CheckBox) view;
                  String txt =  chk.getText().toString();
                  if(!txt.equals(end.getText().toString())) {
                    chk.setChecked(false);
                  }
                }
              }
              endulzantes.clear();
              endulzantes.add(checkText);
              showMessage("ENDULZANTE " + checkText.toUpperCase());
              break;
            case "lacteos":
              CheckBox lac = (CheckBox) buttonView;
              for (int i = 0; i <= elPadre.getChildCount(); i++) {
                View view = elPadre.getChildAt(i);
                if (view instanceof CheckBox) {
                  CheckBox chk = (CheckBox) view;
                  String txt =  chk.getText().toString();
                  if(!txt.equals(lac.getText().toString())) {
                    chk.setChecked(false);
                  }
                }
              }
              lacteos.clear();
              lacteos.add(checkText);
              showMessage("LACTEO " + checkText.toUpperCase());
              break;
          }
        } else {
          switch (checkSwitch) {
            case "guarnicion":
              for (int i = 0; i < acompanantes.size(); i++) {
                String acompanante = acompanantes.get(i).getName();
                if(acompanante.equals(checkText)) {
                  acompanantes.remove(i);
                  break;
                }
              }
              showMessage("ELIMINASTE " + checkText.toUpperCase());
              break;
            case "ingredientes":
              ingredientes.add(checkText);
              showMessage("ELIMINASTE " + checkText.toUpperCase());
              break;
            case "termino":
              terminos.clear();
              break;
            case "temperatura":
              temperaturas.clear();
              break;
            case "endulzante":
              endulzantes.clear();
              break;
            case "lacteo":
              lacteos.clear();
              break;
          }
        }
      }
    });
    return checkBox;
  }

  @SuppressWarnings("WeakerAccess")
  public class itemsAdapterViewHolder extends RecyclerView.ViewHolder {
    LinearLayout elpadre;
    TextView txtTitle;

    public ArrayList<String> ingredientes;
    public ArrayList<Guarnicion> acompanantes;
    public ArrayList<String> terminos;
    public ArrayList<String> temperaturas;
    public ArrayList<String> endulzantes;
    public ArrayList<String> lacteos;

    private int cantGuarni;

    @SuppressLint("SimpleDateFormat")
    public itemsAdapterViewHolder(View itemView) {
      super(itemView);

      txtTitle = itemView.findViewById(R.id.txtTitle);
      elpadre = itemView.findViewById(R.id.elpadre);

      cantGuarni = 0;
    }
  }
}
