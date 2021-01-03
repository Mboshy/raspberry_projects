package com.example.wifi_rasp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    Button connectBtn;
    Button disconnectBtn;
    EditText txtAddress;
    Socket myAppSocket = null;
    public static String wifiModuleIP = "";
    public static int wifiModulePort = 0;
    public static String CMD = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectBtn = (Button) findViewById(R.id.connectBtn);
        disconnectBtn = (Button) findViewById(R.id.disconnectBtn);

        txtAddress = (EditText) findViewById(R.id.raspIP);

        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIP();
                CMD = "Hello";
                Socket_AsyncTask cmd_hello = new Socket_AsyncTask();
                cmd_hello.execute();
            }
        });
        disconnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIP();
                CMD = "Bye";
                Socket_AsyncTask cmd_hello = new Socket_AsyncTask();
                cmd_hello.execute();
            }
        });
    }

    public void getIP(){
        String IP = txtAddress.getText().toString();
        String temp[] = IP.split(":");
        wifiModuleIP = temp[0];
        wifiModulePort = Integer.valueOf(temp[1]);
        Log.d("TEST", wifiModuleIP);
        Log.d("TEST", String.valueOf(wifiModulePort));

    }

    public class Socket_AsyncTask extends AsyncTask<Void, Void, Void>{
        Socket socket;

        @Override
        protected Void doInBackground(Void... params){
            try{
                InetAddress inetAddress = InetAddress.getByName(MainActivity.wifiModuleIP);
                socket = new java.net.Socket(inetAddress, MainActivity.wifiModulePort);
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeBytes(CMD);
                dataOutputStream.close();
                socket.close();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}