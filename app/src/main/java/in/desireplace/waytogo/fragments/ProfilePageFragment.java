package in.desireplace.waytogo.fragments;


import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import in.desireplace.waytogo.Constants;
import in.desireplace.waytogo.R;
import in.desireplace.waytogo.activities.LoginActivity;
import in.desireplace.waytogo.activities.SavedAddressesActivity;
import in.desireplace.waytogo.activities.YourOrdersActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfilePageFragment extends Fragment {

    private FirebaseAuth mAuth;

    private Bitmap mBitmap;

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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile_page, container, false);
        TextView yourOrdersTextView = (TextView) rootView.findViewById(R.id.your_orders_text_view);
        TextView savedAddressesTextView = (TextView) rootView.findViewById(R.id.saved_addresses_text_view);
        TextView logOutTextView = (TextView) rootView.findViewById(R.id.log_out_text_view);
        ImageView profilePicImageView = (ImageView) rootView.findViewById(R.id.profile_pic_image_view);
        TextView displayNameTextView = (TextView) rootView.findViewById(R.id.display_name_text_view);
        TextView mobileNumberTextView = (TextView) rootView.findViewById(R.id.user_mobile_number);
        TextView customerCareTextView = (TextView) rootView.findViewById(R.id.customer_care_text_view);
        TextView rateUsTextView = (TextView) rootView.findViewById(R.id.rate_us_text_view);
        TextView notificationsTextView = (TextView) rootView.findViewById(R.id.notifications_text_view);

        notificationsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Coming Soon!!!!!", Toast.LENGTH_SHORT).show();
            }
        });

        customerCareTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog(inflater);
            }
        });

        if (mAuth.getCurrentUser().getDisplayName() != null) {
            displayNameTextView.setText(mAuth.getCurrentUser().getDisplayName());
        } else {
            displayNameTextView.setVisibility(View.GONE);
        }

        if (mAuth.getCurrentUser().getPhoneNumber() != null) {
            mobileNumberTextView.setText(mAuth.getCurrentUser().getPhoneNumber());
        } else {
            mobileNumberTextView.setVisibility(View.GONE);
        }

        if (mAuth.getCurrentUser().getPhotoUrl() != null) {
            Log.d(Constants.TAG, "if part of code is executing");
            Picasso.with(getContext()).load(mAuth.getCurrentUser().getPhotoUrl()).into(profilePicImageView);
        } else {
            profilePicImageView.setBackgroundResource(R.drawable.profile_pic);
        }

        yourOrdersTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), YourOrdersActivity.class));
            }
        });
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
                LoginManager.getInstance().logOut();
                getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                Intent i = new Intent(getContext(), LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
        rateUsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("market://details?id=" + getContext().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getContext().getPackageName())));
                }
            }
        });
        return rootView;
    }

    private void showAlertDialog(LayoutInflater inflater) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Contact Us");
        View view = inflater.inflate(R.layout.dailog_customer_care, null, false);
        builder.setView(view);

        ImageView emailUsImageView = (ImageView) view.findViewById(R.id.email_us_image_view);
        ImageView callUsImageView = (ImageView) view.findViewById(R.id.call_us_image_view);

        emailUsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                emailIntent.setType("text/plain");
                emailIntent.setPackage("com.google.android.gm");
                if (emailIntent.resolveActivity(getContext().getPackageManager())!=null) {
                    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"omniatap.services@gmail.com"});
                    startActivity(emailIntent);
                }
                else
                    Toast.makeText(getContext(),"Gmail App is not installed", Toast.LENGTH_SHORT).show();
            }
        });

        callUsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:9393781818"));

                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(callIntent);
            }
        });
        builder.setPositiveButton("Cancel", null);
        builder.create().show();
    }
}
