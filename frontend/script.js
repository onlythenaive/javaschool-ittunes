
$(function () {

	var addSongBtn = $("#addSongButton");
	addSongBtn.click(function () {
		
		var title = $("#songTitle").val();
		
		
		var song = {
			title: $("#songTitle").val(),
			album: $("#songAlbum").val(),
			artist: $("#songArtist").val(),
			genre: $("#songGenre").val(),
			year: $("#songYear").val()
		};
		
		$.ajax({
			url: "http://localhost:4567/api/songs/",
			method: "POST",
			data: JSON.stringify(song)
		}).done(function (result) {
			
			var container = $("#listContainer");
			
			var songElement = $("<div></div>");
			songElement.appendTo(container);
			
			var songTitle = $("<span></span>");
			songTitle.html("Title: " + result.title);
			songTitle.appendTo(songElement);
			
			var songDetailsBtn = $("<button>Show Details</button>");
			songDetailsBtn.appendTo(songElement);
			songDetailsBtn.click(function () {
				alert(JSON.stringify(result));
			});
			
			
		}).fail(function (error) {
			alert(JSON.stringify(error));
		});		
	});
});
