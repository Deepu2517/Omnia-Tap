package in.desireplace.waytogo.activities;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import in.desireplace.waytogo.R;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private static final int RC_SIGN_IN = 1;
    private AutoCompleteTextView mPhoneNumberEditText;
    private EditText mCodeEditText;
    private TextView mResendTextView;
    private Button mLoginButton;
    private FirebaseAuth mAuth;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private Boolean visible;
    private GoogleApiClient mGoogleApiClient;
    private CallbackManager mCallbackManager;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        initializeGoogle();

        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setMessage("please wait...");
        dialog.setCancelable(false);

        mCallbackManager = CallbackManager.Factory.create();

        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setHeight(48);
        loginButton.setText(R.string.facebook_button_text);
        loginButton.setReadPermissions(Collections.singletonList("email"));

        SignInButton mGoogleSignInButton = (SignInButton) findViewById(R.id.google_sign_in_button);
        mPhoneNumberEditText = (AutoCompleteTextView) findViewById(R.id.phone_number_edit_text);
        mCodeEditText = (EditText) findViewById(R.id.code_edit_text);
        mResendTextView = (TextView) findViewById(R.id.resend_text_view);
        mLoginButton = (Button) findViewById(R.id.log_in_button);

        mCodeEditText.setVisibility(View.INVISIBLE);
        mResendTextView.setVisibility(View.INVISIBLE);

        visible = false;

        mLoginButton.setOnClickListener(this);
        mResendTextView.setOnClickListener(this);
        mGoogleSignInButton.setOnClickListener(this);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onVerificationFailed(FirebaseException e) {
                Crashlytics.log(2, "FirebasePhoneAuthVerificationFailed", e.toString());
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    mPhoneNumberEditText.setError("Invalid phone number.");
                    if (dialog.isShowing() || dialog != null) {
                        dialog.dismiss();
                    }
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    onLoginError("Quota exceeded. Please Contact Our Customer Care");
                    if (dialog.isShowing() || dialog != null) {
                        dialog.dismiss();
                    }
                } else if (e instanceof  FirebaseAuthUserCollisionException) {
                    onLoginError("Please Login With The Email You Used Last Time To Log In");
                    if (dialog.isShowing() || dialog != null) {
                        dialog.dismiss();
                    }
                } else if (e instanceof FirebaseNetworkException) {
                    Toast.makeText(LoginActivity.this, "Please Check Your Network Connection and Try Again", Toast.LENGTH_LONG).show();
                    if (dialog.isShowing() || dialog != null) {
                        dialog.dismiss();
                    }
                } else if (e instanceof FirebaseAuthInvalidUserException)  {
                    if (Objects.equals(((FirebaseAuthInvalidUserException) e).getErrorCode(), "ERROR_USER_DISABLED")) {
                        onLoginError("User Has Been Disabled.. Please Use Another Account To Login");
                        if (dialog.isShowing() || dialog != null) {
                            dialog.dismiss();
                        }
                    } else if (Objects.equals(((FirebaseAuthInvalidUserException) e).getErrorCode(), "ERROR_USER_NOT_FOUND")) {
                        onLoginError("User Has Been Deleted");
                        if (dialog.isShowing() || dialog != null) {
                            dialog.dismiss();
                        }
                    } else if (Objects.equals(((FirebaseAuthInvalidUserException) e).getErrorCode(), "ERROR_USER_TOKEN_EXPIRED")) {
                        Toast.makeText(LoginActivity.this, "Please Try Again...", Toast.LENGTH_LONG).show();
                        if (dialog.isShowing() || dialog != null) {
                            dialog.dismiss();
                        }
                    } else if (Objects.equals(((FirebaseAuthInvalidUserException) e).getErrorCode(), "ERROR_INVALID_USER_TOKEN")) {
                        Toast.makeText(LoginActivity.this, "Please Try Again...", Toast.LENGTH_LONG).show();
                        if (dialog.isShowing() || dialog != null) {
                            dialog.dismiss();
                        }
                    }
                }
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                mVerificationId = verificationId;
                mResendToken = token;
            }
        };
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Log In With Facebook Cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Crashlytics.log(3, "FacebookCallbackLoginError", error.toString());
                Toast.makeText(LoginActivity.this, "Please Try Again...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                Crashlytics.log(1, "GoogleActivityResultLoginError", result.getStatus().toString());
                Toast.makeText(this, result.getStatus().toString() , Toast.LENGTH_SHORT).show();
                Snackbar.make(findViewById(R.id.main), "Google Sign-In Failed!!", Snackbar.LENGTH_LONG).show();
            }
        } else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleFacebookAccessToken(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        dialog.show();
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = task.getResult().getUser();
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    dialog.dismiss();
                    startActivity(intent);
                } else {
                    if(task.getException() instanceof FirebaseAuthUserCollisionException) {
                        onLoginError("Sign In using the method you used for the first time to sign in!");
                        dialog.dismiss();
                        LoginManager.getInstance().logOut();
                        mAuth.signOut();
                    }
                    Crashlytics.log(3, "FacebookHandleAccessTokenError", task.getException().toString());
                }
            }
        });
    }

    public void onLoginError(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Login Error!")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .create()
                .show();
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        dialog.show();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            dialog.dismiss();
                            startActivity(intent);
                        } else {
                            Crashlytics.log(1, "FirebaseAuthWithGoogleFailureError", task.getException().toString());
                        }
                    }
                });
    }

    private void initializeGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        Toast.makeText(this, "Please Wait... ", Toast.LENGTH_SHORT).show();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                        } else {
                            Crashlytics.log(2, "SignInWithPhoneAuthCredentialFailureError", task.getException().toString());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                mCodeEditText.setError("Invalid code.");
                            }
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    private boolean validatePhoneNumber() {
        String phoneNumber = mPhoneNumberEditText.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            mPhoneNumberEditText.setError("Invalid phone number.");
            return false;
        }
        return true;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        onLoginError("Google Connection Failed!!");
        Crashlytics.log(1, "GoogleConnectionFailedError", connectionResult.getErrorMessage());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.log_in_button:
                if (visible) {
                    String code = mCodeEditText.getText().toString();
                    if (TextUtils.isEmpty(code)) {
                        mCodeEditText.setError("Cannot be empty.");
                        return;
                    }
                    verifyPhoneNumberWithCode(mVerificationId, code);
                }
                if (!validatePhoneNumber()) {
                    return;
                }
                mCodeEditText.setVisibility(View.VISIBLE);
                mResendTextView.setVisibility(View.VISIBLE);
                visible = true;
                mLoginButton.setText(R.string.verify_button_text);
                startPhoneNumberVerification("+91" + mPhoneNumberEditText.getText().toString());
                break;
            case R.id.resend_text_view:
                resendVerificationCode(mPhoneNumberEditText.getText().toString(), mResendToken);
                Toast.makeText(LoginActivity.this, "OTP SENT AGAIN SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                break;
            case R.id.google_sign_in_button:
                Intent googleSignInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(googleSignInIntent, RC_SIGN_IN);
                break;
            default:
                break;
        }
    }


}
