package com.example.liaison.utils;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.ClientError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.xmlpull.v1.XmlPullParserException;

import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class Error {

    public Error() {

    }

    public void get_error(VolleyError error, Context activity){
        /*if(error instanceof NoConnectionError){
            AccessibilityService mContext = null;
            ConnectivityManager cm = (ConnectivityManager)mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = null;
            if (cm != null) {
                activeNetwork = cm.getActiveNetworkInfo();
            }
            if(activeNetwork != null && activeNetwork.isConnectedOrConnecting()){
                Toast.makeText(activity, "Server is not connected to internet.",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(activity, "Your device is not connected to internet.",
                        Toast.LENGTH_SHORT).show();
            }
        } else if (error instanceof NetworkError || error.getCause() instanceof ConnectException
                || (error.getCause().getMessage() != null
                && error.getCause().getMessage().contains("connection"))){
            Toast.makeText(activity, "Your device is not connected to internet.",
                    Toast.LENGTH_SHORT).show();
        } else if (error.getCause() instanceof MalformedURLException){
            Toast.makeText(activity, "Bad Request.", Toast.LENGTH_SHORT).show();
        } else if (error instanceof ParseError || error.getCause() instanceof IllegalStateException
                || error.getCause() instanceof JSONException
                || error.getCause() instanceof XmlPullParserException){
            Toast.makeText(activity, "Parse Error (because of invalid json or xml).",
                    Toast.LENGTH_SHORT).show();
        } else if (error.getCause() instanceof OutOfMemoryError){
            Toast.makeText(activity, "Out Of Memory Error.", Toast.LENGTH_SHORT).show();
        }else if (error instanceof AuthFailureError){
            Toast.makeText(activity, "server couldn't find the authenticated request.",
                    Toast.LENGTH_SHORT).show();
        } else if (error instanceof ServerError || error.getCause() instanceof ServerError) {
            Toast.makeText(activity, "Server is not responding.", Toast.LENGTH_SHORT).show();
        }else if (error instanceof TimeoutError || error.getCause() instanceof SocketTimeoutException
                || error.getCause() instanceof ConnectTimeoutException
                || error.getCause() instanceof SocketException
                || (error.getCause().getMessage() != null
                && error.getCause().getMessage().contains("Connection timed out"))) {
            Toast.makeText(activity, "Connection timeout error",
                    Toast.LENGTH_SHORT).show();
        }else */if(error instanceof ClientError) {
            Toast.makeText(activity, "Erreur 400.", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(activity, "An unknown error occurred.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
