package in.desireplace.waytogo.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import in.desireplace.waytogo.Constants;
import in.desireplace.waytogo.R;
import in.desireplace.waytogo.activities.FoodBeveragesActivity;
import in.desireplace.waytogo.activities.SavedAddressesActivity;
import in.desireplace.waytogo.activities.SupportActivity;
import in.desireplace.waytogo.activities.WaterSupplyActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomePageFragment extends Fragment implements View.OnClickListener {

    public HomePageFragment() {
        // Required empty public constructor
    }

    public static HomePageFragment newInstance() {
        return new HomePageFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home_page, container, false);
        Button requestPickUpButton = rootView.findViewById(R.id.request_pick_button);
        Button waterSupplyButton = rootView.findViewById(R.id.water_supply_button);
        Button laundryStoreDropButton = rootView.findViewById(R.id.food_beverages_button);
        Button customerCareButton = rootView.findViewById(R.id.support_button);
        requestPickUpButton.setOnClickListener(this);
        waterSupplyButton.setOnClickListener(this);
        laundryStoreDropButton.setOnClickListener(this);
        customerCareButton.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.request_pick_button:
                Intent laundryIntent = new Intent(getContext(), SavedAddressesActivity.class);
                Bundle laundryBundle = new Bundle();
                laundryBundle.putString(Constants.SERVICE_TYPE, "Laundry Pick Up");
                laundryIntent.putExtras(laundryBundle);
                startActivity(laundryIntent);
                break;
            case R.id.water_supply_button:
                startActivity(new Intent(getContext(), WaterSupplyActivity.class));
                break;
            case R.id.food_beverages_button:
                startActivity(new Intent(getContext(), FoodBeveragesActivity.class));
                break;
            case R.id.support_button:
                startActivity(new Intent(getContext(), SupportActivity.class));
                break;
        }
    }
}
