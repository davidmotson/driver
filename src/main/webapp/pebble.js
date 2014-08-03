var username = "davidmotson@gmail.com";
var password = "password1";
var token;
var favePointer = 0;
var favorites;
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
		
	}else{
		simply.body(e);
	}
}


