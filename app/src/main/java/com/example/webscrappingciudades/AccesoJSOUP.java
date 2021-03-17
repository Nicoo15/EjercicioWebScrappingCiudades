package com.example.webscrappingciudades;

import android.os.Handler;

import com.example.webscrappingciudades.Actualizacion;
import com.google.android.gms.maps.model.LatLng;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;


class AccesoJsoup implements Runnable {
    private static String nombre_ciudad;
    private Actualizacion callback;
    private Handler handler;

    public AccesoJsoup(String nombre_ciudad, Actualizacion callback, Handler handler) {
        this.nombre_ciudad = nombre_ciudad;
        this.callback = callback;
        this.handler = handler;
    }

    //Método explicado por Luis en clase.

    public static LatLng obtenerCoordenadasCiudad() {
        LatLng coordenadas;
        Document doc;
        try {
            doc = Jsoup.connect("https://es.wikipedia.org/wiki/" + nombre_ciudad).get();
            Elements coordLat = doc.getElementsByClass("latitude");
            Elements coordLong = doc.getElementsByClass("longitude");
            String[] latitududes = coordLat.get(0).text().replace('°', ' ')
                    .replace('′', ' ').replace('″', ' ')
                    .split(" ");
            String[] longitudes = coordLong.get(0).text().replace('°', ' ')
                    .replace('′', ' ').replace('″', ' ')
                    .split(" ");
            double latitud = Double.parseDouble(latitududes[0]) + (Double.parseDouble(latitududes[1]) / 60)
                    + (Double.parseDouble(latitududes[2]) / 3600);
            double longuitud = Double.parseDouble(longitudes[0]) + (Double.parseDouble(longitudes[1]) / 60)
                    + (Double.parseDouble(longitudes[2]) / 3600);
            if (latitududes[3].equals("S")) {
                latitud = -latitud;
            }
            if (longitudes[3].equals("O")) {
                longuitud = -longuitud;
            }
            coordenadas = new LatLng(latitud, longuitud);
        } catch (IOException e) {
            e.printStackTrace();
            coordenadas = null;
        }

        return coordenadas;
    }

    @Override
    public void run() {
        LatLng coordenadas = obtenerCoordenadasCiudad();
        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.Actualizar(coordenadas);
            }
        });
    }
}
