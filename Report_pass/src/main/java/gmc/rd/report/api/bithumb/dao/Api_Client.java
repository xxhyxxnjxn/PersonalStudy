
package gmc.rd.report.api.bithumb.dao;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;


@SuppressWarnings("unused")
public class Api_Client {
    protected String api_url = "https://api.bithumb.com";
    protected String api_url2 = "https://www.okex.com/";
    protected String api_key;
    protected String api_secret;
    protected String passPharse="wjdguswls0723";
    

    public Api_Client(String api_key, String api_secret) {
	this.api_key = api_key;
	this.api_secret = api_secret;
    }

    
    /**
     * 占쏙옙占쏙옙占쏙옙 占시곤옙占쏙옙 ns占쏙옙 占쏙옙占쏙옙占싼댐옙.(1/1,000,000,000 占쏙옙)
     * 
     * @return int
     */
    private String usecTime() {
    	/*
		long start = System.nanoTime();
		// do stuff
		long nanoseconds = System.nanoTime();
		long microseconds = TimeUnit.NANOSECONDS.toMicros(nanoseconds);
		long seconds = TimeUnit.NANOSECONDS.toSeconds(nanoseconds);
	
		int elapsedTime = (int) (microseconds + seconds);
	
		System.out.println("elapsedTime ==> " + microseconds + " : " + seconds);
		*/
    	
		return String.valueOf(System.currentTimeMillis());
    }

    private String request(String strHost, String strMemod, HashMap<String, String> rgParams,  HashMap<String, String> httpHeaders) {
    	String response = "";

		// SSL 占쏙옙占쏙옙
		if (strHost.startsWith("https://")) {
		    HttpRequest request = HttpRequest.get(strHost);
		    // Accept all certificates
		    request.trustAllCerts();
		    // Accept all hostnames
		    request.trustAllHosts();
		}
	
		if (strMemod.toUpperCase().equals("HEAD")) {
		} else {
		    HttpRequest request = null;
	
		    // POST/GET 占쏙옙占쏙옙
		    if (strMemod.toUpperCase().equals("POST")) {
	
			request = new HttpRequest(strHost, "POST");
			request.readTimeout(10000);
	
			//System.out.println("POST ==> " + request.url());
	
			if (httpHeaders != null && !httpHeaders.isEmpty()) {
			    httpHeaders.put("api-client-type", "2");
			    request.headers(httpHeaders);
			   // System.out.println(httpHeaders.toString());
			}
			if (rgParams != null && !rgParams.isEmpty()) {
			    request.form(rgParams);
			    //System.out.println(rgParams.toString());
			}
		    } else {
			request = HttpRequest.get(strHost
				+ Util.mapToQueryString(rgParams));
			request.readTimeout(10000);
	
			//System.out.println("Response was: " + response);
		    }
	
		    if (request.ok()) {
			response = request.body();
		    } else {
			response = "error : " + request.code() + ", message : "
				+ request.body();
		    }
		    request.disconnect();
		}
	
		return response;
    }
    
    public static String encodeURIComponent(String s)
    {
      String result = null;
   
      try
      {
        result = URLEncoder.encode(s, "UTF-8")
                           .replaceAll("\\+", "%20")
                           .replaceAll("\\%21", "!")
                           .replaceAll("\\%27", "'")
                           .replaceAll("\\%28", "(")
                           .replaceAll("\\%29", ")")
                           .replaceAll("\\%26", "&")
                           .replaceAll("\\%3D", "=")
                           .replaceAll("\\%7E", "~");
      }
   
      // This exception should never occur.
      catch (UnsupportedEncodingException e)
      {
        result = s;
      }
   
      return result;
    }

