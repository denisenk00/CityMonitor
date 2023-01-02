$(function($){
    $(".clickable-row").click(function(e) {
        window.document.location = $(this).data("href");
    });

    $(".changeAvailabilityBtn").click(function (e){
        e.stopPropagation();
        let layoutId = $(this).data("id");
        let btnValue = $(this).val();

        if (btnValue == 'set_active') {
            changeLayoutAvailability(layoutId, false).then(data => onChangeAvailabilitySuccess(data, layoutId), onChangeAvailabilityError)
        } else if (btnValue == 'set_deprecated') {
            if (confirm("Ви дійсно хочете позначити цей шаблон як застарілий?\nКористувачі не зможуть його використовувати для нових опитувань.")) {
                changeLayoutAvailability(layoutId, true).then(data => onChangeAvailabilitySuccess(data, layoutId), onChangeAvailabilitySuccess, onChangeAvailabilityError)
            }
        }
    })

    function onChangeAvailabilitySuccess(data, layoutId){
        let newStatus = data.newStatus;
        let parentRow = $('#tr_'.concat(layoutId))

        parentRow.find('.statusValue').text(newStatus);

        let button = parentRow.find('.changeAvailabilityBtn');
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
        console.log(data.responseText);
        alert(data.responseText);
    }
});