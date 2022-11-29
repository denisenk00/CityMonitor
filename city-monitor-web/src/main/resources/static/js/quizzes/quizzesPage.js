$(function (){
    $('.finishQuizBtn').click(function (e){
        e.stopPropagation();
        if(confirm("Ви впевнені, що хочете завершити опитування?")) {
            let quizId = $(this).data("id");
            finishQuiz(quizId).then(() => {

                },
                () => {

                })
        }
    });

});