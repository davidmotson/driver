var filteredOptions = [];
var cardict = {};
var carInfo = {};

var newOption = function(price, time, id, type, subtype) {
  d = {
    price: price,
    time: time,
    id: id,
    type: type, 
    subtype: subtype,
  }  
  return d;
}

function initializeCarOptions(){
    token = document.cookie;
    console.log("here");  
    // $.ajax({ 
    //   url: '/driver/api/info?token='+token+'&lat-start='+lat+'&lat-end='+destLat+
    //         '&long-start='+lon+'&long-end='+destLong,
    //   type: 'GET',
    //   success: function(data){
        console.log("here");
        //console.log(data);
        //cars = data.cars;
        //filler data because yay server crashes! 
        cars = [{type: "Uber", id: 1, subtype: "Classic", eta: "5", price: "10"},
                {type: "Lyft", id: 2, subtype: "", eta: "7", price: "12"},
                {type: "Sidecar", id: 3, subtype: "", eta: "Tan Mitsubishi Outlander", price: "13"}]
        var l = cars.length;
        for(i = 0; i < l; i++){
          car = cars[i];
          console.log(car);
          option = newOption(car.price, car.eta, car.id, car.type, car.subtype)
          filteredOptions.push(option);
          cardict[car.id] = option; 
        }
        sortByPrice();
      //}
    }//)
//}

function sortPriceHelper(op1, op2){
  return (op1["price"] - op2["price"]);
}

function sortByPrice(){
  $("#options-list").children().each(function(i, e){
    if(e.id != "tempListItem") e.remove();
  });
  filteredOptions.sort(sortPriceHelper);
  sorted = filteredOptions;
  l = sorted.length;
  for (i = 0; i<l; i++){
    addDOMOption(sorted[i]);
  } 
}

function sortTimeHelper(op1, op2){
  return (op1["time"] - op2["time"]);
}

function sortByTime(){
  $("#options-list").children().each(function(i, e){
    if(e.id != "tempListItem") e.remove();
  });
  filteredOptions.sort(sortPriceHelper);
  sorted = filteredOptions;
  l = sorted.length;
  for (i = 0; i < l; i++){
    addDOMOption(sorted[i]);
  } 
}

function secondToMin(seconds){
  return seconds/60;
}

function addDOMOption(option){
  e = $("#tempListItem").clone();
  e.attr("id", option["id"]);
  e.find(".type").text(option["type"] + ": " + option["subtype"]);
  e.find(".time").text(option["time"] + " min");
  e.find(".price").text("$" + option["price"]);
  el = $("#options-list").append(e);
  e.show();
}

function callCar(id){
  token = document.cookie;
  $.ajax({
    url: '/driver/api/summon',
    type: 'post',
    data: {
      token: token,
      id: id,
      latStart: lat, 
      latEnd: latEnd, 
      longStart: lon, 
      longEnd: longEnd,
    },
    dataType: 'json',
  }).done(function(data){
    car = cardict[id];
    parseColCar({type: car.type, price: car.price, eta: car.eta});
  })
}

$(document).ready(function(){
  $("#opt_btn_f").click(function(){
    sortByPrice();
  });
  $("#opt_btn_s").click(function(){
    sortByTime();
  });

  $(".car-col").click(function(){
    c = $(".car-col").children(".call-car #"+$(this).id).first();
    c.show();
    c.attr('id', $(this).id);
  });

  $(".call-car").click(function(e){
    $(".call-car").hide();
    var id = $(this).id;
    callCar(id);
    return false;
  });

})
