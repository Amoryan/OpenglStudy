package com.fxyan.opengl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fxyan.opengl.entity.ActivityMenu;
import com.fxyan.opengl.geometry.GeometryActivity;
import com.fxyan.opengl.light.BaseLightActivity;
import com.fxyan.opengl.ply.PlyActivity;
import com.fxyan.opengl.texture.TextureConfigActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private RecyclerView recyclerView;

    private List<ActivityMenu> data = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        data.add(new ActivityMenu(GeometryActivity.class, "几何图形"));
        data.add(new ActivityMenu(BaseLightActivity.class, "光照"));
        data.add(new ActivityMenu(TextureConfigActivity.class, "纹理"));
        data.add(new ActivityMenu(PlyActivity.class, "Ply文件"));

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                return new RecyclerView.ViewHolder(LayoutInflater.from(context).inflate(R.layout.listitem_opengl_menu, recyclerView, false)) {
                };
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                Button btn = viewHolder.itemView.findViewById(R.id.menuBtn);
                btn.setText(data.get(i).menu);

                btn.setTag(i);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = (int) v.getTag();
                        startActivity(new Intent(context, data.get(pos).clazz));
                    }
                });
            }

            @Override
            public int getItemCount() {
                return data.size();
            }
        });
    }
}
