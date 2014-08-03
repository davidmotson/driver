package externalapi;

import java.util.ArrayList;

import helpers.Car;
import helpers.SidecarWrapper;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;

import org.json.JSONObject;

public class Sidecar {
	public boolean success;
	public JSONObject self;

	public Sidecar(boolean b) {
		success = b;
	}

	public Sidecar(JSONObject self){
		success = true;
		this.self = self;
	}

	public static Sidecar login(String phoneNumber, String password){
		Form f = new Form()
		.param("username", phoneNumber)
		.param("password", password);
		JSONObject output = new JSONObject(ClientBuilder.newClient()
				.target("https://app.side.cr/account/getAccount")
				.request().post(Entity.form(f))
				.readEntity(String.class));
		if(output.getBoolean("success")){
			return new Sidecar(output.getJSONObject("account"));
		}else{
			return new Sidecar(false);
		}
	}
	
	public static SidecarWrapper getPrices(String phoneNumber, String password, double startLat, double endLat, double startLong, double endLong){
		Form post = new Form()
		.param("username", phoneNumber)
		.param("password", password)
		.param("pLng", ""+startLong)
		.param("pLat", ""+startLat)
		.param("dLng", ""+endLong)
		.param("dLat", ""+endLat);
		JSONObject data = new JSONObject(ClientBuilder.newClient()
				.target("https://app.side.cr/query/getBestMatch")
				.request().post(Entity.form(post)).readEntity(String.class));
		int time = (int) (data.getJSONObject("route").getDouble("time")*60);
		double space = data.getJSONObject("route").getDouble("dist");
		ArrayList<Car> cars= new ArrayList<Car>();
		for (int i = 0;i<data.getJSONArray("drivers").length();i++){
			JSONObject car = data.getJSONArray("drivers").getJSONObject(i);
			cars.add(new Car(car.getInt("DriverID"),"sidecar","",car.getInt("Price")*100,car.getInt("PickupETA")));
		}
		return new SidecarWrapper(cars.toArray(new Car[cars.size()]),time,space);
		
	}
	

}
