
package com.limemobile.app.blog.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.limemobile.app.blog.R;
import com.limemobile.app.blog.constant.ITransKey;
import com.limemobile.app.utils.EditTextUtils;

public class EditUserInfoInputActivity extends ThemeActivity implements OnClickListener {

    private View back;
    private View save;
    private EditText input;
    private String inputText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent != null) {
            inputText = intent.getStringExtra(ITransKey.KEY);
        }

        setContentView(R.layout.activity_edit_input);
        initViews();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initViews() {
        back = findViewById(R.id.back);
        back.setOnClickListener(this);
        save = findViewById(R.id.save);
        save.setOnClickListener(this);
        input = (EditText) findViewById(R.id.edit_input);
        EditTextUtils.setTextWithSelection(input, inputText);
    }

    @Override
    public void themeChanged() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                setResult(Activity.RESULT_CANCELED);
                finish();
                break;
            case R.id.save:
                String inputString = input.getText().toString();
                if (!TextUtils.isEmpty(inputString)) {
                    Intent intent = new Intent();
                    intent.putExtra(ITransKey.KEY, inputString);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    EditTextUtils.ShowErrMsg("error");
                }
                break;
        }
    }

}
