$(function () {
    $('#change-password-btn').click(function () {
        $(this).hide();
        let changePasswordForm =
            '       <h4>Зміна паролю</h4>' +
            '       <br>' +
            '       <div class="mb-2 row">\n' +
            '        <label class="col-md-2 col-form-label" for="old-password">Старий пароль</label>' +
            '        <input class="form-control col-md-10" type="password" id="old-password">' +
            '        <div class="invalid-feedback"></div>' +
            '      </div>\n' +
            '      <div>' +
            '          <div class="mb-2 row">\n' +
            '             <label class="col-md-2 col-form-label" for="new-password">Новий пароль</label>' +
            '             <input class="form-control col-md-10" type="password" id="new-password">' +
            '          </div>\n' +
            '         <div class="mb-2 row">\n' +
            '            <label class="col-md-2 col-form-label" for="new-password2">Підтвердити пароль</label>' +
            '            <input class="form-control col-md-10" type="password" id="new-password2">' +
            '          </div>' +
            '         <div class="invalid-feedback"></div>' +
            '      </div>' +
            '      <br>\n' +
            '      <div class="d-flex justify-content-start">\n' +
            '        <button id="change-password-submit" type="button" class="btn submit-btn">Змінити</button>' +
            '        <button id="cancel" type="button" class="mx-5">Назад</button>' +
            '      </div>';
        $('#change-password-area').html(changePasswordForm);

        $('#cancel').click(function () {
            $('#change-password-area').html(null);
            $('#change-password-btn').show();
        });

        $('#change-password-submit').click(function () {
            $('#old-password, #new-password, #new-password2').each(function (){
                validatePasswordInput($(this));
            });
            if($('.is-invalid').length == 0){
                let username = $('#username-value').text();
                let oldPassword = $('#old-password').val();
                let newPassword = $('#new-password').val();
                $(this).attr('disabled', true);
                $(this)[0].innerHTML = '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>\n' +
                    '  Зміна паролю...';

                changeMyPassword(username, oldPassword, newPassword).then(()=>{
                        alert("Пароль успішно змінено")
                        $('#change-password-area').html(null);
                        $('#change-password-btn').show();
                    },(data) => {
                        if(data.status == 400){
                            $('#old-password, #new-password, #new-password2').each(function (){
                                $(this).removeClass('is-valid');
                                $(this).val(null);

                                let btn = $("#change-password-submit");
                                btn.html("Змінити");
                                btn.attr('disabled', false);
                            });
                        }
                        alert(data.responseText);
                })
            }
        });

        //dynamic fields validation
        $('#old-password, #new-password, #new-password2').unbind().blur(function() {validatePasswordInput($(this))});
    });

    function validatePasswordInput(e) {
        console.log('validation')
        let id = e.attr('id');
        let val = e.val();

        if(id == 'old-password'){
            let size = val.trim().length;
            if (size == 0) {
                setInvalid(e, "Поле є обов'язковим");
            } else {
                setValid(e);
            }
        }else if(id == 'new-password' || id == 'new-password2'){
            let passw = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,20}$/;
            let passwConfirmVal = $("#new-password2").val();
            let passwValue = $("#new-password").val();
            if(!val.match(passw))
            {
                setInvalid(e, "Пароль повинен мати від 8 до 20 символів та містити як мінімум одну цифру, одну велику букву, одну маленьку букву.")
            } else if(passwConfirmVal && passwValue && passwValue != passwConfirmVal){
                setInvalid(e, "Паролі не співпадають")
            } else {
                setValid(e);
            }
        }

        function setValid(e) {
            e.removeClass('is-invalid').addClass('is-valid');
            let id = e.attr('id');
            if(id == 'new-password' || id == 'new-password2'){
                let elem = e.parent().siblings('.invalid-feedback');
                elem.empty();
                elem.hide();
            }else {
                e.next('.invalid-feedback').empty();
            }
        }

        function setInvalid(e, message) {
            e.removeClass('is-valid');
            e.addClass('is-invalid');
            let id = e.attr('id');
            if(id == 'new-password' || id == 'new-password2'){
                let elem = e.parent().siblings('.invalid-feedback');
                elem.text(message);
                elem.show();
            }else {
                e.next('.invalid-feedback').text(message);
            }
        }
    }
});