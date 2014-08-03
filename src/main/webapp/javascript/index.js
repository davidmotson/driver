
var PageNames = {
  "login": "Login to driver",
  "signup": "Create a Driver Account",
  "login-uber": "Login to Uber",
  "login-lyft": "Login to Lyft",
  "login-sidecar": "Login to Sidecar",
  "login-flywheel": "Login to Flywheel",
}

function goto(page_id) {
  $(".page").hide();
  $("#"+page_id).show();
  $("header").show();
  $("#error-msg").text("");
  $("#error-msg").hide();
  if(PageNames[page_id]){
    $("#page-header").text(PageNames[page_id]);
  } 
  /*if(page_id = "options") {
    initializeOptions();
  }*/
  if(page_id == "map"){
    initialize();
  }
}

$(document).ready(function(){

  $("form button").click(function(e){
    e.preventDefault();
  })
    
  if($(window.location.hash).length > 0){
    $(window.location.hash).show();
  } else {
    $("#splash-screen").show();
  }
  if(window.location.hash != "splash-screen"){
    $("header").show();
  }
});
