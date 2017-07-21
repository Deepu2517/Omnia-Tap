package in.desireplace.waytogo.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import in.desireplace.waytogo.R;
import in.desireplace.waytogo.activities.LoginActivity;
import in.desireplace.waytogo.activities.SavedAddressesActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfilePageFragment extends Fragment {

    private FirebaseAuth mAuth;

    public ProfilePageFragment() {
        // Required empty public constructor
    }

    public static ProfilePageFragment newInstance() {
        ProfilePageFragment fragment = new ProfilePageFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile_page, container, false);
        TextView savedAddressesTextView = (TextView) rootView.findViewById(R.id.saved_addresses_text_view);
        TextView logOutTextView = (TextView) rootView.findViewById(R.id.log_out_text_view);
        savedAddressesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SavedAddressesActivity.class);
                intent.putExtra("identifier", "view");
                startActivity(intent);
            }
        });
        logOutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                Intent i = new Intent(getContext(), LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
        return rootView;
    }

}
