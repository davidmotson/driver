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
	data: JSON.stringify({username: username, password: password}),
	async: true
}, function(result){
	simply.body(result)
},function(result){
	simply.body(result);
	simply.title("error");
});

ajax({ url: 'http://xkcd.com/614/info.0.json', type: 'json' }, function(data){
	simply.body(data);
});
