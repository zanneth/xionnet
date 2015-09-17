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

function submitCreateUser(form, completion)
{
    $.ajax({
        type: "POST",
        url: "/api/createuser",
        data: {
            administrator: true,
            username: form.username,
            password: form.password
        }
    }).done(function(data, textStatus, jqXHR) {
        completion(null);
    }).fail(function(data, textStatus, jqXHR) {
        completion(data);
    });
}

function formSubmitHandler(event)
{
    var formElm = event.target;
    var form = new CreateUserForm(formElm);
    
    if (form.isValid) {
        // hide error message if one is visible
        var errorElm = $(".error");
        errorElm.hide();
        
        submitCreateUser(form, function(error) {
            if (error != null) {
                window.location.href = "/index";
            } else {
                errorElm.text(error);
                errorElm.show();
            }
        });
    } else {
        var errorElm = $(".error");
        errorElm.text("Invalid username/password.");
        errorElm.show();
    }
    
    return false;
}

$(document).ready(function()
{
    $("form#create-user").submit(formSubmitHandler);
});
