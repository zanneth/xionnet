function Browser()
{
    this.addGameButton = null;
    this.signalHandlers = {};
    this.selectedGameID = 0;
    
    this.addSignalHandler("gameCreated", $.proxy(this.gameCreatedCallback, this));
}

Browser.prototype.getInspectorElement = function()
{
    return $("#inspector");
}

Browser.prototype.addGameButtonClicked = function(event)
{
    var inspector = this.getInspectorElement();
    this.setSelectedGameID(0);
    
    $.ajax({
        type: "GET",
        url: "/games/creategame",
    }).done(function(data, textStatus, jqXHR) {
        inspector.html(data);
    }).fail(function(data, textStatus, jqXHR) {
        console.log("Error loading create game UI. " + data);
    });
}

Browser.prototype.gameCellClicked = function(event)
{
    var inspector = this.getInspectorElement();
    var gameID = $(event.currentTarget).attr("game-id");
    this.setSelectedGameID(gameID);
    
    $.ajax({
        type: "GET",
        url: "/games/" + gameID
    }).done(function(data, textStatus, jqXHR) {
        inspector.html(data);
    }).fail(function(data, textStatus, jqXHR) {
        console.log("Error loading game data. " + data);
    });
}

Browser.prototype.setSelectedGameID = function(id)
{
    $("#games-list tr").removeClass("selected");
    $("#games-list tr[game-id=" + id + "]").addClass("selected");
}

Browser.prototype.addSignalHandler = function(signal, callback)
{
    this.signalHandlers[signal] = callback;
}

Browser.prototype.signal = function(signal)
{
    this.signalHandlers[signal]();
}

Browser.prototype.setupHandlers = function()
{
    this.addGameButton = $("#add-game-button");
    this.addGameButton.click($.proxy(this.addGameButtonClicked, this));
}

Browser.prototype.gameCreatedCallback = function()
{
    this.getInspectorElement().html("");
    this.reloadGamesList();
}

Browser.prototype.reloadGamesList = function()
{
    $.ajax({
        type: "GET",
        url: "/api/games"
    }).done(function(response, textStatus, jqXHR) {
        var games = response.games;
        var gamesListTable = $("#games-list > table");
        gamesListTable.html("");
        
        for (var i = 0; i < games.length; ++i) {
            var game = games[i];
            var newCol = $("<td/>", {class: "noselect"});
            newCol.text(game.name);
            
            var newRow = $("<tr/>", {
                "game-id" : game.id
            });
            newRow.append(newCol);
            newRow.click(function(event) {
                browser.gameCellClicked(event);
            });
            
            gamesListTable.append(newRow);
        }
        
    }).fail(function(data, textStatus, jqXHR) {
        console.log("Failed to reload games list. " + textStatus);
    });
}

// -----------------------------------------------------------------------------

var browser = new Browser();

$(document).ready(function()
{
    browser.setupHandlers();
    browser.reloadGamesList();
});
