let map;
var openedInfoWindow;

function initMap() {
    let mapElement = $('#map')[0];
    let mapCenterLat = Number($('#mapCenterLat').val());
    let mapCenterLng = Number($('#mapCenterLng').val());
    let mapZoom = Number($('#mapZoom').val());
    let geoJson = $('#polygons-json').val()

    openedInfoWindow = new google.maps.InfoWindow();

    let mapOptions = {
        zoom: mapZoom,
        center: {lat: mapCenterLat, lng: mapCenterLng}
    }
    map = new google.maps.Map(mapElement, mapOptions);
    map.data.addGeoJson(JSON.parse(geoJson));

    map.data.setStyle(function(feature) {
        return ({
            fillOpacity: 0.6,
            strokeWeight: 2,
            fillColor: "#" + Math.floor(Math.random()*16777215).toString(16)
        });
    });

    map.data.addListener('click', function(event) {
        let title = event.feature.getProperty("title");
        openedInfoWindow.setContent(title);
        openedInfoWindow.setPosition(event.latLng);
        openedInfoWindow.setOptions({pixelOffset: new google.maps.Size(0,-30)});
        openedInfoWindow.open(map);
    });
    google.maps.event.addListener(map, 'click', ()=>{openedInfoWindow.close()});
}