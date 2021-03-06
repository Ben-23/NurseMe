package com.example.nurseme;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class ReportsAdapterClass extends RecyclerView.Adapter<ReportsAdapterClass.ViewHolder> {
    private List<reportclass> listItems;
    private Context context;

    public ReportsAdapterClass(List<reportclass> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recyclerview,viewGroup,false);
        Toast.makeText(context, "created", Toast.LENGTH_SHORT).show();
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final reportclass listItem=listItems.get(i);
        //Toast.makeText(context, "binded", Toast.LENGTH_SHORT).show();
        viewHolder.username.setText(listItem.getComplaintsender());
        viewHolder.location.setText(listItem.getSeverity());
        viewHolder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,ViewReport.class);
                intent.putExtra("patientemail",listItem.getComplaintsender());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        {
            return listItems.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView username,location;
        CardView cardview;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username=itemView.findViewById(R.id.textView41);
            location=itemView.findViewById(R.id.textView43);
            cardview=itemView.findViewById(R.id.cardview);
        }
    }
}
