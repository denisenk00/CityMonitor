function changeLayoutAvailability(layoutId, isDeprecated){
    if(layoutId == null || layoutId == undefined || isDeprecated == null || isDeprecated == undefined){
        console.log("update layout status parameters are undefined or null")
    }
    let url = "/layouts/".concat(layoutId).concat("/changeAvailability");
    return  $.ajax({
        url: url,
        data:{"deprecated":isDeprecated},
        method: "PATCH",
        dataType: "JSON"
    });
}

function deleteLayout(layoutId){
    if(layoutId == null || layoutId == undefined){
        console.log("removing layout, layoutId is undefined or null")
    }
    let url = "/layouts/".concat(layoutId);
    return  $.ajax({
        url: url,
        method: "DELETE"
    });
}

function deleteQuiz(quizId){
    if(quizId == null || quizId == undefined){
        console.log("removing quiz, quizId is undefined or null")
    }
    let url = "/quizzes/".concat(quizId);
    return  $.ajax({
        url: url,
        method: "DELETE"
    });
}

function finishQuiz(quizId){
    if(quizId == null || quizId == undefined){
        console.log("removing quiz, quizId is undefined or null")
    }
    let url = "/quizzes/".concat(quizId).concat('/finish');
    return  $.ajax({
        url: url,
        method: "PATCH"
    });
}