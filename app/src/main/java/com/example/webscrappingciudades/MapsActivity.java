package com.example.webscrappingciudades;

import androidx.core.os.HandlerCompat;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.webscrappingciudades.AccesoJsoup;
import com.example.webscrappingciudades.Actualizacion;
import com.example.webscrappingciudades.R;
import com.google.android.gms.common.util.concurrent.HandlerExecutor;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback , Actualizacion {

    private GoogleMap mMap;
    private EditText nameciudad;
    private Button buscar;
    private static String nombre;
    private Actualizacion contexto= this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        nameciudad = findViewById(R.id.nameciudad);
        buscar = findViewById(R.id.buscar);

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 nombre= nameciudad.getText().toString();
                Handler mainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper());

                AccesoJsoup hilohandler = new AccesoJsoup(nombre, contexto, mainThreadHandler);
                Thread hilo = new Thread(hilohandler);
                hilo.start();

            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera

    }

    @Override
    public void Actualizar(LatLng LatLong) {
        mMap.addMarker(new MarkerOptions().position(LatLong).title("Marker in" + nombre));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLong,8));

    }
}