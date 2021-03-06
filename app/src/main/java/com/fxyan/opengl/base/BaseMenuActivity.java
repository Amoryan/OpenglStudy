package com.fxyan.opengl.base;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fxyan.opengl.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fxYan
 */
public abstract class BaseMenuActivity
        extends BaseActivity {

    private Adapter adapter;
    private List<ActivityMenu> data = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_base_menu;
    }

    @Override
    protected void initViews() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter();
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        genData(data);
        adapter.notifyDataSetChanged();
    }

    protected abstract void genData(List<ActivityMenu> data);

    @Override
    protected void initEvents() {
    }

    private class Adapter
            extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new RecyclerView.ViewHolder(LayoutInflater.from(context).inflate(R.layout.listitem_opengl_menu, viewGroup, false)) {
            };
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            View root = viewHolder.itemView;

            Button btn = root.findViewById(R.id.menuBtn);
            btn.setText(data.get(i).menu);

            btn.setTag(i);
            btn.setOnClickListener(v -> {
                int pos = (int) v.getTag();
                context.startActivity(new Intent(context, data.get(pos).clazz));
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
}
