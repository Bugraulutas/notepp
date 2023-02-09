package com.bgr2.notepp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddActivity extends AppCompatActivity {
    private ImageView imageview_add_backbutton;
    private EditText edittextmultiline_add_title,edittexttextmultiline_add_note;

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add);

        imageview_add_backbutton=findViewById(R.id.imageview_add_backbutton);
        edittextmultiline_add_title=findViewById(R.id.edittextmultiline_add_title);
        edittexttextmultiline_add_note=findViewById(R.id.edittextmultiline_add_note);
        String user1= FirebaseAuth.getInstance().getUid();

        database = FirebaseDatabase.getInstance("https://noteapplication-113b6-default-rtdb.europe-west1.firebasedatabase.app/");
        myRef = database.getReference(user1);

        imageview_add_backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title=edittextmultiline_add_title.getText().toString().trim();
                String note=edittexttextmultiline_add_note.getText().toString().trim();

                Notes notes=new Notes("",title,note);
                myRef.push().setValue(notes);

                finish();
            }
        });




       

    }

    @Override
    public void onBackPressed() {
        String title=edittextmultiline_add_title.getText().toString().trim();
        String note=edittexttextmultiline_add_note.getText().toString().trim();

        Notes notes=new Notes("",title,note);
        myRef.push().setValue(notes);

        finish();

    }
}