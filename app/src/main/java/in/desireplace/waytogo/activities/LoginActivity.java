package in.desireplace.waytogo.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import in.desireplace.waytogo.Constants;
import in.desireplace.waytogo.R;

public class LoginActivity extends AppCompatActivity {

    AutoCompleteTextView mPhoneNumberEditText;

    EditText mCodeEditText;

    TextView mResendTextView;

    Button mLoginButton;

    FirebaseAuth mAuth;

    String mVerificationId;

    private PhoneAuthProvider.ForceResendingToken mResendToken;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private Boolean visible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        mPhoneNumberEditText = (AutoCompleteTextView) findViewById(R.id.phone_number_edit_text);
        mCodeEditText = (EditText) findViewById(R.id.code_edit_text);
        mResendTextView = (TextView) findViewById(R.id.resend_text_view);
        mLoginButton = (Button) findViewById(R.id.log_in_button);
        mCodeEditText.setVisibility(View.INVISIBLE);
        mResendTextView.setVisibility(View.INVISIBLE);
        visible = false;
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                mLoginButton.setText("Verify");
                startPhoneNumberVerification(mPhoneNumberEditText.getText().toString());
            }
        });
        mResendTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendVerificationCode(mPhoneNumberEditText.getText().toString(), mResendToken);
                Toast.makeText(LoginActivity.this, "OTP SENT AGAIN", Toast.LENGTH_SHORT).show();
            }
        });
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Log.d(Constants.TAG, "onVerificationCompleted:" + phoneAuthCredential);
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w(Constants.TAG, "onVerificationFailed", e);
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    mPhoneNumberEditText.setError("Invalid phone number.");
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Toast.makeText(LoginActivity.this, "Quota exceeded.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                Log.d(Constants.TAG, "onCodeSent:" + verificationId);
                mVerificationId = verificationId;
                mResendToken = token;
            }
        };
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,      // Activity (for callback binding)
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
                this,      // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(Constants.TAG, "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                        } else {
                            Log.w(Constants.TAG, "signInWithCredential:failure", task.getException());
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
        Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
    }

    private boolean validatePhoneNumber() {
        String phoneNumber = mPhoneNumberEditText.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            mPhoneNumberEditText.setError("Invalid phone number.");
            return false;
        }
        return true;
    }
}
