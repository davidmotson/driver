var username = "davidmotson@gmail.com";
var password = "password1";
var token;
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
	}
},function(result){
	simply.body("Error Logging In");
});
