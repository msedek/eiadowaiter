package com.etabwaiter.msedek.eidotabwaiter.Models;

import java.io.Serializable;
import java.util.Date;

public class Mensaje implements Serializable {

  private String remitente;
  private String texto;
  private String estadomensaje;
  private Date fechamensaje;
  private String _id;

  public String getRemitente() {
    return remitente;
  }

  public void setRemitente(String remitente) {
    this.remitente = remitente;
  }

  public String getTexto() {
    return texto;
  }

  public void setTexto(String texto) {
    this.texto = texto;
  }

  public String getEstadomensaje() {
    return estadomensaje;
  }

  public void setEstadomensaje(String estadomensaje) {
    this.estadomensaje = estadomensaje;
  }


  public String get_id() {
    return _id;
  }

  public void set_id(String _id) {
    this._id = _id;
  }


  public Date getFechamensaje() {
    return fechamensaje;
  }

  public void setFechamensaje(Date fechamensaje) {
    this.fechamensaje = fechamensaje;
  }
}