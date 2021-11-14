package com.etabwaiter.msedek.eidotabwaiter.Models;

import java.io.Serializable;

public class Guarnicion implements Serializable {
  private String name;
  private String sku;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSku() {
    return sku;
  }

  public void setSku(String sku) {
    this.sku = sku;
  }
}
