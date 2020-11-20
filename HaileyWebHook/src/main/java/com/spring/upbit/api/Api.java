package com.spring.upbit.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.naming.spi.DirStateFactory.Result;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.gson.Gson;

public class Api {

	static String serverUrl = "https://api.upbit.com";




	// 전체 계좌 조회
	public static String getAccounts(HashMap<String, String> hashMap) {
		String result="";
		Algorithm algorithm = Algorithm.HMAC256(hashMap.get("secretKey"));
		String jwtToken = JWT.create().withClaim("access_key", hashMap.get("accessKey"))
				.withClaim("nonce", UUID.randomUUID().toString()).sign(algorithm);

		String authenticationToken = "Bearer " + jwtToken;

		try {
			HttpClient client = HttpClientBuilder.create().build();
			HttpGet request = new HttpGet(serverUrl + "/v1/accounts");
			request.setHeader("Content-Type", "application/json");
			request.addHeader("Authorization", authenticationToken);

			HttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();

			result = EntityUtils.toString(entity, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;

	}

	// 개별 주문 조회 - 주문번호 수정해야함
	public static void getOrder(HashMap<String, String> hashMap) throws Exception {
		HashMap<String, String> params = new HashMap<String, String>();
        params.put("uuid", "0f7fadfc-8621-43a9-b0f6-a2456477b806");

		ArrayList<String> queryElements = new ArrayList();
		for (Map.Entry<String, String> entity : params.entrySet()) {
			queryElements.add(entity.getKey() + "=" + entity.getValue());
		}

		String queryString = String.join("&", queryElements.toArray(new String[0]));

		MessageDigest md = MessageDigest.getInstance("SHA-512");
		md.update(queryString.getBytes("UTF-8"));

		String queryHash = String.format("%0128x", new BigInteger(1, md.digest()));

		Algorithm algorithm = Algorithm.HMAC256(hashMap.get("secretKey"));
		String jwtToken = JWT.create().withClaim("access_key", hashMap.get("accessKey"))
				.withClaim("nonce", UUID.randomUUID().toString()).withClaim("query_hash", queryHash)
				.withClaim("query_hash_alg", "SHA512").sign(algorithm);

		String authenticationToken = "Bearer " + jwtToken;

		try {
			HttpClient client = HttpClientBuilder.create().build();
			HttpGet request = new HttpGet(serverUrl + "/v1/order?" + queryString);
			request.setHeader("Content-Type", "application/json");
			request.addHeader("Authorization", authenticationToken);

			HttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();

			System.out.println(EntityUtils.toString(entity, "UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// 주문리스트
	public static void getOrders(HashMap<String, String> hashMap) throws Exception {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("state", "done");

		String[] uuids = { 
//				"392b5e35-aae2-4d41-9da5-71c40b3d14bc" // ...
		};

		ArrayList<String> queryElements = new ArrayList<String>();
		for (Map.Entry<String, String> entity : params.entrySet()) {
			queryElements.add(entity.getKey() + "=" + entity.getValue());
		}

		for (String uuid : uuids) {
			queryElements.add("uuids[]=" + uuid);
		}

		String queryString = String.join("&", queryElements.toArray(new String[0]));

		MessageDigest md = MessageDigest.getInstance("SHA-512");
		md.update(queryString.getBytes("UTF-8"));

		String queryHash = String.format("%0128x", new BigInteger(1, md.digest()));

		Algorithm algorithm = Algorithm.HMAC256(hashMap.get("secretKey"));
		String jwtToken = JWT.create().withClaim("access_key", hashMap.get("accessKey"))
				.withClaim("nonce", UUID.randomUUID().toString()).withClaim("query_hash", queryHash)
				.withClaim("query_hash_alg", "SHA512").sign(algorithm);

		String authenticationToken = "Bearer " + jwtToken;

		try {
			HttpClient client = HttpClientBuilder.create().build();
			HttpGet request = new HttpGet(serverUrl + "/v1/orders?" + queryString);
			request.setHeader("Content-Type", "application/json");
			request.addHeader("Authorization", authenticationToken);

			HttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();

			System.out.println(EntityUtils.toString(entity, "UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	
	// 주문삭제 - 주문번호 수정해야함
		public static String deleteOrder (HashMap<String, String> hashMap) throws Exception {
			String result=""; 
			HashMap<String, String> params = new HashMap<String, String>();
		        params.put("uuid", hashMap.get("uuid"));

		        ArrayList<String> queryElements = new ArrayList<String>();
		        for(Map.Entry<String, String> entity : params.entrySet()) {
		            queryElements.add(entity.getKey() + "=" + entity.getValue());
		        }

		        String queryString = String.join("&", queryElements.toArray(new String[0]));

		        MessageDigest md = MessageDigest.getInstance("SHA-512");
		        md.update(queryString.getBytes("UTF-8"));

		        String queryHash = String.format("%0128x", new BigInteger(1, md.digest()));

		        Algorithm algorithm = Algorithm.HMAC256(hashMap.get("secretKey"));
		        String jwtToken = JWT.create()
		                .withClaim("access_key", hashMap.get("accessKey"))
		                .withClaim("nonce", UUID.randomUUID().toString())
		                .withClaim("query_hash", queryHash)
		                .withClaim("query_hash_alg", "SHA512")
		                .sign(algorithm);

		        String authenticationToken = "Bearer " + jwtToken;

		        try {
		            HttpClient client = HttpClientBuilder.create().build();
		            HttpDelete request = new HttpDelete(serverUrl + "/v1/order?" + queryString);
		            request.setHeader("Content-Type", "application/json");
		            request.addHeader("Authorization", authenticationToken);

		            HttpResponse response = client.execute(request);
		            HttpEntity entity = response.getEntity();

		            result = EntityUtils.toString(entity, "UTF-8");
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
				return result;
		}

		//주문하기
		@Autowired
		public String postOrder (HashMap<String, String> hashMap) throws Exception {
			String result="";
	        HashMap<String, String> params = new HashMap<String, String>();
	        params.put("market", hashMap.get("market"));
	        params.put("side", hashMap.get("side"));
	        params.put("volume", hashMap.get("volume"));
	        params.put("price", hashMap.get("price"));
	        params.put("ord_type", "limit");


	        
	        ArrayList<String> queryElements = new ArrayList<>();
	        for(Map.Entry<String, String> entity : params.entrySet()) {
	            queryElements.add(entity.getKey() + "=" + entity.getValue());
	        }

	        String queryString = String.join("&", queryElements.toArray(new String[0]));

	        MessageDigest md = MessageDigest.getInstance("SHA-512");
	        md.update(queryString.getBytes("UTF-8"));

	        String queryHash = String.format("%0128x", new BigInteger(1, md.digest()));

	        Algorithm algorithm = Algorithm.HMAC256(hashMap.get("secretKey"));
	        String jwtToken = JWT.create()
	                .withClaim("access_key", hashMap.get("accessKey"))
	                .withClaim("nonce", UUID.randomUUID().toString())
	                .withClaim("query_hash", queryHash)
	                .withClaim("query_hash_alg", "SHA512")
	                .sign(algorithm);

	        String authenticationToken = "Bearer " + jwtToken;

	        try {
	            HttpClient client = HttpClientBuilder.create().build();
	            HttpPost request = new HttpPost(serverUrl + "/v1/orders");
	            request.setHeader("Content-Type", "application/json");
	            request.addHeader("Authorization", authenticationToken);
	            request.setEntity(new StringEntity(new Gson().toJson(params)));

	            HttpResponse response = client.execute(request);
	            HttpEntity entity = response.getEntity();

	            result = EntityUtils.toString(entity, "UTF-8");
	          
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    
			return result;
		}
		
		// 주문 가능
		public String getOrdersChance (HashMap<String, String> hashMap) throws Exception {
			String result="";
			HashMap<String, String> params = new HashMap<>();
	        params.put("market", hashMap.get("market"));

	        ArrayList<String> queryElements = new ArrayList<>();
	        for(Map.Entry<String, String> entity : params.entrySet()) {
	            queryElements.add(entity.getKey() + "=" + entity.getValue());
	        }

	        String queryString = String.join("&", queryElements.toArray(new String[0]));

	        MessageDigest md = MessageDigest.getInstance("SHA-512");
	        md.update(queryString.getBytes("UTF-8"));

	        String queryHash = String.format("%0128x", new BigInteger(1, md.digest()));

	        Algorithm algorithm = Algorithm.HMAC256(hashMap.get("secretKey"));
	        String jwtToken = JWT.create()
	                .withClaim("access_key", hashMap.get("accessKey"))
	                .withClaim("nonce", UUID.randomUUID().toString())
	                .withClaim("query_hash", queryHash)
	                .withClaim("query_hash_alg", "SHA512")
	                .sign(algorithm);

	        String authenticationToken = "Bearer " + jwtToken;

	        try {
	            HttpClient client = HttpClientBuilder.create().build();
	            HttpGet request = new HttpGet(serverUrl + "/v1/orders/chance?" + queryString);
	            request.setHeader("Content-Type", "application/json");
	            request.addHeader("Authorization", authenticationToken);

	            HttpResponse response = client.execute(request);
	            HttpEntity entity = response.getEntity();

	            result = EntityUtils.toString(entity, "UTF-8");
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
			return result;

		}
		
		public static String getOrdersChance2 (HashMap<String, String> hashMap) throws Exception {
			String result="";
			HashMap<String, String> params = new HashMap<>();
	        params.put("market", hashMap.get("market"));

	        ArrayList<String> queryElements = new ArrayList<>();
	        for(Map.Entry<String, String> entity : params.entrySet()) {
	            queryElements.add(entity.getKey() + "=" + entity.getValue());
	        }

	        String queryString = String.join("&", queryElements.toArray(new String[0]));

	        MessageDigest md = MessageDigest.getInstance("SHA-512");
	        md.update(queryString.getBytes("UTF-8"));

	        String queryHash = String.format("%0128x", new BigInteger(1, md.digest()));

	        Algorithm algorithm = Algorithm.HMAC256(hashMap.get("secretKey"));
	        String jwtToken = JWT.create()
	                .withClaim("access_key", hashMap.get("accessKey"))
	                .withClaim("nonce", UUID.randomUUID().toString())
	                .withClaim("query_hash", queryHash)
	                .withClaim("query_hash_alg", "SHA512")
	                .sign(algorithm);

	        String authenticationToken = "Bearer " + jwtToken;

	        try {
	            HttpClient client = HttpClientBuilder.create().build();
	            HttpGet request = new HttpGet(serverUrl + "/v1/orders/chance?" + queryString);
	            request.setHeader("Content-Type", "application/json");
	            request.addHeader("Authorization", authenticationToken);

	            HttpResponse response = client.execute(request);
	            HttpEntity entity = response.getEntity();

	            result = EntityUtils.toString(entity, "UTF-8");
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
			return result;

		}
		
}