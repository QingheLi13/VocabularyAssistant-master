package com.limi.andorid.vocabularyassistant.helper;

/**
 * Created by limi on 16/4/18.
 */

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;


/**
 * Created by limi on 16/4/17.
 */
public class CharsetStingRequest extends StringRequest {
    public CharsetStingRequest(int method, String url,
                               Listener<String> listener, ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }


    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {

        String str = null;
        try {
            str = new String(response.data, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return Response.success(str,
                HttpHeaderParser.parseCacheHeaders(response));
    }

}
