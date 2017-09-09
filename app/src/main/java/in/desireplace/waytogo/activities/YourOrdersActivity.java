package in.desireplace.waytogo.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import in.desireplace.waytogo.Constants;
import in.desireplace.waytogo.R;
import in.desireplace.waytogo.adapters.YourOrdersAdapter;
import in.desireplace.waytogo.models.YourOrders;

public class YourOrdersActivity extends AppCompatActivity implements YourOrdersAdapter.Callback{

    private YourOrdersAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_orders);

        setTitle("Your Orders");

        RecyclerView ordersList = (RecyclerView) findViewById(R.id.orders_list);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        ordersList.setLayoutManager(manager);
        mAdapter = new YourOrdersAdapter(null, this);
        ordersList.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(YourOrders orders) {
        Log.wtf(Constants.TAG, "Google Is Awesome!!!");
    }
}
