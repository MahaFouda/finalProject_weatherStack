package algonquin.cst2335.finalproject_weatherstack.UI;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import algonquin.cst2335.finalproject_weatherstack.databinding.ActivityPlaceKittenPageBinding;


public class PlaceKittenPage extends AppCompatActivity {
ActivityPlaceKittenPageBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityPlaceKittenPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



    }
}