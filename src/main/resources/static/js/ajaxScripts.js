$(document).ready(function() {
	$(".inc-form").on('submit', function(e) {
		e.preventDefault();
		var data = $(this)[0];
		console.log(data);
	});
});

$(function() {
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	$(document).ajaxSend(function(e, xhr, options) {
		xhr.setRequestHeader(header, token);
	});
});

function set_current_season(user_season_id){
	var data_to_send = {};
	data_to_send["userSeasonId"] = user_season_id;
	$.ajax({
		type: "POST",
		contentType: "application/json",
		url: "season/set",
		data: JSON.stringify(data_to_send),
		dataType: 'json',
		cache: false,
		timeout: 600000,
		success: function() {
			console.log("Updated current season");
		},
		error: function(e) {
			console.log("Error:" + e);
		}
	});
}

function decrement_season_episode(user_season_id, season_row) {
	var data_to_send = {};
	data_to_send["userSeasonId"] = user_season_id;
	$.ajax({
		type: "POST",
		contentType: "application/json",
		url: "season/episode/decrement",
		data: JSON.stringify(data_to_send),
		dataType: 'json',
		cache: false,
		timeout: 600000,
		success: function(data) {
			$("#season-" + season_row).text(data["episodeNum"]);
			console.log("Success")
		},
		error: function(e) {
			console.log("Error:" + e);
		}
	});
}

function increment_season_episode(user_season_id, season_row) {
	var data_to_send = {};
	data_to_send["userSeasonId"] = user_season_id;
	$.ajax({
		type: "POST",
		contentType: "application/json",
		url: "season/episode/increment",
		data: JSON.stringify(data_to_send),
		dataType: 'json',
		cache: false,
		timeout: 600000,
		success: function(data) {
			$("#season-" + season_row).text(data["episodeNum"]);
			console.log("Success");
		},
		error: function(e) {
			console.log("Error:" + e);
		}
	});
}