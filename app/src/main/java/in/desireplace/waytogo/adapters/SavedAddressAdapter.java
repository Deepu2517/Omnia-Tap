package in.desireplace.waytogo.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import in.desireplace.waytogo.Constants;
import in.desireplace.waytogo.R;
import in.desireplace.waytogo.models.SavedAddresses;

public class SavedAddressAdapter extends RecyclerView.Adapter<SavedAddressAdapter.ViewHolder>{

    private DatabaseReference mDatabaseReference;

    private List<SavedAddresses> mAddresses;

    private Callback mCallback;

    private FirebaseAuth mAuth;

    private ProgressDialog dialog;

    public SavedAddressAdapter(Callback callback, Context context) {
        mAuth = FirebaseAuth.getInstance();
        String firebasePath = "users/" + mAuth.getCurrentUser().getUid();
        mAddresses = new ArrayList<>();
        mCallback = callback;
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(firebasePath + "/SavedAddresses");
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
        mDatabaseReference.addChildEventListener(new SavedAddressChildEventListener());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_single_item_addresses, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final SavedAddresses addresses = mAddresses.get(position);
        holder.mNameTextView.setText(addresses.getFullName());
        String fullAddress = addresses.getHouseNumber() + " " + addresses.getLocality() + " Bangalore \n" + "Near " + addresses.getLandmark();
        holder.mAddressTextView.setText(fullAddress);
        holder.mMobileNumberTextView.setText(addresses.getMobileNumber());
        holder.mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onEditButtonClick(addresses);
            }
        });
        holder.mContainerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onItemClick(addresses);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != mAddresses ? mAddresses.size() : 0);
    }

    public void firebaseAdd(SavedAddresses addresses) {
        mDatabaseReference.push().setValue(addresses);
    }

    public void firebaseUpdate(SavedAddresses addresses, String newFullName, String newMobileNumber, String newHouseNumber, String newLocality, String newLandmark) {
        addresses.setFullName(newFullName);
        addresses.setMobileNumber(newMobileNumber);
        addresses.setHouseNumber(newHouseNumber);
        addresses.setLocality(newLocality);
        addresses.setLandmark(newLandmark);
        mDatabaseReference.child(addresses.getKey()).setValue(addresses);
    }

    public void firebaseDelete(SavedAddresses addresses) {
        mDatabaseReference.child(addresses.getKey()).removeValue();
    }

    public interface Callback {
        void onEditButtonClick(SavedAddresses addresses);
        void onItemClick(SavedAddresses addresses);
    }

    private class SavedAddressChildEventListener implements ChildEventListener {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            SavedAddresses addresses = dataSnapshot.getValue(SavedAddresses.class);
            addresses.setKey(dataSnapshot.getKey());
            mAddresses.add(0, addresses);
            notifyDataSetChanged();
            dialog.dismiss();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            String key = dataSnapshot.getKey();
            SavedAddresses updatedAddress = dataSnapshot.getValue(SavedAddresses.class);
            for (SavedAddresses addresses : mAddresses) {
                if (addresses.getKey().equals(key)) {
                    addresses.setValues(updatedAddress);
                    notifyDataSetChanged();
                    dialog.dismiss();
                }
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            String key = dataSnapshot.getKey();
            for (SavedAddresses addresses : mAddresses) {
                if (addresses.getKey().equals(key)) {
                    mAddresses.remove(addresses);
                    notifyDataSetChanged();
                    dialog.dismiss();
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mNameTextView;
        private TextView mAddressTextView;
        private TextView mMobileNumberTextView;
        private ImageView mEditButton;
        private View mContainerView;

        public ViewHolder(View itemView) {
            super(itemView);
            mNameTextView = (TextView) itemView.findViewById(R.id.name_text);
            mAddressTextView = (TextView) itemView.findViewById(R.id.address_text);
            mMobileNumberTextView = (TextView) itemView.findViewById(R.id.mobile_number_text);
            mEditButton = (ImageView) itemView.findViewById(R.id.edit_button);
            mContainerView = itemView.findViewById(R.id.main_view);
        }
    }
}
