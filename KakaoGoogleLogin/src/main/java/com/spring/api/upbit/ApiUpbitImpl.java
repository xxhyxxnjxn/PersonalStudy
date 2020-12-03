package com.spring.api.upbit;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.gson.Gson;

@Service("ApiUpbit")
public class ApiUpbitImpl implements ApiUpbit {


	static String serverUrl = "https://api.upbit.com";
	static String result = "";

	// 전체 계좌 조회
	@Override
	public String getAccounts(HashMap<String, String> hashMap) {

		Algorithm algorithm = Algorithm.HMAC256(hashMap.get("secretKey"));
		String jwtToken = JWT.create().withClaim("access_key", hashMap.get("apiKey"))
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
	
	@Override
	public String getOrderBook(HashMap<String, String> hashMap) {
		try {
			HttpClient client = HttpClientBuilder.create().build();
			HttpGet request = new HttpGet(serverUrl + "/v1/orderbook?markets=krw-"+hashMap.get("currency"));
			request.setHeader("Content-Type", "application/json");

			HttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();

			result = EntityUtils.toString(entity, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	// 티커

	@Override
	public String getTicker(HashMap<String, String> hashMap) {
		try {
			HttpClient client = HttpClientBuilder.create().build();
			HttpGet request = new HttpGet(serverUrl + "/v1/ticker?markets=krw-"+hashMap.get("currency"));
			request.setHeader("Content-Type", "application/json");

			HttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();

			result = EntityUtils.toString(entity, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 주문 가능

	@Override
	public String getBalance(HashMap<String, String> hashMap) throws Exception {

		HashMap<String, String> params = new HashMap<>();
		params.put("market", "KRW-"+hashMap.get("currency"));

		ArrayList<String> queryElements = new ArrayList<>();
		for (Map.Entry<String, String> entity : params.entrySet()) {
			queryElements.add(entity.getKey() + "=" + entity.getValue());
		}

		String queryString = String.join("&", queryElements.toArray(new String[0]));

		MessageDigest md = MessageDigest.getInstance("SHA-512");
		md.update(queryString.getBytes("UTF-8"));

		String queryHash = String.format("%0128x", new BigInteger(1, md.digest()));

		Algorithm algorithm = Algorithm.HMAC256(hashMap.get("secretKey"));
		String jwtToken = JWT.create().withClaim("access_key", hashMap.get("apiKey"))
				.withClaim("nonce", UUID.randomUUID().toString()).withClaim("query_hash", queryHash)
				.withClaim("query_hash_alg", "SHA512").sign(algorithm);

		String authenticationToken = "Bearer " + jwtToken;

		try {
			HttpClient client = HttpClientBuilder.create().build();
			HttpGet request = new HttpGet(serverUrl + "/v1/orders/chance?"+ queryString);
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

	// 개별 주문 조회

	@Override
	public String getOrderDetail(HashMap<String, String> hashMap) throws Exception {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("uuid", hashMap.get("uuid"));

		ArrayList<String> queryElements = new ArrayList();
		for (Map.Entry<String, String> entity : params.entrySet()) {
			queryElements.add(entity.getKey() + "=" + entity.getValue());
		}

		String queryString = String.join("&", queryElements.toArray(new String[0]));

		MessageDigest md = MessageDigest.getInstance("SHA-512");
		md.update(queryString.getBytes("UTF-8"));

		String queryHash = String.format("%0128x", new BigInteger(1, md.digest()));

		Algorithm algorithm = Algorithm.HMAC256(hashMap.get("secretKey"));
		String jwtToken = JWT.create().withClaim("access_key", hashMap.get("apiKey"))
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

			result = EntityUtils.toString(entity, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 주문리스트

	@Override
	public String getOrderList(HashMap<String, String> hashMap) throws Exception {
		HashMap<String, String> params = new HashMap<String, String>();
		//params.put("state", "done");

		String[] uuids = {
				"388d8ad5-4879-4efb-b7d7-f0223918bedb"
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
		String jwtToken = JWT.create().withClaim("access_key", hashMap.get("apiKey"))
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

			result = EntityUtils.toString(entity, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;

	}

	// 주문삭제 - 주문번호 수정해야함

	@Override
	public String cancelOrder(HashMap<String, String> hashMap) throws Exception {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("uuid", hashMap.get("orderId"));

		ArrayList<String> queryElements = new ArrayList<String>();
		for (Map.Entry<String, String> entity : params.entrySet()) {
			queryElements.add(entity.getKey() + "=" + entity.getValue());
		}

		String queryString = String.join("&", queryElements.toArray(new String[0]));

		MessageDigest md = MessageDigest.getInstance("SHA-512");
		md.update(queryString.getBytes("UTF-8"));

		String queryHash = String.format("%0128x", new BigInteger(1, md.digest()));

		Algorithm algorithm = Algorithm.HMAC256(hashMap.get("secretKey"));
		String jwtToken = JWT.create().withClaim("access_key", hashMap.get("apiKey"))
				.withClaim("nonce", UUID.randomUUID().toString()).withClaim("query_hash", queryHash)
				.withClaim("query_hash_alg", "SHA512").sign(algorithm);

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

	// 지정가 매수

	@Override
	public String limiteBuyOrder(HashMap<String, String> hashMap) throws Exception {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("market", "KRW-" + hashMap.get("currency"));
		params.put("side", "bid");
		params.put("price", hashMap.get("price"));
		params.put("ord_type", "limit");

		ArrayList<String> queryElements = new ArrayList<>();
		for (Map.Entry<String, String> entity : params.entrySet()) {
			queryElements.add(entity.getKey() + "=" + entity.getValue());
		}

		String queryString = String.join("&", queryElements.toArray(new String[0]));

		MessageDigest md = MessageDigest.getInstance("SHA-512");
		md.update(queryString.getBytes("UTF-8"));

		String queryHash = String.format("%0128x", new BigInteger(1, md.digest()));

		Algorithm algorithm = Algorithm.HMAC256(hashMap.get("secretKey"));
		String jwtToken = JWT.create().withClaim("access_key", hashMap.get("apiKey"))
				.withClaim("nonce", UUID.randomUUID().toString()).withClaim("query_hash", queryHash)
				.withClaim("query_hash_alg", "SHA512").sign(algorithm);

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

	// 지정가 매도
	@Override
	public String limiteSellOrder(HashMap<String, String> hashMap) throws Exception {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("market", "KRW-" + hashMap.get("currency"));
		params.put("side", "ask");
		params.put("volume", hashMap.get("units"));
		params.put("ord_type", "limit");

		ArrayList<String> queryElements = new ArrayList<>();
		for (Map.Entry<String, String> entity : params.entrySet()) {
			queryElements.add(entity.getKey() + "=" + entity.getValue());
		}

		String queryString = String.join("&", queryElements.toArray(new String[0]));

		MessageDigest md = MessageDigest.getInstance("SHA-512");
		md.update(queryString.getBytes("UTF-8"));

		String queryHash = String.format("%0128x", new BigInteger(1, md.digest()));

		Algorithm algorithm = Algorithm.HMAC256(hashMap.get("secretKey"));
		String jwtToken = JWT.create().withClaim("access_key", hashMap.get("apiKey"))
				.withClaim("nonce", UUID.randomUUID().toString()).withClaim("query_hash", queryHash)
				.withClaim("query_hash_alg", "SHA512").sign(algorithm);

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
	// 시장가 매수

	@Override
	public String marketBuyOrder(HashMap<String, String> hashMap) throws Exception {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("market", "KRW-" + hashMap.get("currency"));
		params.put("side", "bid");
		params.put("price", hashMap.get("price"));
		params.put("ord_type", "price");

		ArrayList<String> queryElements = new ArrayList<>();
		for (Map.Entry<String, String> entity : params.entrySet()) {
			queryElements.add(entity.getKey() + "=" + entity.getValue());
		}

		String queryString = String.join("&", queryElements.toArray(new String[0]));

		MessageDigest md = MessageDigest.getInstance("SHA-512");
		md.update(queryString.getBytes("UTF-8"));

		String queryHash = String.format("%0128x", new BigInteger(1, md.digest()));

		Algorithm algorithm = Algorithm.HMAC256(hashMap.get("secretKey"));
		String jwtToken = JWT.create().withClaim("access_key", hashMap.get("apiKey"))
				.withClaim("nonce", UUID.randomUUID().toString()).withClaim("query_hash", queryHash)
				.withClaim("query_hash_alg", "SHA512").sign(algorithm);

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

	// 시장가 매도

	@Override
	public String marketSellOrder(HashMap<String, String> hashMap) throws Exception {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("market", "KRW-" + hashMap.get("currency"));
		params.put("side", "ask");
		params.put("volume", hashMap.get("units"));
		params.put("ord_type", "market");

		ArrayList<String> queryElements = new ArrayList<>();
		for (Map.Entry<String, String> entity : params.entrySet()) {
			queryElements.add(entity.getKey() + "=" + entity.getValue());
		}

		String queryString = String.join("&", queryElements.toArray(new String[0]));

		MessageDigest md = MessageDigest.getInstance("SHA-512");
		md.update(queryString.getBytes("UTF-8"));
		String queryHash = String.format("%0128x", new BigInteger(1, md.digest()));
		Algorithm algorithm = Algorithm.HMAC256(hashMap.get("secretKey"));
		String jwtToken = JWT.create().withClaim("access_key", hashMap.get("apiKey"))
				.withClaim("nonce", UUID.randomUUID().toString()).withClaim("query_hash", queryHash)
				.withClaim("query_hash_alg", "SHA512").sign(algorithm);
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

}