package com.etabwaiter.msedek.eidotabwaiter.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.etabwaiter.msedek.eidotabwaiter.MainOrderActivity;
import com.etabwaiter.msedek.eidotabwaiter.Models.Mesa;
import com.etabwaiter.msedek.eidotabwaiter.R;

import java.util.ArrayList;

public class tablesAdapter extends RecyclerView.Adapter<tablesAdapter.tablesAdapterViewHolder> {

  private Context mContext;
  public ArrayList<Mesa> myTables;
//  private DBHelper myDB;

  public tablesAdapter(Context context, ArrayList<Mesa> myTables) {
    this.mContext = context;
    this.myTables =  myTables;
//    myDB = DBHelper.GetDBHelper(mContext);
  }

  @NonNull
  @Override
  public tablesAdapter.tablesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new tablesAdapter.tablesAdapterViewHolder(LayoutInflater.from(mContext).inflate(R.layout.tablerow, parent, false));
  }

  @SuppressLint("SetTextI18n")
  @Override
  public void onBindViewHolder(@NonNull final tablesAdapter.tablesAdapterViewHolder holder, final int position) {
    final Mesa mesa = myTables.get(position);
    String master = "";
    switch (mesa.getEstado().toLowerCase()) {
      case "ocupada":
        holder.elPadreMesa.setBackground(mContext.getDrawable(R.drawable.tablesocup));
        break;
      case "reservada":
        holder.elPadreMesa.setBackground(mContext.getDrawable(R.drawable.tablesreser));
        break;
      case "por pagar":
        holder.elPadreMesa.setBackground(mContext.getDrawable(R.drawable.tablesporpa));
        break;
      case "por cobrar":
        holder.elPadreMesa.setBackground(mContext.getDrawable(R.drawable.tablesporco));
        break;
      case "master":
        master = "M ";
        holder.elPadreMesa.setBackground(mContext.getDrawable(R.drawable.tableslinked));
        break;
      case "slave":
        holder.elPadreMesa.setBackground(mContext.getDrawable(R.drawable.tableslinked));
        break;
      default:
        holder.elPadreMesa.setBackground(mContext.getDrawable(R.drawable.tables));
        break;
    }
    holder.tableNumber.setText(master + mesa.getNumeroMesa());

    holder.elpadre.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(mesa.getEstado().toLowerCase().equals("slave")) {
          showMessage("SELECCIONE MESA MASTER");
        } else {
          Intent intent = new Intent(mContext, MainOrderActivity.class);
          intent.putExtra("mesa", mesa.get_id());
          mContext.startActivity(intent);
        }
      }
    });
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
    }, 500);
  }

  @Override
  public int getItemCount() {
    return myTables.size();
  }

  @SuppressWarnings("WeakerAccess")
  public class tablesAdapterViewHolder extends RecyclerView.ViewHolder {

    ConstraintLayout elpadre;
    TextView tableNumber;
    LinearLayout elPadreMesa;

    @SuppressLint("SimpleDateFormat")
    public tablesAdapterViewHolder(View itemView) {
      super(itemView);

      elpadre = itemView.findViewById(R.id.elpadre);
      tableNumber = itemView.findViewById(R.id.tableNumber);
      elPadreMesa = itemView.findViewById(R.id.elPadreMesa);
    }
  }
}
