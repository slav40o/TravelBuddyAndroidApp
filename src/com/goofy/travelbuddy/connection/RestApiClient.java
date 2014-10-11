package com.goofy.travelbuddy.connection;
import java.util.List;

import android.inputmethodservice.KeyboardView;

import com.google.gson.GsonBuilder;
import org.apache.http.*;

public class RestApiClient {
	private String baseUrl;
	
	public RestApiClient(String baseUrl){
		this.baseUrl = baseUrl;
	}
	
	public <T> T Get(String resourceUrl, final List<NameValuePair> headers){
		
		return null;
	}
	
	public <T> T Post(String resourceUrl, final T bodyParams, final List<NameValuePair> headers){
		
		return null;
	}
	
	public <T> T Put(String resourceUrl, final T bodyParams, final List<NameValuePair> headers){
		
		return null;
	}
}
