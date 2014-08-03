
function goto(page_id) {
  $(".page").hide();
  $("#"+page_id).show();
  $("header").show();
  //test
}

$(document).ready(function(){
  $("form button").click(function(e){
    e.preventDefault();
  })
  
  //console.log($(window.location.hash));
  
  if($(window.location.hash).length > 0){
    $(window.location.hash).show();
  } else {
    $("#splash-screen").show();
  }
});
