package helpers;

import org.json.JSONArray;
import org.json.JSONObject;

public class User {
	
	private int id;
	private String email;
	private String phoneNumber;
	private String uberToken;
	private String lyftToken;
	private String flywheelPassword;
	private String sidecarPassword;
	private Favorite[] favorites;
	public long timeout;

	public User(String email, String phoneNumber, String uberToken,
			String lyftToken, String flywheelPassword, String sidecarPassword, Favorite[] favorites, int id) {
		super();
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.uberToken = uberToken;
		this.lyftToken = lyftToken;
		this.flywheelPassword = flywheelPassword;
		this.sidecarPassword = sidecarPassword;
		this.favorites = favorites;
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public int getId(){
		return id;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getUberToken() {
		return uberToken;
	}

	public String getLyftToken() {
		return lyftToken;
	}

	public String getFlywheelPassword() {
		return flywheelPassword;
	}

	public String getSidecarPassword() {
		return sidecarPassword;
	}
	
	public Favorite[] getFavorites(){
		return favorites;
	}
	
	public JSONObject toJsonObject(){
		JSONObject output = new JSONObject();
		output.put("email", email);
		output.put("phone-number", phoneNumber);
		JSONArray favorites = new JSONArray();
		for(Favorite x : this.favorites){
			favorites.put(x.toJsonObject());
		}
		output.put("favorites",favorites);
		return output;
	}
	
	
	
}
