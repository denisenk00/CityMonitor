($(function (){
    let map;
    let selectedAppealId;

    $('.appeal').click(function (){
        selectedAppealId = $(this).data("id");
        $('#list-appeal-div').hide();
        let parentElem = $('#appeal_' + selectedAppealId);
        parentElem.show();

        if(parentElem.find('.location').length > 0){
            loadMap(parentElem);
        }
        if(parentElem.find('.appeal-status').text() == 'Непрочитано'){
            changeAppealStatus(selectedAppealId, "VIEWED").then(()=>{
                $(this).removeClass('unread');
                let oldVal = Number.parseInt($("#unread-appeal-cnt").text());
                $("#unread-appeal-cnt").text(oldVal-1);
            },(data) => {
                console.log(data.responseText);
                alert(data.responseText);
            });
        }
    });

    $('.go-back').click(function (){
        selectedAppealId = null;
        $(this).parent().hide();
        $('#list-appeal-div').show();
    });

    $('.appeal-checkbox').click(function (e){
        e.stopPropagation();
    });

    $('.status-button').click(function (e){
        let status = $(this).data("status");
        if(selectedAppealId){
            changeAppealStatus(selectedAppealId, status).then(() => {
                    window.location.reload();
                },
                (data) => {
                    console.log(data.responseText);
                    alert(data.responseText);
                });
        } else {
            let selectedAppealIDs = [];
            $('.appeal-checkbox:checked').each(function() {
                let id = $(this).closest('li').data("id");
                selectedAppealIDs.push(id);
            });
            changeSetOfAppealsStatus(selectedAppealIDs, status).then(() => {
                    window.location.reload();
                },
                (data) => {
                    console.log(data.responseText);
                    alert(data.responseText);
                });
        }

    });

    function loadMap(parent){
        let mapElement = parent.find('.location .map')[0];
        let latitude = Number.parseFloat(parent.find('.location .latitude').val());
        let longitude = Number.parseFloat(parent.find('.location .longitude').val());

        let mapOptions = {
            zoom: 12,
            center: {lat: latitude, lng: longitude}
        }
        map = new google.maps.Map(mapElement, mapOptions);
        let marker = new google.maps.Marker({
            position: new google.maps.LatLng(latitude, longitude),
            map: map
        });
    }
}));