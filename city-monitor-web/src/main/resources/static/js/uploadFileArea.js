function readFiles(input) {

    if (input.files && input.files[0]) {
        let boxZone = $(input).parent().parent().find('.preview-zone').find('.box').find('.box-body');
        boxZone.empty();
        if(input.files.length > 10){
            input.value = "";
            let message = "Перевершено максимальну кількість файлів (10), поточна кількість - " + input.files.length;
            fileErrorMessage(message);
        }
        for(let i=0; i<input.files.length; i++){
            let file = input.files[i];
            if(typeof file !== 'undefined'){
                const maxSize = 31457280;
                const size = file.size;
               if(maxSize < size){
                   input.value = "";
                    let message = "Файл " + file.name + " занадто великий, максимальний розмір файлу - 30МБ";
                    fileErrorMessage(message);
               } else {
                   let reader = new FileReader();

                   reader.onload = function(e) {
                       let htmlPreview =
                           '<img width="50" src="' + e.target.result + '" />' +
                           '<p>' + file.name + '</p>';
                       let wrapperZone = $(input).parent();
                       let previewZone = $(input).parent().parent().find('.preview-zone');

                       wrapperZone.removeClass('dragover');
                       previewZone.removeClass('hidden');
                       boxZone.append(htmlPreview);
                   };

                   reader.readAsDataURL(file);
               }
            }
        }
    }
}

function fileErrorMessage(message){
    $('#fileErrorZone').text(message);
    setTimeout(function (){
        $('#fileErrorZone').text("");
    }, 10000)
}


function reset(e) {
    e.wrap('<form>').closest('form').get(0).reset();
    e.unwrap();
    e.value = "";
    $('#fileErrorZone').text("");
}

$(".dropzone").change(function() {
    $('#fileErrorZone').text("");
    readFiles(this);
});


$('.dropzone-wrapper').on('dragover', function(e) {
    e.preventDefault();
    e.stopPropagation();
    $(this).addClass('dragover');
});

$('.dropzone-wrapper').on('dragleave', function(e) {
    e.preventDefault();
    e.stopPropagation();
    $(this).removeClass('dragover');
});

$('.remove-preview').on('click', function() {
    var boxZone = $(this).parents('.preview-zone').find('.box-body');
    var previewZone = $(this).parents('.preview-zone');
    var dropzone = $(this).parents('.form-group').find('.dropzone');
    boxZone.empty();
    previewZone.addClass('hidden');
    reset(dropzone);
});

