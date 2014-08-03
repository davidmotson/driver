package externalapi;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;

import org.json.JSONObject;

public class Uber {
	public boolean success;
	public String token;
	public JSONObject self;
	
	public Uber(boolean b){
		success = b;
	}
	
	public Uber(String token, JSONObject self){
		this.token = token;
		this.self = self;
	}

	public static Uber login(String email, String password){
		JSONObject post = new JSONObject();
		post.put("app", "client");
		post.put("password", hashPass(password));
		post.put("email", email);
		post.put("messageType","Login");
		JSONObject output = new JSONObject(ClientBuilder.newClient()
				.target("https://cn-dc1.uber.com/")
				.request().post(Entity.json(post.toString()))
				.readEntity(String.class));
		if("error".equals(output.optString("messageType"))){
			return new Uber(false);
		}
		return new Uber(output.getString("token"),output.getJSONObject("client"));
	}

	private static String hashPass(String pass){
		StringBuilder temp = new StringBuilder();
		for(char x : pass.toCharArray()){
			temp.append(MD5Hex(String.valueOf(x)));
		}
		return MD5Hex(temp.toString().toLowerCase()).toLowerCase();
	}
	
	private static String MD5Hex(String s) {
		String result = null;
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] digest = md5.digest(s.getBytes());
			result = toHex(digest);
		}
		catch (NoSuchAlgorithmException e) {
			// this won't happen, we know Java has MD5!
		}
		return result;
	}

	private static String toHex(byte[] a) {
		StringBuilder sb = new StringBuilder(a.length * 2);
		for (int i = 0; i < a.length; i++) {
			sb.append(Character.forDigit((a[i] & 0xf0) >> 4, 16));
			sb.append(Character.forDigit(a[i] & 0x0f, 16));
		}
		return sb.toString();
	}


}
