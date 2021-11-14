package com.etabwaiter.msedek.eidotabwaiter.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class Mesa implements Serializable {

  private int pax;
  private String _id;
  private String numeroMesa;
  private String estado;
  private String especial;
  private String ubicacion;
  private String master;
  private ArrayList<Order>orders;
  private ArrayList<Empleado> empleados;


  public String getNumeroMesa() {
    return numeroMesa;
  }

  public void setNumeroMesa(String numeroMesa) {
    this.numeroMesa = numeroMesa;
  }

  public String getEstado() {
    return estado;
  }

  public void setEstado(String estado) {
    this.estado = estado;
  }

  public String getEspecial() {
    return especial;
  }

  public void setEspecial(String especial) {
    this.especial = especial;
  }

  public String getUbicacion() {
    return ubicacion;
  }

  public void setUbicacion(String ubicacion) {
    this.ubicacion = ubicacion;
  }

  public String get_id() {
    return _id;
  }

  public void set_id(String _id) {
    this._id = _id;
  }

  public ArrayList<Empleado> getEmpleados() {
    return empleados;
  }

  public void setEmpleados(ArrayList<Empleado> empleados) {
    this.empleados = empleados;
  }

  public String getMaster() {
    return master;
  }

  public void setMaster(String master) {
    this.master = master;
  }

  public int getPax() {
    return pax;
  }

  public void setPax(int pax) {
    this.pax = pax;
  }


  public ArrayList<Order> getOrders() {
    return orders;
  }

  public void setOrders(ArrayList<Order> orders) {
    this.orders = orders;
  }
}
