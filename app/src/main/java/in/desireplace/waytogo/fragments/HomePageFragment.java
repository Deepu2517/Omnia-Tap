package in.desireplace.waytogo.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import in.desireplace.waytogo.Constants;
import in.desireplace.waytogo.R;
import in.desireplace.waytogo.activities.SavedAddressesActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomePageFragment extends Fragment implements View.OnClickListener {

    Intent intent;
    Bundle bundle;

    public HomePageFragment() {
        // Required empty public constructor
    }

    public static HomePageFragment newInstance() {
        HomePageFragment fragment = new HomePageFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home_page, container, false);
        Button requestPickUpButton = (Button) rootView.findViewById(R.id.request_pick_button);
        Button waterSupplyButton = (Button) rootView.findViewById(R.id.water_supply_button);
        requestPickUpButton.setOnClickListener(this);
        waterSupplyButton.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.request_pick_button:
                intent = new Intent(getContext(), SavedAddressesActivity.class);
                bundle = new Bundle();
                bundle.putString(Constants.SERVICE_TYPE, "Laundry Pick Up");
                intent.putExtras(bundle);
                startActivity(intent);
            case R.id.water_supply_button:
                intent = new Intent(getContext(), SavedAddressesActivity.class);
                bundle = new Bundle();
                bundle.putString(Constants.SERVICE_TYPE, "Water Supply");
                intent.putExtras(bundle);
                startActivity(intent);
        }
    }
}
