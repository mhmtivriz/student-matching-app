package com.example.helloworldapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.helloworldapplication.entities.Request;
import com.example.helloworldapplication.entities.User;
import com.example.helloworldapplication.utils.RequestAdapter;
import com.example.helloworldapplication.utils.UserAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Requests extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RequestAdapter adapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference requestRef = db.collection("request");

    FirebaseAuth auth = FirebaseAuth.getInstance();
    private String currentUserId;

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Requests.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);



        currentUserId = auth.getCurrentUser().getUid();

        recyclerView = findViewById(R.id.recyclerViewRequests);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        Query query = requestRef.whereEqualTo("senderId", currentUserId);

        System.out.println(query);

        Query query2 = db.collection("request")
                .whereEqualTo("receiverId", currentUserId);

        List<Request> requestList = new ArrayList<>();

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Request request;
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    request = documentSnapshot.toObject(Request.class);
                    requestList.add(request);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("senderda hata");
            }
        });

        query2.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Request request;
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    request = documentSnapshot.toObject(Request.class);
                    requestList.add(request);
                }
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("receiverde hata");

            }
        });


        adapter = new RequestAdapter(requestList);
        recyclerView.setAdapter(adapter);



    }
}