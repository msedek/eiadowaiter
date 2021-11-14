package com.etabwaiter.msedek.eidotabwaiter.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class Comanda implements Serializable {

  private String mesa;
  private String mesaEstado;
  private String numeroMesa;
  private String fondoId;
  private int pax;
  private ArrayList<String> orders;
  private boolean enviado;
  private Empleado empleado;

  public boolean isEnviado() {
    return enviado;
  }

  public void setEnviado(boolean enviado) {
    this.enviado = enviado;
  }

  public ArrayList<String> getOrders() {
    return orders;
  }

  public void setOrders(ArrayList<String> orders) {
    this.orders = orders;
  }

  public String getMesa() {
    return mesa;
  }

  public void setMesa(String mesa) {
    this.mesa = mesa;
  }

  public Empleado getEmpleado() {
    return empleado;
  }

  public void setEmpleado(Empleado empleado) {
    this.empleado = empleado;
  }


  public String getNumeroMesa() {
    return numeroMesa;
  }

  public void setNumeroMesa(String numeroMesa) {
    this.numeroMesa = numeroMesa;
  }

  public String getMesaEstado() {
    return mesaEstado;
  }

  public void setMesaEstado(String mesaEstado) {
    this.mesaEstado = mesaEstado;
  }

  public String getFondoId() {
    return fondoId;
  }

  public void setFondoId(String fondoId) {
    this.fondoId = fondoId;
  }

  public int getPax() {
    return pax;
  }

  public void setPax(int pax) {
    this.pax = pax;
  }
}