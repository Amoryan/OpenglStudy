package com.fxyan.opengl.geometry.polygon;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fxyan.opengl.R;
import com.fxyan.opengl.base.BaseActivity;

/**
 * @author fxYan
 */
public final class EdgeInputActivity
        extends BaseActivity {

    private EditText edgeInput;

    @Override
    public int getLayoutId() {
        return R.layout.activity_edge_input;
    }

    @Override
    protected void initViews() {
        edgeInput = findViewById(R.id.edgeNumEt);
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initEvents() {
        edgeInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String trim = edgeInput.getText().toString().trim();
                    if (TextUtils.isEmpty(trim)) {
                        Toast.makeText(context, "请输入多边形边数", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    int edgeNum = Integer.parseInt(trim);
                    if (edgeNum < 3) {
                        Toast.makeText(context, "边数需要大于3", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    PolygonActivity.open(context, edgeNum);
                }
                return true;
            }
        });
    }
}
