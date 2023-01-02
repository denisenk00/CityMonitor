$(function (){

    $('#finishQuizBtn').click(function (e){
        if(confirm("Ви впевнені, що хочете завершити опитування?")){
            let quizId = $('#quizId').val();
            $(this).attr('disabled', true);
            $(this)[0].innerHTML = '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>\n' +
                '  Підрахунок результатів...';

            finishQuiz(quizId).then(() => {
                    window.location.reload();
                },
                (data) => {
                    $(this).attr('disabled', false);
                    $(this).innerText = "Завершити";
                    console.log(data.responseText);
                    alert(data.responseText);
                });
        }
    });

    $('#deleteQuizBtn').click(function (e){
        if(confirm("Ви впевнені, що хочете видалити всі дані цього опитування?")){
            let quizId = $('#quizId').val();
            deleteQuiz(quizId).then(() => {
                    window.location.href = '/quizzes';
                },
                (data) => {
                    alert(data.responseText);
                    console.log(data.responseText);
                })
        }
    });

})