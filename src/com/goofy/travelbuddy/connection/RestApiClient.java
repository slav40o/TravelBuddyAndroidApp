package com.goofy.travelbuddy.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class RestApiClient {
	private String baseUrl;
	
	public RestApiClient(String baseUrl){
		this.baseUrl = baseUrl;
	}
	
	public String Get(String resourceUrl, final List<NameValuePair> headers){
		String fullUrl = this.baseUrl + resourceUrl;
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(fullUrl);
		if (headers != null) {
			for (NameValuePair header : headers) {
				get.addHeader(header.getName(), header.getValue());
			}
		}
		
		try {
			HttpResponse resp = client.execute(get);
			String responce = this.redResponce(resp);
			return responce;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public <T> T Post(String resourceUrl, final T bodyParams, final List<NameValuePair> headers){
		
		return null;
	}
	
	public <T> T Put(String resourceUrl, final T bodyParams, final List<NameValuePair> headers){
		
		return null;
	}
	
	private String redResponce(HttpResponse responce) throws IllegalStateException, IOException{
		InputStream is  = responce.getEntity().getContent();            
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder str = new StringBuilder();
		String line = null;
		while((line = reader.readLine()) != null){
		    str.append(line + "\n");
		}
		is.close();
		reader.close();
		
		Log.d("GET", str.toString());
		return str.toString();
	}
}
