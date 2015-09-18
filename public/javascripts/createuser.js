function CreateUserForm(formElm)
{
    this.username = null;
    this.password = null;
    this.isValid = false;
    
    var username = $(formElm).children("input[name='username']").val();
    var pass1 = $(formElm).children("input[name='password1']").val();
    var pass2 = $(formElm).children("input[name='password2']").val();
    
    if (username.length > 0 && pass1.length > 0 && pass1 == pass2) {
        this.isValid = true;
        this.username = username;
        this.password = pass1;
    } else {
        this.isValid = false;
    }
}

function formSubmitHandler(event)
{
    var formElm = event.target;
    var form = new CreateUserForm(formElm);
    
    if (!form.isValid) {
        var errorElm = $(".error");
        errorElm.text("Invalid username/password.");
        errorElm.show();
    }
    
    return form.isValid;
}

$(document).ready(function()
{
    $("form#create-user").submit(formSubmitHandler);
});
