var filteredOptions = [];
var cardict = {};

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

function initializeOptions(){
    token = document.cookie;
    $.ajax({ 
      url: ' /api/info?token='+token+'&lat-start='+lat+'&lat-end='+destLat+
            '&long-start='+lon+'&long-end='+destLong,
      type: 'GET'
    }).done(function(data){
      console.log("here");
      console.log(data);
      cars = data["cars"];
      var l = cars.length;
      for(i = 0; i < l; i++){
        console.log(car);
        car = cars[i];
        option = newOption(car["price"], car["eta"], car["id"], car["type"], car["subtype"])
        filteredOptions.push(option);
        cardict[car["id"]] = option; 
      }
      sortByPrice();
    })
}

function sortPriceHelper(op1, op2){
  return (op1["price"] - op2["price"]);
}

function sortByPrice(){
  $("#options-table").each(function(i, e){
    if(e.attr(id) != "tempListItem") e.remove();
  });
  sorted = filteredOptions.sort(sortPriceHelper);
  l = sorted.length;
  for (i = 0; i<l; i++){
    addDOMOption(sorted[i]);
  } 
}

function sortTimeHelper(op1, op2){
  return (op1["time"] - op2["time"]);
}

function sortByTime(){
  $("#options-table").each(function(i, e){
    if(e.attr(id) != "tempListItem") e.remove();
  });
  sorted = filteredOptions.sort(sortTimeHelper);
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
  e.attr("id", "");
  e.find(".type").text(option["type"] + ": " + option["subtype"]);
  e.find(".time").text(option["time"] + " min");
  e.find(".price").text("$" + option["price"]);
  el = $("#options-list").append(e);
  e.show();
}

$(document).ready(function(){
  test = newOption(5, 4, 3, "Lyft", "Car")
  addDOMOption(test);
})