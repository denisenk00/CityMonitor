let map;
let drawingManager;
let polygons = [];
let polyTitles = new Map();
let dataLayer ;
let selectedShape;
let uniqueId = 0;
var openedInfoWindow;

function initMap(){
    let mapElement = $('#map')[0];
    let mapCenterLat = Number($('#mapCenterLat').val());
    let mapCenterLng = Number($('#mapCenterLng').val());
    let mapZoom = Number($('#mapZoom').val());
    openedInfoWindow = new google.maps.InfoWindow();

    let mapOptions = {
        zoom : mapZoom,
        center : {lat: mapCenterLat, lng : mapCenterLng}
    }
    map = new google.maps.Map(mapElement, mapOptions);
    drawingManager = new google.maps.drawing.DrawingManager({
        drawingMode: google.maps.drawing.OverlayType.POLYGON,
        drawingControl: true,
        drawingControlOptions: {
            position: google.maps.ControlPosition.TOP_CENTER,
            drawingModes: [google.maps.drawing.OverlayType.POLYGON]
        },
        polygonOptions: {
            clickable: true,
            fillOpacity: 0.6,
            strokeWeight: 2
        }
    });
    drawingManager.setMap(map);

    const removeShapeButton = createRemoveShapeButton();
    const holeControlDiv = createHoleControlDiv();

    map.controls[google.maps.ControlPosition.TOP_CENTER].push(holeControlDiv);
    map.controls[google.maps.ControlPosition.TOP_CENTER].push(removeShapeButton);

    google.maps.event.addListener(drawingManager, "polygoncomplete", function (polygon){
        var path = polygon.getPath().getArray();
        console.log(path.length);
        //check if polygon properly made
        if (path.length < 3) {
            polygon.setMap(null);
            return;
        }

        path = rewindRing(path, true);
        polygon.setPath(path);

        var foundParent = false;
        //if user want to make a hole in some polygon
        if($("#hole-checkbox").is(':checked')) {
            for (var i = 0; i < polygons.length; i++) {
                if (isPolygonInsidePolygon(polygon, polygons[i])) {
                    foundParent = true;
                    var path = polygon.getPath().getArray();
                    path = rewindRing(path, false);
                    polygons[i].getPaths().push(new google.maps.MVCArray(path));
                    polygon.setMap(null);
                    break;
                }
            }
        }
        if (!foundParent) {
            polygon.id = getUniqueId();
            polygons.push(polygon);
        }
        //set random color
        let randomColor = "#" + Math.floor(Math.random()*16777215).toString(16);
        let options = {
            fillColor: randomColor,
        }
        polygon.setOptions(options);

        // Switch back to non-drawing mode after drawing a shape.
        drawingManager.setDrawingMode(null);
        setSelection(polygon);

        google.maps.event.addListener(polygon, 'click', () => {
            setSelection(polygon);
        });

        google.maps.event.addListener(polygon, 'contextmenu', (ev) => {
            openedInfoWindow.close();
            selectedShape = polygon;
            drawPolygonWindow(ev, polygon);
        });
    });

    google.maps.event.addListener(drawingManager, 'drawingmode_changed', clearSelection);
    google.maps.event.addListener(map, 'click', clearSelection);
}

function drawPolygonWindow(ev, poly){
    let title = '';
    if(polyTitles.has(poly.id)) title = polyTitles.get(poly.id);

    let content =
        "<div class=\"needs-validation\" style='margin: 15px; width: 300px'>" +
        "   <label for='poly-title' class=\"form-label\">Опис району:</label>" +
        "   <input class=\"form-control\" id='poly-title' value='" + title +"' title='Введіть інформацію, що характеризує цей район'>" +
        "   <div class=\"invalid-feedback\"></div>" +
        "   <br>" +
        "   <button id='save-poly-params' class='btn submit-btn' type='button'>Зберегти</button>" +
        "</div>"
    openedInfoWindow.setContent(content);
    openedInfoWindow.setPosition(ev.latLng)
    openedInfoWindow.open(map);

    $("#save-poly-params").click((e)=>{
        let title = $('#poly-title').val().trim();
        if(title.length >= 10 && title.length <= 100){
            console.log("id = " + selectedShape.id + ", title = " + title)
            polyTitles.set(selectedShape.id, title);
            setValid($('#poly-title'), "Дані збережено успішно.");
        }else {
            setInvalid($('#poly-title'), "Мінімальна довжина опису 10 символів, максимальна - 100.");
        }
    });
}

function getUniqueId(){
    return uniqueId++;
}

function setValid(e, message){
    e.removeClass('is-invalid').addClass('is-valid');
    e.next('.invalid-feedback').empty();
    if(message != null) e.next('.invalid-feedback').text(message);
}

function setInvalid(e, message){
    e.removeClass('is-valid');
    e.addClass('is-invalid');
    if(message != null) e.next('.invalid-feedback').text(message);
}
// from https://github.com/mapbox/geojson-rewind/blob/main/index.js
function rewindRing(ring, dir) {
    var area = 0;
    for (var i = 0, len = ring.length, j = len - 1; i < len; j = i++) {
        area += ((ring[i].lng() - ring[j].lng()) * (ring[j].lat() + ring[i].lat()));
    }
    console.log("area=" + area + " dir=" + dir);
    if (area >= 0 !== !dir)
        ring.reverse();
    return ring;
}

