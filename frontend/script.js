

var buttonClicked = (function buttonClicked() {

    var data = {
        title: $('#songTitle').val(),
        album: $('#songAlbum').val(),
        genre: $('#songGenre').val(),
        artist: $('#songArtist').val(),
        year: $('#songYear').val()
    };

    $.ajax({
        url: 'http://localhost:4567/api/songs/',
        method: 'POST',
        data: data
    }).done(function () {
        alert('AJAX is successful');
    }).fail(function () {
        alert('too bad, too sad');
    });
});
