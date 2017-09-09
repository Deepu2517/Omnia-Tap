package in.desireplace.waytogo.adapters;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import in.desireplace.waytogo.Constants;
import in.desireplace.waytogo.R;
import in.desireplace.waytogo.models.YourOrders;

public class YourOrdersAdapter extends RecyclerView.Adapter<YourOrdersAdapter.ViewHolder> {

    private final DatabaseReference mCheckRef;

    private List<YourOrders> mOrders;

    private Callback mCallback;

    private Context mContext;

    private ProgressDialog dialog;

    public YourOrdersAdapter(Callback callback, Context context) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String firebasePath = "users/" + mAuth.getCurrentUser().getUid();
        mCheckRef = FirebaseDatabase.getInstance().getReference().child(firebasePath);
        mOrders = new ArrayList<>();
        mCallback = callback;
        mContext = context;
        Log.d(Constants.TAG, context.getClass().getSimpleName());
        dialog = new ProgressDialog(context);
        dialog.setIndeterminate(true);
        dialog.setMessage("please wait...");
        dialog.setCancelable(false);
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(firebasePath + "/YourOrders");
        dialog.show();
        mCheckRef.addListenerForSingleValueEvent(new OrdersCheckListener());
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
        holder.mOrderIdTextView.setText(String.format("Order ID : %s", orders.getOrderNumber()));
        holder.mAddressOrderTextView.setText(String.format("Address : %s", fullAddress));
        holder.mServiceTypeOrderTextView.setText(String.format("Service Type : %s", orders.getServiceType()));
        holder.mMobileNumberTextView.setText(String.format("Mobile Number : %s", orders.getMobileNumber()));
        holder.mEmailTextView.setText(String.format("E-mail : %s", orders.getEmail()));
        holder.mDateTimeOrderTextView.setText("Order Date : " + orders.getOrderDate() + ", " + orders.getOrderTime());
        String service_type = orders.getServiceType();
        switch (service_type) {
            case "Laundry Pick Up":
                String shirts = orders.getShirts();
                String trousers = orders.getTrousers();
                String others = orders.getOthers();
                if (!Objects.equals(shirts, "") && !Objects.equals(trousers, "") && !Objects.equals(others, "")) {
                    int Shirts = Integer.parseInt(shirts);
                    int Trousers = Integer.parseInt(trousers);
                    int Others = Integer.parseInt(others);
                    int totalGarments = Shirts + Trousers + Others;
                    String TotalGarments = String.valueOf(totalGarments);
                    holder.mTotalGarmentsTextView.setText(String.format("Total Garments : %s", TotalGarments));
                } else {
                    Toast.makeText(mContext, "Order Is Invalid", Toast.LENGTH_SHORT).show();
                }
                break;
            case "Water Can Delivery":
                String cans = orders.getCans();
                if (!Objects.equals(cans, "")) {
                    int can = Integer.parseInt(cans);
                    String totalCans = String.valueOf(can);
                    holder.mTotalGarmentsTextView.setText(String.format("Total Cans : %s", totalCans));
                } else {
                    Toast.makeText(mContext, "Order Is Invalid", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                Log.e(Constants.TAG, "Invalid service_type : " + service_type);
                break;
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
            assert orders != null;
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
            mCheckRef.addListenerForSingleValueEvent(new OrdersCheckListener());
            String key = dataSnapshot.getKey();
            for (YourOrders orders : mOrders) {
                if (orders.getKey().equals(key)) {
                    mOrders.remove(orders);
                    notifyDataSetChanged();
                    return;
                }
            }
            mCheckRef.removeEventListener(new OrdersCheckListener());
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Crashlytics.log(7, "FirebaseDatabaseChildEventListenerError", databaseError.toString());
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mOrderIdTextView;
        private TextView mAddressOrderTextView;
        private TextView mServiceTypeOrderTextView;
        private TextView mMobileNumberTextView;
        private TextView mEmailTextView;
        private TextView mDateTimeOrderTextView;
        private TextView mTotalGarmentsTextView;
        private View mContainerView;
        public ViewHolder(View itemView) {
            super(itemView);
            mOrderIdTextView = itemView.findViewById(R.id.order_id_text_view);
            mDateTimeOrderTextView = itemView.findViewById(R.id.order_date_time_text_view);
            mAddressOrderTextView = itemView.findViewById(R.id.order_address_text_view);
            mMobileNumberTextView = itemView.findViewById(R.id.mobile_number_text_view);
            mEmailTextView = itemView.findViewById(R.id.email_orders_text_view);
            mServiceTypeOrderTextView = itemView.findViewById(R.id.service_type_orders_text_view);
            mTotalGarmentsTextView = itemView.findViewById(R.id.total_garments_text_view);
            mContainerView = itemView.findViewById(R.id.orders_main_view);
        }
    }

    private class OrdersCheckListener implements ValueEventListener {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (!dataSnapshot.hasChild("YourOrders")) {
                dialog.dismiss();
                Toast.makeText(mContext, "No Orders Found", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }
}
