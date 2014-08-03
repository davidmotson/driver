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
	data: JSON.stringify({username: username, password: password}),
	async: true
}, function(result){
	simply.body(result)
},function(result){
	simply.body(result);
	simply.title("error");
});

ajax({ url: 'http://simplyjs.io' }, function(data){
	var headline = data.match(/<h1>(.*?)<\/h1>/)[1];
	simply.title(headline);
});
