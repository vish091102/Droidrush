package com.example.loginpage.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginpage.R;
import com.example.loginpage.models2.recipies;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    ArrayList<recipies>list;
    Context context;

    public RecipeAdapter(ArrayList<recipies> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.samplelayout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    recipies model=list.get(position);
    holder.img.setImageResource(model.getPic());
    holder.name.setText(model.getName());
    holder.cost.setText(model.getPrice());
    holder.add.setText(model.getCheckout());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name,cost,add;

        public ViewHolder(@NonNull View itemView) {


            super(itemView);
            img=itemView.findViewById(R.id.foodpic);
            cost=itemView.findViewById(R.id.price);
            name=itemView.findViewById(R.id.foodname);
            add=itemView.findViewById(R.id.checkout);

        }
    }


}
