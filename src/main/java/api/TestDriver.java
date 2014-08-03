package api;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;

import externalapi.Flywheel;
import externalapi.Lyft;
import externalapi.Sidecar;

@Path("/test")
public class TestDriver {
	
	@Path("/login")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String login(String postData){
		JSONObject data = new JSONObject(postData);
		Lyft output = Lyft.login(data.getString("number"), data.getInt("code"));
		if(output.success){
			return output.self.toString();
		}
		return "{success: false}";
	}

}
