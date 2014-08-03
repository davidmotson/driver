
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

});