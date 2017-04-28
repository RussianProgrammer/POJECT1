package sis.pewpew.utils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.net.Socket;

import sis.pewpew.R;

public class NetworkStatusInspectorActivity extends AppCompatActivity {

    public boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new AsyncHttpTask().execute("www.google.com");
    }

    private class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            Integer result;
            try {
                Socket s = new Socket(params[0], 80);
                s.close();
                result = 1;
            } catch (Exception e) {
                result = 0;
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            isConnected = result == 1;
        }
    }
}