    private HashMap<String, String> getHttpHeaders(String endpoint, HashMap<String, String> rgData, String apiKey, String apiSecret) {
	    	
		String strData = Util.mapToQueryString(rgData).replace("?", "");
		String nNonce = usecTime();
		
		strData = strData.substring(0, strData.length()-1);
	
	
		//System.out.println("1 : " + strData);
		
		strData = encodeURIComponent(strData);
		
		HashMap<String, String> array = new HashMap<String, String>();
	
		
		String str = endpoint + ";"	+ strData + ";" + nNonce;
		//String str = "/info/balance;order_currency=BTC&payment_currency=KRW&endpoint=%2Finfo%2Fbalance;272184496";
		
        String encoded = asHex(hmacSha512(str, apiSecret));
		
		//System.out.println("strData was: " + str);
		//System.out.println("apiSecret was: " + apiSecret);
		array.put("Api-Key", apiKey);
		array.put("Api-Sign", encoded);
		array.put("Api-Nonce", String.valueOf(nNonce));
	
		return array;
		
    }
    
    private HashMap<String, String> getHttpHeaders2(String endpoint, HashMap<String, String> rgData) {
    	
		String strData = Util.mapToQueryString(rgData).replace("?", "");
		String nNonce = usecTime();
		
		strData = strData.substring(0, strData.length()-1);
	
	
		//System.out.println("1 : " + strData);
		
		strData = encodeURIComponent(strData);
		
		HashMap<String, String> array = new HashMap<String, String>();
	
		
		String str = endpoint + ";"	+ strData + ";" + nNonce;
		//String str = "/info/balance;order_currency=BTC&payment_currency=KRW&endpoint=%2Finfo%2Fbalance;272184496";
		
		array.put("Api-Nonce", String.valueOf(nNonce));
	
		return array;
		
    }
    
    
    private static final String DEFAULT_ENCODING = "UTF-8";
	private static final String HMAC_SHA512 = "HmacSHA512";
	 
	public static byte[] hmacSha512(String value, String key){
	    try {
	        SecretKeySpec keySpec = new SecretKeySpec(
	                key.getBytes(DEFAULT_ENCODING),
	                HMAC_SHA512);
	 
	        Mac mac = Mac.getInstance(HMAC_SHA512);
	        mac.init(keySpec);
	
	        final byte[] macData = mac.doFinal( value.getBytes( ) );
	        byte[] hex = new Hex().encode( macData );
	        
	        //return mac.doFinal(value.getBytes(DEFAULT_ENCODING));
	        return hex;
	 
	    } catch (NoSuchAlgorithmException e) {
	        throw new RuntimeException(e);
	    } catch (InvalidKeyException e) {
	        throw new RuntimeException(e);
	    } catch (UnsupportedEncodingException e) {
	        throw new RuntimeException(e);
	    }
	}
	 
	public static String asHex(byte[] bytes){
	    return new String(Base64.encodeBase64(bytes));
	}

    @SuppressWarnings("unchecked")
    public String callApi(String endpoint, HashMap<String, String> params) {
		String rgResultDecode = "";
		HashMap<String, String> rgParams = new HashMap<String, String>();
		rgParams.put("endpoint", endpoint);
	
		if (params != null) {
		    rgParams.putAll(params);
		}
	
		String api_host = api_url + endpoint;
		HashMap<String, String> httpHeaders = getHttpHeaders(endpoint, rgParams, api_key, api_secret);
	
		rgResultDecode = request(api_host, "POST", rgParams, httpHeaders);
	
		if (!rgResultDecode.startsWith("error")) {
		    // json 占식쏙옙
		    HashMap<String, String> result;
		    try {
			result = new ObjectMapper().readValue(rgResultDecode,
				HashMap.class);
	
			//System.out.println("==== 占쏙옙占� 占쏙옙占� ====");
			//System.out.println(result.get("status"));
		    } catch (IOException e) {
			e.printStackTrace();
		    }
		}
		return rgResultDecode;
    }
    
