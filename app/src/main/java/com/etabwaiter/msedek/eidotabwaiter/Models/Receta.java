package com.etabwaiter.msedek.eidotabwaiter.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class Receta implements Serializable {
  private String _id;
  private String sku;
  private String item_id;
  private String nombre;
  private String tax_id;
  private String tax_name;
  private String foto_movil;
  private String descripcion;
  private Double precio_receta;
  private String categoria_receta;
  private String sub_categoria_receta;
  private String tax_percentage;
  private Integer cf_cant_guarnicion;
  private ArrayList<Guarnicion> contorno;
  private ArrayList<String> ingredientes;
  private ArrayList<String> cf_temperatura; //BEBIDAS
  private ArrayList<String> cf_cocci_n; //TERMINO
  private ArrayList<String> endulzante;
  private ArrayList<String> cf_lacteos;

  public String get_id() {
    return _id;
  }

  public void set_id(String _id) {
    this._id = _id;
  }

  public String getSku() {
    return sku;
  }

  public void setSku(String sku) {
    this.sku = sku;
  }

  public String getItem_id() {
    return item_id;
  }

  public void setItem_id(String item_id) {
    this.item_id = item_id;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getTax_id() {
    return tax_id;
  }

  public void setTax_id(String tax_id) {
    this.tax_id = tax_id;
  }

  public String getTax_name() {
    return tax_name;
  }

  public void setTax_name(String tax_name) {
    this.tax_name = tax_name;
  }

  public String getFoto_movil() {
    return foto_movil;
  }

  public void setFoto_movil(String foto_movil) {
    this.foto_movil = foto_movil;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public Double getPrecio_receta() {
    return precio_receta;
  }

  public void setPrecio_receta(Double precio_receta) {
    this.precio_receta = precio_receta;
  }

  public String getCategoria_receta() {
    return categoria_receta;
  }

  public void setCategoria_receta(String categoria_receta) {
    this.categoria_receta = categoria_receta;
  }

  public String getSub_categoria_receta() {
    return sub_categoria_receta;
  }

  public void setSub_categoria_receta(String sub_categoria_receta) {
    this.sub_categoria_receta = sub_categoria_receta;
  }

  public String getTax_percentage() {
    return tax_percentage;
  }

  public void setTax_percentage(String tax_percentage) {
    this.tax_percentage = tax_percentage;
  }

  public Integer getCf_cant_guarnicion() {
    return cf_cant_guarnicion;
  }

  public void setCf_cant_guarnicion(Integer cf_cant_guarnicion) {
    this.cf_cant_guarnicion = cf_cant_guarnicion;
  }

  public ArrayList<Guarnicion> getContorno() {
    return contorno;
  }

  public void setContorno(ArrayList<Guarnicion> contorno) {
    this.contorno = contorno;
  }

  public ArrayList<String> getIngredientes() {
    return ingredientes;
  }

  public void setIngredientes(ArrayList<String> ingredientes) {
    this.ingredientes = ingredientes;
  }

  public ArrayList<String> getCf_temperatura() {
    return cf_temperatura;
  }

  public void setCf_temperatura(ArrayList<String> cf_temperatura) {
    this.cf_temperatura = cf_temperatura;
  }

  public ArrayList<String> getCf_cocci_n() {
    return cf_cocci_n;
  }

  public void setCf_cocci_n(ArrayList<String> cf_cocci_n) {
    this.cf_cocci_n = cf_cocci_n;
  }

  public ArrayList<String> getEndulzante() {
    return endulzante;
  }

  public void setEndulzante(ArrayList<String> endulzante) {
    this.endulzante = endulzante;
  }

  public ArrayList<String> getCf_lacteos() {
    return cf_lacteos;
  }

  public void setCf_lacteos(ArrayList<String> cf_lacteos) {
    this.cf_lacteos = cf_lacteos;
  }
}
