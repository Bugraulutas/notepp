package com.bgr2.notepp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button button_forgetpassword_sendbutton;
    private EditText edittext_forgetpassword_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        button_forgetpassword_sendbutton=findViewById(R.id.button_forgetpassword_sendbutton);
        edittext_forgetpassword_email=findViewById(R.id.edittext_forgetpassword_email);

        
        mAuth=FirebaseAuth.getInstance();


        button_forgetpassword_sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=edittext_forgetpassword_email.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(getApplicationContext(), "Mail adresinizi girin.", Toast.LENGTH_SHORT).show();
                }

                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(), "Başarıyla Gönderildi", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(getApplicationContext(), "Gönderilemedi", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
        });
    }
}