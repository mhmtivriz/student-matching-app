package com.example.helloworldapplication.utils;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.helloworldapplication.Profile;
import com.example.helloworldapplication.R;
import com.example.helloworldapplication.Students;
import com.example.helloworldapplication.entities.User;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class UserViewHolder extends RecyclerView.ViewHolder {

    private CardView studentCardView;
    private TextView firstNameTextView, statusTextView, departmentLabelTextView, gradeLabelTextView, durationLabelTextView, distanceLabelTextView;
    private TextView studentDepartmentTextView, studentGradeTextView, studentDurationTextView, studentDistanceTextView;
    private ImageView imageViewProfilePhoto;

    private String userId;



    public UserViewHolder(View itemView) {
        super(itemView);
        studentCardView = itemView.findViewById(R.id.cardViewStudent);
        departmentLabelTextView = itemView.findViewById(R.id.departmenLabel);
        gradeLabelTextView = itemView.findViewById(R.id.gradeLabel);
        durationLabelTextView = itemView.findViewById(R.id.durationLabel);
        distanceLabelTextView = itemView.findViewById(R.id.distanceLabel);
        studentDepartmentTextView = itemView.findViewById(R.id.studentDepartment);
        studentGradeTextView = itemView.findViewById(R.id.studentGrade);
        studentDurationTextView = itemView.findViewById(R.id.studentDuration);
        studentDistanceTextView = itemView.findViewById(R.id.studentDistance);
        firstNameTextView = itemView.findViewById(R.id.nameTextView);
        statusTextView = itemView.findViewById(R.id.studentStatus);
        imageViewProfilePhoto = itemView.findViewById(R.id.profileImageView);

        studentCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Context context = itemView.getContext();
                Intent intent = new Intent(context, Profile.class);
                intent.putExtra("userId", userId);
                intent.putExtra("FROM_ACTIVITY", "STUDENTS");
                context.startActivity(intent);
            }
        });

    }

    public void bind(User user) {
        userId = user.getId();
        if(user.getFirstName() != null) firstNameTextView.setText(user.getFirstName() + " " + user.getLastName());
        if(user.getStatus() != null) statusTextView.setText(user.getStatus().toString());
        if(user.getDepartment() != null) studentDepartmentTextView.setText(user.getDepartment());
        if(user.getGrade() != null) studentGradeTextView.setText(user.getGrade());
        if(user.getDuration() != null) studentDurationTextView.setText(user.getDuration());
        if(user.getDistance() != null) studentDistanceTextView.setText(user.getDistance());
        if(user.getProfilePhotoUrl() != null ){
            Glide.with(itemView.getContext()).load(user.getProfilePhotoUrl()).into(imageViewProfilePhoto);

        }
    }
}
