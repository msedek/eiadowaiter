package com.etabwaiter.msedek.eidotabwaiter.Models;

import java.io.Serializable;

public class ConfigPosition implements Serializable {

  private String categoria;
  private Integer position;

  public Integer getPosition() {
    return position;
  }

  public void setPosition(Integer position) {
    this.position = position;
  }

  public String getCategoria() {
    return categoria;
  }

  public void setCategoria(String categoria) {
    this.categoria = categoria;
  }

}
