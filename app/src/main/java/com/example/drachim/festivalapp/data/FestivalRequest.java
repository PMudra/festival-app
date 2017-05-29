package com.example.drachim.festivalapp.data;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Copied from https://developer.android.com/training/volley/request-custom.html
 */
public class FestivalRequest extends Request<List<Festival>> {
    private final Gson gson = new Gson();
    private final Map<String, String> headers;
    private final Response.Listener<List<Festival>> listener;

    /**
     * Make a GET request and return a parsed object from JSON.
     *
     * @param url     URL of the request to make
     * @param headers Map of request headers
     */
    public FestivalRequest(String url, Map<String, String> headers, Response.Listener<List<Festival>> listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.headers = headers;
        this.listener = listener;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    protected void deliverResponse(List<Festival> response) {
        listener.onResponse(response);
    }

    @Override
    protected Response<List<Festival>> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            Type collectionType = new TypeToken<List<Festival>>() {
            }.getType();
            List<Festival> result = gson.fromJson(json, collectionType);
            return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }
}
