package com.ftx.wl.accessibilityservice;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Dialog mTipsDialog;
    //已经在xml设定了id为bt_accessibility
    private Button accessibilityBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        accessibilityBt = (Button) findViewById(R.id.btn_1);
        accessibilityBt.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(ClickService.isRunning()) {

            LogUtils.e("ClickService  已经运行了   ");

            if(mTipsDialog != null) {
                mTipsDialog.dismiss();

                LogUtils.e("mTipsDialog 不为空时候   进行了关闭处理   ");

            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    Intent intent = new Intent("auto.click");
                    intent.putExtra("flag",1);
//                    通过id
//                    intent.putExtra("id","btn_1");


//                    通过问本
                    intent.putExtra("text","按钮-1");
                    LogUtils.e("Handler  发送了广播消息   ");

                    sendBroadcast(intent);
                }
            },1500);
        } else {
            showOpenAccessibilityServiceDialog();

            LogUtils.e("权限窗口  ");
        }
    }

    /** 显示未开启辅助服务的对话框*/
    private void showOpenAccessibilityServiceDialog() {
        if(mTipsDialog != null && mTipsDialog.isShowing()) {

            LogUtils.e("showOpenAccessibilityServiceDialog  已经开启了   ");

            return;
        }
        View view = getLayoutInflater().inflate(R.layout.dialog_tips_layout, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAccessibilityServiceSettings();

                LogUtils.e("openAccessibilityServiceSettings  跳转到设置 知道开启对应的 无障碍选项  ");
            }
        });






        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("需要开启辅助服务正常使用");
        builder.setView(view);
        builder.setPositiveButton("打开辅助服务", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                openAccessibilityServiceSettings();

                LogUtils.e("引导界面  跳转到设置 知道开启对应的 无障碍选项  ");

                mTipsDialog.cancel();

            }
        });
        mTipsDialog = builder.show();

    }

    /** 打开辅助服务的设置*/
    private void openAccessibilityServiceSettings() {
        try {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
            Toast.makeText(this, "找[AutoClick],然后开启服务", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        LogUtils.e(" 按钮已被点击  ");

        new AlertDialog.Builder(this).setTitle("按钮已被点击").show();
    }
}
