package com.example.asincrono;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity  {

    Button btn_execute_AsynTask;
    TextView textoCara;
    ProgressBar barraDeProgreso;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btn_execute_AsynTask = findViewById(R.id.button3);
        barraDeProgreso = findViewById(R.id.progressBar);
        textoCara = findViewById(R.id.caraFeliz);

        dialog = new ProgressDialog(MainActivity.this);


        dialog.setCanceledOnTouchOutside(true);
        btn_execute_AsynTask.setOnClickListener(v-> {
            //se crea un objeto cada que se da click al btn.
            AsynTaskExampl asynTask = new AsynTaskExampl();
            asynTask.execute();
        } );

    }



    //Usando Hilos nativos de JAVA
    void Hilo(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <=5;i++){
                    UnSegundo();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Hey", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }).start();
    }

    //Espera un segundo para hacer algo
    private void UnSegundo() {

            try {
                Thread.sleep(1000);
            }catch (InterruptedException e){}

     }


    //primer parametro es el tipo de entrada doInBackground, segundo parametro es el tipo de dato
    //con el que se actualiza la tarea, ultimo parametro es lo que regresa el postExecute
    private class AsynTaskExampl extends AsyncTask<Void,Integer,Boolean>{

        //OnPreExecute ejecuta en el hilo principal,
        //todoo lo que se ejecute antes de la tarea en segundo plano, VARIABLES O UI
        @Override
        protected void onPreExecute() {
            textoCara.setText("");
            barraDeProgreso.setMax(50);
            barraDeProgreso.setProgress(0);
            dialog.setTitle("Segundos para Easter Egg:");
            dialog.setMessage("5");
            dialog.show();
            //cualquira de las dos opciones
            //dialog.setOnCancelListener(dialog -> cancel(true));
            dialog.setOnDismissListener(dialog1 -> cancel(true));
        }

        //si existen tres puntos como en Void... o Integer...  Significa que puede ser mas de uno

        //lo que se hace en segundo plano el parametro es el primer valor de la clase
        //dentro se puede invocar publishProgress para comunicar al Hilo principal el progreso de la tarea
        @Override
        protected Boolean doInBackground(Void... voids) {

            for (int i = 1; i <=5;i++){
                UnSegundo();
                publishProgress(i*10);
                if (isCancelled()){
                    break;
                }

            }

            return true;
        }

        //se ejecuta en el hilo principal, cando haces la llamada a publishprogress,
        //se prolonga en segundo plano hasta que la tarea termine
        //el int es el valor para actualizar
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            barraDeProgreso.setProgress(values[0].intValue());
            dialog.setMessage(""+(5- (values[0].intValue() /10)));
        }

        //se ejecuta cuando la tarea termina, Un Toast que diga que se termino completamente, recibe el valor desde doInBackground
        @Override
        protected void onPostExecute(Boolean isOK) {
            //super.onPostExecute(isOK);
            if (isOK ){
                dialog.dismiss();
                textoCara.setText(":D");
                Toast.makeText(MainActivity.this, "TERMINADO CON EXITO!!!", Toast.LENGTH_SHORT).show();
            }



        }

        //si se corta la ejecucion del segundo hilo se corre este metodo
        @Override
        protected void onCancelled() {
            super.onCancelled();
            textoCara.setText("TT__TT");
            barraDeProgreso.setProgress(0);
            Toast.makeText(MainActivity.this, "Tarea Interrumpida...", Toast.LENGTH_SHORT).show();

        }


    }


}
