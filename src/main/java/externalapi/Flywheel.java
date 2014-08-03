package externalapi;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;

import org.json.JSONObject;

public class Flywheel {
	public boolean success;
	public String token;
	public JSONObject self;
	
	public Flywheel(String token,JSONObject self){
		this.token = token;
		this.self = self;
		success = true;
	}
	
	public Flywheel(boolean f){
		if(!f){
			token = null;
			success = false;
		}
	}
	
	public static Flywheel login(String username, String password){
		JSONObject postData = new JSONObject();
		postData.put("password",password);
		postData.put("email",username);
		JSONObject output = new JSONObject(ClientBuilder.newClient()
				.target("https://mobile.flywheel.com/login")
				.request().post(Entity.json(postData.toString())).readEntity(String.class));
		if(output.has("error")){
			return new Flywheel(false);
		}
		return new Flywheel(output.getString("auth_token"), output.getJSONObject("passenger"));
	}

}
