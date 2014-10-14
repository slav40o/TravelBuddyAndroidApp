package com.goofy.travelbuddy.connection;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

public class RestApiClient {
	private String baseUrl;
	
	public RestApiClient(String baseUrl){
		this.baseUrl = baseUrl;
	}
	
	public HttpResponse Get(String resourceUrl, final List<NameValuePair> headers, final List<NameValuePair> urlParams){
		String fullUrl = this.baseUrl + resourceUrl;
		if(urlParams != null){
			fullUrl += this.parseUrlParams(urlParams);
		}
		
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(fullUrl);
		if (headers != null) {
			for (NameValuePair header : headers) {
				get.addHeader(header.getName(), header.getValue());
			}
		}
		
		HttpResponse resp = null;
		try {
			resp = client.execute(get);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return resp;
	}
	
	public <T> HttpResponse  Post(String resourceUrl, final List<NameValuePair> bodyParams,
		final List<NameValuePair> urlParams, final List<NameValuePair> headers) throws ClientProtocolException, IOException{
		HttpResponse responce = null;

        try{
			String fullUrl = this.baseUrl + resourceUrl;
			if(urlParams != null){
				fullUrl += this.parseUrlParams(urlParams);
			}

			DefaultHttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(fullUrl);
			post.setEntity(new UrlEncodedFormEntity(bodyParams));
			 
			if (headers != null) {
				for (NameValuePair header : headers) {
					post.setHeader(header.getName(), header.getValue());
				}
			}
			
			responce = client.execute(post);
		}  catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}  
		
		return responce;
	}
	
	public <T> T Put(String resourceUrl, final T bodyParams, final List<NameValuePair> headers){
		
		return null;
	}
	
	private String parseUrlParams(List<NameValuePair> urlParams){
		StringBuilder str = new StringBuilder();
		str.append("?");
		
		for (NameValuePair param : urlParams) {
			str.append(param.getName() + "=\"" + param.getValue() + "\"");
		}
		
		return str.toString();
	}
}
