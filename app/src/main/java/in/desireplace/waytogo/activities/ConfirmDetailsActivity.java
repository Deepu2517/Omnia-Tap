package in.desireplace.waytogo.activities;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.instamojo.android.Instamojo;
import com.instamojo.android.activities.PaymentDetailsActivity;
import com.instamojo.android.callbacks.OrderRequestCallBack;
import com.instamojo.android.models.Errors;
import com.instamojo.android.models.Order;
import com.instamojo.android.network.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import in.desireplace.waytogo.Constants;
import in.desireplace.waytogo.R;
import in.desireplace.waytogo.models.YourOrders;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class ConfirmDetailsActivity extends AppCompatActivity {

    String mServiceType;
    private EditText mShirts_Laundry_EditText, mTrousers_Laundry_EditText, mOthers_Laundry_EditText, mCans_Water_EditText;
    private DatabaseReference mDatabaseReference, mReference, mRef;
    private FirebaseAuth mAuth;
    private ProgressDialog dialog;
    private String accessToken = null;
    private String amount = null;

    private static Integer randomInt(Integer low, Integer high) {
        return (int) (Math.floor(Math.random() * (high - low + 1)) + low);
    }

    private static Character randomChar(String str) {
        return str.charAt(randomInt(0, str.length() - 1));
    }

    private static String generateRandSeq(Integer length, String src) {
        String seq = "";
        for (int i = 1; i <= length; i = i + 1) {
            seq += randomChar(src);
        }
        return seq;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_details);

        setTitle("Confirm Details");

        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setMessage("please wait...");
        dialog.setCancelable(false);

        //firebase auth
        mAuth = FirebaseAuth.getInstance();

        String firebasePath = "users/" + mAuth.getCurrentUser().getUid();

        final Calendar calendar = Calendar.getInstance();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(firebasePath + "/YourOrders");
        mReference = FirebaseDatabase.getInstance().getReference().child("Orders");
        mRef = FirebaseDatabase.getInstance().getReference().child(firebasePath + "/StatusUrl");

        LinearLayout mLaundry_LinearLayout = (LinearLayout) findViewById(R.id.laundry_linear_layout);
        LinearLayout mWater_LinearLayout = (LinearLayout) findViewById(R.id.water_linear_layout);
        LinearLayout mSubscription_LinearLayout = (LinearLayout) findViewById(R.id.subscription_linear_layout);

        TextView mName_TextView = (TextView) findViewById(R.id.name_text_view);
        TextView mHouse_no_TextView = (TextView) findViewById(R.id.house_no_locality_text_view);
        TextView mLaundry_TextView = (TextView) findViewById(R.id.city_pincode_text_view);
        TextView mPhoneNumber_TextView = (TextView) findViewById(R.id.phone_number_text_view);
        TextView mEmail_TextView = (TextView) findViewById(R.id.email_text_view);

        TextView mService_Type_Laundry_TextView = (TextView) findViewById(R.id.selected_service_type_laundry_text_view);
        TextView mService_Type_Water_TextView = (TextView) findViewById(R.id.selected_service_type_water_text_view);
        TextView mService_Type_Subscription_TextView = (TextView) findViewById(R.id.selected_service_type_subscription_text_view);

        TextView mPrice_TextView = (TextView) findViewById(R.id.price_text_view);
        TextView mDelivery_Charges_TextView = (TextView) findViewById(R.id.delivery_charges_text_view);
        TextView mTotal_Amount_TextView = (TextView) findViewById(R.id.amount_payable_text_view);

        mShirts_Laundry_EditText = (EditText) findViewById(R.id.shirts_laundry_edit_text);
        mTrousers_Laundry_EditText = (EditText) findViewById(R.id.trousers_edit_text);
        mOthers_Laundry_EditText = (EditText) findViewById(R.id.others_edit_text);

        mCans_Water_EditText = (EditText) findViewById(R.id.cans_edit_text);

        Button mPlace_Order_Laundry_Button = (Button) findViewById(R.id.place_order_laundry_button);
        Button mPlace_Order_Water_Button = (Button) findViewById(R.id.place_order_water_button);

        Button mProceed_To_Payment_Button = (Button) findViewById(R.id.proceed_to_payment_button);

        Bundle bundle = getIntent().getExtras();

        mServiceType = bundle.getString(Constants.SERVICE_TYPE);
        final String selectedName = bundle.getString(Constants.SELECTED_NAME);
        final String selectedMobileNumber = bundle.getString(Constants.SELECTED_MOBILE_NUMBER);
        final String selectedEmail = bundle.getString(Constants.SELECTED_EMAIL);
        final String selectedHouseNumber = bundle.getString(Constants.SELECTED_HOUSE_NUMBER);
        final String selectedLocality = bundle.getString(Constants.SELECTED_LOCALITY);
        final String selectedLandmark = bundle.getString(Constants.SELECTED_LANDMARK);

        switch (mServiceType) {
            case "Water Can Delivery":
                mSubscription_LinearLayout.setVisibility(View.GONE);
                mLaundry_LinearLayout.setVisibility(View.GONE);
                mWater_LinearLayout.setVisibility(View.VISIBLE);

                mName_TextView.setText(selectedName);
                mHouse_no_TextView.setText(selectedHouseNumber + " " + selectedLocality);
                mLaundry_TextView.setText(selectedLandmark);
                mPhoneNumber_TextView.setText(selectedMobileNumber);
                mEmail_TextView.setText(selectedEmail);
                mService_Type_Water_TextView.setText(mServiceType);

                mPlace_Order_Water_Button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isNetworkAvailable()) {
                            Toast.makeText(ConfirmDetailsActivity.this, "No Internet Connection.. Please Connect!!", Toast.LENGTH_SHORT).show();
                        } else {
                            dialog.show();
                            int month = calendar.get(Calendar.MONTH) + 1;
                            String currentDate = calendar.get(Calendar.DATE) + "/" + month + "/" + calendar.get(Calendar.YEAR);
                            String currentTime = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);

                            if (!validateCans(mCans_Water_EditText)) {
                                dialog.dismiss();
                                Toast.makeText(ConfirmDetailsActivity.this, "Cans Number Is Required", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            final YourOrders orders = new YourOrders(orderIdGenerator(), mServiceType, selectedName, selectedMobileNumber, selectedEmail, selectedHouseNumber, selectedLocality, selectedLandmark, mCans_Water_EditText.getText().toString(), currentDate, currentTime);
                            mDatabaseReference.push().setValue(orders, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    mReference.push().setValue(orders);
                                    dialog.dismiss();
                                    Toast.makeText(ConfirmDetailsActivity.this, "Order Placed Successfully... We Will Contact You Soon.", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(ConfirmDetailsActivity.this, HomeActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            });
                        }
                    }
                });
                break;
            case "Laundry Pick Up":
                mSubscription_LinearLayout.setVisibility(View.GONE);
                mWater_LinearLayout.setVisibility(View.GONE);
                mLaundry_LinearLayout.setVisibility(View.VISIBLE);
                mName_TextView.setText(selectedName);
                mHouse_no_TextView.setText(selectedHouseNumber + " " + selectedLocality);
                mLaundry_TextView.setText(selectedLandmark);
                mPhoneNumber_TextView.setText(selectedMobileNumber);
                mEmail_TextView.setText(selectedEmail);
                mService_Type_Laundry_TextView.setText(mServiceType);

                mPlace_Order_Laundry_Button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isNetworkAvailable()) {
                            Toast.makeText(ConfirmDetailsActivity.this, "No Internet Connection.. Please Connect!!", Toast.LENGTH_SHORT).show();
                        } else {
                            dialog.show();
                            int month = calendar.get(Calendar.MONTH) + 1;
                            String currentDate = calendar.get(Calendar.DATE) + "/" + month + "/" + calendar.get(Calendar.YEAR);
                            String currentTime = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);
                            Log.d(Constants.TAG, currentDate + " " + currentTime);
                            if (!validateShirts(mShirts_Laundry_EditText)) {
                                dialog.dismiss();
                                Toast.makeText(ConfirmDetailsActivity.this, "Shirts Number is Required", Toast.LENGTH_LONG).show();
                                return;
                            }
                            if (!validateTrousers(mTrousers_Laundry_EditText)) {
                                dialog.dismiss();
                                Toast.makeText(ConfirmDetailsActivity.this, "Trouser Number is Required", Toast.LENGTH_LONG).show();
                                return;
                            }
                            if (!validateOthers(mOthers_Laundry_EditText)) {
                                dialog.dismiss();
                                Toast.makeText(ConfirmDetailsActivity.this, "Enter Valid Others Number", Toast.LENGTH_LONG).show();
                                return;
                            }
                            final YourOrders orders = new YourOrders(orderIdGenerator(), mServiceType, selectedName, selectedMobileNumber, selectedEmail, selectedHouseNumber, selectedLocality, selectedLandmark, mShirts_Laundry_EditText.getText().toString(), mTrousers_Laundry_EditText.getText().toString(), mOthers_Laundry_EditText.getText().toString(), currentDate, currentTime);
                            Log.d(Constants.TAG, "Shirts : " + mShirts_Laundry_EditText.getText().toString() + " trousers : " + mTrousers_Laundry_EditText.getText().toString() + " others : " + mOthers_Laundry_EditText.getText().toString());
                            mDatabaseReference.push().setValue(orders, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    mReference.push().setValue(orders);
                                    dialog.dismiss();
                                    Toast.makeText(ConfirmDetailsActivity.this, "Order Placed Successfully... We Will Contact You Soon.", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(ConfirmDetailsActivity.this, HomeActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            });
                        }
                    }
                });

                break;
            case "Three Months Subscription":
            case "Six Months Subscription":
            case "One Month Subscription":
                mLaundry_LinearLayout.setVisibility(View.GONE);
                mWater_LinearLayout.setVisibility(View.GONE);
                mSubscription_LinearLayout.setVisibility(View.VISIBLE);

                Instamojo.setBaseUrl(Constants.BASE_URL);

                mName_TextView.setText(selectedName);
                mHouse_no_TextView.setText(selectedHouseNumber + " " + selectedLocality);
                mLaundry_TextView.setText(selectedLandmark);
                mPhoneNumber_TextView.setText(selectedMobileNumber);
                mEmail_TextView.setText(selectedEmail);
                mService_Type_Subscription_TextView.setText(mServiceType);

                if (Objects.equals(mServiceType, "Three Months Subscription")) {
                    mPrice_TextView.setText(R.string.three_months_subscription_amount);
                    mDelivery_Charges_TextView.setText(R.string.delivery_charges_amount);
                    mTotal_Amount_TextView.setText(R.string.three_months_subscription_amount);
                    amount = "1050.00";
                } else if (Objects.equals(mServiceType, "Six Months Subscription")) {
                    mPrice_TextView.setText(R.string.six_months_subscription_amount);
                    mDelivery_Charges_TextView.setText(R.string.delivery_charges_amount);
                    mTotal_Amount_TextView.setText(R.string.six_months_subscription_amount);
                    amount = "2100.00";
                } else if (Objects.equals(mServiceType, "One Month Subscription")) {
                    mDelivery_Charges_TextView.setText(R.string.delivery_charges_amount);
                    mPrice_TextView.setText(R.string.one_months_subscription_amount);
                    mTotal_Amount_TextView.setText(R.string.one_months_subscription_amount);
                    amount = "370.00";
                }

                dialog = new ProgressDialog(this);
                dialog.setIndeterminate(true);
                dialog.setMessage("please wait...");
                dialog.setCancelable(false);
                mProceed_To_Payment_Button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isNetworkAvailable()) {
                            Toast.makeText(ConfirmDetailsActivity.this, "No Internet Connection.. Please Connect!!", Toast.LENGTH_SHORT).show();
                        } else {
                            fetchTokenAndTransactionID(selectedName, selectedMobileNumber, selectedEmail, mServiceType, amount);
                        }
                    }
                });
                Instamojo.setLogLevel(Log.DEBUG);
                break;
            default:
                Toast.makeText(this, "Unknown Behaviour: Please Try Again", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private boolean validateCans(EditText cansEditText) {
        String cans = cansEditText.getText().toString();
        if (TextUtils.isEmpty(cans)) {
            cansEditText.setError("Invalid Can Number.");
            return false;
        }
        return true;
    }

    private boolean validateShirts(EditText shirtsEditText) {
        String shirts = shirtsEditText.getText().toString();
        if (TextUtils.isEmpty(shirts)) {
            shirtsEditText.setError("Invalid Shirts Number.");
            return false;
        }
        return true;
    }

    private boolean validateTrousers(EditText trousersEditText) {
        String trousers = trousersEditText.getText().toString();
        if (TextUtils.isEmpty(trousers)) {
            trousersEditText.setError("Invalid Trousers Number.");
            return false;
        }
        return true;
    }

    private boolean validateOthers(EditText othersEditText) {
        String others = othersEditText.getText().toString();
        if (TextUtils.isEmpty(others)) {
            othersEditText.setError("Invalid Number.");
            return false;
        }
        return true;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void fetchTokenAndTransactionID(final String name, final String number, final String email, final String serviceType, final String amount) {
        if (!dialog.isShowing()) {
            dialog.show();
        }

        OkHttpClient client = new OkHttpClient();
        HttpUrl url = getHttpURLBuilder()
                .addPathSegment("create")
                .build();

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        showToast("Failed to fetch the Order Tokens, Please Try Again");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseString;
                String errorMessage = null;
                String transactionID = null;
                responseString = response.body().string();
                response.body().close();
                try {
                    JSONObject responseObject = new JSONObject(responseString);
                    if (responseObject.has("error")) {
                        errorMessage = responseObject.getString("error");
                    } else {
                        accessToken = responseObject.getString("access_token");
                        transactionID = responseObject.getString("transaction_id");
                    }
                } catch (JSONException e) {
                    errorMessage = "Something Went Wrong!! Please Try Again";
                }

                final String finalErrorMessage = errorMessage;
                final String finalTransactionID = transactionID;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }

                        if (finalErrorMessage != null) {
                            showToast(finalErrorMessage);
                            return;
                        }

                        createOrder(accessToken, finalTransactionID, name, number, email, serviceType, amount);
                    }
                });

            }
        });
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void createOrder(String accessToken, String transactionID, String name, String phone, String email, String serviceType, String amount) {
        if (Objects.equals(serviceType, "One Month Subscription")) {
            amount = "370.00";
        } else if (Objects.equals(serviceType, "Three Months Subscription")) {
            amount = "1050.00";
        } else if (Objects.equals(serviceType, "Six Months Subscription")) {
            amount = "2100.00";
        } else {
            Toast.makeText(this, "Something Went Wrong... Please Try Again!!", Toast.LENGTH_LONG).show();
            finish();
        }
        Order order = new Order(accessToken, transactionID, name, email, phone, amount, "Water Can " + serviceType);

        if (!order.isValid()) {

            if (!order.isValidName()) {
                Toast.makeText(this, "Buyer name is invalid", Toast.LENGTH_LONG).show();
            }

            if (!order.isValidEmail()) {
                Toast.makeText(this, "Buyer email is invalid", Toast.LENGTH_LONG).show();
            }

            if (!order.isValidPhone()) {
                Toast.makeText(this, "Buyer phone is invalid", Toast.LENGTH_LONG).show();
            }

            if (!order.isValidAmount()) {
                Toast.makeText(this, "Amount is invalid or has more than two decimal places", Toast.LENGTH_LONG).show();
            }

            if (!order.isValidDescription()) {
                Toast.makeText(this, "Description is invalid", Toast.LENGTH_LONG).show();
            }

            if (!order.isValidTransactionID()) {
                showToast("Transaction is Invalid");
            }

            if (!order.isValidRedirectURL()) {
                showToast("Redirection URL is invalid");
            }

            if (!order.isValidWebhook()) {
                showToast("Webhook URL is invalid");
            }
            return;
        }


        dialog.show();
        Request request = new Request(order, new OrderRequestCallBack() {
            @Override
            public void onFinish(final Order order, final Exception error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        if (error != null) {
                            if (error instanceof Errors.ConnectionError) {
                                showToast("No internet connection");
                            } else if (error instanceof Errors.ServerError) {
                                showToast("Server Error. Try again");
                            } else if (error instanceof Errors.AuthenticationError) {
                                showToast("Something went wrong! Please Try again");
                            } else if (error instanceof Errors.ValidationError) {
                                Errors.ValidationError validationError = (Errors.ValidationError) error;

                                if (!validationError.isValidTransactionID()) {
                                    showToast("Transaction ID is not Unique, Please Try Again.");
                                    return;
                                }

                                if (!validationError.isValidRedirectURL()) {
                                    showToast("Redirect url is invalid");
                                    return;
                                }

                                if (!validationError.isValidWebhook()) {
                                    showToast("Webhook url is invalid");
                                    return;
                                }

                                if (!validationError.isValidPhone()) {
                                    showToast("Buyer's Phone Number is invalid/empty");
                                    return;
                                }

                                if (!validationError.isValidAmount()) {
                                    showToast("Amount is either less than Rs.9 or has more than two decimal places");
                                    return;
                                }

                                if (!validationError.isValidName()) {
                                    showToast("Buyer's Name is required");
                                    return;
                                }
                            } else {
                                showToast(error.getMessage());
                            }
                            return;
                        }

                        startPreCreatedUI(order);
                    }
                });
            }
        });
        request.execute();
    }

    private void startPreCreatedUI(Order order) {
        //Using Pre created UI
        Intent intent = new Intent(getBaseContext(), PaymentDetailsActivity.class);
        intent.putExtra(com.instamojo.android.helpers.Constants.ORDER, order);
        startActivityForResult(intent, com.instamojo.android.helpers.Constants.REQUEST_CODE);
    }

    private void showToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private HttpUrl.Builder getHttpURLBuilder() {
        return new HttpUrl.Builder()
                .scheme("http")
                .host("insta.eu-4.evennode.com");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == com.instamojo.android.helpers.Constants.REQUEST_CODE && data != null) {
            String orderID = data.getStringExtra(com.instamojo.android.helpers.Constants.ORDER_ID);
            String transactionID = data.getStringExtra(com.instamojo.android.helpers.Constants.TRANSACTION_ID);
            String paymentID = data.getStringExtra(com.instamojo.android.helpers.Constants.PAYMENT_ID);

            // Check transactionID, orderID, and orderID for null before using them to check the Payment status.
            if (transactionID != null || paymentID != null) {
                checkPaymentStatus(transactionID, orderID, mServiceType);
            } else {
                showToast("Oops!! Payment was cancelled");
            }
        }
    }

    /**
     * Will check for the transaction status of a particular Transaction
     *
     * @param transactionID Unique identifier of a transaction ID
     */
    private void checkPaymentStatus(final String transactionID, final String orderID, final String serviceType) {
        if (accessToken == null || (transactionID == null && orderID == null)) {
            Toast.makeText(this, "Please Contact Our Customer Service", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ConfirmDetailsActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }

        showToast("checking transaction status");
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder builder = getHttpURLBuilder();
        builder.addPathSegment("status");
        builder.addQueryParameter("transaction_id", transactionID);
        builder.addQueryParameter("id", orderID);
        builder.addQueryParameter("authorization", "Bearer " + accessToken);
        HttpUrl url = builder.build();

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .build();
        String mRequest = request.toString();
        mRef.push().setValue(mRequest);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        new AlertDialog.Builder(ConfirmDetailsActivity.this)
                                .setTitle("Payment Error!")
                                .setMessage("Failed to fetch the Transaction status.. Please Contact Our Customer Care")
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(ConfirmDetailsActivity.this, HomeActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                })
                                .create()
                                .show();

                    }
                });
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseString = response.body().string();
                response.body().close();
                String status = null;
                String paymentID = null;
                String amount = null;
                String errorMessage = null;
                String failure = null;
                String reason = null;
                String message = null;

                try {
                    JSONObject responseObject = new JSONObject(responseString);
                    JSONObject payment = responseObject.getJSONArray("payments").getJSONObject(0);
                    status = payment.getString("status");
                    if (!Objects.equals(status, "successful")) {
                        failure = payment.getString("failure");
                    }
                    paymentID = payment.getString("id");
                    amount = responseObject.getString("amount");
                    JSONObject failureJSONObject = payment.getJSONObject("failure");
                    reason = failureJSONObject.getString("reason");
                    message = failureJSONObject.getString("message");

                } catch (JSONException e) {
                    errorMessage = "Failed to fetch the Transaction status, Please Contact Our Customer Service";
                }

                final String finalFailure = failure;
                final String finalStatus = status;
                final String finalErrorMessage = errorMessage;
                final String finalPaymentID = paymentID;
                final String finalAmount = amount;
                final String finalFailureReason = reason;
                final String finalFailureMessage = message;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        if (finalStatus == null) {
                            showToast(finalErrorMessage);
                            Intent intent = new Intent(ConfirmDetailsActivity.this, HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                        Intent intent = new Intent(ConfirmDetailsActivity.this, PaymentStatusActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("finalStatus", finalStatus);
                        bundle.putString("finalErrorMessage", finalErrorMessage);
                        bundle.putString("finalPaymentID", finalPaymentID);
                        bundle.putString("finalAmount", finalAmount);
                        bundle.putString("finalFailureReason", finalFailureReason);
                        bundle.putString("finalFailureMessage", finalFailureMessage);
                        bundle.putString("finalFailure", finalFailure);
                        bundle.putString("serviceType", serviceType);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }
        });
    }

    private List<String> generateUid() {
        String uid = mAuth.getCurrentUser().getUid();
        List<String> ret = new ArrayList<>((uid.length() + 6 - 1) / 6);

        for (int start = 0; start < uid.length(); start += 6) {
            ret.add(uid.substring(start, Math.min(uid.length(), start + 6)));
        }
        return ret;
    }

    private String generateRandNum() {
        Random random = new Random();
        return String.format("%03d", random.nextInt(1000));
    }

    private String orderIdGenerator() {
        String src = "abcdefghijklmnopqrstuvwxyz";
        return "OT" + generateUid().get(0) + generateRandSeq(3, src) + generateRandNum();
    }
}
