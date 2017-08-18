package in.desireplace.waytogo.adapters;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import in.desireplace.waytogo.Constants;
import in.desireplace.waytogo.R;
import in.desireplace.waytogo.models.YourOrders;

public class YourOrdersAdapter extends RecyclerView.Adapter<YourOrdersAdapter.ViewHolder> {

    private DatabaseReference mDatabaseReference;

    private List<YourOrders> mOrders;

    private FirebaseAuth mAuth;

    private Callback mCallback;

    private Context mContext;

    private ProgressDialog dialog;

    public YourOrdersAdapter(Callback callback, Context context) {
        mAuth = FirebaseAuth.getInstance();
        String firebasePath = "users/" + mAuth.getCurrentUser().getUid();
        mOrders = new ArrayList<>();
        mCallback = callback;
        mContext = context;
        Log.d(Constants.TAG, context.getClass().getSimpleName());
        dialog = new ProgressDialog(context);
        dialog.setIndeterminate(true);
        dialog.setMessage("please wait...");
        dialog.setCancelable(false);
        if (getItemCount() == 0) {
            dialog.show();
        }
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 12000);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(firebasePath + "/YourOrders");
        mDatabaseReference.addChildEventListener(new YourOrdersChildEventListener());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_single_item_your_orders, parent, false);
        return new ViewHolder(view);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final YourOrders orders = mOrders.get(position);
        String fullAddress =  orders.getHouseNumber() + " " + orders.getLocality();
        holder.mOrderIdTextView.setText("Order ID : " + orders.getOrderNumber());
        holder.mAddressOrderTextView.setText("Address : " + fullAddress);
        holder.mServiceTypeOrderTextView.setText("Service Type : " + orders.getServiceType());
        holder.mMobileNumberTextView.setText("Mobile Number : " + orders.getMobileNumber());
        holder.mDateTimeOrderTextView.setText("Order Date : " + orders.getOrderDate() + ", " + orders.getOrderTime());
        String service_type = orders.getServiceType();
        if (service_type.equals("Laundry Pick Up")) {
            String shirts = orders.getShirts();
            String trousers = orders.getTrousers();
            String others = orders.getOthers();
            try{
                int Shirts = Integer.parseInt(shirts);
                int Trousers = Integer.parseInt(trousers);
                int Others = Integer.parseInt(others);
                int totalGarments = Shirts + Trousers + Others;
                String TotalGarments = String.valueOf(totalGarments);
                holder.mTotalGarmentsTextView.setText("Total Garments : " + TotalGarments);
            }catch(NumberFormatException ex){
                Log.e(Constants.TAG, "Error is : " + ex.getMessage());
            }
        } else if (service_type.equals("Water Can Delivery")) {
            String cans = orders.getCans();
            int can = Integer.parseInt(cans);
            String totalCans = String.valueOf(can);
            holder.mTotalGarmentsTextView.setText("Total Cans : " + totalCans);
        } else {
            Log.e(Constants.TAG, "Invalid service_type : " + service_type);
        }
        if (!Objects.equals(mContext.getClass().getSimpleName(), "YourOrdersActivity")) {
            holder.mContainerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.onItemClick(orders);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return (null != mOrders ? mOrders.size() : 0);
    }

    public interface Callback {
        void onItemClick(YourOrders orders);
    }

    private class YourOrdersChildEventListener implements ChildEventListener {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            YourOrders orders = dataSnapshot.getValue(YourOrders.class);
            orders.setKey(dataSnapshot.getKey());
            mOrders.add(0, orders);
            notifyDataSetChanged();
            dialog.dismiss();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            String key = dataSnapshot.getKey();
            YourOrders updatedOrder = dataSnapshot.getValue(YourOrders.class);
            for (YourOrders orders : mOrders) {
                if (orders.getKey().equals(key)) {
                    orders.setValues(updatedOrder);
                    notifyDataSetChanged();
                }
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            String key = dataSnapshot.getKey();
            for (YourOrders orders : mOrders) {
                if (orders.getKey().equals(key)) {
                    mOrders.remove(orders);
                    notifyDataSetChanged();
                    return;
                }
            }
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.e(Constants.TAG, "Database Error: " + databaseError);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mOrderIdTextView;
        private TextView mAddressOrderTextView;
        private TextView mServiceTypeOrderTextView;
        private TextView mMobileNumberTextView;
        private TextView mDateTimeOrderTextView;
        private TextView mTotalGarmentsTextView;
        private View mContainerView;
        public ViewHolder(View itemView) {
            super(itemView);
            mOrderIdTextView = (TextView) itemView.findViewById(R.id.order_id_text_view);
            mDateTimeOrderTextView = (TextView) itemView.findViewById(R.id.order_date_time_text_view);
            mAddressOrderTextView = (TextView) itemView.findViewById(R.id.order_address_text_view);
            mMobileNumberTextView = (TextView) itemView.findViewById(R.id.mobile_number_text_view);
            mServiceTypeOrderTextView = (TextView) itemView.findViewById(R.id.service_type_orders_text_view);
            mTotalGarmentsTextView = (TextView) itemView.findViewById(R.id.total_garments_text_view);
            mContainerView = itemView.findViewById(R.id.orders_main_view);
        }
    }
}
