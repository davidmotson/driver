var username = "davidmotson@gmail.com";
var password = "password1";
var lat = 37.386003;
var long = -122.066665;
var token;
var favePointer = 0;
var favorites;
var carPointer = 0;
var cars;
var summoned;
simply.fullscreen(true);
simply.text({
	title: "Driver",
	subtitle: "Welcome to Driver",
	body: "Logging in..."
});

ajax({
	method: 'post',
	url: 'http://107.150.8.38:8080/driver/api/login',
	type: 'json',
	data: {username: username, password: password},
}, function(result){
	if(!result.success){
		simply.body("Error Logging In");
	}else{
		token = result.token;
		favorites = result.user.favorites;
		if(favorites.length == 0){
			simply.body("You have no Favorite places to go");
			return;
		}
		simply.subtitle("Where to?");
		simply.body(favorites[favePointer].name);
		simply.on('singleClick',faveMover);

	}
},function(result){
	simply.body("Error Logging In");
});


var faveMover = function(e){
	if(e.button === "up"){
		if(favePointer == 0){
			return;
		}
		favePointer--;
		simply.body(favorites[favePointer].name);
	}else if(e.button === "down"){
		if(favePointer == favorites.length-1){
			return;
		}
		favePointer++;
		simply.body(favorites[favePointer].name);
	}else if(e.button === "select"){
		simply.subtitle("Getting Ride Info!");
		simply.body("Please Wait...");
		ajax({
			url: 'http://107.150.8.38:8080/driver/api/info?token='+token+'&lat-start='+lat+'&long-start='+long+'&lat-end='+favorites[favePointer].lat+'&long-end='+favorites[favePointer].long,
		},function(e){
			simply.off('singleClick');
			cars = [{
				type: 'uberx',
				price: 854,
				eta: 300
			},{
				type: 'uberxL',
				price: 1283,
				eta: 300
			},{
				type: 'lyft',
				price: 763,
				eta: 342,
			},{
				type: 'sidecar',
				price: 1023,
				eta: 219,
			}];
			simply.on('singleClick',carMover);
			simply.subtitle(car[carPointer].type);
			simply.body("$"+(car[carPointer].price/100));
		});
	}
}

var carMover = function(e){
	if(e.button === "up"){
		if(carPointer == 0){
			return;
		}
		carPointer--;
		simply.subtitle(car[carPointer].type);
		simply.body("$"+(car[carPointer].price/100));
	}else if(e.button === "down"){
		if(carPointer == cars.length-1){
			return;
		}
		carPointer++;
		simply.subtitle(car[carPointer].type);
		simply.body("$"+(car[carPointer].price/100));
	}else if(e.button === "select"){
		simply.subtitle("Fetching Ride");
		simply.body("Please Wait...")
	}
}


