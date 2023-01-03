$(function (){
   $('#create-user-btn').click(function (){
       $(this).hide();
       let createUserArea = $('#create-user-area');
       createUserArea.show();

       $('#cancel').click(function (){
           createUserArea.hide();
           $('#create-user-btn').show();
       });

       $('#new-username').unbind().blur(function() {validateUsername($(this))});

       $('#create-user-submit').click(function (){
           let usernameEl = $('#new-username');
           validateUsername(usernameEl);

           if($('.is-invalid').length == 0) {
               let username = usernameEl.val();
               let userRole = $("#new-role").val();
               $(this).attr('disabled', true);
               $(this)[0].innerHTML = '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>\n' +
                   '  Створення користувача...';
                createUser(username, userRole).then((data)=>{
                    let message = "Користувача створено успішно. Пароль для входу в аккаунт - " + data.password;
                    alert(message);
                    window.location.reload();
                }, (data) => {
                    if(data.status == 400){
                        $('#new-username').each(function (){
                            $(this).removeClass('is-valid');
                            $(this).val(null);

                            let btn = $("#create-user-submit");
                            btn.html("Створити нового");
                            btn.attr('disabled', false);
                        });
                    }
                    alert(data.responseText);
                })
           }
       });

       function validateUsername(usernameEl){
           let value = usernameEl.val();
           let usernamePattern = /^[A-Za-z0-9]{6,20}$/;
           if(!value.match(usernamePattern)){
               setInvalid(usernameEl, "Ім'я користувача повинно мати від 6 до 20 символів, в які входять латинські букви та цифри");
           }else {
               setValid(usernameEl);
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
   });

   $(".status-selector").change(function (){
       let username = $(this).data('username');
       let value = $(this).val();

       changeUserAccountStatus(username, value).then(() => {}, (data) => {
           alert(data.responseText);
       })
   })

    $(".role-selector").change(function (){
        let username = $(this).data('username');
        let value = $(this).val();

        changeUserRole(username, value).then(() => {}, (data) => {
            alert(data.responseText);
        })
    })

    $(".reset-password").click(function (){
        let username = $(this).data('username');
        let btn = $(this);
        btn.attr('disabled', true);
        btn[0].innerHTML = '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>\n' +
            '  Скидання паролю...';


        resetPasswordByUsername(username).then((data)=>{
            btn.html("Скинути пароль");
            btn.attr('disabled', false);
            let message = "Скидання паролю для користувача " + username + " успішне. Новий пароль - " + data.password;
            alert(message);
        }, (data)=>{
            btn.html("Скинути пароль");
            btn.attr('disabled', false);
            alert(data.responseText);
        })
    })

});