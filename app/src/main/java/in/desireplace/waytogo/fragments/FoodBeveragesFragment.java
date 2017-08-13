package in.desireplace.waytogo.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.desireplace.waytogo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FoodBeveragesFragment extends Fragment {

    public FoodBeveragesFragment() {
        // Required empty public constructor
    }

    public static FoodBeveragesFragment newInstance() {
        FoodBeveragesFragment fragment = new FoodBeveragesFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_food_beverages, container, false);
    }

}
