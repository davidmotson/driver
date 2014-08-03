package api;

import helpers.Car;
import helpers.DBHelper;
import helpers.Favorite;
import helpers.SidecarWrapper;
import helpers.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.json.JSONArray;
import org.json.JSONObject;

import externalapi.Flywheel;
import externalapi.Lyft;
import externalapi.Sidecar;
import externalapi.Uber;

@Path("/")
public class Driver {
	
	List<String> VALID_SERVICES = new ArrayList<String>();
	HashMap<String,User> sessions = new HashMap<String,User>();
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
		User user = DBHelper.getUser(data.getString("username"), data.getString("password"));
		if(user != null){
			String token = generateToken();
			sessions.put(token, user);
			user.timeout = System.currentTimeMillis() + 7200000;
			user.flywheelToken = Flywheel.login(user.getEmail(), user.getEmail()).token;
			return "{\"success\": true, \"token\": \"" + token + "\", \"user\": " + user.toJsonObject().toString()+"}";
		}
		return "{\"success\": false}";
		
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
		boolean success = false;
		if("uber".equals(data.getString("service"))){
			success = Uber.login(data.getString("username"), data.getString("password")).success;
		}else if ("lyft".equals(data.getString("service"))){
			success = Lyft.login(data.getString("username"), data.getInt("password")).success;
		}else if ("flywheel".equals(data.getString("service"))){
			success = Flywheel.login(data.getString("username"), data.getString("password")).success;
		}else if ("sidecar".equals(data.getString("service"))){
			success = Sidecar.login(data.getString("username"), data.getString("password")).success;
		}
		return "{success: "+ success + "}";
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
		if(!Flywheel.login(data.getJSONObject("flywheel").getString("username"), data.getJSONObject("flywheel").getString("password")).success){
			return "{success: false,fail-reason: 'bad flywheel credentials'}";
		}
		if(!Sidecar.login(data.getJSONObject("sidecar").getString("username"), data.getJSONObject("sidecar").getString("password")).success){
			return "{success: false,fail-reason: 'bad sidecar credentials'}}";
		}
		String uberToken;
		String lyftToken;
		Uber uber = Uber.login(data.getJSONObject("uber").getString("username"), data.getJSONObject("uber").getString("password"));
		if(!uber.success){
			return "{success: false,fail-reason: 'bad uber credentials'}}";
		}else{
			uberToken = uber.token;
		}
		Lyft lyft = Lyft.login(data.getJSONObject("lyft").getString("username"), data.getJSONObject("lyft").getInt("password"));
		if(!lyft.success){
			return "{success: false,fail-reason: 'bad lyft credentials'}}";
		}else{
			lyftToken = lyft.token;
		}
		DBHelper.createUser(data.getString("username"), data.getString("password"), data.getString("phonenumber"), uberToken, lyftToken, data.getJSONObject("sidecar").getString("password"), data.getJSONObject("flywheel").getString("password"));
		return login(postData);
	}
	
	@Path("/info")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getInfo(@QueryParam("token") String token,
						  @QueryParam("long-start") double longStart,
						  @QueryParam("long-end") double longEnd,
						  @QueryParam("lat-start") double latStart,
						  @QueryParam("lat-end") double latEnd){
		
		if(!sessions.containsKey(token)){
			return "{success: false}";
		}
		User user = sessions.get(token);
		JSONObject output = new JSONObject();
		JSONArray cars = new JSONArray();
		List<Car> carList = new ArrayList<Car>();
		SidecarWrapper sc = Sidecar.getPrices(user.getPhoneNumber(), user.getSidecarPassword(), latStart, latEnd, longStart, longEnd);
		Car[] uber = Uber.getPrices(latStart, latEnd, longStart, longEnd);
		Car flywheel = Flywheel.getPrice(latStart, longStart, sc.time, sc.space);
		Car lyft = Lyft.getPrice(sc.time, sc.space);
		carList.addAll(Arrays.asList(uber));
		carList.addAll(Arrays.asList(sc.cars));
		carList.add(flywheel);
		carList.add(lyft);
		Collections.sort(carList);
		for(Car x : carList){
			cars.put(x.toJSONObject());
		}
		output.put("cars", cars);
		output.put("success", true);
		
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
	
	@Path("/getcode/{phoneNumber}")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getCode(@PathParam("phoneNumber") String phoneNumber){
		Lyft.sendConfirmationCode(phoneNumber);
		return "";
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
		if(sessions.containsKey(data.getString("token"))){
			if(!DBHelper.createFavorite(sessions.get(data.getString("token")).getId(), data.getDouble("lat"), data.getDouble("long"), data.getString("name"))){
				return "{success: false}";
			}else{
				JSONArray output = new JSONArray();
				for(Favorite x : DBHelper.getFavorites(sessions.get(data.getString("token")).getId())){
					output.put(x.toJsonObject());
				}
				return "{success: true, faves: "+ output.toString()+"}";
			}
		}else{
			return "{success: false}";
		}
		
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
		if(!sessions.containsKey(data.getString("token"))){
			return "{success: false}";
		}
		if(!DBHelper.deleteFavorite(data.getInt("id"),sessions.get(data.getString("token")).getId())){
			return "{success: false}";
		}
		JSONArray output = new JSONArray();
		for(Favorite x : DBHelper.getFavorites(sessions.get(data.getString("token")).getId())){
			output.put(x.toJsonObject());
		}
		return "{success: true, faves: "+ output.toString()+"}";
	}
	
	private static final String alphaNum = "ABCDEFGHIJLKMNOPQRTSUVWXYZ1234567890abcdefghijklmnopqrstuvwxyz";
	
	private String generateToken(){
		StringBuilder token = new StringBuilder();
		for(int i=0;i<20;i++){
			token.append(alphaNum.charAt(generator.nextInt(alphaNum.length())));
		}
		return token.toString();
	}
	
	
	

}
