package in.desireplace.waytogo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import in.desireplace.waytogo.Constants;
import in.desireplace.waytogo.R;

public class WaterSupplyActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_supply);

        setTitle("Water Can");

        Button oneCanOrderButton = (Button) findViewById(R.id.water_order_once_button);
        Button oneMonthSubscriptionButton = (Button) findViewById(R.id.one_month_subscription_button);
        Button threeMonthsSubscriptionButton = (Button) findViewById(R.id.three_months_subscription_button);
        Button sixMonthsSubscriptionButton = (Button) findViewById(R.id.six_months_subscription_button);
        oneCanOrderButton.setOnClickListener(this);
        oneMonthSubscriptionButton.setOnClickListener(this);
        threeMonthsSubscriptionButton.setOnClickListener(this);
        sixMonthsSubscriptionButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.water_order_once_button :
                Intent waterIntent = new Intent(getApplicationContext(), SavedAddressesActivity.class);
                Bundle waterBundle = new Bundle();
                waterBundle.putString(Constants.SERVICE_TYPE, "Water Can Delivery");
                waterIntent.putExtras(waterBundle);
                startActivity(waterIntent);
                break;
            case R.id.one_month_subscription_button:
                Intent oneMonthIntent = new Intent(getApplicationContext(), SavedAddressesActivity.class);
                Bundle oneMonthBundle = new Bundle();
                oneMonthBundle.putString(Constants.SERVICE_TYPE, "One Month Subscription");
                oneMonthIntent.putExtras(oneMonthBundle);
                startActivity(oneMonthIntent);
                break;
            case R.id.three_months_subscription_button:
                Intent threeMonthIntent = new Intent(getApplicationContext(), SavedAddressesActivity.class);
                Bundle threeMonthBundle = new Bundle();
                threeMonthBundle.putString(Constants.SERVICE_TYPE, "Three Months Subscription");
                threeMonthIntent.putExtras(threeMonthBundle);
                startActivity(threeMonthIntent);
                break;
            case R.id.six_months_subscription_button:
                Intent sixMonthIntent = new Intent(getApplicationContext(), SavedAddressesActivity.class);
                Bundle sixMonthBundle = new Bundle();
                sixMonthBundle.putString(Constants.SERVICE_TYPE, "Six Months Subscription");
                sixMonthIntent.putExtras(sixMonthBundle);
                startActivity(sixMonthIntent);
                break;
            default:
                Log.e(Constants.TAG, "Invalid Option, View Id : " + v.getId());
        }
    }
}