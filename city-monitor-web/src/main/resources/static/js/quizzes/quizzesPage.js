$(function (){
    $(function($){
        $(".clickable-row").click(function(e) {
            window.document.location = $(this).data("href");
        });
    });

    $('.finishQuizBtn').click(function (e){
        e.stopPropagation();
        if(confirm("Ви впевнені, що хочете завершити опитування?")) {
            let quizId = $(this).data("id");
            $(this).attr('disabled', true);
            $(this)[0].innerHTML = '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>\n' +
                '  Підрахунок результатів...';
            finishQuiz(quizId).then(() => {
                    let parentRow = $('#tr_'.concat(quizId))
                    parentRow.find('.statusValue').text('Завершено');
                    let button = parentRow.find('.finishQuizBtn');
                    button.remove();
                },
                (data) => {
                    $(this).attr('disabled', false);
                    $(this).innerText = "Завершити";

                    alert(data.responseText);
                })
        }
    });

});