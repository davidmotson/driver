var username = "davidmotson@gmail.com";
var password = "password1";
var token;
var favePointer = 0;
var favorites;
var cars;
var summoned;
simply.fullscreen(true);
simply.scrollable(true);
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
		favorites = result.favorites;
		if(favorites.length == 0){
			simply.body("You have no Favorite places to go");
			return false;
		}
		simply.subtitle("Where to?");
		simply.body(favorites[favePointer].name);
		simply.on('singleClick',faceMover);

	}
},function(result){
	simply.body("Error Logging In");
});


var faveMover = function(e){
	if(e === "up"){
		if(favePointer == 0){
			return false;
		}
		favePointer--;
		simply.body(favorites[favePointer].name);
	}else if(e === "down"){
		if(favePointer == favorites.length-1){
			return false;
		}
		favePointer++;
		simply.body(favorites[favePointer].name);
	}else if(e === "select"){

	}
}


