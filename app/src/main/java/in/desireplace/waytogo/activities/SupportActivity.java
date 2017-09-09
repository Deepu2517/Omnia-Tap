package in.desireplace.waytogo.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import in.desireplace.waytogo.Constants;
import in.desireplace.waytogo.R;
import in.desireplace.waytogo.adapters.YourOrdersAdapter;
import in.desireplace.waytogo.models.SupportTicket;
import in.desireplace.waytogo.models.YourOrders;

public class SupportActivity extends AppCompatActivity implements YourOrdersAdapter.Callback {

    private EditText mEmailEditText, mAboutProblemEditText;

    private YourOrders mOrder;

    private DatabaseReference mDatabaseReference;

    private FirebaseAuth mAuth;

    private Dialog mDialog;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setMessage("please wait...");
        dialog.setCancelable(false);

        setTitle("Support");

        mAuth = FirebaseAuth.getInstance();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("SupportTicket");

        mEmailEditText = (EditText) findViewById(R.id.email_edit_text);
        mAboutProblemEditText = (EditText) findViewById(R.id.about_problem_edit_text);
        Button mSelectOrderButton = (Button) findViewById(R.id.select_order_button);
        Button mCreateTicketButton = (Button) findViewById(R.id.create_ticket_button);

        mSelectOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkAvailable()) {
                    Toast.makeText(SupportActivity.this, "No Internet Connection.. Please Connect!!", Toast.LENGTH_SHORT).show();
                } else {
                    showYourOrdersList();
                }
            }
        });
        mCreateTicketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isNetworkAvailable()) {
                    Toast.makeText(SupportActivity.this, "No Internet Connection.. Please Connect!!", Toast.LENGTH_SHORT).show();
                } else {
                    if (mOrder != null) {
                        SupportTicket ticket = new SupportTicket(mEmailEditText.getText().toString(), mAboutProblemEditText.getText().toString(), mAuth.getCurrentUser().getUid(), mOrder.getFullName(), mOrder.getOrderNumber(), mOrder.getServiceType(), mOrder.getMobileNumber(), mOrder.getHouseNumber(), mOrder.getLocality(), mOrder.getLandmark(), mOrder.getOrderDate(), mOrder.getOrderTime());
                        dialog.show();
                        mDatabaseReference.push().setValue(ticket, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                dialog.dismiss();
                                Log.d(Constants.TAG, "Is there any error : " + databaseError);
                                Toast.makeText(SupportActivity.this, "Ticket Created... We Will Contact You Soon.", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        });
                    } else {
                        Toast.makeText(SupportActivity.this, "Select an order first!!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void showYourOrdersList() {
        mDialog = new Dialog(SupportActivity.this);
        mDialog.setTitle("Choose The Order");
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mDialog.setContentView(R.layout.dialog_layout);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setCancelable(true);
        mDialog.show();
        RecyclerView rvTest = mDialog.findViewById(R.id.rvTest);
        rvTest.setHasFixedSize(true);
        rvTest.setLayoutManager(new LinearLayoutManager(SupportActivity.this));
        YourOrdersAdapter rvAdapter = new YourOrdersAdapter(this, this);
        rvTest.setAdapter(rvAdapter);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onItemClick(YourOrders orders) {
        mOrder = orders;
        if (mOrder != null) {
            mDialog.dismiss();
            Toast.makeText(this, "Order Selected", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Please Select A Valid Order", Toast.LENGTH_SHORT).show();
        }
    }
}
