package com.etabwaiter.msedek.eidotabwaiter.Models;


import java.io.Serializable;

public class Cuentadata implements Serializable {

  private String nombre_plato;
  private Double precio_plato;
  private Boolean boleta;
  private Double subtotl;
  private Integer posit;


  public String getNombre_plato() {
    return nombre_plato;
  }

  public void setNombre_plato(String nombre_plato) {
    this.nombre_plato = nombre_plato;
  }

  public Double getPrecio_plato() {
    return precio_plato;
  }

  public void setPrecio_plato(Double precio_plato) {
    this.precio_plato = precio_plato;
  }

  public Boolean getBoleta() {
    return boleta;
  }

  public void setBoleta(Boolean boleta) {
    this.boleta = boleta;
  }

  public Double getSubtotl() {
    return subtotl;
  }

  public void setSubtotl(Double subtotl) {
    this.subtotl = subtotl;
  }

  public Integer getPosit() {
    return posit;
  }

  public void setPosit(Integer posit) {
    this.posit = posit;
  }
}
