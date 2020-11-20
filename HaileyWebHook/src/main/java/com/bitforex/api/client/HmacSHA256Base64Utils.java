package com.bitforex.api.client;


import org.apache.commons.lang3.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.management.RuntimeErrorException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Formatter;
import org.apache.commons.codec.binary.Hex;

/**
 * Hmac SHA256 Base64 Signature Utils.<br/>
 *
 * @author DimouJ
 * @version 1.0.0
 * @date 2018/2/1 11:41
 */
public class HmacSHA256Base64Utils {
	private static final String DEFAULT_ENCODING = "MS949";
	private static final String HMAC_SHA256 = "HmacSHA256";
	public static String hmacSha256(String value, String key){
		
	    try {
	       
	    	SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), HMAC_SHA256);
	        Mac mac = Mac.getInstance(HMAC_SHA256);
	        mac.init(secretKeySpec);
	        return toHexString(mac.doFinal(value.getBytes()));
	 
	    } catch (NoSuchAlgorithmException e) {
	        throw new RuntimeException(e);
	    } catch (InvalidKeyException e) {
	        throw new RuntimeException(e);
	    } 
	}
	public static String toHexString(byte[] bytes){
		Formatter formatter = new Formatter();
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
	}
   
}
