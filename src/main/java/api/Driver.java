package api;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.json.JSONArray;
import org.json.JSONObject;

@Path("/")
public class Driver {
	
	List<String> VALID_SERVICES = new ArrayList<String>();
	Random generator = new Random();
	
	{
		String[] list ={"uber","lyft","sidecar","flywheel"};
		for(String x : list){
			VALID_SERVICES.add(x);
		}
	}
	
	@Path("/login")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String login(String postData){
		JSONObject data = new JSONObject(postData);
		if(!data.has("username")){
			return "{success: false, fail-reason: 'no username'}";
		}
		if(!data.has("password")){
			return "{success: false, fail-reason: 'no password'}";
		}
		return "{success: true, token: 'asdfsa01239kw0fas7df1123asd', fave-locs:[{id: 1, long: 123.2,lat: 132.34, name: 'my house'},{id: 2, long: 123.3, lat:132.65, name: 'cute girls house'}]}";
		
	}
	
	@Path("/credtest")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String credTest(String postData){
		JSONObject data = new JSONObject(postData);
		if(!data.has("username")){
			return "{success: false, fail-reason: 'no username'}";
		}
		if(!data.has("password")){
			return "{success: false, fail-reason: 'no password'}";
		}
		if(!data.has("service")){
			return "{success: false, fail-reason: 'no service'}";
		}
		if(!VALID_SERVICES.contains(data.getString("service"))){
			return "{success: false, fail-reason: 'invalid service'}";
		}
		return "{success: true}";
	}
	
	@Path("/create")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String createAccount(String postData){
		JSONObject data = new JSONObject(postData);
		if(!data.has("username")){
			return "{success: false, fail-reason: 'no username'}";
		}
		if(!data.has("password")){
			return "{success: false, fail-reason: 'no password'}";
		}
		for(String x: VALID_SERVICES){
			if(!data.has(x)){
				return "{success: false, fail-reason: 'no " + x + "'}";
			}
		}
		return "{success: true, token: 'asdfsa01239kw0fas7df1123asd', fave-locs:[{id: 1, long: 123.2,lat: 132.34, name: 'my house'},{id: 2, long: 123.3, lat:132.65, name: 'cute girls house'}]}";
	}
	
	@Path("/info")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getInfo(@QueryParam("token") String token,
						  @QueryParam("long-start") double longStart,
						  @QueryParam("long-end") double longEnd,
						  @QueryParam("lat-start") double latStart,
						  @QueryParam("lat-end") double latEnd){
		
		JSONObject output = new JSONObject();
		JSONArray cars = new JSONArray();
		for(int i=0; i<10; i++){
			JSONObject car = new JSONObject();
			car.put("id", i);
			car.put("type", VALID_SERVICES.get(generator.nextInt(VALID_SERVICES.size())));
			car.put("eta", 300 + (generator.nextInt(300)-150));
			car.put("price", 1000 + (generator.nextInt(1000)-500));
			if(car.getString("type").equals("uber") && generator.nextBoolean()){
				car.put("subtype", "Uber XL");
			}
			cars.put(car);
		}
		output.put("cars",cars);
		
		return output.toString();
	}
	
	@Path("/summon")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String summon(String postData){
		JSONObject data = new JSONObject(postData);
		if(!data.has("token")){
			return "{success: false, fail-reason: 'no token'}";
		}
		if(!data.has("long-start")){
			return "{success: false, fail-reason: 'no long-start'}";
		}
		if(!data.has("long-end")){
			return "{success: false, fail-reason: 'no long-end'}";
		}
		if(!data.has("lat-end")){
			return "{success: false, fail-reason: 'no lat-end'}";
		}
		if(!data.has("lat-start")){
			return "{success: false, fail-reason: 'no lat-start'}";
		}
		return "{summoned: {car-image-url: 'http://lyftapi.s3.amazonaws.com/production/photos/320x200/1238612373_car.jpg', driver-image-url: 'https://lyftapi.s3.amazonaws.com/production/photos/320x200/1238612373_driver.jpg',type: 'uber', id: 5, eta: 300, price: 100, phone-number: '9139530509', car-desc: 'pimp as fuck'}}";
	}
	
	@Path("/unsummon")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String unsummon(String postData){
		JSONObject data = new JSONObject(postData);
		if(!data.has("token")){
			return "{success: false, fail-reason: 'no token'}";
		}
		if(!data.has("car-type")){
			return "{success: false, fail-reason: 'no car-type'}";
		}
		if(!data.has("id")){
			return "{success: false, fail-reason: 'no id'}";
		}
		return "{success: true}";
	}
	
	@Path("/favorite")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String favorite(String postData){
		JSONObject data = new JSONObject(postData);
		if(!data.has("token")){
			return "{success: false, fail-reason: 'no token'}";
		}
		if(!data.has("name")){
			return "{success: false, fail-reason: 'no name'}";
		}
		if(!data.has("lat")){
			return "{success: false, fail-reason: 'no lat'}";
		}
		if(!data.has("long")){
			return "{success: false, fail-reason: 'no long'}";
		}
		JSONObject output = new JSONObject();
		output.put("lat", data.getDouble("lat"));
		output.put("name", data.getString("name"));
		output.put("long", data.getDouble("long"));
		output.put("id", 5);
		return "{success: true, faves:[{id: 1, long: 123.2,lat: 132.34, name: 'my house'},{id: 2, long: 123.3, lat:132.65, name: 'cute girls house'},"+output.toString()+"]}";
	}
	
	@Path("/unfavorite")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String unfavorite(String postData){
		JSONObject data = new JSONObject(postData);
		if(!data.has("token")){
			return "{success: false, fail-reason: 'no token'}";
		}
		if(!data.has("id")){
			return "{success: false, fail-reason: 'no id'}";
		}
		return "{success: true, faves:[{id: 1, long: 123.2,lat: 132.34, name: 'my house'},{id: 2, long: 123.3, lat:132.65, name: 'cute girls house'}]}";
	}
	
	
	

}
