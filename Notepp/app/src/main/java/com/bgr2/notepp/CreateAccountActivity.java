package com.bgr2.notepp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CreateAccountActivity extends AppCompatActivity {
    private EditText edittext_createaccount_email,edittext_createaccount_password;
    private Button button_createaccount_register;
    private FirebaseAuth auth;


        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        edittext_createaccount_email=findViewById(R.id.edittext_createaccount_email);
        edittext_createaccount_password=findViewById(R.id.edittext_createaccount_password);
        button_createaccount_register=findViewById(R.id.button_createaccount_register);
        auth=FirebaseAuth.getInstance();

        button_createaccount_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=edittext_createaccount_email.getText().toString().trim();
                String password=edittext_createaccount_password.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(getApplicationContext(),"Lütfen emailinizi giriniz",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(getApplicationContext(),"Lütfen parolanızı giriniz",Toast.LENGTH_SHORT).show();

                }
                if(password.length()<6){
                    Toast.makeText(getApplicationContext(),"Parola en az 6 haneli olmalıdır",Toast.LENGTH_SHORT).show();

                }

                auth.createUserWithEmailAndPassword(email,password)
                            .addOnCompleteListener(CreateAccountActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful()){
                                    //FirebaseUser user = auth.getCurrentUser();
                                    startActivity(new Intent(CreateAccountActivity.this, MainActivity.class));
                                    finish();
                                    finishAffinity();

                                    //apı key hatası veriyorsa build->clean build yap.

                                } else {
                                    Toast.makeText(CreateAccountActivity.this, task.getException().getMessage()+"Yetkilendirme Hatası",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

    }

}