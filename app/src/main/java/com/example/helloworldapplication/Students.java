package com.example.helloworldapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.helloworldapplication.entities.User;
import com.example.helloworldapplication.utils.UserAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Students extends AppCompatActivity {

    private LinearLayout filterLayout;
    private RecyclerView recyclerView;
    private UserAdapter adapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Students.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        CollectionReference usersRef = db.collection("user");

        usersRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<User> userList = new ArrayList<>();
                User user;
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    user = documentSnapshot.toObject(User.class);
                    userList.add(user);
                }
                adapter = new UserAdapter(userList);
                recyclerView.setAdapter(adapter);
            }
        });



    }
}