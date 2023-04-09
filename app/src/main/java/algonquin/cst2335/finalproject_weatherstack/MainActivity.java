package algonquin.cst2335.finalproject_weatherstack;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import algonquin.cst2335.finalproject_weatherstack.UI.NasaPage;
import algonquin.cst2335.finalproject_weatherstack.UI.NewYorkPage;
import algonquin.cst2335.finalproject_weatherstack.UI.PlaceKittenPage;
import algonquin.cst2335.finalproject_weatherstack.UI.WeatherNowPage;
import algonquin.cst2335.finalproject_weatherstack.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {
   private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         binding=ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());


        navigation();

    }




    void navigation(){



        binding.nasa.setOnClickListener(clk->{

            Intent NasaPage=new Intent(this, NasaPage.class);

            startActivity(NasaPage);


        });


        binding.newYork.setOnClickListener(clk->{
            Intent NewYorkPage=new Intent(this, NewYorkPage.class);

            startActivity(NewYorkPage);


        });


        binding.kitten.setOnClickListener(clk->{
    Intent PlaceKittenPage=new Intent(this, PlaceKittenPage.class);

    startActivity(PlaceKittenPage);
});


        binding.weather.setOnClickListener(clk->{

    Intent weatherNowPage=new Intent(this, WeatherNowPage.class);

    startActivity(weatherNowPage);

});







    }




}