$(function (){
    $('#changeAvailabilityBtn').click(function () {
        let layoutId = $('#layoutId').val();
        let btnValue = $(this).val();

        if (btnValue == 'set_active') {
            changeLayoutAvailability(layoutId, false).then(onChangeAvailabilitySuccess, onChangeAvailabilityError)
        } else if (btnValue == 'set_deprecated') {
            if(confirm("Ви дійсно хочете позначити цей шаблон як застарілий?\nКористувачі не зможуть його використовувати для нових опитувань.")) {
                changeLayoutAvailability(layoutId, true).then(onChangeAvailabilitySuccess, onChangeAvailabilityError);
            }
        }

    });

    $('#delete').click(function () {
        if(confirm("Ви дійсно хочете видалити цей макет районування?")) {
            let layoutId = $('#layoutId').val();
            deleteLayout(layoutId).then(function () {
                window.location.href = '/layouts';
            }, function (data) {
                alert(data.responseText);
                console.log(data.responseText);
            });
        }
    });

    function onChangeAvailabilitySuccess(data, e){
        let newStatus = data.newStatus;
        $("#statusValue").text(newStatus);

        let button = $("#changeAvailabilityBtn");
        let oldBtnVal = button.val();

        if(oldBtnVal == 'set_active'){
            button.val('set_deprecated');
            button.text('Позначити як застарілий');
        }
        else {
            button.val('set_active');
            button.text('Позначити як доступний для використання');
        }

    }
    function onChangeAvailabilityError(data){
        alert(data.responseText);
        console.log(data.responseText);
    }

});