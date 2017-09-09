package in.desireplace.waytogo.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import in.desireplace.waytogo.Constants;
import in.desireplace.waytogo.R;

public class PaymentStatusActivity extends AppCompatActivity {

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_status);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        String firebasePath = "users/" + mAuth.getCurrentUser().getUid();

        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(firebasePath + "/");

        Bundle bundle = getIntent().getExtras();
        String finalStatus = bundle.getString("finalStatus");
        String finalPaymentID = bundle.getString("finalPaymentID");
        String finalAmount = bundle.getString("finalAmount");
        String finalFailureMessage = bundle.getString("finalFailureMessage");
        String serviceType = bundle.getString("serviceType");

        Crashlytics.log(9, "PaymentStatusDetails", " FinalStatus : " + finalStatus + " FinalPaymentID : " + finalPaymentID +
                " FinalAmount : " + finalAmount + " FinalFailureMessage : " + finalFailureMessage + " ServiceType : " + serviceType);

        TextView mPaymentIdTextView = (TextView) findViewById(R.id.payment_id_text_view);
        Button mFinishButton = (Button) findViewById(R.id.finish_activity_button);
        ImageView mStatusImageView = (ImageView) findViewById(R.id.status_image_view);
        TextView mPriceTextView = (TextView) findViewById(R.id.status_price_text_view);
        TextView mServiceTypeTextView = (TextView) findViewById(R.id.service_type_text_view);
        TextView mStatusTypeTextView = (TextView) findViewById(R.id.status_text_type_text_view);
        TextView mMessageTextView = (TextView) findViewById(R.id.message_text_view);
        TextView mErrorMessageTextView = (TextView) findViewById(R.id.error_message_text_view);

        mFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PaymentStatusActivity.this, HomeActivity.class));
            }
        });

        if (Objects.equals(finalStatus, "successful")) {
            mPaymentIdTextView.setText(String.format("Payment ID : %s", finalPaymentID));
            mStatusImageView.setBackgroundResource(R.drawable.success);
            mPriceTextView.setText(String.format("₹  %s", finalAmount));
            mServiceTypeTextView.setText(String.format("%s - Water Can", serviceType));
            mStatusTypeTextView.setText(R.string.successful_payment_message);
            mStatusTypeTextView.setTextColor(Color.GREEN);
            mMessageTextView.setText(R.string.thank_you_subscription_message);
            mErrorMessageTextView.setVisibility(View.GONE);
            if (Objects.equals(serviceType, "One Month Subscription")) {
                Log.d(Constants.TAG, "One Month if is executing");
                mDatabaseReference.child("OneMonthSubscription").setValue(true);
                mDatabaseReference.child("CanStatus").setValue("0");
            } else if (Objects.equals(serviceType, "Three Months Subscription")) {
                Log.d(Constants.TAG, "Three Months if is executing");
                mDatabaseReference.child("ThreeMonthSubscription").setValue(true);
                mDatabaseReference.child("CanStatus").setValue("0");
            } else if (Objects.equals(serviceType, "Six Months Subscription")) {
                Log.d(Constants.TAG, "Six Months if is executing");
                mDatabaseReference.child("SixMonthSubscription").setValue(true);
                mDatabaseReference.child("CanStatus").setValue("0");
            } else {
                Toast.makeText(this, "Something Went Wrong.", Toast.LENGTH_SHORT).show();
            }
        } else if (Objects.equals(finalStatus, "failed")) {
            mPaymentIdTextView.setText(String.format("Payment ID : %s", finalPaymentID));
            mStatusImageView.setBackgroundResource(R.drawable.sad_emoji);
            mPriceTextView.setText(String.format("₹  %s", finalAmount));
            mServiceTypeTextView.setText(String.format("%s - Water Can", serviceType));
            mStatusTypeTextView.setText(R.string.failed_payment_subscription_message);
            mStatusTypeTextView.setTextColor(Color.RED);
            mMessageTextView.setText(finalFailureMessage);
            mErrorMessageTextView.setText(R.string.take_screenshot_message);
            mErrorMessageTextView.setTextColor(Color.RED);
        }
    }
}
