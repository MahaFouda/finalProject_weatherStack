package algonquin.cst2335.finalproject_weatherstack.UI;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import algonquin.cst2335.finalproject_weatherstack.databinding.ActivityNasaPageBinding;


public class NewYorkPage extends AppCompatActivity {


    ActivityNasaPageBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityNasaPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }
}