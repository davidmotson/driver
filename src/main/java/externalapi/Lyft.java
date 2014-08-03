package externalapi;

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

}
