package com.etabwaiter.msedek.eidotabwaiter.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.etabwaiter.msedek.eidotabwaiter.DetailActivity;
import com.etabwaiter.msedek.eidotabwaiter.Models.Mesa;
import com.etabwaiter.msedek.eidotabwaiter.Models.Receta;
import com.etabwaiter.msedek.eidotabwaiter.R;

import java.io.File;
import java.util.ArrayList;

public class itemsAdapter extends RecyclerView.Adapter<itemsAdapter.itemsAdapterViewHolder> {

  private Context mContext;
  public ArrayList<Receta> myPlatos;
  private Mesa mesa;
  private TextView txtCategory;

  @SuppressWarnings("WeakerAccess")
  public itemsAdapter(Context context, ArrayList<Receta> myPlatos, Mesa mesa, TextView textCategory) {
    this.mContext = context;
    this.myPlatos =  myPlatos;
    this.mesa = mesa;
    this.txtCategory = textCategory;
  }

  @NonNull
  @Override
  public itemsAdapter.itemsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new itemsAdapter.itemsAdapterViewHolder(LayoutInflater.from(mContext).inflate(R.layout.itemrow, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull final itemsAdapter.itemsAdapterViewHolder holder, final int position) {
    final Receta receta = myPlatos.get(position);
    holder.itemName.setText(receta.getNombre());

    if(!txtCategory.getText().toString().equals(receta.getCategoria_receta())) txtCategory.setText(receta.getCategoria_receta());

    if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
      File sdcard = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
      File d1 = new File(sdcard, receta.getFoto_movil());
      Uri uri = Uri.fromFile(d1);
      Glide.with(mContext)
              .load(uri)
              .into(holder.itemPhoto);
    }

    holder.elpadre.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(mContext, DetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("receta", receta);
        intent.putExtra("mesa", mesa);
        mContext.startActivity(intent);
      }
    });
  }

  @Override
  public int getItemCount() {
    return myPlatos.size();
  }

  @SuppressWarnings("WeakerAccess")
  public class itemsAdapterViewHolder extends RecyclerView.ViewHolder {
    FrameLayout elpadre;
    TextView itemName;
    ImageView itemPhoto;

    @SuppressLint("SimpleDateFormat")
    public itemsAdapterViewHolder(View itemView) {
      super(itemView);

      elpadre = itemView.findViewById(R.id.elpadre);
      itemName = itemView.findViewById(R.id.itemName);
      itemPhoto = itemView.findViewById(R.id.itemPhoto);
    }
  }
}
