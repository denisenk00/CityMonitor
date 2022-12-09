let map;
let censusMin = Number.MAX_VALUE,
    censusMax = -Number.MAX_VALUE;
let geoJson = $('#polygons-json').val();
let resultsData = JSON.parse($('#results-json').val());

let generalResults = getGeneralResults();

google.charts.load('current', {'packages': ['corechart']});
google.charts.setOnLoadCallback(() => {
    let ele = $('#general-statistics-chart')[0];
    drawChart(ele, 'Загальна статистика', generalResults);
});


function drawChart(element, title, data) {
    data = google.visualization.arrayToDataTable(data);
    var options = {
        title: title,
        is3D: true
    };

    var chart = new google.visualization.PieChart(element);
    chart.draw(data, options);
}

function getGeneralResults() {
    let generalResults = new Map();
    let options = new Map();
    resultsData.forEach((result) => {
        const option = result.option;
        const answersCnt = result.answersCnt;
        if (generalResults.has(option.id)) {
            const currAnswCnt = generalResults.get(option.id) + answersCnt;
            generalResults.set(option.id, currAnswCnt);
        } else {
            generalResults.set(option.id, answersCnt);
            options.set(option.id, option.title);
        }
    });

    let generalResultsArray = new Array();
    generalResultsArray.push(['Option', 'Answers amount']);
    generalResults.forEach((answersCnt, optionId) => {
        let optionTitle = options.get(optionId);
        generalResultsArray.push([optionTitle, answersCnt]);
    });
    return generalResultsArray;
}

function initMap() {
    let mapElement = $('#map')[0];
    let mapCenterLat = Number($('#mapCenterLat').val());
    let mapCenterLng = Number($('#mapCenterLng').val());
    let mapZoom = Number($('#mapZoom').val());
    let mapOptions = {
        zoom: mapZoom,
        center: {lat: mapCenterLat, lng: mapCenterLng},
    };

    map = new google.maps.Map(mapElement, mapOptions);

    map.data.setStyle(styleFeature);

    map.data.addListener("mouseover", mouseInToPolygon);
    map.data.addListener("mouseout", mouseOutOfPolygon);
    map.data.addListener("click", (e) => {
        let polygonResults = getResultsByPolygon(e);
        let element = document.getElementById('resultsByPolygon');
        element.style.display = 'block';

        let chartTitle = "Статистика для обраного району: " + e.feature.getProperty("title");

        drawChart(element, chartTitle, polygonResults);

        element.scrollIntoView({block: "center", inline: "center"});
    });

    const selectBox = $("#option-variable")[0];

    google.maps.event.addDomListener(selectBox, "change", () => {
        clearMapFromData();
        loadResultsDataByOption(selectBox.options[selectBox.selectedIndex].value);
    });
    loadMapShapes();

}

function getResultsByPolygon(e) {
    let polygonId = e.feature.getProperty("id");
    let resultsByPolygon = resultsData.filter((result) => result.polygonId == polygonId);

    let resultsByPolygonArray = new Array();
    resultsByPolygonArray.push(['Option', 'Answers amount']);
    resultsByPolygon.forEach((result) => {
        resultsByPolygonArray.push([result.option.title, result.answersCnt]);
    });
    return resultsByPolygonArray;
}

function loadMapShapes() {
    map.data.addGeoJson(JSON.parse(geoJson), {idPropertyName: "id"});
    google.maps.event.trigger($("#option-variable")[0], "change");
}

function loadResultsDataByOption(selectedOption) {
    resultsData.forEach((result) => {
        if (result.option.id == selectedOption) {
            const answersCnt = result.answersCnt;
            const polygonId = result.polygonId;

            if (answersCnt < censusMin) {
                censusMin = answersCnt;
            }
            if (answersCnt > censusMax) {
                censusMax = answersCnt;
            }
            const state = map.data.getFeatureById(polygonId);
            if (state) {
                state.setProperty("option_variable", answersCnt);
            }
        }
    });
    $("#votes-min").textContent = censusMin.toLocaleString();
    $("#votes-max").textContent = censusMax.toLocaleString();
}

function clearMapFromData() {
    censusMin = Number.MAX_VALUE;
    censusMax = -Number.MAX_VALUE;
    map.data.forEach((row) => {
        row.setProperty("option_variable", undefined);
    });
    $("#data-box").hide();
    $("#data-caret").hide();
}

function styleFeature(feature) {
    const low = [5, 69, 54]; // color of smallest datum
    const high = [151, 83, 34]; // color of largest datum
    // delta represents where the value sits between the min and max
    let delta = (feature.getProperty("option_variable") - censusMin) / (censusMax - censusMin);
    const color = [];

    for (let i = 0; i < 3; i++) {
        // calculate an integer color based on the delta
        if (isNaN(delta)) {
            color.push((high[i] - low[i]) + low[i]);
        } else {
            color.push((high[i] - low[i]) * delta + low[i]);
        }
    }
    // determine whether to show this shape or not
    let showRow = true;
    if (feature.getProperty("option_variable") == null || isNaN(feature.getProperty("option_variable"))) {
        showRow = false;
    }

    let outlineWeight = 2;
    let zIndex = 1;

    if (feature.getProperty("state") === "hover") {
        outlineWeight = 1;
        zIndex = 0.5;
    }
    return {
        strokeWeight: outlineWeight,
        zIndex: zIndex,
        fillColor: "hsl(" + color[0] + "," + color[1] + "%," + color[2] + "%)",
        fillOpacity: 0.6,
        visible: showRow,
    };
}

function mouseInToPolygon(e) {
    e.feature.setProperty("state", "hover");

    const percent =
        ((e.feature.getProperty("option_variable") - censusMin) /
            (censusMax - censusMin)) * 100;

    $("#data-label").text(e.feature.getProperty("title"));
    $("#data-value").text(e.feature.getProperty("option_variable").toLocaleString());
    $("#data-box").show();
    $("#data-caret").show();
    $("#data-caret")[0].style.paddingLeft = percent + "%";
}

function mouseOutOfPolygon(e) {
    e.feature.setProperty("state", "normal");
}


