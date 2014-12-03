package com.example.unsubscribe;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends Activity {
    Pubnub pubnub;
    String publish_key = "demo";
    String subscribe_key = "demo";
    String[] channels = new String[] {"unsubscribe_test", "blah1", "blah2", "blah3"};
    String channel = "unsubscribe_test";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        setContentView(R.layout.main);
    }

    private void init() {
        pubnub = new Pubnub(publish_key, subscribe_key);
        notifyUser("inited");
    }

    public void subscribeHandler(View view) {
        try {
            pubnub.subscribe(channels, new Callback() {
                @Override
                public void successCallback(String s, Object o) {
                    super.successCallback(s, o);
                    notifyUser("New message: " + o.toString());
                }

                @Override
                public void connectCallback(String s, Object o) {
                    super.connectCallback(s, o);
                    notifyUser("connected");
                }

                @Override
                public void errorCallback(String s, PubnubError pubnubError) {
                    notifyUser("error:" + pubnubError.toString());
                }

                @Override
                public void disconnectCallback(String s, Object o) {
                    notifyUser("unsubscribed");
                }
            });
        } catch (Exception e) {
            notifyUser("exception");
        }
    }

    public void unsubscribeHandler(View view) {
        pubnub.unsubscribeAll();
    }

    public void publishHandler(View view) {
        pubnub.publish(channel, "hey)", new Callback() {
            @Override
            public void successCallback(String s, Object o, String s1) {
                super.successCallback(s, o, s1);
            }
        });
    }

    private void notifyUser(Object message) {
        try {
            if (message instanceof JSONObject) {
                final JSONObject obj = (JSONObject) message;
                this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), obj.toString(),
                                Toast.LENGTH_LONG).show();

                        Log.i("Received msg : ", String.valueOf(obj));
                    }
                });

            } else if (message instanceof String) {
                final String obj = (String) message;
                this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), obj,
                                Toast.LENGTH_LONG).show();
                        Log.i("Received msg : ", obj.toString());
                    }
                });

            } else if (message instanceof JSONArray) {
                final JSONArray obj = (JSONArray) message;
                this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), obj.toString(),
                                Toast.LENGTH_LONG).show();
                        Log.i("Received msg : ", obj.toString());
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
