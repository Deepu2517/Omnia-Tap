package in.desireplace.waytogo.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import in.desireplace.waytogo.Constants;
import in.desireplace.waytogo.R;
import in.desireplace.waytogo.models.Subscriptions;

public class PaymentStatusActivity extends AppCompatActivity {

    private TextView mPaymentIdTextView;
    private Button mFinishButton;
    private ImageView mStatusImageView;
    private TextView mPriceTextView;
    private TextView mServiceTypeTextView;
    private TextView mStatusTypeTextView;
    private TextView mMessageTextView;
    private TextView mErrorMessageTextView;

    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_status);

        mAuth = FirebaseAuth.getInstance();

        String firebasePath = "users/" + mAuth.getCurrentUser().getUid();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(firebasePath + "/Subscriptions");

        Bundle bundle = getIntent().getExtras();
        String finalStatus = bundle.getString("finalStatus");
        String finalErrorMessage = bundle.getString("finalErrorMessage");
        String finalPaymentID = bundle.getString("finalPaymentID");
        String finalAmount = bundle.getString("finalAmount");
        String finalFailureReason = bundle.getString("finalFailureReason");
        String finalFailureMessage = bundle.getString("finalFailureMessage");
        String finalFailure = bundle.getString("finalFailure");
        String serviceType = bundle.getString("serviceType");

        //TODO: Remove These 4 lines.
//        String finalStatus = "successful";
//        String finalPaymentID = " MOJO7809005A53631621";
//        String finalAmount = "10";
//        String serviceType = "One Month Subscription";

        mPaymentIdTextView = (TextView) findViewById(R.id.payment_id_text_view);
        mFinishButton = (Button) findViewById(R.id.finish_activity_button);
        mStatusImageView = (ImageView) findViewById(R.id.status_image_view);
        mPriceTextView = (TextView) findViewById(R.id.status_price_text_view);
        mServiceTypeTextView = (TextView) findViewById(R.id.service_type_text_view);
        mStatusTypeTextView = (TextView) findViewById(R.id.status_text_type_text_view);
        mMessageTextView = (TextView) findViewById(R.id.message_text_view);
        mErrorMessageTextView = (TextView) findViewById(R.id.error_message_text_view);

        mFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PaymentStatusActivity.this, HomeActivity.class));
            }
        });

        if (Objects.equals(finalStatus, "successful")) {
            mPaymentIdTextView.setText("Payment ID : " + finalPaymentID);
            mStatusImageView.setBackgroundResource(R.drawable.success);
            mPriceTextView.setText("\u20B9  " + finalAmount);
            mServiceTypeTextView.setText(serviceType + " - Water Can");
            mStatusTypeTextView.setText("Is Successful");
            mStatusTypeTextView.setTextColor(Color.GREEN);
            mMessageTextView.setText("Thank You For Subscribing. We Will Contact You Soon");
            mErrorMessageTextView.setVisibility(View.GONE);
            if (Objects.equals(serviceType, "One Month Subscription")) {
                Subscriptions subscriptions = new Subscriptions(true, false, false);
                mDatabaseReference.push().setValue(subscriptions);
            } else if (Objects.equals(serviceType, "Three Months Subscription")) {
                Subscriptions subscriptions = new Subscriptions(false, true, false);
                mDatabaseReference.push().setValue(subscriptions);
            } else if (Objects.equals(serviceType, "Six Months Subscription")) {
                Subscriptions subscriptions = new Subscriptions(false, false, true);
                mDatabaseReference.push().setValue(subscriptions);
            } else {
                Log.e(Constants.TAG, "Invalid Service Type : " + serviceType);
            }
        } else if (Objects.equals(finalStatus, "failed")) {
            mPaymentIdTextView.setText("Payment ID : " + finalPaymentID);
            mStatusImageView.setBackgroundResource(R.drawable.cross);
            mPriceTextView.setText("\u20B9  " + finalAmount);
            mServiceTypeTextView.setText(serviceType + " - Water Can");
            mStatusTypeTextView.setText("Has Failed");
            mStatusTypeTextView.setTextColor(Color.RED);
            mMessageTextView.setText(finalFailureMessage);
            mErrorMessageTextView.setText("Please Take A Screen Shot Of This And, Contact Our Customer Care");
            mErrorMessageTextView.setTextColor(Color.RED);
        }
    }
}
