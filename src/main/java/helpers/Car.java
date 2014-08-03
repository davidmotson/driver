package helpers;

import org.json.JSONObject;

public class Car implements Comparable{
	
	private int id;
	private String type;
	private String subtype;
	private int price;
	private int eta;
	
	public Car(int id, String type, String subtype, int price, int eta) {
		super();
		this.id = id;
		this.type = type;
		this.subtype = subtype;
		this.price = price;
		this.eta = eta;
	}

	public int getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public String getSubtype() {
		return subtype;
	}

	public int getPrice() {
		return price;
	}

	public int getEta() {
		return eta;
	}
	
	public JSONObject toJSONObject(){
		return new JSONObject()
		.put("id", id)
		.put("type", type)
		.put("subtype", subtype)
		.put("price", price)
		.put("eta",eta);
	}

	@Override
	public int compareTo(Object arg0) {
		return price - ((Car)arg0).price;
	}
	
	

}
