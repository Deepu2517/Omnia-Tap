package in.desireplace.waytogo.activities;



import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import in.desireplace.waytogo.Constants;
import in.desireplace.waytogo.R;
import in.desireplace.waytogo.models.YourOrders;

public class ConfirmDetailsActivity extends AppCompatActivity {

    TextView mName_TextView, mHouse_no_TextView, mCity_PinCode_TextView, mPhoneNumber_TextView, mService_Type_TextView;

    Button mPlace_Order_Button;

    DatabaseReference mDatabaseReference;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_details);

        mAuth = FirebaseAuth.getInstance();

        String firebasePath = "users/" + mAuth.getCurrentUser().getUid();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(firebasePath + "/YourOrders");

        mName_TextView= (TextView) findViewById(R.id.name_text_view);
        mHouse_no_TextView = (TextView) findViewById(R.id.house_no_locality_text_view);
        mCity_PinCode_TextView = (TextView) findViewById(R.id.city_pincode_text_view);
        mPhoneNumber_TextView = (TextView) findViewById(R.id.phone_number_text_view);
        mService_Type_TextView = (TextView) findViewById(R.id.selected_service_type_text_view);

        mPlace_Order_Button = (Button) findViewById(R.id.place_order_button);

        Bundle bundle = getIntent().getExtras();

        final String service_type = bundle.getString(Constants.SERVICE_TYPE);
        final String selectedName = bundle.getString(Constants.SELECTED_NAME);
        final String selectedMobileNumber = bundle.getString(Constants.SELECTED_MOBILE_NUMBER);
        final String selectedPinCode = bundle.getString(Constants.SELECTED_PIN_CODE);
        final String selectedHouseNumber = bundle.getString(Constants.SELECTED_HOUSE_NUMBER);
        final String selectedLocality = bundle.getString(Constants.SELECTED_LOCALITY);
        final String selectedCity = bundle.getString(Constants.SELECTED_CITY);
        final String selectedState = bundle.getString(Constants.SELECTED_STATE);

        mName_TextView.setText(selectedName);
        mHouse_no_TextView.setText(selectedHouseNumber + "" + selectedLocality);
        mCity_PinCode_TextView.setText(selectedCity + ", " + selectedState + " - " + selectedPinCode );
        mPhoneNumber_TextView.setText(selectedMobileNumber);
        mService_Type_TextView.setText(service_type);

        mPlace_Order_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YourOrders orders = new YourOrders(service_type, selectedName, selectedMobileNumber, selectedPinCode, selectedHouseNumber, selectedLocality, selectedCity, selectedState);
                mDatabaseReference.push().setValue(orders);
                Toast.makeText(ConfirmDetailsActivity.this, "Order Placed", Toast.LENGTH_LONG).show();
            }
        });
    }
}
