package com.example.final_login.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.final_login.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DBOpenHelper mDBOpenHelper;
    private TextView mTvLoginactivityRegister;
    private RelativeLayout mRlloginactivityTop;
    private EditText mEtLoginactivityUsername;
    private EditText mEtLoginactivityPassword;
    private LinearLayout mLlLoginactivityTwo;
    private Button mBtLoginactivityLogin;
    private EditText mEtLoginactivityPhonecodes;
    private ImageView mIvLoginactivityShowcode;
    private String realCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        mDBOpenHelper = new DBOpenHelper(this);

        //将验证码用图片的形式显示出来
        mIvLoginactivityShowcode.setImageBitmap(Code.getInstance().createBitmap());
        realCode = Code.getInstance().getCode().toLowerCase();
    }

    private void initView() {
        // 初始化控件
        mBtLoginactivityLogin = findViewById(R.id.bt_loginactivity_login);
        mTvLoginactivityRegister = findViewById(R.id.tv_loginactivity_register);
        mEtLoginactivityUsername = findViewById(R.id.et_loginactivity_username);
        mEtLoginactivityPassword = findViewById(R.id.et_loginactivity_password);
        mEtLoginactivityPhonecodes = findViewById(R.id.et_loginactivity_phoneCodes);
        mIvLoginactivityShowcode = findViewById(R.id.iv_loginactivity_showCode);

        //设置点击事件监听器
        mBtLoginactivityLogin.setOnClickListener(this);
        mTvLoginactivityRegister.setOnClickListener(this);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_loginactivity_register://注册
                startActivity(new Intent(this, Register.class));
                finish();
                break;
            case R.id.iv_loginactivity_showCode://改变验证码
                mIvLoginactivityShowcode.setImageBitmap(Code.getInstance().createBitmap());
                realCode = Code.getInstance().getCode().toLowerCase();
                break;

            case R.id.bt_loginactivity_login://登录
                String name = mEtLoginactivityUsername.getText().toString().trim();
                String password = mEtLoginactivityPassword.getText().toString().trim();
                String phoneCode = mEtLoginactivityPhonecodes.getText().toString().toLowerCase();

                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(phoneCode)) {
                    //根据用户名查询数据库中的密码，然后进行比对
                    if (phoneCode.equals(realCode)) {
                        ArrayList<User> data = mDBOpenHelper.getAllData();
                        boolean match = false;
                        for (int i = 0; i < data.size(); i++) {
                            User user = data.get(i);
                            if (name.equals(user.getName()) && password.equals(user.getPassword())) {
                                match = true;
                                break;
                            } else {
                                match = false;
                            }
                        }
                        if (match) {
                            Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(this, Home.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(this, "用户名或密码不正确，请重新输入", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "验证码错误，登录失败", Toast.LENGTH_SHORT).show();
                    }
                }   else {
                    Toast.makeText(this, "请输入用户名或密码", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}