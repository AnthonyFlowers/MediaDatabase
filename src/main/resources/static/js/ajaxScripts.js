var old_season_id;
$(document).ready(function() {
	$(".inc-form").on('submit', function(e) {
		e.preventDefault();
	});
});

$(function() {
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	$(document).ajaxSend(function(e, xhr, options) {
		xhr.setRequestHeader(header, token);
	});
});

function set_current_season(old_season, new_season_id) {
	if(!old_season_id){
		old_season_id = old_season;
	}
	var data_to_send = {};
	data_to_send["userSeasonId"] = new_season_id;
	$.ajax({
		type: "POST",
		contentType: "application/json",
		url: "season/set",
		data: JSON.stringify(data_to_send),
		dataType: 'json',
		cache: false,
		timeout: 600000,
		success: function(data) {
			console.log("Updated current season: " + data['msg']);
			$('#btn-set-season-' + new_season_id).attr('hidden', true);
			$('#btn-set-season-' + old_season_id).attr('hidden', false);
			old_season_id = new_season_id;
		},
		error: function(e) {
			console.log("Error setting current season:\n" + e);
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
		},
		error: function(e) {
			console.log("Error decrementing season episode:\n" + e);
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
		},
		error: function(e) {
			console.log("Error incrementing season episode:\n" + e);
		}
	});
}