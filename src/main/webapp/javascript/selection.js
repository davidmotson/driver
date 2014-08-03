function parseCallCar(jsonObject){
	var data = JSON.parse(jsonObject);
	document.getElementById("eta_time").innerHTML =
	data.summoned.eta + " min";
	
	document.getElementById("car_view").src = 
	data.summoned.car-image-url;

	document.getElementById("driver_profile").src =
	data.summoned.driver-image-url;

	document.getElementById("phone_num").innerHTML = 
	data.summoned.phone-number;

	document.getElementById("call_link").href = 
	data.summoned.phone-number;

	//document.getElementById("cancel_link").href = 
	//data.
}

