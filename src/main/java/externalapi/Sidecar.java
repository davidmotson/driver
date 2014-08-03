package externalapi;

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

}
