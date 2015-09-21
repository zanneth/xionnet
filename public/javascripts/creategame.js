function requestNewGame(name, completion)
{
    $.ajax({
        type: "POST",
        url: "/api/creategame",
        data: {
            name: name
        }
    }).done(function(data, textStatus, jqXHR) {
        completion(null);
    }).fail(function(data, textStatus, jqXHR) {
        console.log("Error creating game. " + data);
        completion(textStatus);
    });
}

function createGameFormSubmit(event)
{
    var form = event.target;
    var name = $(form).children("input[name='name']").val();
    var errorElm = $(".error");
    
    if (name.length > 0) {
        requestNewGame(name, function(error) {
            if (error == null) {
                browser.signal("gameCreated");
            } else {
                errorElm.text(error);
                errorElm.show();
            }
        });
    } else {
        errorElm.text("A name is required.");
        errorElm.show();
    }
    
    return false;
}

$(document).ready(function()
{
    $("#create-game-form").submit(createGameFormSubmit);
});
