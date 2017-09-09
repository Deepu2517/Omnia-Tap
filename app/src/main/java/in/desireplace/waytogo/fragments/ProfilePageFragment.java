package in.desireplace.waytogo.fragments;


import android.Manifest;
import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
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

import in.desireplace.waytogo.R;
import in.desireplace.waytogo.activities.LoginActivity;
import in.desireplace.waytogo.activities.NotificationsActivity;
import in.desireplace.waytogo.activities.SavedAddressesActivity;
import in.desireplace.waytogo.activities.YourOrdersActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfilePageFragment extends Fragment {

    static final Integer CALL = 0x2;
    private FirebaseAuth mAuth;

    public ProfilePageFragment() {
        // Required empty public constructor
    }

    public static ProfilePageFragment newInstance() {
        return new ProfilePageFragment();
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
        TextView yourOrdersTextView = rootView.findViewById(R.id.your_orders_text_view);
        TextView savedAddressesTextView = rootView.findViewById(R.id.saved_addresses_text_view);
        TextView logOutTextView = rootView.findViewById(R.id.log_out_text_view);
        ImageView profilePicImageView = rootView.findViewById(R.id.profile_pic_image_view);
        TextView displayNameTextView = rootView.findViewById(R.id.display_name_text_view);
        TextView mobileNumberTextView = rootView.findViewById(R.id.user_mobile_number);
        TextView customerCareTextView = rootView.findViewById(R.id.customer_care_text_view);
        TextView rateUsTextView = rootView.findViewById(R.id.rate_us_text_view);
        TextView notificationsTextView = rootView.findViewById(R.id.notifications_text_view);

        notificationsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), NotificationsActivity.class));
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
            Picasso.with(getContext()).load(mAuth.getCurrentUser().getPhotoUrl()).into(profilePicImageView);
        } else {
            profilePicImageView.setBackgroundResource(R.drawable.profile_pic);
        }

        yourOrdersTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isNetworkAvailable()) {
                    Toast.makeText(getContext(), "No Internet Connection.. Please Connect!!", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(getContext(), YourOrdersActivity.class));
                }
            }
        });
        savedAddressesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isNetworkAvailable()) {
                    Toast.makeText(getContext(), "No Internet Connection.. Please Connect!!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getContext(), SavedAddressesActivity.class);
                    intent.putExtra("identifier", "view");
                    startActivity(intent);
                }
            }
        });
        logOutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isNetworkAvailable()) {
                    Toast.makeText(getContext(), "No Internet Connection.. Please Connect!!", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.signOut();
                    LoginManager.getInstance().logOut();
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    Intent i = new Intent(getContext(), LoginActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }
            }
        });
        rateUsTextView.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
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

        ImageView emailUsImageView = view.findViewById(R.id.email_us_image_view);
        ImageView callUsImageView = view.findViewById(R.id.call_us_image_view);

        emailUsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                emailIntent.setType("text/plain");
                emailIntent.setPackage("com.google.android.gm");
                if (emailIntent.resolveActivity(getContext().getPackageManager()) != null) {
                    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"omniatap.services@gmail.com"});
                    startActivity(emailIntent);
                } else
                    Toast.makeText(getContext(), "Gmail App is not installed", Toast.LENGTH_SHORT).show();
            }
        });

        callUsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPermissionGranted()) {
                    call_action();
                }
            }
        });
        builder.setPositiveButton("Cancel", null);
        builder.create().show();
    }

    public void call_action() {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + "9393781818"));
        startActivity(callIntent);
    }

    public boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getContext().checkSelfPermission(android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted");
                return true;
            } else {
                Log.v("TAG", "Permission is revoked");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted");
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 1: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                    call_action();
                } else {
                    Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
