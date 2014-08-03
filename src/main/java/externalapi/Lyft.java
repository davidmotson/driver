package externalapi;

import helpers.Car;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;

import org.json.JSONObject;


public class Lyft {
	
	public boolean success;
	public JSONObject self;
	public String token;
	
	public Lyft(boolean b){
		success = b;
	}
	
	public Lyft(JSONObject self, String token){
		this.self = self;
		this.token = token;
		success = true;
	}
	
	public static void sendConfirmationCode(String phoneNumber){
		JSONObject post = new JSONObject();
		JSONObject phone = new JSONObject();
		phone.put("number", phoneNumber);
		post.put("phone", phone);
		
		ClientBuilder.newClient()
		.target("https://api.lyft.com/phoneauth")
		.request().post(Entity.json(post.toString()))
		.getEntity();
	}
	
	public static Lyft login(String phoneNumber, int code){
		JSONObject post = new JSONObject();
		JSONObject phone = new JSONObject();
		phone.put("number", "+1" + phoneNumber);
		phone.put("verificationCode", code);
		post.put("phone", phone);
		
		JSONObject output = new JSONObject(ClientBuilder.newClient()
			.target("https://api.lyft.com/users")
			.request().post(Entity.json(post.toString()))
			.readEntity(String.class));
		
		if(output.has("errors")){
			return new Lyft(false);
		}else{
			return new Lyft(output.getJSONObject("user"),output.getJSONObject("user").getString("lyftToken"));
		}
	}
	
	public static Car getPrice(int duration, double length){
		JSONObject prices = new JSONObject(ClientBuilder.newClient()
				.target("https://www.lyft.com/api/help?article=1263247&")
				.request().get().readEntity(String.class)).getJSONObject("pricing");
		int price = (int) Math.round(prices.getDouble("pickupCharge")*100);
		price += prices.getDouble("Trust & Safety Fee")*100;
		price += prices.getDouble("costPerMile")*100*length;
		price += prices.getDouble("costPerMinute")*duration*100/60;
		return new Car(6,"sidecar","",price,250);
	}

}
