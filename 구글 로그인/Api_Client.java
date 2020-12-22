package com.spring.api.bithumb.dao;
import java.io.IOException;



import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

//import org.codehaus.jackson.map.ObjectMapper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


@SuppressWarnings("unused")
public class Api_Client {


    public Api_Client(String api_key, String api_secret) {
	this.api_key = api_key;
	this.api_secret = api_secret;
    }
    
    @SuppressWarnings("unchecked")
    public static JsonNode getGoogleAccessToken(String code) {
    	 
        final String RequestUrl = "https://oauth2.googleapis.com/token"; // Host
        final List<NameValuePair> postParams = new ArrayList<NameValuePair>();
 
       
        postParams.add(new BasicNameValuePair("code", code)); // 로그인 과정중 얻은 code 값
        postParams.add(new BasicNameValuePair("client_id", "224677599374-p7ep3bont28baeu6l829pg50g74r96o1.apps.googleusercontent.com")); // REST API KEY
        postParams.add(new BasicNameValuePair("client_secret", "Ou2Hwlyp9kMXCxFVZJofiRQz"));
        postParams.add(new BasicNameValuePair("redirect_uri", "http://localhost:8090/googleAuth")); // 리다이렉트 URI
        postParams.add(new BasicNameValuePair("grant_type", "authorization_code"));
 
        final HttpClient client = HttpClientBuilder.create().build();
        final HttpPost post = new HttpPost(RequestUrl);
 
        JsonNode returnNode = null;
 
        try {
            post.setEntity(new UrlEncodedFormEntity(postParams));
 
            final HttpResponse response = client.execute(post);
            final int responseCode = response.getStatusLine().getStatusCode();
 
            System.out.println("\nSending 'POST' request to URL : " + RequestUrl);
            System.out.println("Post parameters : " + postParams);
            System.out.println("Response Code : " + responseCode);
 
            // JSON 형태 반환값 처리
            ObjectMapper mapper = new ObjectMapper();
 
            returnNode = mapper.readTree(response.getEntity().getContent());
 
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
 
        return returnNode;
    }
    
    @SuppressWarnings("unchecked")
    public JsonNode getGoogleUserInfo(String accessToken) {//String jsonMessage

    	JsonNode returnNode = null;
		try{
			OkHttpClient client = new OkHttpClient();
			HttpUrl httpUrl = new HttpUrl.Builder()
					.scheme("https")
					.host("www.googleapis.com")
					.addPathSegment("oauth2")
					.addPathSegment("v2")
					.addPathSegment("userinfo")
					.addQueryParameter("access_token", accessToken)
					.build();
			
			Request request = new Request.Builder()
					.url(httpUrl)
					.build();

            //동기 처리시 execute함수 사용
			okhttp3.Response response = client.newCall(request).execute();  

			ObjectMapper mapper = new ObjectMapper();
            returnNode = mapper.readTree(response.body().string());


		} catch (Exception e) {
			System.err.println(e.toString());
		}
		return returnNode;
	}
    
}
