
function goto(page_id) {
  $(".page").hide();
  $("#"+page_id).show();
  $("header").show();
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
