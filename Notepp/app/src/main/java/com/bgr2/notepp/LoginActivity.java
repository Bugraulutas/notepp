package com.bgr2.notepp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private EditText edittext_login_email,edittext_login_password;
    private Button button_login_signin;
    private TextView textview_login_createaccount,textview_login_forgotpassword;
    private FirebaseAuth mAuth;
    private SignInButton signInButton;

    private static final int RC_SIGN_IN = 1001;
    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edittext_login_email=findViewById(R.id.edittext_login_email);
        edittext_login_password=findViewById(R.id.edittext_login_password);
        button_login_signin =findViewById(R.id.button_login_signin);
        signInButton=findViewById(R.id.button_login_signingoogle);
        textview_login_createaccount=findViewById(R.id.textview_login_createaccount);
        textview_login_forgotpassword=findViewById(R.id.textview_login_forgotpassword);

        mAuth=FirebaseAuth.getInstance();

        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("490305265890-l52ernn6r9gontfpoiv2asf8ir6v515p.apps.googleusercontent.com")
                .requestEmail()
                .build();
        googleSignInClient=GoogleSignIn.getClient(this,gso);

        button_login_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=edittext_login_email.getText().toString().trim();
                String password=edittext_login_password.getText().toString().trim();

                if(TextUtils.isEmpty(email)||TextUtils.isEmpty(password)){
                    Toast.makeText(getApplicationContext(), "Lütfen email veya parola bölümünü doldurun", Toast.LENGTH_SHORT).show();
                }
               else  if(password.length()<6){
                    Toast.makeText(getApplicationContext(),"Parola en az 6 haneli olmalıdır",Toast.LENGTH_SHORT).show();

                }
                else{
                    mAuth.signInWithEmailAndPassword(email,password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                        finish();
                                    }else{
                                        Toast.makeText(getApplicationContext(),task.getException().getMessage()+ "", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                }

            }
        });

        textview_login_createaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,CreateAccountActivity.class);
                startActivity(intent);

            }
        });
        textview_login_forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,ForgetPasswordActivity.class));
                finish();
            }
        });
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        if(mAuth.getCurrentUser()!=null){
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }
     
    }
    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In basarili oldugunda Firebase ile yetkilendir
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);

            } else {
                // Google Sign In hatası.
                //Log.e(TAG, "Google Sign In hatası.");
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        // Log.d(TAG, "firebaseAuthWithGooogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //             Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());


                        if (!task.isSuccessful()) {
                            //               Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Yetkilendirme hatası.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }
                    }
                });}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }
}
