package com.etabwaiter.msedek.eidotabwaiter.Interfaz;

import com.etabwaiter.msedek.eidotabwaiter.Models.ClearTable;
import com.etabwaiter.msedek.eidotabwaiter.Models.Empleado;
import com.etabwaiter.msedek.eidotabwaiter.Models.Marchar;
import com.etabwaiter.msedek.eidotabwaiter.Models.Mensaje;
import com.etabwaiter.msedek.eidotabwaiter.Models.Menua;
import com.etabwaiter.msedek.eidotabwaiter.Models.Mesa;
import com.etabwaiter.msedek.eidotabwaiter.Models.RecetaYacho;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface endPoints {

  @GET("api/mesasMobile")
  Call<ArrayList<Mesa>> getJSONMesas();

  @GET("api/mesas/{id}")
  Call<Mesa> getMesaById(@Path("id") String mesaId);

  @DELETE("api/mensajes/{id}")
  Call<Mensaje> deleteMensaje(@Path("id") String mensajeId);

  @GET("api/empleados/{id}")
  Call<ArrayList<Empleado>> getJSONEmpleado(@Path("id") String empleadoId);

  @PUT("api/logInEmpleado/{id}")
  Call<Empleado> logEmpleado(@Path("id") String empleadoId);

  @PUT("api/logOutEmpleado/{id}")
  Call<Empleado> logOutEmpleado(@Path("id") String empleadoId);

//  @PUT("api/cleartable/{id}")
//  Call<ClearTable> clearTable(@Path("id") String id, @Body ClearTable clearTable);

//  @GET("api/recetasMobile")
//  Call<ArrayList<Receta>> getJSONRecetas();

  @GET("api/recetasYacho")
  Call<ArrayList<RecetaYacho>> getJSONRecetas();

//  @GET("api/menues")
//  Call<ArrayList<Menua>> getJSONMenues();

  @POST("api/comandas")
  Call<Object> addComanda(@Body Object object);

  @POST("api/marchar")
  Call<Marchar> sendMarchar(@Body Marchar marchar);
}
