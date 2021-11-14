package com.etabwaiter.msedek.eidotabwaiter.Utils;


import com.etabwaiter.msedek.eidotabwaiter.Models.Receta;

import java.util.Comparator;

public class CategoryComparator implements Comparator<Receta> {
  @Override
  public int compare(Receta o1, Receta o2) {
    return o1.getCategoria_receta().compareTo(o2.getCategoria_receta());
  }
}
