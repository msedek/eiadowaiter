package com.etabwaiter.msedek.eidotabwaiter.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etabwaiter.msedek.eidotabwaiter.Interfaz.endPoints;
import com.etabwaiter.msedek.eidotabwaiter.Models.Empleado;
import com.etabwaiter.msedek.eidotabwaiter.Models.Mensaje;
import com.etabwaiter.msedek.eidotabwaiter.R;

import com.etabwaiter.msedek.eidotabwaiter.SQlite.DBHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class smsAdapter extends RecyclerView.Adapter<smsAdapter.VH> {

  private Context mContext;
  public ArrayList<Mensaje> mensajes;
  private DBHelper myDB;

  public smsAdapter(Context context, ArrayList<Mensaje>mensajes) {
    this.mContext = context;
    this.mensajes = mensajes;
    myDB = DBHelper.GetDBHelper(mContext);
  }

  @NonNull
  @Override
  public smsAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.smsrow, parent, false);
    return new smsAdapter.VH(v);
  }

  @SuppressLint("ClickableViewAccessibility")
  @Override
  public void onBindViewHolder(@NonNull final smsAdapter.VH holder, @SuppressLint("RecyclerView") final int position) {
    final Mensaje mensaje = mensajes.get(position);
    int colorP = Color.TRANSPARENT;
    int colorO = Color.TRANSPARENT;

    String date = holder.df.format(mensaje.getFechamensaje());

    holder.txtSender.setText(mensaje.getRemitente());
    holder.txtTime.setText(date);
    holder.txtSms.setText(mensaje.getTexto());

    switch (mensaje.getRemitente().toUpperCase()) {
      case "BAR":
        colorP = ContextCompat.getColor(mContext, R.color.barp);
        colorO = ContextCompat.getColor(mContext, R.color.bar);
        holder.elpadre.setBackgroundColor(colorO);
        break;
      case "CAJA":
        colorP = ContextCompat.getColor(mContext, R.color.cajap);
        colorO = ContextCompat.getColor(mContext, R.color.caja);
        holder.elpadre.setBackgroundColor(colorO);
        break;
      case "COCINA":
        colorP = ContextCompat.getColor(mContext, R.color.cocinap);
        colorO = ContextCompat.getColor(mContext, R.color.cocina);
        holder.elpadre.setBackgroundColor(colorO);
        break;
    }

    final int finalColorP = colorP;
    final int finalColorO = colorO;

    holder.checkParent.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        myDB.deleteSms(mensaje.get_id());
        deleteSms(mensaje.get_id());
        mensajes.remove(position);
        Empleado emp = myDB.getEmpleado();
        emp.getMensajes().clear();
        emp.getMensajes().addAll(mensajes);
        myDB.updateEmpleado(emp);
        notifyDataSetChanged();
      }
    });

//    holder.checkParent.setOnTouchListener(new View.OnTouchListener() {
//      @Override
//      public boolean onTouch(View v, MotionEvent event) {
//        int evento = event.getAction();
//        switch (evento) {
//          case MotionEvent.ACTION_DOWN :
//            holder.elpadre.setBackgroundColor(finalColorP);
//            holder.txtCheck.setTextColor(ContextCompat.getColor(mContext, R.color.black));
//            holder.txtTime.setTextColor(ContextCompat.getColor(mContext, R.color.black));
//            holder.txtSender.setTextColor(ContextCompat.getColor(mContext, R.color.black));
//            holder.txtSms.setTextColor(ContextCompat.getColor(mContext, R.color.black));
//
//            break;
////          case MotionEvent.ACTION_UP :
////            holder.elpadre.setBackgroundColor(finalColorO);
////            holder.txtCheck.setTextColor(ContextCompat.getColor(mContext, R.color.whiteText));
////            holder.txtTime.setTextColor(ContextCompat.getColor(mContext, R.color.whiteText));
////            holder.txtSender.setTextColor(ContextCompat.getColor(mContext, R.color.whiteText));
////            holder.txtSms.setTextColor(ContextCompat.getColor(mContext, R.color.whiteText));
////
////            break;
//          default:
//            break;
//        }
//        return true;
//      }
//    });
  }

  private void deleteSms(final String id) {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(mContext.getString(R.string.iptab))
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    String empleado = myDB.getEmpleado().get_id();
    final endPoints request = retrofit.create(endPoints.class);
    Call<Mensaje> call = request.deleteMensaje(id + "-" + empleado);
    call.enqueue(new Callback<Mensaje>() {
      @Override
      public void onResponse(@NonNull Call<Mensaje> call, @NonNull Response<Mensaje> response) {
        myDB.deleteSms(id);
      }
      @Override
      public void onFailure(@NonNull Call<Mensaje> call, @NonNull Throwable t) {
        Log.d("Error clear sms", t.getMessage());
      }
    });
  }

  @Override
  public int getItemCount() {
    return mensajes.size();
  }

  @SuppressWarnings("WeakerAccess")
  public class VH extends RecyclerView.ViewHolder {

    ConstraintLayout elpadre;
    TextView txtCheck;
    TextView txtTime;
    TextView txtSender;
    TextView txtSms;
    LinearLayout checkParent;

    TimeZone tz;
    DateFormat df;

    @SuppressLint("SimpleDateFormat")
    public VH(View itemView) {
      super(itemView);

      elpadre  = itemView.findViewById(R.id.elpadre);
      checkParent  = itemView.findViewById(R.id.checkParent);
      txtCheck = itemView.findViewById(R.id.txtCheck);
      txtTime = itemView.findViewById(R.id.txtTime);
      txtSender = itemView.findViewById(R.id.txtSender);
      txtSms = itemView.findViewById(R.id.txtSms);

      Calendar cal = Calendar.getInstance();
      tz = cal.getTimeZone();
      df = new SimpleDateFormat("h:mm a");
      df.setTimeZone(tz);

      Typeface tf = Typeface.createFromAsset(mContext.getAssets(),"fonts/Font Awesome 5 Free-Regular-400.otf");
      txtCheck.setTypeface(tf);
      txtCheck.setText("\uf058");
    }
  }
}