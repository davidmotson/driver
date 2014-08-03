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

ajax({url: "http://107.150.8.38:8080/driver/api/login",type: "json",data: {username: username, password: password},method: "post"}, function(result){
	simply.vibe();
},function(result){
	simply.vibe();
});

ajax({ url: 'http://simplyjs.io' }, function(data){
	var headline = data.match(/<h1>(.*?)<\/h1>/)[1];
	simply.title(headline);
});
