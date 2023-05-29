package com.example.helloworldapplication.utils;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.helloworldapplication.R;
import com.example.helloworldapplication.entities.Request;
import com.example.helloworldapplication.entities.User;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class RequestViewHolder extends RecyclerView.ViewHolder {

    private CardView requestCardView;

    private TextView nameTextView, statusTextView, departmentTextView, gradeTextView, statusMatch;

    private ImageView acceptedImageView, rejectedImageView;

    private String UserId;

     FirebaseAuth mAuth = FirebaseAuth.getInstance();



    public RequestViewHolder(@NonNull View itemView) {
        super(itemView);

        requestCardView = itemView.findViewById(R.id.cardViewRequest);
        nameTextView = itemView.findViewById(R.id.textName);
        statusTextView = itemView.findViewById(R.id.statusRequest);
        departmentTextView = itemView.findViewById(R.id.departmentRequest);
        gradeTextView = itemView.findViewById(R.id.gradeRequest);
        statusMatch = itemView.findViewById(R.id.statusMatch);

        acceptedImageView = itemView.findViewById(R.id.imageAccept);
        rejectedImageView = itemView.findViewById(R.id.imageReject);




    }

    public void bind(Request request) {



        if (mAuth.getCurrentUser().getUid()  == request.getSenderId() ){
            // kullanıcı gönderici
            statusMatch.setVisibility(View.VISIBLE);
        } else {
            // kullanıcı alıcı
            acceptedImageView.setVisibility(View.VISIBLE);
            rejectedImageView.setVisibility(View.VISIBLE);
        }

    }
}
