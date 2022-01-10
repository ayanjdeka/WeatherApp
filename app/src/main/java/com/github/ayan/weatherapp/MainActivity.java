package com.github.ayan.weatherapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText zipCodeInput;
    TextView currentWeatherData;
    TextView currentWeatherDescription;
    TextView firstDayForecast;
    TextView secondDayForecast;
    TextView thirdDayForecast;
    TextView fourthDayForecast;
    TextView location;

    ImageView currentWeatherPicture;
    ImageView oneDayWeatherPicture;
    ImageView twoDayWeatherPicture;
    ImageView threeDayWeatherPicture;
    ImageView fourDayWeatherPicture;

    Button getWeather;

    String rain = "Storm is using her powers as there is rain coming.";
    String clear = "Superman is using his solar energy as today is a sunny day." ;
    String clouds = "It is going to be cloudy as Storm is about to use her powers." ;
    String snow = "Iceman is in town as it is going to snow today.";
    String thunderstorm = "The superhero Black Lightning is setting predicted thunderstorms.";
    String mist = "Aquaman is out of the sea as it is misty.";
    String drizzle = "Aquaman's wife is causing a drizzle.";
    String rainAndSnow = "Iceman and Aquaman are together since rain and snow are predicted.";
    String tornado = "Red Tornado is casting his tornadoes today";
    String smoke = "The Human Torch remnants will be observed with smoke in the air";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        zipCodeInput = findViewById(R.id.id_editText_zipCodeText);
        currentWeatherData = findViewById(R.id.id_textView_data);
        currentWeatherDescription = findViewById(R.id.id_textView_description);
        firstDayForecast = findViewById(R.id.id_textView_oneDayForecast);
        secondDayForecast = findViewById(R.id.id_textView_secondDayForecast);
        thirdDayForecast = findViewById(R.id.id_textView_thirdDayForecast);
        fourthDayForecast = findViewById(R.id.id_textView_fourthDayForecast);
        location = findViewById(R.id.id_textView_location);
        currentWeatherPicture = findViewById(R.id.id_imageView_currentWeatherPicture);
        oneDayWeatherPicture = findViewById(R.id.id_imageView_oneDayImage);
        twoDayWeatherPicture = findViewById(R.id.id_imageView_secondDayForecast);
        threeDayWeatherPicture = findViewById(R.id.id_imageView_thirdDayForecast);
        fourDayWeatherPicture = findViewById(R.id.id_imageView_fourthDay);
        getWeather = findViewById(R.id.id_button_getWeather);

        zipCodeInput.setText("08824");

        getWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String zipCode = zipCodeInput.getText().toString();
                        try {
                            Integer.parseInt(zipCode);
                            System.out.println("Selecting Zipcode: "+zipCodeInput.getText().toString());
                            new getCurrentWeatherThread().execute(zipCodeInput.getText().toString());
                            new getForeCastWeather().execute(zipCodeInput.getText().toString());
                        }catch (Exception e){
                            Toast toast = Toast.makeText(getApplicationContext(), "Please enter a valid zip code", Toast.LENGTH_SHORT);
                            toast.show();
                        }

                    }
                });

            }
        });




    }


    public class getCurrentWeatherThread extends AsyncTask<String,Void,Void> {

        JSONObject obj = null;

        @Override
        protected Void doInBackground(String... strings) {

            obj = getWeather(strings[0], "weather");
            return null;

        }



        protected void onPostExecute(Void aVoid) {

            JSONObject main = null;
            JSONArray weather = null;


            try {
                main = obj.getJSONObject("main");
                weather = obj.getJSONArray("weather");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {

                int temperature = (int) Math.round(convertToDouble(main.getString("temp")));
                int minTemperature = (int) Math.round(convertToDouble(main.getString("temp_min")));
                int maxTemperature = (int) Math.round(convertToDouble(main.getString("temp_max")));

                currentWeatherData.setText("Current Weather: " + Integer.toString(temperature) + " F" + "\n" + "Minimum Temperature: " + Integer.toString(minTemperature) + " F" + "\n" + "Maximum Temperature: " + Integer.toString(maxTemperature) + " F");
                JSONObject object = weather.getJSONObject(0);
                location.setText(obj.getString("name"));

                if (object.getString("main").equals("Rain")) {
                    currentWeatherDescription.setText(rain);
                    currentWeatherPicture.setImageResource(R.drawable.rain);
                }

                if (object.getString("main").equals("Clear")) {
                    currentWeatherDescription.setText(clear);
                    currentWeatherPicture.setImageResource(R.drawable.sunny);
                }

                if (object.getString("main").equals("Snow")) {
                    if (object.getString("description").equals("rain and snow")) {
                        currentWeatherDescription.setText(rainAndSnow);
                        currentWeatherPicture.setImageResource(R.drawable.rainsnow);
                    }

                    currentWeatherPicture.setImageResource(R.drawable.snow);
                    currentWeatherDescription.setText(snow);
                }

                if (object.getString("main").equals("Thunderstorm")) {
                    currentWeatherDescription.setText(thunderstorm);
                    currentWeatherPicture.setImageResource(R.drawable.thunderstorm);
                }

                if (object.getString("main").equals("Drizzle")) {
                    currentWeatherPicture.setImageResource(R.drawable.drizzle);
                    currentWeatherDescription.setText(drizzle);
                }


                if (object.getString("main").equals("Clouds")) {
                    currentWeatherDescription.setText(clouds);
                    currentWeatherPicture.setImageResource(R.drawable.clouds);
                }

                if (object.getString("description").equals("mist")) {
                    currentWeatherPicture.setImageResource(R.drawable.mist);
                    currentWeatherDescription.setText(mist);
                }

                if (object.getString("description").equals("smoke")) {
                    currentWeatherDescription.setText(smoke);
                    currentWeatherPicture.setImageResource(R.drawable.smoke);
                }

                if (object.getString("description").equals("tornado")) {
                    currentWeatherPicture.setImageResource(R.drawable.tornado);
                    currentWeatherDescription.setText(tornado);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    public class getForeCastWeather extends AsyncTask<String,Void,Void> {

        JSONObject obj = null;


        @Override
        protected Void doInBackground(String... strings) {
            obj = getWeather(strings[0], "forecast");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            JSONArray list = null;


            ArrayList<String> dateData = new ArrayList<>();
            ArrayList<String> mainData = new ArrayList<>();
            ArrayList<String> descriptionData = new ArrayList<>();
            ArrayList<Integer> minTempData = new ArrayList<>();
            ArrayList<Integer> maxTempData = new ArrayList<>();
            int count = 0;
            try {
                list = obj.getJSONArray("list");
                for (int i = 0; i < list.length(); i++) {
                    JSONObject listObj = list.getJSONObject(i);
                    JSONObject main = listObj.getJSONObject("main");
                    JSONObject weather = listObj.getJSONArray("weather").getJSONObject(0);
                    String dateValue = listObj.getString("dt_txt");

                    String minTempValue = main.getString("temp_min");
                    String maxTempValue = main.getString("temp_max");

                    String mainWeather = weather.getString("main");
                    String description = weather.getString("description");


                    if(dateValue.contains("18:00:00") && count<4) {
                        dateValue = dateValue.replace("18:","1:");
                        dateData.add(dateValue);
                        int minFahrenheit = (int)Math.round(convertToDouble(minTempValue));
                        int maxFahrenheit = (int)Math.round(convertToDouble(maxTempValue));
                        minTempData.add(minFahrenheit);
                        maxTempData.add(maxFahrenheit);
                        mainData.add(mainWeather);
                        descriptionData.add(description);

                        count++;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            firstDayForecast.setText("Date: "+mainData.get(0)+" PM"+"\n"+ "Max Temperature: "+maxTempData.get(0)+" F"+"\n" +"Min Temperature: "+minTempData.get(0)+" F");
            secondDayForecast.setText("Date: "+dateData.get(1)+" PM"+"\n"+ "Max Temperature: "+maxTempData.get(1)+" F"+"\n" +"Min Temperature: "+minTempData.get(1)+" F");
            thirdDayForecast.setText("Date: "+dateData.get(2)+" PM"+"\n"+ "Max Temperature: "+maxTempData.get(2)+" F"+"\n" +"Min Temperature: "+minTempData.get(2)+" F");
            fourthDayForecast.setText("Date: "+dateData.get(3)+" PM"+"\n"+ "Max Temperature: "+maxTempData.get(3)+" F"+"\n" +"Min Temperature: "+minTempData.get(3)+" F");

            oneDayWeatherPicture.setImageResource(setImageRes(mainData.get(0), descriptionData.get(0)));
            twoDayWeatherPicture.setImageResource(setImageRes(mainData.get(1), descriptionData.get(1)));
            threeDayWeatherPicture.setImageResource(setImageRes(mainData.get(2),descriptionData.get(2)));
            fourDayWeatherPicture.setImageResource(setImageRes(mainData.get(3),descriptionData.get(3)));

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


    public Double convertToDouble(String str){
        Double fahrenheit = Double.parseDouble(str);
        fahrenheit -=273.15;
        fahrenheit*=1.8;
        fahrenheit+=32;

        return fahrenheit;
    }

    public int setImageRes (String main, String description ){
        if(main.equals("Rain")){
            return R.drawable.rain;
        }

        if(main.equals("Clear")){
            return R.drawable.sunny;
        }

        if(main.equals("Snow")){
            if(description.equals("rain and snow")){
                return R.drawable.rainsnow;
            }
            return  R.drawable.snow;
        }

        if(main.equals("Thunderstorm")){
            return R.drawable.thunderstorm;
        }

        if(main.equals("Drizzle")){
            return R.drawable.drizzle;
        }

        if(description.equals("tornado")){
            return R.drawable.tornado;
        }

        if(description.equals("mist")){
            return R.drawable.mist;
        }

        if(description.equals("smoke")){
            return R.drawable.smoke;
        }

        if(main.equals("Clouds")){
            return R.drawable.clouds;
        }else {

            return R.drawable.sunny;
        }

    }


}
