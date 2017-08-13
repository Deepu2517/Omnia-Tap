package in.desireplace.waytogo.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import in.desireplace.waytogo.R;
import in.desireplace.waytogo.adapters.YourOrdersAdapter;

public class YourOrdersActivity extends AppCompatActivity {

    private YourOrdersAdapter mAdapter;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_orders);

        RecyclerView ordersList = (RecyclerView) findViewById(R.id.orders_list);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        ordersList.setLayoutManager(manager);
        mAdapter = new YourOrdersAdapter(null);
        ordersList.setAdapter(mAdapter);

        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setMessage("please wait...");
        dialog.setCancelable(false);
        dialog.show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 3000);
    }
}
