package com.bitforex.api.client;

import com.alibaba.fastjson.JSON;

import java.net.HttpURLConnection;
import java.io.*;
import java.net.URL;
//import java.net.http.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.TreeMap;

/**
 * OKEX API Client
 *
 * @author DimouJ
 * @version 1.0.0
 * @date 2019/8/8 13:43
 */
@SuppressWarnings("unused")
public class APIClient2 {
	
	public static final String BASE_URL = "https://api.bitforex.com";
	private static final String CHARSET_UTF_8 = "UTF-8";
	String apiKey="";
	String secretKey="";
	String encoded = "";
	String nonce = Long.toString(System.currentTimeMillis());
	HashMap<String,String> hash = new HashMap();
	String json="";
	
	public void APISet(String apiKey,String secretKey) {
		this.apiKey = apiKey;
		this.secretKey = secretKey;
	}
	
	public String getRequest(String url,HashMap<String,String> rgData) {
		
		 List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
	        if (null != rgData) {
	            for (Entry<String, String> entry : rgData.entrySet()) {
	                if (entry.getValue() != null) {
	                    valuePairs.add(new BasicNameValuePair(entry.getKey(),
	                            entry.getValue().toString()));
	                }
	            }
	        }
		
		String strData = URLEncodedUtils.format(valuePairs,CHARSET_UTF_8);
		String requestURL = url+"?"+strData;
		//System.out.println(url+"?"+strData);
		return requestURL;
		
		
	}
	public String postRequest(String url,HashMap<String,String> rgData) {
		rgData.put("accessKey",apiKey);
		rgData.put("nonce",nonce);
		
		List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
        //if (null != rgData) {
            //for (Entry<String, String> entry : rgData.entrySet()) {
                //if (entry.getValue() != null) {
                    //valuePairs.add(new BasicNameValuePair(entry.getKey(),
                            //entry.getValue().toString()));
                //}
            //}
        //}
        
       
		HashMap<String, String> result = sortMapByKey(rgData);
		for (Map.Entry<String, String> entry : result.entrySet()) {
		    //System.out.println("Key: " + entry.getKey() + ", "
		            //+ "Value: " + entry.getValue());
			valuePairs.add(new BasicNameValuePair(entry.getKey(),
                    entry.getValue().toString()));
			
		}
		String strData = URLEncodedUtils.format(valuePairs,CHARSET_UTF_8);
        String requestURL = url+"?"+strData;
        System.out.println(url+"?"+strData);
        return requestURL;
        
	}
	public static LinkedHashMap<String, String> sortMapByKey(Map<String, String> map) {
	    List<Map.Entry<String, String>> entries = new LinkedList<>(map.entrySet());
	    Collections.sort(entries, (o1, o2) -> o1.getKey().compareTo(o2.getKey()));

	    LinkedHashMap<String, String> result = new LinkedHashMap<>();
	    for (Map.Entry<String, String> entry : entries) {
	        result.put(entry.getKey(), entry.getValue());
	    }
	    return result;
	}
	public String encoded(String url) {
		HmacSHA256Base64Utils test2 = new HmacSHA256Base64Utils(); //
		String encode = "";
		
		encode = test2.hmacSha256(url,secretKey);
		
		return encode;
	}
	public String get(String url,HashMap<String,String> rgData) {
		String strURL = getRequest(url,rgData);
		String result;
		String requestURL = BASE_URL+strURL;
		try {
			OkHttpClient client = new OkHttpClient();
			Request request = new Request.Builder()
					//.addHeader("x-api-key", "12aedb5c4b1ed733a722cee8af95c652")
					.url(requestURL)
					.build(); //GET Request 
                        
                       //동기 처리시 execute함수 사용 
			okhttp3.Response response = client.newCall(request).execute(); 
			
			//출력 
			JSONObject jsonObject = new JSONObject(response.body().string());
			result = jsonObject.toString();
			//String message = response.body().toString();
			
			//System.out.println(message);
		} catch (Exception e){
			result = e.toString();
		}
		return result;
	}
	public String getSymbol(String url) {
		String result;
		String requestURL = BASE_URL+url;
		try {
			OkHttpClient client = new OkHttpClient();
			Request request = new Request.Builder()
					//.addHeader("x-api-key", "12aedb5c4b1ed733a722cee8af95c652")
					.url(requestURL)
					.build(); //GET Request 
			
			//동기 처리시 execute함수 사용 
			okhttp3.Response response = client.newCall(request).execute(); 
			
			//출력 
			JSONObject jsonObject = new JSONObject(response.body().string());
			result = jsonObject.toString();
			//String message = response.body().toString();
			
			//System.out.println(message);
		} catch (Exception e){
			result = e.toString();
		}
		return result;
	}
	
	public String post(String url,HashMap<String,String> rgData) {//String jsonMessage
		String strurl = postRequest(url,rgData);
		String encode = encoded(strurl);
		String json = strurl+"&signData="+encode;
		
		String requestURL = BASE_URL+json;
		
		String result="";
		try{
			OkHttpClient client = new OkHttpClient();
			Request request = new Request.Builder()
					//.addHeader("accessKey", "12aedb5c4b1ed733a722cee8af95c652")
					//.addHeader("signData",encoded)
					.url(requestURL)
					.post(RequestBody.create(MediaType.parse("application/json"), json)) //POST로 전달할 내용 설정 
					.build();

                        //동기 처리시 execute함수 사용s
			okhttp3.Response response = client.newCall(request).execute();  

			//출력
			JSONObject jsonObject = new JSONObject(response.body().string());
			result = jsonObject.toString();
			//System.out.println(jsonObject);
			//String message = response.body().string();
			//System.out.println(message);

		} catch (Exception e) {
			System.err.println(e.toString());
		}
		return result;
	}

    
}
