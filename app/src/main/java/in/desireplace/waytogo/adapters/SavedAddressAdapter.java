package in.desireplace.waytogo.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

import in.desireplace.waytogo.R;
import in.desireplace.waytogo.activities.SavedAddressesActivity;
import in.desireplace.waytogo.models.SavedAddresses;

public class SavedAddressAdapter extends RecyclerView.Adapter<SavedAddressAdapter.ViewHolder> {

    private DatabaseReference mDatabaseReference;
    private DatabaseReference mCheckRef;

    private List<SavedAddresses> mAddresses;

    private Callback mCallback;

    private ProgressDialog dialog;

    private SavedAddressesActivity addressesActivity;

    public SavedAddressAdapter(Callback callback, final Context context, SavedAddressesActivity activity) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String firebasePath = "users/" + mAuth.getCurrentUser().getUid() + "/";
        mAddresses = new ArrayList<>();
        mCallback = callback;
        addressesActivity = activity;
        mCheckRef = FirebaseDatabase.getInstance().getReference().child(firebasePath);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(firebasePath + "SavedAddresses");
        dialog = new ProgressDialog(context);
        dialog.setIndeterminate(true);
        dialog.setMessage("please wait...");
        dialog.setCancelable(false);
        dialog.show();
        mCheckRef.addListenerForSingleValueEvent(new SavedAddressCheckListener());
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
        holder.mEmailTextView.setText(addresses.getEmail());
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

    public void firebaseUpdate(SavedAddresses addresses, String newFullName, String newMobileNumber, String newEmail, String newHouseNumber, String newLocality, String newLandmark) {
        addresses.setFullName(newFullName);
        addresses.setMobileNumber(newMobileNumber);
        addresses.setEmail(newEmail);
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
            assert addresses != null;
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
            mCheckRef.addListenerForSingleValueEvent(new SavedAddressCheckListener());
            String key = dataSnapshot.getKey();
            for (SavedAddresses addresses : mAddresses) {
                if (addresses.getKey().equals(key)) {
                    mAddresses.remove(addresses);
                    notifyDataSetChanged();
                    dialog.dismiss();
                    return;
                }
            }
            mCheckRef.removeEventListener(new SavedAddressCheckListener());
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Crashlytics.log(7, "FirebaseDatabaseChildEventListenerError", databaseError.toString());
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mNameTextView;
        private TextView mAddressTextView;
        private TextView mMobileNumberTextView;
        private TextView mEmailTextView;
        private ImageView mEditButton;
        private View mContainerView;

        public ViewHolder(View itemView) {
            super(itemView);
            mNameTextView = itemView.findViewById(R.id.name_text);
            mAddressTextView = itemView.findViewById(R.id.address_text);
            mMobileNumberTextView = itemView.findViewById(R.id.mobile_number_text);
            mEmailTextView = itemView.findViewById(R.id.email_text);
            mEditButton = itemView.findViewById(R.id.edit_button);
            mContainerView = itemView.findViewById(R.id.main_view);
        }
    }

    private class SavedAddressCheckListener implements ValueEventListener {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (!dataSnapshot.hasChild("SavedAddresses")) {
                dialog.dismiss();
                Toast.makeText(addressesActivity, "No Addresses Found", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }
}
