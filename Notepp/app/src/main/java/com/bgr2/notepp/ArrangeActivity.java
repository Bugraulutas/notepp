package com.bgr2.notepp;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ArrangeActivity extends AppCompatActivity {
    private ImageView imageview_arrange_backbutton, imageview_arrange_delete;
    private EditText edittextmultiline_arrange_title,edittexttextmultiline_arrange_note;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private Notes notes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrange);

        imageview_arrange_backbutton=findViewById(R.id.imageview_arrange_backbutton);
        imageview_arrange_delete=findViewById(R.id.imageview_arrange_delete);
        edittextmultiline_arrange_title=findViewById(R.id.edittextmultiline_arrange_title);
        edittexttextmultiline_arrange_note=findViewById(R.id.edittextmultiline_arrange_note);
        String user1= FirebaseAuth.getInstance().getUid();

        database = FirebaseDatabase.getInstance("https://noteapplication-113b6-default-rtdb.europe-west1.firebasedatabase.app/");
        myRef = database.getReference(user1);

        notes=(Notes)getIntent().getSerializableExtra("object");

        edittextmultiline_arrange_title.setText(notes.getNote_title());
        edittexttextmultiline_arrange_note.setText(notes.getNote());

        imageview_arrange_backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title=edittextmultiline_arrange_title.getText().toString().trim();
                String note=edittexttextmultiline_arrange_note.getText().toString().trim();

                Map<String,Object> uptadeınfo=new HashMap<>();
                uptadeınfo.put("note_title",title);
                uptadeınfo.put("note",note);
                myRef.child(notes.getNote_id()).updateChildren(uptadeınfo);
                finish();
            }
        });

        imageview_arrange_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "silindi", Toast.LENGTH_SHORT).show();
                myRef.child(notes.getNote_id()).removeValue();
                finish();

            }
        });

    }

    @Override
    public void onBackPressed() {
        String title=edittextmultiline_arrange_title.getText().toString().trim();
        String note=edittexttextmultiline_arrange_note.getText().toString().trim();

        Map<String,Object> uptadeınfo=new HashMap<>();
        uptadeınfo.put("note_title",title);
        uptadeınfo.put("note",note);
        myRef.child(notes.getNote_id()).updateChildren(uptadeınfo);
        finish();

    }
}