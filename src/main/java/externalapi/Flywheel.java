package externalapi;

import helpers.Car;

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
		success = f;
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
	
	public static Car getPrice(double latStart, double longStart,int duration, double length){
		JSONObject data = new JSONObject(ClientBuilder.newClient()
				.target("https://mobile.flywheel.com/application_context")
				.queryParam("application", "passenger")
				.queryParam("platform", "android")
				.queryParam("latitude", latStart)
				.queryParam("longitude", longStart)
				.queryParam("version", "4.7.32")
				.request().get().readEntity(String.class));
		int price = 0;
		JSONObject fare = data.getJSONArray("service_availabilities").getJSONObject(0).getJSONObject("fare_schedule");
		price += fare.getInt("base_fare");
		price += fare.getInt("cost_per_mile") * length;
		price += fare.getInt("cost_per_minute") * duration/60.0;
		return new Car(1,"flywheel","Taxi",price,data.getInt("straight_line_approximation_speed")*60);
	}

}
