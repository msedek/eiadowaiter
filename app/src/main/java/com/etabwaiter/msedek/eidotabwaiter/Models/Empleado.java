package com.etabwaiter.msedek.eidotabwaiter.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class Empleado implements Serializable {

  private String _id;
  private String contact_id;
  private String contact_name;
  private String customer_name;
  private String  vendor_name;
  private String cf_ruc_cliente;
  private String cf_dni_cliente;
  private String company_name;
  private String contact_type;
  private String contact_type_formatted;
  private String first_name;
  private String last_name;
  private String email;
  private String cf_direccion_cliente;
  private boolean cf_empleado;
  private String cf_cargo;
  private String cf_clave_de_usuario;
  private String horaEntrada;
  private String horaSalida;
  private boolean logged;
  private String fondoId;
  private ArrayList<Mensaje> mensajes;

  public String get_id() {
    return _id;
  }

  public void set_id(String _id) {
    this._id = _id;
  }

  public String getContact_id() {
    return contact_id;
  }

  public void setContact_id(String contact_id) {
    this.contact_id = contact_id;
  }

  public String getContact_name() {
    return contact_name;
  }

  public void setContact_name(String contact_name) {
    this.contact_name = contact_name;
  }

  public String getCustomer_name() {
    return customer_name;
  }

  public void setCustomer_name(String customer_name) {
    this.customer_name = customer_name;
  }

  public String getVendor_name() {
    return vendor_name;
  }

  public void setVendor_name(String vendor_name) {
    this.vendor_name = vendor_name;
  }

  public String getCf_ruc_cliente() {
    return cf_ruc_cliente;
  }

  public void setCf_ruc_cliente(String cf_ruc_cliente) {
    this.cf_ruc_cliente = cf_ruc_cliente;
  }

  public String getCf_dni_cliente() {
    return cf_dni_cliente;
  }

  public void setCf_dni_cliente(String cf_dni_cliente) {
    this.cf_dni_cliente = cf_dni_cliente;
  }

  public String getCompany_name() {
    return company_name;
  }

  public void setCompany_name(String company_name) {
    this.company_name = company_name;
  }

  public String getContact_type() {
    return contact_type;
  }

  public void setContact_type(String contact_type) {
    this.contact_type = contact_type;
  }

  public String getContact_type_formatted() {
    return contact_type_formatted;
  }

  public void setContact_type_formatted(String contact_type_formatted) {
    this.contact_type_formatted = contact_type_formatted;
  }

  public String getFirst_name() {
    return first_name;
  }

  public void setFirst_name(String first_name) {
    this.first_name = first_name;
  }

  public String getLast_name() {
    return last_name;
  }

  public void setLast_name(String last_name) {
    this.last_name = last_name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getCf_direccion_cliente() {
    return cf_direccion_cliente;
  }

  public void setCf_direccion_cliente(String cf_direccion_cliente) {
    this.cf_direccion_cliente = cf_direccion_cliente;
  }

  public String getCf_cargo() {
    return cf_cargo;
  }

  public void setCf_cargo(String cf_cargo) {
    this.cf_cargo = cf_cargo;
  }

  public String getCf_clave_de_usuario() {
    return cf_clave_de_usuario;
  }

  public void setCf_clave_de_usuario(String cf_clave_de_usuario) {
    this.cf_clave_de_usuario = cf_clave_de_usuario;
  }

  public String getHoraEntrada() {
    return horaEntrada;
  }

  public void setHoraEntrada(String horaEntrada) {
    this.horaEntrada = horaEntrada;
  }

  public String getHoraSalida() {
    return horaSalida;
  }

  public void setHoraSalida(String horaSalida) {
    this.horaSalida = horaSalida;
  }

  public boolean isLogged() {
    return logged;
  }

  public void setLogged(boolean logged) {
    this.logged = logged;
  }

  public String getFondoId() {
    return fondoId;
  }

  public void setFondoId(String fondoId) {
    this.fondoId = fondoId;
  }

  public boolean isCf_empleado() {
    return cf_empleado;
  }

  public void setCf_empleado(boolean cf_empleado) {
    this.cf_empleado = cf_empleado;
  }

  public ArrayList<Mensaje> getMensajes() {
    return mensajes;
  }

  public void setMensajes(ArrayList<Mensaje> mensajes) {
    this.mensajes = mensajes;
  }
}
