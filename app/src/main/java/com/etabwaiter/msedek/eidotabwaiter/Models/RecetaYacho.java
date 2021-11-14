package com.etabwaiter.msedek.eidotabwaiter.Models;

import java.io.Serializable;
import java.util.ArrayList;


public class RecetaYacho implements Serializable {
  private String item_id;
  private String name;
  private String sku;
  private String tax_name;
  private String  description;
  private String tax_id;
  private String tax_percentage;
  private Double precio_receta;
  private String  cf_familia;
  private String cf_subfamilia;
  private String image_name;
  private Integer cf_cant_guarnicion;
  private ArrayList<String> cf_temperatura; //BEBIDAS
  private ArrayList<String> endulzante;
  private ArrayList<String> cf_lacteos;
  private ArrayList<String> cf_cocci_n; //TERMINO
  private ArrayList<Guarnicion> cf_guarnicion;
  private ArrayList<String> cf_ingredientes;

  public String getItem_id() {
    return item_id;
  }

  public void setItem_id(String item_id) {
    this.item_id = item_id;
  }

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

  public String getTax_name() {
    return tax_name;
  }

  public void setTax_name(String tax_name) {
    this.tax_name = tax_name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getTax_id() {
    return tax_id;
  }

  public void setTax_id(String tax_id) {
    this.tax_id = tax_id;
  }

  public String getTax_percentage() {
    return tax_percentage;
  }

  public void setTax_percentage(String tax_percentage) {
    this.tax_percentage = tax_percentage;
  }

  public Double getPrecio_receta() {
    return precio_receta;
  }

  public void setPrecio_receta(Double precio_receta) {
    this.precio_receta = precio_receta;
  }

  public String getCf_familia() {
    return cf_familia;
  }

  public void setCf_familia(String cf_familia) {
    this.cf_familia = cf_familia;
  }

  public String getCf_subfamilia() {
    return cf_subfamilia;
  }

  public void setCf_subfamilia(String cf_subfamilia) {
    this.cf_subfamilia = cf_subfamilia;
  }

  public String getImage_name() {
    return image_name;
  }

  public void setImage_name(String image_name) {
    this.image_name = image_name;
  }

  public Integer getCf_cant_guarnicion() {
    return cf_cant_guarnicion;
  }

  public void setCf_cant_guarnicion(Integer cf_cant_guarnicion) {
    this.cf_cant_guarnicion = cf_cant_guarnicion;
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

  public ArrayList<Guarnicion> getCf_guarnicion() {
    return cf_guarnicion;
  }

  public void setCf_guarnicion(ArrayList<Guarnicion> cf_guarnicion) {
    this.cf_guarnicion = cf_guarnicion;
  }

  public ArrayList<String> getCf_ingredientes() {
    return cf_ingredientes;
  }

  public void setCf_ingredientes(ArrayList<String> cf_ingredientes) {
    this.cf_ingredientes = cf_ingredientes;
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
