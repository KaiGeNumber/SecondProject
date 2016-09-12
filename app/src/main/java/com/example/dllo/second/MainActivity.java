package com.example.dllo.second;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button getBtn, postBtn;
    private String getUrl = "http://172.16.18.89/file/get.php?username=DLA160606&count=20&page=1";
    private String postUrl = "http://172.16.18.89/file/post.php";
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getBtn = (Button) findViewById(R.id.get_btn);
        postBtn = (Button) findViewById(R.id.post_btn);
        postBtn.setOnClickListener(this);
        getBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_btn://发送get请求
                try {
                    myGet();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.post_btn://发送post请求

                myPost();

                break;
        }
    }
    //发送post请求

    private void myPost() {
        OkHttpClient client = new OkHttpClient();
        //拿到图片
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.sanmao);
        byte[] bytes;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        bytes = byteArrayOutputStream.toByteArray();
        RequestBody body = RequestBody.create(JSON, bytes);
        RequestBody finalBody = new MultipartBody
                .Builder()
                .addFormDataPart("file", "下午.png", body)
                .setType(MultipartBody.FORM)
                .build();

        Request request = new Request.Builder()
                .url(postUrl)
                .post(finalBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("MainActivity", "失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("MainActivity", "成功");
            }
        });
    }

    //发送get请求
    private void myGet() throws IOException {
        //OkHttpClient 是需要单例的
        OkHttpClient client = new OkHttpClient();


        Request request = new Request.Builder()
                .url(getUrl)
                .build();
//
//        Response response = client.newCall(request).execute();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Log.d("MainActivity", "请求失败" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //请求回来的数据,都在response.body里
                //虽然网络请求 回调的,但是!!!!!
                //它在子线程回调,想要用还得用handler回调在子线程
                Log.d("MainActivity", response.body().string());
            }
        });
    }
}
