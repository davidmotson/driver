var geocoder;
var faves;
var lat = 37.385696;
var lon = -122.066327;
var destLat;
var destLong;
var map;
function initialize() {
  var markers = [];
	 geocoder = new google.maps.Geocoder();
   map = new google.maps.Map(document.getElementById('map-canvas'), {
    mapTypeId: google.maps.MapTypeId.ROADMAP,
    disableDefaultUI: true,
    mapTypeControlOptions:{
      mapTypeIds: [google.maps.MapTypeId.RoadMAP]
    }
  });

 $('<div/>').addClass('centerMarker').appendTo(map.getDiv())

/*	marker = new google.maps.Marker({
		position: map.getCenter(),
		map: map
	});
	google.maps.event.addListener(map, 'drag',function(){
		marker.setPosition(map.getCenter());
	});*/
	google.maps.event.addListener(map, 'dragend',function(){
		destLat = map.getCenter().k;
		destLong = map.getCenter().B;
		geocoder.geocode({location: map.getCenter()}, function(result,status){
		});
	});
  var defaultBounds = new google.maps.LatLngBounds(
      //new google.maps.LatLng(-33.8902, 151.1759),
      //new google.maps.LatLng(-33.8474, 151.2631));
      new google.maps.LatLng(lat+.0114, lon-.0236),
      new google.maps.LatLng(lat-.0114, lon+.0236));
  map.fitBounds(defaultBounds);

  // Create the search box and link it to the UI element.
  var input = /** @type {HTMLInputElement} */(
      document.getElementById('pac-input'));
  map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);

  var searchBox = new google.maps.places.SearchBox(
    /** @type {HTMLInputElement} */(input));

  // [START region_getplaces]
  // Listen for the event fired when the user selects an item from the
  // pick list. Retrieve the matching places for that item.
  google.maps.event.addListener(searchBox, 'places_changed', function() {
    var places = searchBox.getPlaces();

    if (places.length == 0) {
      return;
    }
    for (var i = 0, marker; marker = markers[i]; i++) {
      marker.setMap(null);
    }

    // For each place, get the icon, place name, and location.
    markers = [];
		for (var i = 0; i < faves.length; i++){
      var image = {
        url: place.icon,
        size: new google.maps.Size(71, 71),
        origin: new google.maps.Point(0, 0),
        anchor: new google.maps.Point(17, 34),
        scaledSize: new google.maps.Size(25, 25)
      };
      var marker = new google.maps.Marker({
        map: map,
        icon: image,
        title: faves[i].name,
        position: new google.maps.LatLng(faves[i].lat, faves[i].long)
      });
      markers.push(marker);
      bounds.extend(place.geometry.location);
		}
    var bounds = new google.maps.LatLngBounds();
    for (var i = 0, place; place = places[i]; i++) {
      var image = {
        url: place.icon,
        size: new google.maps.Size(71, 71),
        origin: new google.maps.Point(0, 0),
        anchor: new google.maps.Point(17, 34),
        scaledSize: new google.maps.Size(25, 25)
      };

      // Create a marker for each place.
      var marker = new google.maps.Marker({
        map: map,
        icon: image,
        title: place.name,
        position: place.geometry.location
      });

      markers.push(marker);

      bounds.extend(place.geometry.location);
    }

    map.fitBounds(bounds);
  });
  // [END region_getplaces]

  // Bias the SearchBox results towards places that are within the bounds of the
  // current map's viewport.
  google.maps.event.addListener(map, 'bounds_changed', function() {
    var bounds = map.getBounds();
    searchBox.setBounds(bounds);
  });
}
$(document).ready(function(){
	$("#find_driver").click(function(){
		goto("options")
	});
});
