package in.desireplace.waytogo.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import in.desireplace.waytogo.R;

public class PaymentStatusActivity extends AppCompatActivity {

    private TextView mPaymentIdTextView;
    private TextView mPaymentStatusTextView;
    private Button mFinishButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_status);

        mPaymentIdTextView = (TextView) findViewById(R.id.payment_id_text_view);
        mPaymentStatusTextView = (TextView) findViewById(R.id.payment_status_text_view);
        mFinishButton = (Button) findViewById(R.id.finish_activity_button);


    }
}
