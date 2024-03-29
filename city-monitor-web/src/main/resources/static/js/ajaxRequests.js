function changeLayoutAvailability(layoutId, isDeprecated){
    if(layoutId == null || layoutId == undefined || isDeprecated == null || isDeprecated == undefined){
        console.log("update layout status parameters are undefined or null");
        return;
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
        console.log("removing layout, layoutId is undefined or null");
        return;
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
        return;
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
        return;
    }
    let url = "/quizzes/".concat(quizId).concat('/finish');
    return  $.ajax({
        url: url,
        method: "PATCH"
    });
}

function changeAppealStatus(appealId, status){
    if(appealId == null || appealId == undefined || status == null || status == undefined){
        console.log("changing appeal status, appealId or status is undefined or null");
        return;
    }
    console.log("changing status")
    let url = "/appeals/".concat(appealId).concat('/changeStatus?status=').concat(status);
    return $.ajax({
        url: url,
        method: "PATCH"
    })
}

function changeSetOfAppealsStatus(appealIDs, status){
    if(appealIDs == null || appealIDs == undefined || appealIDs.length < 1){
        console.log("changing appeals statuses, appealIDs array is null, undefined or empty");
    }
    if(status == null || status == undefined){
        console.log("changing appeals statuses, status is undefined or null");
    }
    function urlBuilder(currentValue, index, arr){
        url = url.concat(encodeURIComponent(currentValue));
        if(index != arr.length-1) url = url.concat("+");
    }
    let url = "/appeals/changeStatuses?ids=";
    appealIDs.forEach(urlBuilder);
    url = url.concat('&status=').concat(status);
    return $.ajax({
        url: url,
        method: "PATCH"
    })
}

function changeMyPassword(username, oldPassword, newPassword){
    if(username == null || username == undefined){
        console.log("changing password, username is null or undefined");
    }
    if(oldPassword == null || oldPassword == undefined){
        console.log("changing password, oldPassword is null or undefined");
    }
    if(newPassword == null || newPassword == undefined){
        console.log("changing password, newPassword is null or undefined");
    }
    let url = "/myprofile/changePassword"
    return $.ajax({
        url: url,
        method: "PATCH",
        data: { username: username, oldPassword: oldPassword, newPassword: newPassword}
    })
}

function createUser(username, role){
    if(username == null || username == undefined){
        console.log("creating user, username is null or undefined");
    }
    if(role == null || role == undefined){
        console.log("creating user, role is null or undefined");
    }
    let url = "/users";

    return $.ajax({
        url: url,
        method: "POST",
        data: { username: username, role: role}
    })
}

function changeUserAccountStatus(username, status){
    if(username == null || username == undefined){
        console.log("changing user account status, username is null or undefined");
    }
    if(status == null || status == undefined){
        console.log("changing user account status, status is null or undefined");
    }

    let url = "/users/changeAccountStatus";

    return $.ajax({
        url: url,
        method: "PATCH",
        data: { username: username, status: status}
    })
}

function changeUserRole(username, role){
    if(username == null || username == undefined){
        console.log("changing user role, username is null or undefined");
    }
    if(role == null || role == undefined){
        console.log("changing user role, role is null or undefined");
    }

    let url = "/users/changeRole";

    return $.ajax({
        url: url,
        method: "PATCH",
        data: { username: username, role: role}
    })
}

function resetPasswordByUsername(username){
    if(username == null || username == undefined){
        console.log("resetting password, username is null or undefined");
    }
    let url = "/users/resetPassword";

    return $.ajax({
        url: url,
        method: "PATCH",
        data: { username: username}
    })
}