function isPolygonInsidePolygon(innerPolygon, outerPolygon) {
    var points = innerPolygon.getPath().getArray();

    for (var i = 0; i < points.length; i++) {
        if (!google.maps.geometry.poly.containsLocation(points[i], outerPolygon)) {
            return false;
        }
    }
    return true;
}

function createRemoveShapeButton(){
    const button = document.createElement("button");
    button.type = "button";
    button.id = "remove-shape";
    button.style.border = "none";
    button.style.width = "24px";
    button.style.height = "24px";
    button.style.margin = "5px";
    button.style.padding = "0px"
    button.style.backgroundColor = "#ffffff"

    button.innerHTML = "<i class=\"bi bi-trash3-fill\" style=\"font-size: 16px\">";

    button.addEventListener("click", deleteSelectedShape);
    button.addEventListener("mouseover", function (){
        button.style.backgroundColor = "rgb(235 235 235)";
    })
    button.addEventListener("mouseout", function (){
        button.style.backgroundColor = "#ffffff";
    })

    return button;
}

function createHoleControlDiv(){
    const holeControlDiv = document.createElement("div");
    holeControlDiv.style.backgroundColor = "#ffffff";
    holeControlDiv.style.margin = "5px";
    holeControlDiv.style.width = "auto";
    holeControlDiv.style.padding = "5px";
    holeControlDiv.style.height = "24px";
    holeControlDiv.style.textAlign = "center";
    holeControlDiv.style.fontSize = "12px";
    holeControlDiv.style.verticalAlign = "middle";

    const checkbox = document.createElement("input");
    checkbox.type = "checkbox";
    checkbox.id = "hole-checkbox";

    const label = document.createElement("label");
    label.htmlFor = "hole-checkbox";
    label.innerText = "Діра"
    label.style.paddingLeft = "3px";

    holeControlDiv.appendChild(checkbox);
    holeControlDiv.appendChild(label);

    return holeControlDiv;
}

function setSelection (polygon) {
    clearSelection();
    let options = {
        draggable: true,
        editable: true,
        fillOpacity: 0.4,
        strokeWeight: 1
    }
    polygon.setOptions(options);
    selectedShape = polygon;
}

function clearSelection () {
    openedInfoWindow.close();
    if (selectedShape) {
        let options = {
            editable: false,
            draggable: false,
            fillOpacity: 0.6,
            strokeWeight: 2
        }
        selectedShape.setOptions(options);
        selectedShape = null;
    }
}

function deleteSelectedShape () {
    openedInfoWindow.close();
    if (selectedShape) {
        for (var i = 0; i < polygons.length; i++) {
            console.log("selected = "  +selectedShape.id + " , polId = " + polygons[i].id)
            if (polygons[i].id == selectedShape.id) {
                //Remove the marker from Map
                polygons[i].setMap(null);
                //Remove the marker from array.
                polygons.splice(i, 1);
                return;
            }
        }
    }
}

function validateInput(e){
    let id = e.attr('id');
    let val = e.val();
    switch (id){
        case "layout-title":
            if(val.length < 10 || val.length > 100){
                setInvalid(e, "Мінімальна довжина назви 10 символів, максимальна - 100.")
            }else {
                setValid(e, null);
            }
            break;
        case "polygons-json":
            if(polygons.length == 0){
                setInvalid(e, "Макет не може бути пустим.");
            }else if(polygons.length != polyTitles.size){
                polygons.forEach((p) => {
                    if(!polyTitles.has(p.id)){
                        p.setOptions({strokeColor: "#ff0018"})
                    }
                });
                setInvalid(e, "Кожен район повинен мати опис. Для його вказання натисніть правою кнопкою миші по багатокутнику");
            }else{
                setValid(e, null);
            }
            break;
    }
}

$('#layout-title').blur(function (){
    validateInput($(this));
})

//validation on submitting
$('#layoutForm').submit((ev) => {
    ev.preventDefault();
    validateInput($('#layout-title'));
    validateInput($('#polygons-json'));

    if ($('.is-invalid').length == 0) {
        getPolygonsGeoJson()
            .done(function (json) {
                $('#polygons-json').val(json);
                $('#layoutForm')[0].submit();
            })
            .fail(function (){
                console.log("Getting polygons is failed")
            });
    }
});

function getPolygonsGeoJson(){
    let dfrd = $.Deferred();
    dataLayer = new google.maps.Data();
    polygons.forEach((poly) => {
        let paths = [];
        poly.getPaths().forEach((p) => {
            var path = [];
            for (k=0; k < p.getLength(); k++) {
                path.push(p.getAt(k));
            }
            paths.push(path);
        })
        let title = "";
        if(polyTitles.has(poly.id)){
            title = polyTitles.get(poly.id);
        }
        dataLayer.add(new google.maps.Data.Feature({
            geometry: new google.maps.Data.Polygon(paths),
            properties: {"title" : title}
        }));
    })
    let json = null;
    dataLayer.toGeoJson(function(obj) {
        json = JSON.stringify(obj);
        dfrd.resolve(json);
    });

    return dfrd;
}