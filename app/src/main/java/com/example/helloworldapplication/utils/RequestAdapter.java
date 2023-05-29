package com.example.helloworldapplication.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.helloworldapplication.R;
import com.example.helloworldapplication.entities.Request;
import com.example.helloworldapplication.entities.User;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RequestAdapter extends RecyclerView.Adapter<RequestViewHolder> {
    private List<Request> requestList;
    private List<User> userList;

    public RequestAdapter(List<Request> requestList) {
        this.requestList = requestList;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_request, parent, false);
        return new RequestViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        Request request = requestList.get(position);
        holder.bind(request);
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }
}
