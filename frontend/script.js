
$(function() {

	$("#saveButton").click(function() {

		var song = {
			title: $("#songTitle").val(),
			artist: $("#songArtist").val(),
			album: $("#songAlbum").val(),
			genre: $("#songGenre").val(),
			year: $("#songYear").val()
		};

		console.log(JSON.stringify(song));

		$.ajax({
			"method": "POST",
			"url": "http://localhost:4567/api/songs/",
			"data": JSON.stringify(song)
		}).done(function(result) {

			var container = $("#elementContainer");

			var songElement = $("<div></div>");
			songElement.appendTo(container);

			var songTitle = $("<span></span>");
			songTitle.html("Title: " + result.title);
			songTitle.appendTo(songElement);

			var songButton = $("<button>Show Details</button>");
			songButton.appendTo(songElement);
			songButton.click(function() {
				alert(JSON.stringify(result));
			});


		}).fail(function() {
			alert("Something went wrong");
		});
	});

});
