var username = "davidmotson@gmail.com";
var password = "password2";
var token;
var cars;
var summoned;

simply.fullscreen(true);
simply.text({
	title: "Driver",
	subtitle: "Welcome to Driver",
	body: "Logging in..."
});

simply.ajax({
	method: "post",
	url: "http://107.150.8.38:8080/driver/api/login",
	type: "json",
	data: {username: username, password: password},
}, function(result){
	simply.body(result);
});
