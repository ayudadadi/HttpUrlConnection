package com.example.yls.demoa;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int GET_IMG =20002 ;
    private static final int GET_PHONE =1001 ;
    private Button b1;
    private Button b2;
    private ImageView iv_img;
    private Handler handler;
    private EditText ed_phone;
    private TextView t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv_img = findViewById(R.id.imgv);
        ed_phone = findViewById(R.id.ed_phone);
        t1 = findViewById(R.id.t1);  
        b1 = findViewById(R.id.b1);
        b2 = findViewById(R.id.b2);
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                if(message.what == GET_IMG){
                    Bitmap bitmap = (Bitmap) message.obj;
                    iv_img.setImageBitmap(bitmap);
                    return true;
                }else if(message.what==GET_PHONE){
                    String result =(String)message.obj;
                    t1.setText(result);
                    return true;

                }
                return false;
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String phone = ed_phone.getText().toString();
                        useHttpUrlConnectionGet(phone);
                    }
                }).start();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String phone = ed_phone.getText().toString();
                        useHttpUrlConnectionPost(phone);
                    }
                }).start();
            }
        });
    }

    private void useHttpUrlConnectionPost(String phone) {
        String baseUrl = "http://ws.webxml.com.cn/WebServices/MobileCodeWS.asmx/getMobileCodeInfo";
/*
        String phoneUrl = baseUrl.concat("?mobileCode=").concat(phone).concat("&userID=");
*/
        try {
            URL url = new URL(baseUrl);
            HttpURLConnection httpURLConnection =(HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            String body ="mobileCode=".concat(phone).concat("&userID=");
            OutputStream outputStream =httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"utf-8"));
            bufferedWriter.write(body);
            bufferedWriter.flush();
            bufferedWriter.close();
            int code = httpURLConnection.getResponseCode();
            Log.e(TAG,"code = "+code);
            InputStream inputStream = httpURLConnection.getInputStream();

           /* Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            Message message = Message.obtain();
            message.what = GET_IMG;
            message.obj = bitmap;
            iv_img.setImageBitmap(bitmap);
            handler.sendMessage(message);*/


            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuffer stringBuffer = new StringBuffer();
            String line = bufferedReader.readLine();
            while(line!=null){
                stringBuffer.append(line);
                line = bufferedReader.readLine();
            }
            Log.e(TAG,"stringBuffer = "+stringBuffer.toString());
            Message message =Message.obtain();
            message.what = GET_PHONE;
            message.obj = stringBuffer.toString();
            handler.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void useHttpUrlConnectionGet(String phone) {
        String strUrl = "http://www.baidu.com";
        String imgUrl =
                "http://jsxy.gdcp.cn/UploadFile/2/2018/11/27/20181127161424161.jpg";
        String baseUrl = "http://ws.webxml.com.cn/WebServices/MobileCodeWS.asmx/getMobileCodeInfo";
        String phoneUrl = baseUrl.concat("?mobileCode=").concat(phone).concat("&userID=");
        try {
            URL url = new URL(phoneUrl);
            HttpURLConnection httpURLConnection =(HttpURLConnection) url.openConnection();
            int code = httpURLConnection.getResponseCode();
            Log.e(TAG,"code = "+code);
            InputStream inputStream = httpURLConnection.getInputStream();

           /* Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            Message message = Message.obtain();
            message.what = GET_IMG;
            message.obj = bitmap;
            iv_img.setImageBitmap(bitmap);
            handler.sendMessage(message);*/
           

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuffer stringBuffer = new StringBuffer();
            String line = bufferedReader.readLine();
            while(line!=null){
                stringBuffer.append(line);
                line = bufferedReader.readLine();
            }
            Log.e(TAG,"stringBuffer = "+stringBuffer.toString());
            Message message =Message.obtain();
            message.what = GET_PHONE;
            message.obj = stringBuffer.toString();
            handler.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
