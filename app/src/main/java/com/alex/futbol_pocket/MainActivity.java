package com.alex.futbol_pocket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.icu.lang.UCharacter;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    Sensor miAcelerometro;
    TextView tvx,tvy,tvz,pelota;
    float x,y,z=0; // Definen las cordenadas de la pelota
    int metricaX=0; //Tamaño pantalla en X
    int metricaY=0; //Tamaño pantalla en Y

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        tvx = findViewById(R.id.x);
        tvy = findViewById(R.id.y);
        tvz = findViewById(R.id.z);
        pelota = findViewById(R.id.pelota);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Obtener ancho y largo de pantalla
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        metricaX = metrics.widthPixels-20;
        metricaY = metrics.heightPixels-80;
    }

    @Override
    protected void onResume() {
        super.onResume();
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);//Se obtiene el servicio del sensor
        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER); //Se enlistan los sensores acelerometro
        //si la lista de sensores es mas que 0 si hay sensor de tipo acelerometro
        if (sensors.size()>0){
            //Se registra en sensorManager
            sensorManager.registerListener(this,sensors.get(0),sensorManager.SENSOR_DELAY_GAME);
        }
    }

    @Override
    protected void onPause() {
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.unregisterListener(this,miAcelerometro);
        super.onPause();

    }

    @Override
    protected void onStop() {
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.unregisterListener(this,miAcelerometro);
        super.onStop();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //Define el valor de X con limites de pantalla
        if(x>=0 && x<=metricaX){
            pelota.setX(x);
            x+= -5*event.values[SensorManager.DATA_X];
        }else{
            if (x<=0){
                x=0;
            }else{
                x=metricaX;
            }
        }
        //Define el valor de Y con limites de pantalla
        if(y>=-10 && y<=metricaY){
            pelota.setY(y);
            y+= 7*event.values[SensorManager.DATA_Y];
        }else{
            if (y<=-10){
                y=-10;
            }else{
                y=metricaY;
            }
        }

        //No mover
        tvx.setText("X: "+ x);
        tvy.setText("Y: "+ y);
        tvz.setText("Z: "+ event.values[SensorManager.DATA_Z]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
