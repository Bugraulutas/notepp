package com.bgr2.notepp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private Toolbar toolbar_main;
    private RecyclerView rv_main;
    private FloatingActionButton fab_main_add,fab_main_settings;

    private ArrayList<Notes> notesArrayList;
    private NotesAdapter notesAdapter;
    static boolean calledAlready = false;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        toolbar_main = findViewById(R.id.toolbar_main);
        fab_main_add = findViewById(R.id.fab_main_add);
        fab_main_settings = findViewById(R.id.fab_main_settings);
        rv_main = findViewById(R.id.rv_main);

        String user1=FirebaseAuth.getInstance().getUid();
        if (!calledAlready)//offline iken yaptığımız işlemlerin kayıt edilmesi için ve hata vermemesi için
        {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            calledAlready = true;
        }
        database = FirebaseDatabase.getInstance("https://noteapplication-113b6-default-rtdb.europe-west1.firebasedatabase.app/");
        myRef = database.getReference(user1);

        mAuth = FirebaseAuth.getInstance();
        setSupportActionBar(toolbar_main);

        rv_main.setHasFixedSize(true);

        rv_main.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

        notesArrayList = new ArrayList<>();

        allNotes();


        notesAdapter = new NotesAdapter(this, notesArrayList);

        rv_main.setAdapter(notesAdapter);


        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=firebaseAuth.getCurrentUser();

           /*   if(user==null){
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                    finish();
            }*/
        }

           };


        final FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();


        PopupMenu popupMenu=new PopupMenu(MainActivity.this,fab_main_settings);
        popupMenu.getMenuInflater().inflate(R.menu.fab_main_settings_menu,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.fab_main_settings_signout_item:
                        Toast.makeText(getApplicationContext(), "Çıkış yapıldı", Toast.LENGTH_SHORT).show();
                        mAuth.signOut();
                        startActivity(new Intent(MainActivity.this,LoginActivity.class));
                        finish();
                        return true;
                    case R.id.fab_maşn_settings_deleteaccount_item:
                        if(user!=null){
                            user.delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(getApplicationContext(), "Hesap Silindi!!!", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                                                finish();
                                            }
                                            else{
                                                Toast.makeText(MainActivity.this, "Hesap silinemedi!", Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });
                        }
                        return true;

                }

                return true;
            }
        });
        fab_main_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupMenu.show();


            }
        });


        fab_main_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(MainActivity.this,AddActivity.class);
                    startActivity(intent);

                }
            });
    }

    public  void allNotes(){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                notesArrayList.clear();
                for(DataSnapshot d:snapshot.getChildren()){
                    Notes note=d.getValue(Notes.class);
                    note.setNote_id(d.getKey());

                    notesArrayList.add(note);
                }
                notesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_main_menu,menu);


        MenuItem item=menu.findItem(R.id.toolbar_main_searchicon_item);


        SearchView searchView=(SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this);

        return true;
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authStateListener != null) {
            mAuth.removeAuthStateListener(authStateListener);
        }
    }
}