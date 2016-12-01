package fr.lille.bour.armand.waterryday.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import fr.lille.bour.armand.waterryday.R;
import fr.lille.bour.armand.waterryday.activity.fragment.AddPlantFragment;
import fr.lille.bour.armand.waterryday.models.Plant;

public class AddPlantActivity extends AppCompatActivity implements AddPlantFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant);
    }

    @Override
    public void savePlant(final Intent intent) {
        setResult(RESULT_OK, intent);
        finish();
    }
}
