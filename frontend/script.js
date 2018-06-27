$(function () {
// this function runs when DOM is loaded completely

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

            // when ready and OK -> get updated HTML table with song records
            $.ajax({
                method: "GET",
                url: "http://localhost:4567/song-list-partial"
            }).done(function (html) {

                // when HTML received -> put it inside the prepared container
                $("#tableContainer").html(html);
            });

         }).fail(function (error) {
            // if something went really bad -> show an alert
            alert("Not ok, AJAX failed");
         });
    });
});
