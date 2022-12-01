$(function(){
    function getCurrentTime() {
        let tzoffset = ((new Date()).getTimezoneOffset()) * 60000;
        let time = (new Date(Date.now() - tzoffset)).toISOString().slice(0, -8);
        return time;
    }

    function validateQuizInput(e){
        let id = e.attr('id');
        let val = e.val();

        switch(id) {
            case 'title':
                var size = val.trim().length;
                if (size == 0) {
                    setInvalid(e, "Тема опитування не може бути пустою");
                } else if (size < 10 || size > 100) {
                    setInvalid(e, "Мінімальна кількість символів - 10, максимальна - 100");
                } else {
                    setValid(e);
                }
                break;
            case 'description':
                var size = val.trim().length;
                if (size == 0) {
                    setInvalid(e, "Опис опитування не може бути пустим");
                } else if (size < 30 || size > 1000) {
                    setInvalid(e, "Мінімальна кількість символів - 30, максимальна - 1000");
                } else {
                    setValid(e);
                }
                break;
            case 'optionTitle':
                var size = val.trim().length;
                if (size == 0) {
                    setInvalid(e, "Варіант відповіді не може бути пустим");
                } else if (size > 500) {
                    setInvalid(e, "Максимальна кількість символів - 500");
                } else {
                    setValid(e);
                }
                break;
            case 'layoutSelector':
                if (val == null) {
                    setInvalid(e, "Оберіть макет місцевості");
                } else {
                    setValid(e);
                }
                break;
            case 'startDatePicker':
                if (val == "") {
                    setInvalid(e, "Визначте час початку опитування");
                } else if (val <= getCurrentTime()) {
                    setInvalid(e, "Час початку не може бути в минулому")
                } else if(val >= e.attr('max')){
                    setInvalid(e, "Час початку більший за час завершення")
                } else {
                    setValid(e);
                }
                break;
            case 'endDatePicker':
                if(val == ""){
                    setInvalid(e, "Визначте час завершення опитування");
                }else if(val <= getCurrentTime()){
                    setInvalid(e, "Час завершення опитування не може бути в минулому");
                }else if(val <= e.attr('min')){
                    setInvalid(e, "Час завершення опитування менший ніж час початку");
                }else{
                    setValid(e);
                }
                break;
        }
        function setValid(e){
            e.removeClass('is-invalid').addClass('is-valid');
            e.next('.invalid-feedback').empty();
        }
        function setInvalid(e, message){
            e.removeClass('is-valid');
            e.addClass('is-invalid');
            e.next('.invalid-feedback').text(message);
        }
    }

    //submit validation
    $('#quizForm').submit(function (ev){
        $('#title, #description, #optionTitle, #layoutSelector, #startDatePicker, #endDatePicker').each(function (){
            if(!($(this).attr('id') == 'startDatePicker' && $("#startImmediate").is(':checked'))){
                validateQuizInput($(this));
            }
        })

        if(!$('.is-invalid').length == 0) ev.preventDefault();
    });

    //dynamic fields validation
    $('#title, #description, #optionTitle, #layoutSelector, #startDatePicker, #endDatePicker').unbind().blur(function() {validateQuizInput($(this))});

    //datetime range picker managing
    $("#startDatePicker").attr('min', getCurrentTime());
    $("#endDatePicker").attr('min', getCurrentTime());

    $("#startDatePicker").change(function () {
        $('#endDatePicker').attr('min', $(this).val());
    });
    $("#endDatePicker").change(function () {
        $('#startDatePicker').attr('max', $(this).val());
    });

    $('#startImmediate').on("click", function (){
        let currentTime = getCurrentTime();
        if($("#startImmediate").is(':checked')) {
            $("#startDateFragment").hide();
            $("#startDatePicker").val('').removeClass('is-invalid').removeClass('is-valid').next('.invalid-feedback').empty();
            $("#endDatePicker").attr('min', currentTime);
        }else {
            $("#startDatePicker").attr('min', currentTime);
            $('#startDatePicker').attr('max', $("#endDatePicker").val());
            $("#startDateFragment").show();
        }
    });

    //managing options
    let optionIndex = $('#options-div').children().length - 1;
    const maxCountOfOption = 10;

    $('#addOption').click(function (){
        $('<div>'+
            '<div class="mb-2 row" >' +
            '<label class="col-md-2 col-form-label" for="optionTitle">Варіант відповіді №' + (optionIndex + 2) + ':</label>' +
            '<div class="col-md-10">' +
            '<input class="form-control" type="text" id="optionTitle" name="optionDTOs['+ (optionIndex + 1) +'].title" value=\"\">' +
            '<div class="invalid-feedback"></div>' +
            '</div>'+
            '</div>' +
            '</div>'
        ).appendTo('#options-div');
        if(optionIndex >= 1){
            $('#removeOption').removeAttr('disabled');
        }
        optionIndex++;
        if(optionIndex > maxCountOfOption - 2){
            $('#addOption').attr('disabled', 'disabled');
        }
        $('#title, #description, #optionTitle, #layoutSelector, #startDatePicker, #endDatePicker').unbind().blur(function() {validateQuizInput($(this))});
    });

    $('#removeOption').click(function (){
        $('#options-div').children().last().remove();
        if(optionIndex < 3){
            $('#removeOption').attr('disabled', 'disabled');
        }
        optionIndex--;
        if(optionIndex < maxCountOfOption - 1){
            $('#addOption').removeAttr('disabled');
        }
    });
});