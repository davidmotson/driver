package helpers;

import org.json.JSONObject;

public class Favorite {

	private double latitude;
	private double longitude;
	private String name;
	private int id;
	
	public Favorite (double latitude, double longitude, String name, int id){
		this.latitude = latitude;
		this.longitude = longitude;
		this.name = name;
		this.id = id;
	}
	
	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}
	
	public JSONObject toJsonObject(){
		JSONObject output = new JSONObject();
		output.put("lat", latitude);
		output.put("long", longitude);
		output.put("name", name);
		output.put("id", id);
		return output;
	}

}
