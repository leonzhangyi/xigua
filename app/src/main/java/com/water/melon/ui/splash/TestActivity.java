package com.water.melon.ui.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.water.melon.R;
import com.water.melon.utils.SharedPreferencesUtil;
import com.water.melon.utils.ToastUtil;

import androidx.annotation.Nullable;

public class TestActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_text);
        EditText test_et = findViewById(R.id.test_et);
        Button test_sure = findViewById(R.id.test_sure);

        test_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = test_et.getText().toString();
                if (text != null && !text.equals("")) {
                    SharedPreferencesUtil.getInstance().putString(SharedPreferencesUtil.KEY_WATER_TEST_IME, text);
                    goToMain();
                } else {
                    ToastUtil.showToastLong("请输入IMEI");
                }


            }
        });
    }

    private void goToMain() {
        Intent intent = new Intent(this, SplashActivity.class);
        startActivity(intent);

        this.finish();
    }
}
