package com.etabwaiter.msedek.eidotabwaiter.Utils;

import com.etabwaiter.msedek.eidotabwaiter.Models.Receta;

import java.util.Comparator;

public class Subcaterator implements Comparator<Receta> {
  public int compare(Receta cmd1, Receta cmd2) {
    int stringResult = cmd1.getCategoria_receta().compareTo(cmd2.getCategoria_receta());
    if (stringResult == 0) {
      // Strings are equal, sort by subcate
      return cmd1.getSub_categoria_receta().compareTo(cmd2.getSub_categoria_receta());
    } else {
      return stringResult;
    }
  }
}
