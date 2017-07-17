package cn.cjp.utils;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

@Component
public class Password {

	/**
	 * 
	 * @param text
	 * @return {encodeText, pubk, prik}
	 */
	public static Map<String, String> encode(String text) {
		Map<String, byte[]> keyMap = RSA.generateKeyBytes();

		// 加密
		PublicKey pk = RSA.restorePublicKey(keyMap.get(RSA.PUBLIC_KEY));

		byte[] encodedText = RSA.RSAEncode(pk, text.getBytes());
		System.out.println("RSA encoded: " + Base64.encodeBase64String(encodedText));

		Map<String, String> data = new HashMap<>();
		data.put("encodeText", Base64.encodeBase64String(encodedText));
		data.put("pubk", Base64.encodeBase64String(keyMap.get(RSA.PUBLIC_KEY)));
		data.put("prik", Base64.encodeBase64String(keyMap.get(RSA.PRIVATE_KEY)));
		return data;
	}

	public static String decode(String pubk, String prik, String encodeText) {
		byte[] prikBytes = Base64.decodeBase64(prik);

		// 解密
		PrivateKey privateKey = RSA.restorePrivateKey(prikBytes);
		byte[] decodedPassword = Base64.decodeBase64(encodeText);
		String text = RSA.RSADecode(privateKey, decodedPassword);
		System.out.println("RSA decoded: " + text);
		return text;
	}

	public static void main(String[] args) {
		String text = "123";
		Map<String, String> data = encode(text);
		System.out.println(data);

		System.out.println(decode(data.get("pubk"), data.get("prik"), data.get("encodeText")));
	}

}
