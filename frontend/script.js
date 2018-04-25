
$(function () {


    function createSong() {

        // Parse values

        var song = {};

        song.title = $('#songTitle').val();
        song.artist = $('#songArtist').val();
        song.album = $('#songAlbum').val();
        song.genre = $('#songGenre').val();
        song.year = $('#songYear').val();

        var list = $('#listOfAddedSongs');

        $.ajax({
            url: 'http://localhost:4567/api/songs/',
            method: 'POST',
            data: JSON.stringify(song)
        })
        .done(function (result) {
            // TODO: add object dynamically...

            var addedSongSpan = $('<span></span>');
            var text = [result.title, result.artist, result.genre].join(', ');
            addedSongSpan.text(text);

            addedSongSpan.appendTo(list);

            $('<br/>').appendTo(list);


        }).fail(function (message) {
            alert(message);
        });
    }

    $('#createSong').click(createSong);
});
