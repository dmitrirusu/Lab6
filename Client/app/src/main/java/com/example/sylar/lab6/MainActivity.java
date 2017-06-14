package com.example.sylar.lab6;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import org.java_websocket.client.WebSocketClient;

import java.io.UnsupportedEncodingException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class MainActivity extends AppCompatActivity implements WebSocket.onMessage {

    public static String token = "secret";
    private static String jwtSecret = "secret";
    @BindView(R.id.val1)
    TextView val1;

    @BindView(R.id.val2)
    TextView val2;

    @BindView(R.id.val3)
    TextView val3;

    @BindView(R.id.answer)
    TextView answer;

    @BindView(R.id.send)
    Button send;
    WebSocketClient socketClient;

    @OnClick(R.id.btnDay)
    public void onBtnClick() {
        Message message = new Message("hello",
                "getDay",
                String.valueOf(System.currentTimeMillis()),
                val3.getText().toString());
        socketClient.send(new Gson().toJson(message));
    }

    @OnClick(R.id.btnMonth)
    public void onMonthBtnClick() {
        Message message = new Message("hello",
                "getMonth",
                String.valueOf(System.currentTimeMillis()),
                val3.getText().toString());
        socketClient.send(new Gson().toJson(message));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            generateToken();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        WebSocket socket = new WebSocket(this);
        socket.start();
        socketClient = socket.socketClient;

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Message message = new Message("hello",
                            val1.getText().toString(),
                            val2.getText().toString(),
                            val3.getText().toString());
                    socketClient.send(new Gson().toJson(message));

                } catch (Exception e) {
                    Exception e1 = e;
                }
            }
        });

    }

    private void generateToken() throws UnsupportedEncodingException {
        token = Jwts.builder()
                .setSubject("auth_token")
                .signWith(SignatureAlgorithm.HS256, jwtSecret.getBytes("UTF-8"))
                .compact();
    }

    @Override
    public void onMessage(String message) {
        answer.setText(message);
    }
}
