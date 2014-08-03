package api;

import helpers.Car;
import helpers.SidecarWrapper;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONArray;
import org.json.JSONObject;

import externalapi.Flywheel;
import externalapi.Lyft;
import externalapi.Sidecar;
import externalapi.Uber;

@Path("/test")
public class TestDriver {
	
	@Path("/login")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String login(String postData){
		JSONObject data = new JSONObject(postData);
		SidecarWrapper output = Sidecar.getPrices(data.getString("phone"),data.getString("password"),data.getDouble("slat"), data.getDouble("elat"), data.getDouble("slong"), data.getDouble("elong"));
		Car output2 = Lyft.getPrice(output.time, output.space);
		JSONArray temp = new JSONArray();
		for(Car x : output.cars){
			temp.put(x.toJSONObject());
		}
		return output2.toJSONObject().toString();
	}

}
