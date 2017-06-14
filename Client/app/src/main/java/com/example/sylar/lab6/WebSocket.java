package com.example.sylar.lab6;


import android.app.Activity;
import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class WebSocket {
    private static final int CONNECTION_TIMEOUT = 1000;
    public WebSocketClient socketClient;

    private onMessage onMessage;
    public WebSocket(Activity activity) {
        onMessage = (WebSocket.onMessage) activity;
    }

    private void connectWebSocket() {
        URI serverURI = URI.create("ws://192.168.1.102:12448/request");
        if (socketClient == null || socketClient.getConnection().isClosed()) {
            createWebSocket(serverURI);
        }
    }

    private void createWebSocket(final URI serverURI) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", MainActivity.token);
        socketClient = new WebSocketClient(serverURI, new Draft_17(), headers, CONNECTION_TIMEOUT) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                ServerHandshake handshakedata1 = handshakedata;
            }

            @Override
            public void onMessage(String message) {
                onMessage.onMessage(message);
                Log.d("MyLog", message);
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
            }

            @Override
            public void onError(Exception ex) {
                Exception ex1 = ex;
            }
        };
        socketClient.connect();
    }

    public void start() {
        connectWebSocket();
    }

    public void stop() {
        if (socketClient != null) {
            socketClient.close();
        }
    }

    interface onMessage{
        void onMessage(String message);
    }

}