    @SuppressWarnings("unchecked")
    public String callApi2(String endpoint, HashMap<String, String> params) { // orderbook method
		String rgResultDecode = "";
		HashMap<String, String> rgParams = new HashMap<String, String>();
		rgParams.put("endpoint", endpoint);
	
		if (params != null) {
		    rgParams.putAll(params);
		}
	
		String api_host = api_url + endpoint;
		//HashMap<String, String> httpHeaders = getHttpHeaders(endpoint, rgParams, api_key, api_secret);
		HashMap<String, String> httpHeaders = getHttpHeaders2(endpoint, rgParams);
		rgResultDecode = request(api_host, "GET", rgParams, httpHeaders);
	
		if (!rgResultDecode.startsWith("error")) {
		    // json 占식쏙옙
		    HashMap<String, String> result;
		    try {
			result = new ObjectMapper().readValue(rgResultDecode,
				HashMap.class);
	
			//System.out.println("==== 占쏙옙占� 占쏙옙占� ====");
			//System.out.println(result.get("status"));
		    } catch (IOException e) {
			e.printStackTrace();
		    }
		}
		return rgResultDecode;
    }
    
    @SuppressWarnings("unchecked")
    public String callApi2_Post(String endpoint, HashMap<String, String> params) { // ticker, transactionHistory method
		String rgResultDecode = "";
		HashMap<String, String> rgParams = new HashMap<String, String>();
		rgParams.put("endpoint", endpoint);
		if (params != null) {
		    rgParams.putAll(params);
		}
	
		String api_host = api_url + endpoint;
		//HashMap<String, String> httpHeaders = getHttpHeaders(endpoint, rgParams, api_key, api_secret);
		HashMap<String, String> httpHeaders = getHttpHeaders2(endpoint, rgParams);
		rgResultDecode = request(api_host, "POST", rgParams, httpHeaders);
	
		if (!rgResultDecode.startsWith("error")) {
		    // json 占식쏙옙
		    HashMap<String, String> result;
		    try {
			result = new ObjectMapper().readValue(rgResultDecode,
				HashMap.class);
	
			//System.out.println("==== 占쏙옙占� 占쏙옙占� ====");
			//System.out.println(result.get("status"));
		    } catch (IOException e) {
			e.printStackTrace();
		    }
		}
		return rgResultDecode;
    }
    @SuppressWarnings("unchecked")
    public static JsonNode getKakaoAccessToken(String code) {
    	 
        final String RequestUrl = "https://kauth.kakao.com/oauth/token"; // Host
        final List<NameValuePair> postParams = new ArrayList<NameValuePair>();
 
        postParams.add(new BasicNameValuePair("grant_type", "authorization_code"));
        postParams.add(new BasicNameValuePair("client_id", "7adab7a284d33aebf8f758a613b2ee76")); // REST API KEY
        postParams.add(new BasicNameValuePair("redirect_uri", "http://localhost:8090/kakaologin")); // 리다이렉트 URI
        postParams.add(new BasicNameValuePair("code", code)); // 로그인 과정중 얻은 code 값
 
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
    public JsonNode getKakaoUserInfo(String accessToken) {//String jsonMessage

    	JsonNode returnNode = null;
		try{
			OkHttpClient client = new OkHttpClient();
			Request request = new Request.Builder()
					.header("Authorization", "Bearer "+accessToken)
					//.addHeader("signData",encoded)
					.url("https://kapi.kakao.com/v2/user/me")
					.build();

                        //동기 처리시 execute함수 사용s
			okhttp3.Response response = client.newCall(request).execute();  

			//returnNode 타입이 스트링일 때 출력
			//JSONObject jsonObject = new JSONObject(response.body().string());
			//returnNode = jsonObject.toString();
			
			ObjectMapper mapper = new ObjectMapper();
            returnNode = mapper.readTree(response.body().string());


		} catch (Exception e) {
			System.err.println(e.toString());
		}
		return returnNode;
	}
    
    @SuppressWarnings("unchecked")
    public JsonNode kakaoLogout(String accessToken) {//String jsonMessage
    	 final String RequestUrl = "https://kapi.kakao.com/v1/user/logout";
    	 
         final HttpClient client = HttpClientBuilder.create().build();
  
         final HttpPost post = new HttpPost(RequestUrl);
  
         post.addHeader("Authorization", "Bearer " + accessToken);
  
         JsonNode returnNode = null;
  
         try {
  
             final HttpResponse response = client.execute(post);
  
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
