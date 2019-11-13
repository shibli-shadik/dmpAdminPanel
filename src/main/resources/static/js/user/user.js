/*<![CDATA[*/
function validateForm() {
    
    if($("#firstName").val() === "" || $("#firstName").val() === null)
    {
        $("#lblStatus").text('Please enter first name');
        return false;
    }
    
    if($("#lastName").val() === "" || $("#lastName").val() === null)
    {
        $("#lblStatus").text('Please enter last name');
        return false;
    }
    
    var email = $("#email").val();
    if(email === "" || email === null)
    {
        $("#lblStatus").text('Please enter email address');
        return false;
    }
    else if ($("#emailMsg").text() === "Email already exists") 
    {
        $("#lblStatus").text('Email already exists');
        return false;
    }
    
    if (!validateEmail(email, 'lblStatus')) 
    {
        $("#lblStatus").text("Please enter valid email address");
        return false;
    }
    
    if($("#mobile").val() === "" || $("#mobile").val() === null)
    {
        $("#lblStatus").text('Please enter mobile');
        return false;
    }
    
    if ($("#mobileMsg").text() === "Mobile already exists") 
    {
        $("#lblStatus").text('Mobile already exists');
        return false;
    }
    
    if($("#role").val() === "0")
    {
        $("#lblStatus").text('Please select role');
        return false;
    }
    
    if($("#organization").val() === "0")
    {
        $("#lblStatus").text('Please select organization');
        return false;
    }
}

$(document).ready(function() 
{
    $("#user").DataTable();
    
    $("#chkEnabled").change(function() {
        if(this.checked) {
            $("#txtIsEnabled").val("true");
        }
        else
        {
            $("#txtIsEnabled").val("false");
        }
    });
    
    $("#chkResetPassword").change(function() {
        if(this.checked) {
            $("#txtResetPassword").val("true");
        }
        else
        {
            $("#txtResetPassword").val("false");
        }
    });
    
    $("#dateOfBirth").datepicker({
        dateFormat : "dd M, yy",
        changeMonth : true,
        changeYear : true
    });
    
    $("#userImage").change(function()
    {
        $("#lblStatus").text('');
        if (this.files[0].size > 512000) 
        {
            //this.files[0].size gets the size of your file.
            $("#lblStatus").text('Max file size is 500KB');
	}
        else
        {
            readURL(this);
        }
    });
    
    function readURL(input) {
        if (input.files && input.files[0]) {
            var reader = new FileReader();
            
            reader.onload = function (e) 
            {
                $('#imgUser').attr('src', e.target.result);
            }
            
            reader.readAsDataURL(input.files[0]);
        }
    }
    
    $("#firstName").keypress(function() {
	$("#lblStatus").text('');
    });
    
    $("#lastName").keypress(function() {
	$("#lblStatus").text('');
    });
    
    $("#email").keypress(function() {
	$("#lblStatus").text('');
        $("#emailMsg").text('');
    });
    
    $("#mobile").keypress(function() {
	$("#lblStatus").text('');
        $("#mobileMsg").text('');
    });
    
    $("#email").focusout(function() {
        if($("#email").val().trim() !== '')
        {
            checkEmailDuplication($("#email").val());
        }
    });
    
    $("#mobile").focusout(function() {
        if($("#mobile").val().trim() !== '')
        {
            checkMobileDuplication($("#userId").val(), $("#mobile").val());
        }
    });
    
    $("#role").change(function() {
        $("#lblStatus").text('');
    });
    
    $("#organization").change(function() {
        $("#lblStatus").text('');
    });

    $("#close").click(function() {
        $("#trAddress").show();
    });
    
});

function checkEmailDuplication(email) {
    $.ajax({
        url : "/user/checkEmailDuplication",
        type : "POST",
        dataType : 'json',
        data : {
            "email" : email
        },
        success : function(data) {
            $.each(data, function(index, element) {
                $("#emailMsg").text(element.status);
            });
        },
        error : function() {
        }
    });
}

function checkMobileDuplication(id, mobile) {
    $.ajax({
        url : "/user/checkMobileDuplication",
        type : "POST",
        dataType : 'json',
        data : {
            "id" : id,
            "mobile" : mobile
        },
        success : function(data) {
            $.each(data, function(index, element) {
                $("#mobileMsg").text(element.status);
            });
        },
        error : function() {
        }
    });
}

function validateEmail(email, errorMsgLabel) {
    var emailReg = /^([\wupload-file-input\.]+@([\w-]+\.)+[\w-]{2,4})?$/;
    if (!emailReg.test(email)) {
        $("#" + errorMsgLabel).text("Please enter valid email address");
        
        return false;		
    } else {
        $("#" + errorMsgLabel).text("");
        
        return true;			
    }
}

/*]]>*/