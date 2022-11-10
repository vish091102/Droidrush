package com.example.loginpage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.loginpage.Adapter.RecipeAdapter;
import com.example.loginpage.models2.recipies;

import java.util.ArrayList;

public class RecyclerView extends AppCompatActivity {
androidx.recyclerview.widget.RecyclerView recyclerView;
    Button back;
@Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
recyclerView =findViewById(R.id.recyclerview);
back=findViewById(R.id.back);
back.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        startActivity(new Intent(RecyclerView.this,UserProfile_Activity.class));
        finish();
    }
});
        ArrayList<recipies> list=new ArrayList<>();
        list.add(new recipies(R.drawable.chillypotatoe2,"chilly potato","$4","checkout"));
        list.add(new recipies(R.drawable.momos,"momos","$3","checkout"));
        list.add(new recipies(R.drawable.munchurian,"manchurian","$4","checkout"));
        list.add(new recipies(R.drawable.narutoicecream,"popsicles","$2.5","checkout"));
        list.add(new recipies(R.drawable.pasta2,"pasta","$6","checkout"));
        list.add(new recipies(R.drawable.pizza2,"pizza","$5","checkout"));
        list.add(new recipies(R.drawable.samose,"samosa","$3","checkout"));
        list.add(new recipies(R.drawable.ramen," ramen","$5","checkout"));
        list.add(new recipies(R.drawable.sand,"sandwich","$3.5","checkout"));
        list.add(new recipies(R.drawable.soup,"soup","$3","checkout"));
        list.add(new recipies(R.drawable.springrole,"springroll","$2.99","checkout"));
        list.add(new recipies(R.drawable.sushi,"sushi","$4","checkout"));
        list.add(new recipies(R.drawable.pastry2,"cake","$4","checkout"));
        list.add(new recipies(R.drawable.dosa,"dosa","$5","checkout"));

        RecipeAdapter adapter=new RecipeAdapter(list,this);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager =new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


    }


}