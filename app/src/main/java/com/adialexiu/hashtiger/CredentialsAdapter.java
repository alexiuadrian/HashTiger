package com.adialexiu.hashtiger;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class CredentialsAdapter extends RecyclerView.Adapter<CredentialsAdapter.ViewHolder> {

    Context context;
    List<Credential> credentialList;
    RecyclerView recyclerView;

    final View.OnClickListener onClickListener = new MyOnClickListener();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView serviceName;
        TextView userName;
        TextView password;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            serviceName = itemView.findViewById(R.id.item_serviceName);
            userName = itemView.findViewById(R.id.item_userName);
            password = itemView.findViewById(R.id.item_password);
        }
    }

    public CredentialsAdapter(Context context, List<Credential> credentialList, RecyclerView recyclerView) {
        this.context = context;
        this.credentialList = credentialList;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public CredentialsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.single_item, parent, false);
        view.setOnClickListener(onClickListener);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CredentialsAdapter.ViewHolder holder, int position) {
        Credential credential = credentialList.get(position);

            holder.serviceName.setText(credential.getServiceName());
            holder.userName.setText(credential.getUserName());
            holder.password.setText(credential.getPassword());

    }

    @Override
    public int getItemCount() {
        return credentialList.size();
    }

    private class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

        }
    }
}
