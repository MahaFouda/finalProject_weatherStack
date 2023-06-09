package algonquin.cst2335.finalproject_weatherstack.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalproject_weatherstack.Data.WeatherDatabase;
import algonquin.cst2335.finalproject_weatherstack.Data.WeatherItem;
import algonquin.cst2335.finalproject_weatherstack.Data.WeatherItemDAO;
import algonquin.cst2335.finalproject_weatherstack.R;
import algonquin.cst2335.finalproject_weatherstack.databinding.ActivityWeatherNowPageBinding;

public class WeatherNowPage extends AppCompatActivity {


    private ActivityWeatherNowPageBinding binding;
    protected String cityName;

    protected RequestQueue queue = null;

    Bitmap image;
    String url = null;

    String localtime;
    String imgUrl;
    String weather_descriptions;
    String humidity;
    int temperature;
    String pathName;
    SharedPreferences prefs;
    private WeatherItemDAO mDAO;
    private Executor thread;
    private static String apiKey = "edf449bdd0ca749e25b626405a399d41";









    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.weather_now_menu, menu);



        return true;

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);



        switch( item.getItemId() ) {

            case R.id.Item_1:

                Intent intent = new Intent(this,SavedWeatherPage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                startActivity(intent);
                this.finish();


                break;



                case R.id.Item_2:



                AlertDialog.Builder builder = new AlertDialog.Builder(WeatherNowPage.this);
                builder.setMessage("•\tClick on “Weather now”:\n" +
                                "   o\tType in the city you would like to \n       retrieve the weather details.\n" +
                                "   o\tHit search.\n" +
                                "   o\tYou may save your research for future \n       retrieval, by hitting on save button.  \n\n" +
                                "•\tClick on “Saved weather”:\n" +
                                "   o\tYou may navigate between the saved \n       destination to display the respective \n       weather.\n" ).
                        setTitle("How to use the WeatherStack?").
                        setNegativeButton("ok", (dialog, cl) -> {
                        }).create().show();



                break;

            case R.id.Item_3:



                thread.execute(() ->
                {
                    if (cityName != null) {

                        mDAO.insertMessage(new WeatherItem(cityName, localtime, String.valueOf(temperature), pathName, weather_descriptions, humidity, pathName));



                        runOnUiThread(() -> {
                            Toast.makeText(WeatherNowPage.this,  cityName + " saved to your favourite list", Toast.LENGTH_LONG).show();
                        });





                        // Snackbar.make(binding.getRoot(), "City " + cityName + " saved", Snackbar.LENGTH_SHORT).show();

                    } else {
                        runOnUiThread(() -> {
                            Toast.makeText(binding.getRoot().getContext(), "No Item Exist to save", Toast.LENGTH_LONG).show();
                        });

                        //   Snackbar.make(binding.getRoot(), "please search city first", Snackbar.LENGTH_SHORT).show();
                    }
                });



                break;
        }
        return true;


    }

















    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Search Weather");


        queue = Volley.newRequestQueue(this);


        binding = ActivityWeatherNowPageBinding.inflate(getLayoutInflater());
        setSupportActionBar(binding.toolbar);

        prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);


        binding.weatherEditText.setText(prefs.getString("search", ""));


        thread = Executors.newSingleThreadExecutor();

        thread.execute(() ->
        {

            WeatherDatabase db = Room.databaseBuilder(getApplicationContext(), WeatherDatabase.class, "database-name").build();
            mDAO = db.cmDAO();


        });


        runOnUiThread(() -> {
            setContentView(binding.getRoot());
        });





        binding.weatherSearchButton.setOnClickListener(clk -> {


            cityName = binding.weatherEditText.getText().toString();


            try {
                url = "http://api.weatherstack.com/current?access_key="+apiKey+"&query=" + URLEncoder.encode(cityName, "UTF-8");


                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                        response -> {

                            try {

                                prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("search", cityName);
                                editor.apply();


                                JSONObject locationObj = response.getJSONObject("location");

                                Log.i("jason", "onCreate: " + response);

                                cityName = locationObj.getString("name");
                                localtime = locationObj.getString("localtime");


                                JSONObject currentObj = response.getJSONObject("current");

                                temperature = currentObj.getInt("temperature");
                                imgUrl = currentObj.getJSONArray("weather_icons").get(0).toString();

                                weather_descriptions = currentObj.getJSONArray("weather_descriptions").get(0).toString();
                                humidity = currentObj.getString("humidity");


                                pathName = getFilesDir() + "/" + cityName + ".png";
                                File file = new File(pathName);
                                if (file.exists()) {
                                    image = BitmapFactory.decodeFile(pathName);
                                } else {
                                    ImageRequest imgReq = new ImageRequest(imgUrl, new Response.Listener<Bitmap>() {
                                        @Override
                                        public void onResponse(Bitmap bitmap) {
                                            image = bitmap;
                                            FileOutputStream fOut = null;
                                            try {
                                                fOut = openFileOutput(cityName + ".png", Context.MODE_PRIVATE);
                                                image.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                                                fOut.flush();
                                                fOut.close();
                                            } catch (FileNotFoundException e) {
                                                e.printStackTrace();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    }, 1024, 1024, ImageView.ScaleType.CENTER, null, (error) -> {

                                    });


                                    queue.add(imgReq);


                                }

                                runOnUiThread(() -> {


                                    binding.weatherCityName.setText( cityName);
                                    binding.weatherCityName.setVisibility(View.VISIBLE);


                                    binding.weatherDetailsLocateTime.setText( localtime);
                                    binding.weatherDetailsLocateTime.setVisibility(View.VISIBLE);


                                    binding.weatherTemperature.setText("temperature : " + temperature + " °C");
                                    binding.weatherTemperature.setVisibility(View.VISIBLE);


                                    binding.weatherHumidity.setText("humidity : " + humidity + "%");
                                    binding.weatherHumidity.setVisibility(View.VISIBLE);

                                    binding.weatherIcon.setImageBitmap(image);

                                    binding.weatherIcon.setVisibility(View.VISIBLE);


                                    binding.weatherDescription.setText(weather_descriptions);
                                    binding.weatherDescription.setVisibility(View.VISIBLE);


                                });


                            } catch (JSONException e) {


                            }

                        }, error -> {


                });


                queue.add(request);


            } catch (UnsupportedEncodingException e) {
                //  Log.i("tag", "onCreate: "+e);


                throw new RuntimeException(e);
            }


        });


    }
}