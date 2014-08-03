var sendLyftCode;

$(document).ready(function(){
  $(".login-credentials input[type=email]").click(function(){
    if(this.value == "Email"){
      $(this).val("");
    } else {
      $(this).select();
    }
  }).blur(function() {
    if (this.value == "") {
        $(this).val("Email");
    }
  });

  $(".login-credentials input[type=password]").click(function(){
    if(this.value == "Password"){
      $(this).val("");
    } else {
      $(this).select();
    }
  }).blur(function() {
    if (this.value == "") {
        $(this).val("Password");
    }
  });

  var user_email;
  var user_phone;
  function validateEmail(email) { 
    var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(email);
  } 
  function invalidPassword(password){
    if (password.length < 6) {
      return "Password must be 6 or more charecters!"
    } else {
      return false;
    }
  }


  var error = $("#error-msg");

  $("validate-driver-credentials").click(function(){
    var email = $("#driver-email").text()
    var password = $("#driver-password").text();
    var passwordConfirm = $("#driver-password-confirm").text();
    var phone = $("#driver-phone");
    if (!validateEmail(email)){
      error.text("Invalid Email!");
      error.show();
    } else if (passwordConfirm != passwordConfirm) {
      error.text("Passwords don't match!");
      error.show();
    } else if (invalidPassword(password)){
      error.text(invalidPassword(password));
      error.show();
    } else {
      goto('login-uber');
    }
  });

  $("validate-uber-credentials").click(function(){ 
    var username = $("#uber-email").text()
    var password = $("#uber-password").text();
    $.ajax({
      url: '/api/create',
      type: 'POST',
      data: JSON.stringify({ 
        username: username,
        password: password,
        serivice: 'uber',
      }),
      dataType: 'json',
  }).done(function(data){
    if (data["success"]){
      goto("login-lyft");
    } else {
      error.text("There was an error with your Username/Password");
      error.show();
    }
  });

  $("validate-lyft-credentials").click(function(){ 
    var username = $("#lyft-email").text()
    var password = $("#lyft-password").text();
    $.ajax({
      url: '/api/create',
      type: 'POST',
      data: JSON.stringify({ 
        username: username,
        password: password,
        serivice: 'lyft',
      }),
      dataType: 'json',
  }).done(function(data){
    if (data["success"]){
      goto("login-lyft");
    } else {
      error.text("There was an error with your Username/Password");
      error.show();
    }
  });


  $("#lyft-phone").click(function(e){
    e.preventDefault();
  })
  sendLyftCode = function(){
    var phone = $("#lyft-phone").text();
    //Lyft send code? 
  }

  $("driver-signup").click(function(){
    $.ajax({
      url: '/api/create',
      type: 'POST',
      data: JSON.stringify({
        username: $("#driver-email").text(),
        password: $("#driver-password").text(),
        phone: $("#driver-phone"),
        uber: {
          username: $("#uber-email").text(),
          password: $("#uber-password").text(),
        },
        lyft: {
          username: $("#lyft-email").text(),
          password: $("#lyft-password").text(),
        }, 
        sidecar: {
          username: $("#sidecar-email").text(),
          password: $("#sidecar-password").text(),
        },
        flywheel: {
          username: $("#flywheel-email").text(),
          password: $("#flywheel-password").text(),
        },
      }),
      dataType: 'json',
      success: function(data) {
        if(!data["success"]){
          error.text(data["fail-reason"]);
          error.show();
          return; 
        }
        faves = data["fave-locs"];
        token = data["token"];
        document.cookie="token="+token; 
      }
    })
  });

});