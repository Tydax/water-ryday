package fr.lille.bour.armand.waterryday.activity.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import fr.lille.bour.armand.waterryday.R;
import fr.lille.bour.armand.waterryday.models.Plant;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddPlantFragment.OnFragmentInteractionListener} interface
 */
public class AddPlantFragment extends Fragment {

    public static final String KEY_NAME = "key_name";
    public static final String KEY_SPECIE = "key_specie";
    public static final String KEY_LOCATION = "key_location";
    public static final String KEY_WATERFREQ = "key_waterfreq";

    private EditText mName;
    private EditText mSpecie;
    private EditText mLocation;
    private EditText mWaterFreq;
    private Button mSaveButt;

    private OnFragmentInteractionListener mListener;

    public AddPlantFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View layout = inflater.inflate(R.layout.fragment_add_plant, container, false);
        // Get all views
        mName = (EditText) layout.findViewById(R.id.plantAddName);
        mSpecie = (EditText) layout.findViewById(R.id.plantAddSpecie);
        mLocation = (EditText) layout.findViewById(R.id.plantAddLocation);
        mWaterFreq = (EditText) layout.findViewById(R.id.plantAddWaterFrequency);
        mSaveButt = (Button) layout.findViewById(R.id.plantAddButton);
        mSaveButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkForm()) {
                    convertPlant();
                }
            }
        });
        return layout;
    }

    private boolean checkForm() {
        final String name = mName.getText().toString();
        final String specie = mSpecie.getText().toString();
        final String waterFreq = mWaterFreq.getText().toString();

        // Need a name or a specie
        if (name.isEmpty() && specie.isEmpty()) {
            Toast.makeText(getActivity(), R.string.toast_addPlant_emptyNameSpecie, Toast.LENGTH_LONG).show();
            mName.requestFocus();
            return false;
        }

        // Check if watering frequency is valid
        try {
            final int waterFreqInt = Integer.parseInt(waterFreq);

            // Watering frequency should be >= 1
            if (Integer.parseInt(waterFreq) < 1) {
                Toast.makeText(getActivity(), R.string.toast_addPlant_wrongWaterFreq, Toast.LENGTH_LONG).show();
                mWaterFreq.requestFocus();
                return false;
            }
        } catch (final NumberFormatException exc) {
            Toast.makeText(getActivity(), R.string.toast_addPlant_invalidWaterFreq, Toast.LENGTH_LONG).show();
            mWaterFreq.requestFocus();
            return false;
        }

        return true;
    }

    private void convertPlant() {
        final String name = mName.getText().toString();
        final String specie = mSpecie.getText().toString();
        final String waterFreqStr = mWaterFreq.getText().toString();
        final int waterFreq = Integer.parseInt(waterFreqStr);
        final String location = mLocation.getText().toString();

        final Intent intent = new Intent();
        intent.putExtra(KEY_NAME, name);
        intent.putExtra(KEY_SPECIE, specie);
        intent.putExtra(KEY_LOCATION, location);
        intent.putExtra(KEY_WATERFREQ, waterFreq);

        mListener.savePlant(intent);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void savePlant(final Intent intent);
    }
}
