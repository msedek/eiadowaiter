package com.etabwaiter.msedek.eidotabwaiter.Models;

import java.io.Serializable;

public class Order implements Serializable {
  private String order;
  private boolean cobrado;
  private boolean sent;

  public String getOrder() {
    return order;
  }

  public void setOrder(String order) {
    this.order = order;
  }

  public boolean isCobrado() {
    return cobrado;
  }

  public void setCobrado(boolean cobrado) {
    this.cobrado = cobrado;
  }

  public boolean isSent() {
    return sent;
  }

  public void setSent(boolean sent) {
    this.sent = sent;
  }
}
