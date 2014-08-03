package externalapi;

import helpers.Car;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;

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
		success = true;
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
		if("Error".equals(output.optString("messageType"))){
			return new Uber(false);
		}
		return new Uber(output.getJSONObject("client").getString("token"),output.getJSONObject("client"));
	}
	
	public static Car[] getPrices(double startLat, double endLat, double startLong, double endLong){
		Form post = new Form()
			.param("origin_lat", ""+startLat)
			.param("origin_lng", ""+startLong)
			.param("destination_lng", ""+endLong)
			.param("destination_lat", ""+endLat)
			.param("vehicle_view_ids[]", "8")
			.param("vehicle_view_ids[]", "942")
			.param("vehicle_view_ids[]", "1")
			.param("vehicle_view_ids[]", "2");
		JSONObject data = new JSONObject(ClientBuilder.newClient()
				.target("https://www.uber.com/api/fares/multi_estimate")
				.request().post(Entity.form(post)).readEntity(String.class));
		Car[] output = new Car[data.getJSONArray("estimates").length()];
		for(int i=0;i<output.length;i++){
			JSONObject car = data.getJSONArray("estimates").getJSONObject(i);
			output[i] = new Car(2,"uber",car.getJSONObject("vehicle_view").getString("display_name"),parsePrice(car.getString("fare_string")),300);
		}
		return output;
	}

	private static int parsePrice(String price) {
		if(price.contains("-")){
			String price1 = "";
			String price2 = "";
			boolean swap = false;
			for(char x : price.toCharArray()){
				if (x == '-'){
					swap = true;
					continue;
				}
				if (x != '$'){
					if (swap){
						price1 += x;
					}else{
						price2 += x;
					}
				}
			}
			return (Integer.parseInt(price1) + Integer.parseInt(price2))*50;
		}else{
			return (Integer.parseInt(price.replaceAll("$", "")))*100;
		}
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
