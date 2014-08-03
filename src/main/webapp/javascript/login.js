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
    if (password.length < 6 || password == "Password") {
      return "Password must be 6 or more charecters!"
    } else {
      return false;
    }
  }


  var error = $("#error-msg");

  $("#login-submit").click(function(){
    username = $("#login-username").val();
    password = $("#login-password").val();
    $.ajax({
      url: '/driver/api/login', 
      type: 'POST',
      data: JSON.stringify({ 
        username: username,
        password: password,
      }),
      dataType: 'json',
    }).done(function(data){
      if(data["success"]){
        faves = data["fave-locs"];
        token = data["token"];
        document.cookie=token; 
        goto('map');
      } else {
        error.text(data["fail-reason"]);
        error.show();
      }
    })
  })

  $("#validate-driver-credentials").click(function(){
    error.text("");
    var email = $("#driver-email").val();
    var password = $("#driver-password").val();
    if (!validateEmail(email)){
      error.text("Invalid Email!");
      error.show();
    } else if (invalidPassword(password)){
      error.text(invalidPassword(password));
      error.show();
    } else {
      $("input[type=email]").val($("#driver-email").text());
      goto('login-uber');
    }
  });

  $("#validate-uber-credentials").click(function(){ 
    var username = $("#uber-email").val();
    var password = $("#uber-password").val();
    $.ajax({
      url: '/driver/api/credtest',
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
  });

  $("validate-lyft-credentials").click(function(){ 
    var username = $("#lyft-email").val();
    var password = $("#lyft-code").val();
    $.ajax({
      url: '/driver/api/credtest',
      type: 'POST',
      data: JSON.stringify({ 
        username: username,
        password: password,
        serivice: 'lyft',
      }),
      dataType: 'json',
    }).done(function(data){
      if (data["success"]){
        goto("login-sidecar");
      } else {
        error.text("There was an error with your Username/Password");
        error.show();
        $("pre-lyft-text").show();
        $("post-lyft-text").hide();
      }
    });
  });


  $("#lyft-phone").click(function(e){
    e.preventDefault();
  })
  sendLyftCode = function(){
    var phone = $("#lyft-phone").val();
    $.ajax({
      url: '/driver/api/getcode/'+phone,
      type: 'GET',
    }).done(function(){
      $("pre-lyft-text").hide();
      $("post-lyft-text").show();
    })
  }

  $("validate-sidecar-credentials").click(function(){ 
    var username = $("#sidecar-phone").val();
    var password = $("#sidecar-password").val();
    $.ajax({
      url: '/driver/api/credtest',
      type: 'POST',
      data: JSON.stringify({ 
        username: username,
        password: password,
        serivice: 'sidecar',
      }),
      dataType: 'json',
    }).done(function(data){
      if (data["success"]){
        goto("map");
      } else {
        error.text("There was an error with your Username/Password");
        error.show();
      }
    });
  });


  var validateFlywheelCredentials = function(){ 
    var username = $("#flywheel-email").val();
    var password = $("#flywheel-password").val();
    $.ajax({
      url: '/driver/api/credtest',
      type: 'POST',
      data: JSON.stringify({ 
        username: username,
        password: password,
        serivice: 'lyft',
      }),
      dataType: 'json',
    }).done(function(data){
      if (data["success"]){
        return true;
      } else {
        error.text("There was an error with your Username/Password");
        error.show();
        return false;
      }
    });
  }

  $("#driver-signup").click(function(){
    if (validateFlywheelCredentials()){
      $.ajax({
        url: '/driver/api/create',
        type: 'POST',
        data: JSON.stringify({
          username: $("#driver-email").val(),
          password: $("#driver-password").val(),
          phone: $("#driver-phone"),
          uber: {
            username: $("#uber-email").val(),
            password: $("#uber-password").val(),
          },
          lyft: {
            username: $("#lyft-phone").val(),
            password: $("#lyft-code").val(),
          }, 
          sidecar: {
            username: $("#sidecar-phone").val(),
            password: $("#sidecar-password").val(),
          },
          flywheel: {
            username: $("#flywheel-email").val(),
            password: $("#flywheel-password").val(),
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
    }
  });

});