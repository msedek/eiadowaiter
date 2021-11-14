package com.etabwaiter.msedek.eidotabwaiter;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.etabwaiter.msedek.eidotabwaiter.Interfaz.endPoints;
import com.etabwaiter.msedek.eidotabwaiter.Models.Empleado;
import com.etabwaiter.msedek.eidotabwaiter.Models.Menua;
import com.etabwaiter.msedek.eidotabwaiter.Models.Receta;
import com.etabwaiter.msedek.eidotabwaiter.Models.RecetaYacho;
import com.etabwaiter.msedek.eidotabwaiter.SQlite.DBHelper;
import com.etabwaiter.msedek.eidotabwaiter.Utils.CategoryComparator;
import com.karan.churi.PermissionManager.PermissionManager;
import com.owncloud.android.lib.common.OwnCloudClient;
import com.owncloud.android.lib.common.OwnCloudClientFactory;
import com.owncloud.android.lib.common.OwnCloudCredentialsFactory;
import com.owncloud.android.lib.common.network.OnDatatransferProgressListener;
import com.owncloud.android.lib.common.operations.OnRemoteOperationListener;
import com.owncloud.android.lib.common.operations.RemoteOperation;
import com.owncloud.android.lib.common.operations.RemoteOperationResult;
import com.owncloud.android.lib.resources.files.DownloadRemoteFileOperation;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity implements OnRemoteOperationListener, OnDatatransferProgressListener {

  PermissionManager permissionManager;

  EditText dni;
  EditText password;
  Button login;
  boolean isGettingImage;
  boolean gotError;
  Menua menua;

  boolean canLog;

  ArrayList<String> denied;

  DBHelper myDB;

  String username;
  String passwordOwn;
  File downloadfolder;
  private OwnCloudClient mClient;
  private Handler mHandler = new Handler();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    denied = new ArrayList<>();
    permissionManager = new PermissionManager() {};
    permissionManager.checkAndRequestPermissions(this);

    dni = findViewById(R.id.dniEdt);
    password = findViewById(R.id.passwordEdt);
    login = findViewById(R.id.btnLogin);

    boolean permden = denied.size() > 0;

    if(permden) {
      showMessage("ACTIVE PERMISOS MANUALMENTE EN LA CONFIGURACION");
    } else {
      iniSetup();
      loadRecetas();
    }
  }

  private void loadRecetas() {
    gotError =false;
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(getString(R.string.iptab))
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    endPoints request = retrofit.create(endPoints.class);
    Call<ArrayList<RecetaYacho>> call = request.getJSONRecetas();
    call.enqueue(new Callback<ArrayList<RecetaYacho>>() {
      @Override
      public void onResponse(@NonNull Call<ArrayList<RecetaYacho>> call, @NonNull Response<ArrayList<RecetaYacho>> response) {
        if(response.code()==200) {
          ArrayList<RecetaYacho> dataYacho = response.body();
          ArrayList<Receta> data = new ArrayList<>();
          assert dataYacho != null;
          for (RecetaYacho recetaYacho: dataYacho) {
            Receta receta = new Receta();
            receta.setSku(recetaYacho.getSku());
            receta.setItem_id(recetaYacho.getItem_id());
            receta.setNombre(recetaYacho.getName());
            receta.setTax_id(recetaYacho.getTax_id());
            receta.setTax_name(recetaYacho.getTax_name());
            receta.setFoto_movil(recetaYacho.getImage_name());
            receta.setDescripcion(recetaYacho.getDescription());
            receta.setPrecio_receta(recetaYacho.getPrecio_receta());
            receta.setCategoria_receta(recetaYacho.getCf_familia());
            receta.setTax_percentage(recetaYacho.getTax_percentage());
            receta.setSub_categoria_receta(recetaYacho.getCf_subfamilia());
            receta.setCf_cant_guarnicion(recetaYacho.getCf_cant_guarnicion());
            receta.setContorno(recetaYacho.getCf_guarnicion());
            receta.setCf_cocci_n(recetaYacho.getCf_cocci_n());
            receta.setIngredientes(recetaYacho.getCf_ingredientes());
            receta.setCf_temperatura(recetaYacho.getCf_temperatura());
            receta.setEndulzante(recetaYacho.getEndulzante());
            receta.setCf_lacteos(recetaYacho.getCf_lacteos());
            data.add(receta);
          }
          myDB.deleteAllReceta();
          myDB.deleteAllSms();
          myDB.deleteAllMesa();
          Collections.sort(data, new CategoryComparator());
          for (Receta receta : data) {
            myDB.addReceta(receta);
            File file = new File(String.valueOf(downloadfolder), receta.getSku() + ".jpg");
            if(!file.exists()) {
              startDownload(receta.getSku() + ".jpg", downloadfolder);
            }
          }
          isGettingImage = true;
        } else {
          showMessage("A ocurrido un problema en comunicacion intentando de nuevo");
          gotError = true;
        }
      }
      @Override
      public void onFailure(@NonNull Call<ArrayList<RecetaYacho>> call, @NonNull Throwable t) {
        Log.i("Error from Receta: " , t.getMessage());
      }
    });
  }

  private void getEmpleado(String dni) {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(getString(R.string.iptab))
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    endPoints request = retrofit.create(endPoints.class);
    Call<ArrayList<Empleado>> call = request.getJSONEmpleado(dni);
    call.enqueue(new Callback<ArrayList<Empleado>>() {
      @Override
      public void onResponse(@NonNull Call<ArrayList<Empleado>> call, @NonNull Response<ArrayList<Empleado>> response) {
        myDB.deleteAllEmpleado();
        assert response.body() != null;
        if(response.body().size() > 0) {
          Empleado empleado = response.body().get(0);
          if(empleado.getCf_cargo().toLowerCase().equals("mozo") ||empleado.getCf_cargo().toLowerCase().contains("gere") ) {
            if(empleado.getCf_clave_de_usuario().toLowerCase().equals(password.getText().toString().toLowerCase().trim())) {
              logMozo(empleado.get_id());
            } else {
              showMessage("Clave Invalida intente de nuevo");
            }
          } else {
            showMessage("NIVEL DE ACCESO INVALIDO");
          }
        } else {
          showMessage("Empleado no registrado");
        }
      }
      @Override
      public void onFailure(@NonNull Call<ArrayList<Empleado>> call, @NonNull Throwable t) {
        Log.i("Error from Empleado: " , t.getMessage());
        showMessage("No hay Conexion con el servidor");
        gotError = true;
      }
    });
  }

  private void logMozo(String id) {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(getString(R.string.iptab))
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    endPoints request = retrofit.create(endPoints.class);
    Call<Empleado> call = request.logEmpleado(id);
    call.enqueue(new Callback<Empleado>() {
      @Override
      public void onResponse(@NonNull Call<Empleado> call, @NonNull Response<Empleado> response) {
        if(response.code()==200) {
          if(isGettingImage) {
            myDB.addEmpleado(response.body());
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
          } else {
            showMessage("OBTENIENDO MENU POR FAVOR ESPERE");
            if(gotError) loadRecetas();
          }
        } else if(response.code()==401) {
          showMessage("NO HAY CAJERO");
        } else {
          showMessage("Problemas de conexion con el server");
        }
      }
      @Override
      public void onFailure(@NonNull Call<Empleado> call, @NonNull Throwable t) {
        Log.i("Error from LogEmp: " , t.getMessage());
      }
    });
  }

  private void iniSetup() {
    myDB = DBHelper.GetDBHelper(this);
    username = getString(R.string.username);
    passwordOwn = getString(R.string.passwordOwn);
    Uri serverUri = Uri.parse(getString(R.string.iptab2) + "owncloud");
    canLog = false;

    downloadfolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    mClient = OwnCloudClientFactory.createOwnCloudClient(serverUri, this, true);
    mClient.setCredentials(OwnCloudCredentialsFactory.newBasicCredentials(username, passwordOwn));

    dni.requestFocus();
    isGettingImage = false;
    gotError = false;
    menua = new Menua();
    myDB.deleteAllMesa();
    myDB.deleteAllSms();
    myDB.deleteAllEmpleado();

    login.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (isNetworkConnected()) {
          if(dni.getText().toString().equals("")) {
            showMessage("Coloque su DNI");
          } else if(password.getText().toString().equals("")) {
            showMessage("Coloque su password");
          } else {
            getEmpleado(dni.getText().toString());
          }
        } else {
          showMessage("WIFI DESCONECTADO");
        }
      }
    });
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (getIntent().getBooleanExtra("EXIT", false)) {
      MainActivity main = new MainActivity();
      main.finish();
    }
  }

  private void showMessage(String mensaje) {
    final Toast toast = Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT);
    toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
    toast.show();
    Handler handler = new Handler();
    handler.postDelayed(new Runnable() {
      @Override
      public void run() {
        toast.cancel();
      }
    }, 800);
  }

  private boolean isNetworkConnected() {
    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    assert cm != null;
    return cm.getActiveNetworkInfo() != null;
  }

  private void startDownload(String filePath, File targetDirectory) {
    DownloadRemoteFileOperation downloadOperation = new DownloadRemoteFileOperation(filePath, targetDirectory.getAbsolutePath() + "/");
    downloadOperation.addDatatransferProgressListener(this);
    downloadOperation.execute( mClient, this, mHandler);
  }

  @Override
  public void onTransferProgress(long l, long l1, long l2, String s) {
  }

  @Override
  public void onRemoteOperationFinish(RemoteOperation remoteOperation, RemoteOperationResult remoteOperationResult) {
    if (remoteOperation instanceof DownloadRemoteFileOperation) {
      if (remoteOperationResult.isSuccess()) {
        Log.i("OwncloudRemote", "onRemoteOperationFinish: success");
      }
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    permissionManager.checkResult(requestCode,permissions,grantResults);
//    ArrayList<String> granted = permissionManager.getStatus().get(0).granted;
//    ArrayList<String> denied = permissionManager.getStatus().get(0).denied;
  }
}
