/*<![CDATA[*/
function validateForm() {

    if($("#txtOldPassword").val() === "" || $("#txtOldPassword").val() === null)
    {
        $("#lblStatus").text('Please enter old password');
        return false;
    }

    if($("#txtNewPassword").val() === "" || $("#txtNewPassword").val() === null)
    {
        $("#lblStatus").text('Please enter new password');
        return false;
    }

    if($("#txtConfirmNewPassword").val() === "" || $("#txtConfirmNewPassword").val() === null)
    {
        $("#lblStatus").text('Please enter confirm new password');
        return false;
    }

    if($("#txtNewPassword").val() !== $("#txtConfirmNewPassword").val())
    {
        $("#lblStatus").text('Passwords do not match');
        return false;
    }

    return true;
}

$(document).ready(function() {

    $("#chkShowOldPassword").click(function(){

        var oldPasswordField = $("#txtOldPassword");
        var oldPassword = oldPasswordField.val();
        oldPasswordField.remove();

        if($(this).is(':checked'))
        {  
            $(this).before('<input type="text" id="txtOldPassword" onkeyup="clearMsg()" style="width: 300px;" value="' + oldPassword + '"> ');
        }
        else
        {
            $(this).before('<input type="password" id="txtOldPassword" onkeyup="clearMsg()" style="width: 300px;" value="' + oldPassword + '"> ');
        }

        //$("#oldPassword").attr('type',$(this).is(':checked') ? 'text' : 'password')
    });

    $("#chkShowNewPassword").click(function(){

        var newPasswordField = $("#txtNewPassword");
        var newPassword = newPasswordField.val();
        newPasswordField.remove();

        if($(this).is(':checked'))
        {  
            $(this).before('<input type="text" id="txtNewPassword" onkeyup="checkPasswordStrength()" style="width: 300px;" value="' + newPassword + '"> ');
        }
        else
        {
            $(this).before('<input type="password" id="txtNewPassword" onkeyup="checkPasswordStrength()" style="width: 300px;" value="' + newPassword + '"> ');
        }
    });

    $("#chkShowConfirmNewPassword").click(function(){

        var confirmNewPasswordField = $("#txtConfirmNewPassword");
        var confirmNewPassword = confirmNewPasswordField.val();
        confirmNewPasswordField.remove();

        if($(this).is(':checked'))
        {  
            $(this).before('<input type="text" id="txtConfirmNewPassword" onkeyup="clearMsg()" style="width: 300px;" value="' + confirmNewPassword + '"> ');
        }
        else
        {
            $(this).before('<input type="password" id="txtConfirmNewPassword" onkeyup="clearMsg()" style="width: 300px;" value="' + confirmNewPassword + '"> ');
        }
    });

    $("#btnChangePassword").click(function(){
        if(validateForm() === true)
        {
            saveNewPassword($("#txtOldPassword").val(), $("#txtNewPassword").val());
        }
    });

});

function clearMsg() {
    $("#lblStatus").text('');
}

function checkPasswordStrength() {
    $("#lblStatus").text('');
    var newPassword = $("#txtNewPassword").val();
    var strongRegex = new RegExp("^(?=.{8,})(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*\\W).*$", "g");
    var mediumRegex = new RegExp("^(?=.{7,})(((?=.*[A-Z])(?=.*[a-z]))|((?=.*[A-Z])(?=.*[0-9]))|((?=.*[a-z])(?=.*[0-9]))).*$", "g");
    var enoughRegex = new RegExp("(?=.{6,}).*", "g");

    if (false === enoughRegex.test(newPassword)) {
        $('#passstrength').html('More Characters');
    } else if (strongRegex.test(newPassword)) {
        $('#passstrength').className = 'ok';
        $('#passstrength').html('Strong!');
    } else if (mediumRegex.test(newPassword)) {
        $('#passstrength').className = 'alert';
        $('#passstrength').html('Medium!');
    } else {
        $('#passstrength').className = 'error';
        $('#passstrength').html('Weak!');
    }

    return true;
}

function saveNewPassword(oldPassword, newPassword) {

    $.ajax({
        url : "/user/saveNewPassword",
        type : "POST",
        dataType : 'json',
        data : {
            "oldPassword" : oldPassword,
            "newPassword" : newPassword
        },
        success : function(data) {
            $.each(data, function(index, element) {
                $("#lblStatus").text(element.status);
            });
        },
        error : function() {
        }
    });
}

/*]]>*/