function CreateScoreForm()
{
    var self = this;
    
    var formElement = $("<form/>", {"id" : "create-score"});
    var nameField = $("<input/>", {
        "type" : "text",
        "name" : "name",
        "placeholder" : "Player Name"
    });
    nameField.keyup(function(event) { self.formChanged(event); });
    formElement.append(nameField);
    
    var scoreField = $("<input/>", {
        "type" : "text",
        "name" : "score",
        "placeholder" : "score"
    });
    scoreField.keyup(function(event) { self.formChanged(event); });
    formElement.append(scoreField);
    
    var submitButton = $("<input/>", {"type" : "submit"});
    submitButton.prop("disabled", true);
    formElement.append(submitButton);
    
    formElement.submit(function() { return self.submit() });
    
    this.formElement = formElement;
    this.nameField = nameField;
    this.scoreField = scoreField;
    this.submitButton = submitButton;
    this.callback = null;
}

CreateScoreForm.prototype.isValid = function()
{
    return (
        this.nameField.val().length > 0 &&
        this.scoreField.val().length > 0 &&
        !isNaN(this.scoreField.val())
    );
}

CreateScoreForm.prototype.formChanged = function(event)
{
    this.submitButton.prop("disabled", !this.isValid());
}

CreateScoreForm.prototype.submit = function()
{
    var name = this.nameField.val();
    var score = this.scoreField.val();
    var gameID = getGameID();
    var errorElm = $(".error");
    
    $.ajax({
        type: "POST",
        url: "/api/createscore",
        data: {
            name: name,
            value: score,
            gameid: gameID
        }
    }).done(function(data, textStatus, jqXHR) {
        errorElm.hide();
        clearFormUI();
        reloadScores();
    }).fail(function(data, textStatus, jqXHR) {
        errorElm.text("Error: " + textStatus);
        errorElm.show();
    });
    
    return false;
}

// -----------------------------------------------------------------------------

function addScoreButtonClicked(event)
{
    clearFormUI();
    
    var addScorePanel = $("#add-score-panel");
    var form = new CreateScoreForm();
    addScorePanel.append(form.formElement);
}

function getGameID()
{
    return $("input[name='game-id']").val();
}

function clearFormUI()
{
    var addScorePanel = $("#add-score-panel");
    addScorePanel.children().remove("form");
}

function reloadScores()
{
    var gameID = getGameID();
    var tableBody = $("table#game-scores > tbody");
    tableBody.children("tr").remove();
    
    $.ajax({
        type: "GET",
        url: "/api/games/" + gameID
    }).done(function(response, textStatus, jqXHR) {
        var scores = response.scores;
        scores.sort(function(a, b) { return b.scoreValue - a.scoreValue; });
        
        for (var i = 0; i < scores.length; ++i) {
            var score = scores[i];
            var newRow = $("<tr/>");
            
            var nameCol = $("<td/>", {"class" : "score-name-col"});
            nameCol.text(score.playerName);
            newRow.append(nameCol);
            
            var valueCol = $("<td/>", {"class" : "score-value-col"});
            valueCol.text(score.scoreValue);
            newRow.append(valueCol);
            
            var dateCol = $("<td/>", {"class" : "score-date-col"});
            dateCol.text(score.dateCreatedString);
            newRow.append(dateCol);
            
            tableBody.append(newRow);
        }
    }).fail(function(data, textStatus, jqXHR) {
        console.log("Failed to reload scores. " + textStatus);
    });
}

$(document).ready(function()
{
    $("#add-score").click(addScoreButtonClicked);
    reloadScores();
});