package com.github.ayan.weatherapp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class TestWeather {

    public static void main(String[] args) throws Exception{

        TestWeather w = new TestWeather();
        JSONObject obj = w.getWeather("08824","forecast");
        JSONArray list = obj. getJSONArray("list");
        for (int i = 0; i < list.length(); i++) {
            System.out.println(list.get(i));
        }

    }

    public JSONObject getWeather(String zipCode, String type){
        URLConnection weatherConnection = null;
        InputStream weatherInputStream = null;
        BufferedReader weatherBufferedReader = null;
        JSONObject dataKeeper = null;
        try {
            URL currentWeather = new URL("http://api.openweathermap.org/data/2.5/"+type+"?zip="+zipCode+",us&APPID=e0f4d44dd7b3997341fa283d770454c5");
            weatherConnection = currentWeather.openConnection();
            weatherConnection.connect();
            weatherInputStream = weatherConnection.getInputStream();
            weatherBufferedReader = new BufferedReader((new InputStreamReader(weatherInputStream)));

            String data = new String();
            for (String line; (line = weatherBufferedReader.readLine()) != null; data += line);
            if(data != null){
                dataKeeper = new JSONObject(data);
            }
            System.out.println("-----");
            System.out.println(data);
            System.out.println("-----");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if(weatherInputStream!= null) {
                try {
                    weatherInputStream.close();
                } catch (IOException e) {
                }
            }
            if(weatherBufferedReader!= null) {
                try {
                    weatherBufferedReader.close();
                } catch (IOException e) {
                }
            }

        }

        return dataKeeper;

    }

}
