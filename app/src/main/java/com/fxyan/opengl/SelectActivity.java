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

import com.fxyan.opengl.entity.RendererMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fxYan
 */
public abstract class SelectActivity extends AppCompatActivity {

    private Context context;
    private List<RendererMenu> data = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        context = this;

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        genData(data);
        recyclerView.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                return new RecyclerView.ViewHolder(LayoutInflater.from(context).inflate(R.layout.listitem_opengl_menu, viewGroup, false)) {
                };
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                final Button menuBtn = viewHolder.itemView.findViewById(R.id.menuBtn);
                menuBtn.setText(data.get(i).name);
                menuBtn.setTag(i);
                menuBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = ((int) menuBtn.getTag());
                        RendererMenu menu = data.get(position);
                        back(menu);
                    }
                });
            }

            @Override
            public int getItemCount() {
                return data.size();
            }
        });
    }

    protected abstract void genData(List<RendererMenu> data);

    private void back(RendererMenu menu) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("menu", menu);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }
}
