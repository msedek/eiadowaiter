package com.etabwaiter.msedek.eidotabwaiter.Models;

public class Pedido {
  private StringBuilder pedido;
  private String date;
  private String id;

  public StringBuilder getPedido() {
    return pedido;
  }

  public void setPedido(StringBuilder pedido) {
    this.pedido = pedido;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
