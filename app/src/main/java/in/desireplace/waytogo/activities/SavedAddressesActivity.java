package in.desireplace.waytogo.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import in.desireplace.waytogo.Constants;
import in.desireplace.waytogo.R;
import in.desireplace.waytogo.adapters.SavedAddressAdapter;
import in.desireplace.waytogo.models.SavedAddresses;

public class SavedAddressesActivity extends AppCompatActivity implements SavedAddressAdapter.Callback {

    public TextView mNoAddressesTextView;
    private SavedAddressAdapter mAdapter;
    private String identifier;
    private DatabaseReference mCheckRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_addresses);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (!isNetworkAvailable()) {
            Toast.makeText(this, "No Internet Connection.. Please Connect!!", Toast.LENGTH_SHORT).show();
        }

        setTitle("Add/Edit Address");

        identifier = getIntent().getStringExtra("identifier");

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        String firebasePath = "users/" + mAuth.getCurrentUser().getUid() + "/";

        mCheckRef = FirebaseDatabase.getInstance().getReference().child(firebasePath);

        mNoAddressesTextView = (TextView) findViewById(R.id.no_addresses_text_view);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddEditDeleteDialog(null);
            }
        });

        RecyclerView addressList = (RecyclerView) findViewById(R.id.address_list);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        addressList.setLayoutManager(manager);
        mAdapter = new SavedAddressAdapter(this, this, this);
        addressList.setAdapter(mAdapter);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    private void showAddEditDeleteDialog(final SavedAddresses addresses) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Address");
        View view = getLayoutInflater().inflate(R.layout.dailog_save_address, null, false);
        builder.setView(view);

        final EditText fullNameEditText = view.findViewById(R.id.full_name_input);
        final EditText mobileNumberTextView = view.findViewById(R.id.mobile_number_input);
        final EditText houseNumberTextView = view.findViewById(R.id.house_no_input);
        final EditText localityTextView = view.findViewById(R.id.locality_input);
        final EditText landmarkEditText = view.findViewById(R.id.landmark_input);
        final EditText emailEditText = view.findViewById(R.id.email_input);
        if (addresses != null) {
            fullNameEditText.setText(addresses.getFullName());
            mobileNumberTextView.setText(addresses.getMobileNumber());
            emailEditText.setText(addresses.getEmail());
            houseNumberTextView.setText(addresses.getHouseNumber());
            localityTextView.setText(addresses.getLocality());
            landmarkEditText.setText(addresses.getLandmark());

            builder.setNegativeButton(android.R.string.cancel, null);
            builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (!validateName(fullNameEditText)) {
                        validateName(fullNameEditText);
                        Toast.makeText(SavedAddressesActivity.this, "Name is Required", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!validatePhoneNumber(mobileNumberTextView)) {
                        validatePhoneNumber(mobileNumberTextView);
                        Toast.makeText(SavedAddressesActivity.this, "Mobile Number is Required", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!isEmailValid(emailEditText)) {
                        isEmailValid(emailEditText);
                        Toast.makeText(SavedAddressesActivity.this, "Email is Required", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!validateHouseNumber(houseNumberTextView)) {
                        validateHouseNumber(houseNumberTextView);
                        Toast.makeText(SavedAddressesActivity.this, "House Number is Required", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!validateLocality(localityTextView)) {
                        validateLocality(localityTextView);
                        Toast.makeText(SavedAddressesActivity.this, "Locality is Required", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!validateLandmark(landmarkEditText)) {
                        validateLandmark(landmarkEditText);
                        Toast.makeText(SavedAddressesActivity.this, "Landmark is Required", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String fullName = fullNameEditText.getText().toString();
                    String mobileNumber = mobileNumberTextView.getText().toString();
                    String email = emailEditText.getText().toString();
                    String houseNumber = houseNumberTextView.getText().toString();
                    String locality = localityTextView.getText().toString();
                    String landmark = landmarkEditText.getText().toString();
                    mAdapter.firebaseUpdate(addresses, fullName, mobileNumber, email, houseNumber, locality, landmark);
                }
            });
            builder.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mAdapter.firebaseDelete(addresses);
                }
            });
        } else {
            builder.setNegativeButton(android.R.string.cancel, null);
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String fullName = fullNameEditText.getText().toString();
                    String mobileNumber = mobileNumberTextView.getText().toString();
                    String email = emailEditText.getText().toString();
                    String houseNumber = houseNumberTextView.getText().toString();
                    String locality = localityTextView.getText().toString();
                    String landmark = landmarkEditText.getText().toString();
                    if (!validateName(fullNameEditText)) {
                        validateName(fullNameEditText);
                        Toast.makeText(SavedAddressesActivity.this, "Name is Required", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!validatePhoneNumber(mobileNumberTextView)) {
                        validatePhoneNumber(mobileNumberTextView);
                        Toast.makeText(SavedAddressesActivity.this, "Mobile Number is Required", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!isEmailValid(emailEditText)) {
                        isEmailValid(emailEditText);
                        Toast.makeText(SavedAddressesActivity.this, "Email is Required", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!validateHouseNumber(houseNumberTextView)) {
                        validateHouseNumber(houseNumberTextView);
                        Toast.makeText(SavedAddressesActivity.this, "House Number is Required", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!validateLocality(localityTextView)) {
                        validateLocality(localityTextView);
                        Toast.makeText(SavedAddressesActivity.this, "Locality is Required", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!validateLandmark(landmarkEditText)) {
                        validateLandmark(landmarkEditText);
                        Toast.makeText(SavedAddressesActivity.this, "Landmark is Required", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    SavedAddresses addresses = new SavedAddresses(fullName, mobileNumber, email, houseNumber, locality, landmark);
                    mAdapter.firebaseAdd(addresses);
                }
            });
        }
        builder.create().show();
    }

    @Override
    public void onEditButtonClick(SavedAddresses addresses) {
        showAddEditDeleteDialog(addresses);
    }

    @Override
    public void onItemClick(SavedAddresses addresses) {
        if (identifier == null) {
            Intent intent;
            Bundle bundle = getIntent().getExtras();

            String service_type = bundle.getString(Constants.SERVICE_TYPE);

            bundle = new Bundle();
            intent = new Intent(SavedAddressesActivity.this, ConfirmDetailsActivity.class);

            bundle.putString(Constants.SERVICE_TYPE, service_type);

            bundle.putString(Constants.SELECTED_NAME, addresses.getFullName());
            bundle.putString(Constants.SELECTED_MOBILE_NUMBER, addresses.getMobileNumber());
            bundle.putString(Constants.SELECTED_EMAIL, addresses.getEmail());
            bundle.putString(Constants.SELECTED_HOUSE_NUMBER, addresses.getHouseNumber());
            bundle.putString(Constants.SELECTED_LOCALITY, addresses.getLocality());
            bundle.putString(Constants.SELECTED_LANDMARK, addresses.getLandmark());
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    private boolean validatePhoneNumber(EditText phonenumberedittext) {
        String phoneNumber = phonenumberedittext.getText().toString();
        if (TextUtils.isEmpty(phoneNumber) && phoneNumber.length() != 10 && phoneNumber.length() > 10 && phoneNumber.length() < 10) {
            phonenumberedittext.setError("Invalid phone number.");
            return false;
        }
        return true;
    }

    private boolean validateName(EditText nameEditText) {
        String name = nameEditText.getText().toString();
        if (TextUtils.isEmpty(name)) {
            nameEditText.setError("Invalid Name.");
            return false;
        }
        return true;
    }

    private boolean validateHouseNumber(EditText houseNumberEditText) {
        String houseNumber = houseNumberEditText.getText().toString();
        if (TextUtils.isEmpty(houseNumber)) {
            houseNumberEditText.setError("Invalid House Number.");
            return false;
        }
        return true;
    }

    private boolean validateLocality(EditText localityEditText) {
        String locality = localityEditText.getText().toString();
        if (TextUtils.isEmpty(locality)) {
            localityEditText.setError("Invalid Locality.");
            return false;
        }
        return true;
    }

    private boolean validateLandmark(EditText landmarkEditText) {
        String landmark = landmarkEditText.getText().toString();
        if (TextUtils.isEmpty(landmark)) {
            landmarkEditText.setError("Invalid Landmark.");
            return false;
        }
        return true;
    }

    private boolean isEmailValid(EditText emailEditText) {
        String email = emailEditText.getText().toString();
        if (!email.contains("@") && TextUtils.isEmpty(email) && !email.contains(".com")) {
            emailEditText.setError("Invalid Email.");
            return false;
        }
        return true;
    }
}
