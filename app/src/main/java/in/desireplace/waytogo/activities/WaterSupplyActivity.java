package in.desireplace.waytogo.activities;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import in.desireplace.waytogo.Constants;
import in.desireplace.waytogo.R;
import in.desireplace.waytogo.adapters.SavedAddressAdapter;
import in.desireplace.waytogo.models.SavedAddresses;

public class WaterSupplyActivity extends AppCompatActivity implements View.OnClickListener, SavedAddressAdapter.Callback {

    private DatabaseReference mDatabaseReference;

    private FirebaseAuth mAuth;

    private Button mWantButton;

    private TextView mTotalsCanTextVew, mPresentCansTextView;

    private ProgressDialog dialog;

    private String key;

    private Dialog mDialog;

    private SavedAddressAdapter mAdapter;

    private String finalCanStatus;

    private DatabaseReference sixReference;

    private DatabaseReference threeReference;

    private DatabaseReference oneReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Water Can");
        mAuth = FirebaseAuth.getInstance();

        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setMessage("please wait...");
        dialog.setCancelable(false);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        mAdapter = new SavedAddressAdapter(this, this, null);

        sixReference = mDatabaseReference.child("users").child(mAuth.getCurrentUser().getUid()).child("SixMonthSubscription");
        threeReference = mDatabaseReference.child("users").child(mAuth.getCurrentUser().getUid()).child("ThreeMonthSubscription");
        oneReference = mDatabaseReference.child("users").child(mAuth.getCurrentUser().getUid()).child("OneMonthSubscription");
        final DatabaseReference canReference = mDatabaseReference.child("users").child(mAuth.getCurrentUser().getUid()).child("CanStatus");

        dialog.show();
        ValueEventListener listener = new ValueEventListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(Boolean.class) != null && dataSnapshot.getValue(Boolean.class)) {
                    setContentView(R.layout.subscribed_layout);
                    mPresentCansTextView = (TextView) findViewById(R.id.present_cans_text_view);
                    mTotalsCanTextVew = (TextView) findViewById(R.id.total_cans_text_view);
                    mWantButton = (Button) findViewById(R.id.want_button);
                    key = dataSnapshot.getKey();
                    canReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String cans = dataSnapshot.getValue(String.class);
                            int canValue = Integer.parseInt(cans);
                            if (Objects.equals(key, "OneMonthSubscription") && canValue == 10) {
                                oneReference.setValue(false);
                                setWaterSupplyActivity();
                                showTakeSubscriptionAgainDialog();
                            } else if (Objects.equals(key, "ThreeMonthSubscription") && canValue == 30) {
                                threeReference.setValue(false);
                                setWaterSupplyActivity();
                                showTakeSubscriptionAgainDialog();
                            } else if (Objects.equals(key, "SixMonthSubscription") && canValue == 60) {
                                sixReference.setValue(false);
                                setWaterSupplyActivity();
                                showTakeSubscriptionAgainDialog();
                            }
                            mPresentCansTextView.setText(cans);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Crashlytics.log(4, "FirebaseSubscriptionSingleValueListenerCancelledError", databaseError.toString());
                        }
                    });
                    if (Objects.equals(key, "OneMonthSubscription")) {
                        mTotalsCanTextVew.setText(R.string.one_month_total_cans);
                    } else if (Objects.equals(key, "ThreeMonthSubscription")) {
                        mTotalsCanTextVew.setText(R.string.three_months_total_cans);
                    } else if (Objects.equals(key, "SixMonthSubscription")) {
                        mTotalsCanTextVew.setText(R.string.six_months_total_cans);
                    }
                    mWantButton.setOnClickListener(WaterSupplyActivity.this);
                    dialog.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Crashlytics.log(4, "FirebaseSubscriptionValueEventListenerCancelledError", databaseError.toString());
            }
        };
        oneReference.addListenerForSingleValueEvent(listener);
        threeReference.addListenerForSingleValueEvent(listener);
        sixReference.addListenerForSingleValueEvent(listener);
        setWaterSupplyActivity();
    }

    private void setWaterSupplyActivity() {
        setContentView(R.layout.activity_water_supply);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 3000);

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
            case R.id.want_button:
                showAddressList();
                break;
            default:
                Toast.makeText(this, "Something Went Wrong.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showAddressList() {
        mDialog = new Dialog(WaterSupplyActivity.this);
        mDialog.setTitle("Choose Address ");
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mDialog.setContentView(R.layout.dialog_layout);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setCancelable(true);
        mDialog.show();
        RecyclerView rvTest = mDialog.findViewById(R.id.rvTest);
        rvTest.setHasFixedSize(true);
        rvTest.setLayoutManager(new LinearLayoutManager(WaterSupplyActivity.this));
        SavedAddressAdapter rvAdapter = new SavedAddressAdapter(this, this, null);
        rvTest.setAdapter(rvAdapter);
    }

    private void showTakeSubscriptionAgainDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Aww Snap...");
        View view = getLayoutInflater().inflate(R.layout.dailog_subscribe_again, null, false);
        builder.setView(view);
        builder.setPositiveButton(android.R.string.ok, null);
        builder.create().show();
    }

    private void showAddEditDeleteDialog(final SavedAddresses addresses) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Address");
        View view = getLayoutInflater().inflate(R.layout.dailog_save_address, null, false);
        builder.setView(view);

        final EditText fullNameEditText = view.findViewById(R.id.full_name_input);
        final EditText mobileNumberTextView = view.findViewById(R.id.mobile_number_input);
        final EditText emailEditText = view.findViewById(R.id.email_input);
        final EditText houseNumberTextView = view.findViewById(R.id.house_no_input);
        final EditText localityTextView = view.findViewById(R.id.locality_input);
        final EditText landmarkEditText = view.findViewById(R.id.landmark_input);
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
        DatabaseReference ref = mDatabaseReference.child("WaterCanSubscriptionOrders");
        final DatabaseReference reference = mDatabaseReference.child("users").child(mAuth.getCurrentUser().getUid()).child("CanStatus");
        dialog.show();
        final ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                int result = Integer.parseInt(value);
                result++;
                finalCanStatus = String.valueOf(result);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Crashlytics.log(5, "FirebaseWaterCansNumberListenerCancelledError", databaseError.toString());
            }
        };
        ref.push().setValue(addresses, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                reference.addListenerForSingleValueEvent(listener);
                mDialog.dismiss();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        reference.setValue(finalCanStatus);
                        dialog.dismiss();
                        Toast.makeText(WaterSupplyActivity.this, "Water Can Will Be Delivered To You", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }, 2000);
            }
        });
    }
}