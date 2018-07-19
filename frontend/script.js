$(function () {
// this function runs when DOM is loaded completely


    function alertInfo(info) {
        alert(info);
    }



    // find the "Add" button in DOM
    var addSongButton = $("#addSong");

    // subscribe to a "Click" event
    addSongButton.click(function () {

        // construct a new Song object
        var song = {
            title: $("#songTitle").val(),
            album: $("#songAlbum").val(),
            artist: $("#songArtist").val(),
            genre: $("#songGenre").val(),
            year: $("#songYear").val()
        };

        // send the new Song over HTTP to the backend
         $.ajax({
            url: "http://localhost:4567/api/songs/",
            method: "POST",
            data: JSON.stringify(song)
         }).done(function (result) {

             // alert(JSON.stringify(result));

             var container = $("#tableContainer");

             var songElement = $("<div></div>");

             var spanTitle = $("<span></span>");
             spanTitle.html("Title: " + result.title);
             spanTitle.appendTo(songElement);

             var button = $("<button>Get info</button>");
             button.appendTo(songElement);
             button.click(function () {
                 alertInfo(JSON.stringify(result));
             });

             songElement.appendTo(container);
         }).fail(function (error) {
            // if something went really bad -> show an alert
            alert("Not ok, AJAX failed");
         });
    });
});
