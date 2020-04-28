package com.pradeesh.knowcovid;

import android.car.Car;
import android.car.CarNotConnectedException;
import android.car.hardware.CarSensorEvent;
import android.car.hardware.CarSensorManager;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pradeesh.knowcovid.model.VehicleEventMessage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.greenrobot.eventbus.EventBus;


import static android.car.hardware.CarSensorManager.*;

import static com.pradeesh.knowcovid.utils.Constant.VEH_IGNITION_STATUS_OFF;
import static com.pradeesh.knowcovid.utils.Constant.VEH_IGNITION_STATUS_ON;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private Car car;
    private final String[] permissions = new String[]{"android.car.permission.CAR_SPEED", "permission:android.car.permission.CAR_POWERTRAIN"};

    private VehicleEventMessage vehicleEventMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard,
                R.id.navigation_map, R.id.rss_feed)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);

        initVehicleConnectivity();

        vehicleEventMessage = new VehicleEventMessage();
    }

    //Testing device or emulator should support automotive feature - Used Automotive powered emulator for validation
    private final void initVehicleConnectivity() {
        if (this.getPackageManager().hasSystemFeature("android.hardware.type.automotive")) {
            if (this.car == null) {
                car = Car.createCar(this, (ServiceConnection) (new ServiceConnection() {

                    @Override
                    public void onServiceConnected(ComponentName name, IBinder service) {
                        Log.v(LOG_TAG, "Vehicle Service Connected!");
                        onCarServiceReady();
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName name) {
                        Log.v(LOG_TAG, "Vehicle Service Disconnected!");
                    }
                }));
            }
        }
    }

    private final void onCarServiceReady() {
        try {
            CarSensorManager carSensorManager = (CarSensorManager) car.getCarManager(Car.SENSOR_SERVICE);
            //Last state was not
            this.watchSpeedSensor(carSensorManager);
            this.watchIgnitionStatus(carSensorManager);
        } catch (CarNotConnectedException e) {
            Log.v(LOG_TAG, "Vehicle Service NOT Connected!");
            e.printStackTrace();
        }
    }

    private final void watchSpeedSensor(CarSensorManager carSensorManager) throws CarNotConnectedException {

        carSensorManager.registerListener(new OnSensorChangedListener() {
                                              @Override
                                              public void onSensorChanged(CarSensorEvent carSensorEvent) {
                                                  vehicleEventMessage.setSpeedValue((int) carSensorEvent.floatValues[0]);
                                                  EventBus.getDefault().post(vehicleEventMessage);
                                              }
                                          },
                CarSensorManager.SENSOR_TYPE_CAR_SPEED,
                CarSensorManager.SENSOR_RATE_NORMAL);
    }

    private final void watchIgnitionStatus(CarSensorManager carSensorManager) throws CarNotConnectedException {

        carSensorManager.registerListener(new OnSensorChangedListener() {
                                              @Override
                                              public void onSensorChanged(CarSensorEvent carSensorEvent) {
                                                  Log.v(LOG_TAG, "Ignition val : " + carSensorEvent.intValues[0]);
                                                  if (carSensorEvent.intValues[0] == CarSensorEvent.IGNITION_STATE_ON) {
                                                      vehicleEventMessage.setIgnitionStatus(VEH_IGNITION_STATUS_ON);
                                                      EventBus.getDefault().post(vehicleEventMessage);
                                                  }else{
                                                      vehicleEventMessage.setIgnitionStatus(VEH_IGNITION_STATUS_OFF);
                                                  }
                                              }
                                          },
                CarSensorManager.SENSOR_TYPE_IGNITION_STATE,
                CarSensorManager.SENSOR_RATE_NORMAL);
    }

    public boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (permissions[0] == Car.PERMISSION_SPEED && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (!car.isConnected() && !car.isConnecting()) {
                car.connect();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (checkSelfPermission(permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            if (!car.isConnected() && !car.isConnecting()) {
                car.connect();
            }
        } else {
            requestPermissions(permissions, 0);
        }
    }

    @Override
    protected void onPause() {
        if (car.isConnected()) {
            car.disconnect();
        }
        super.onPause();
    }
}
