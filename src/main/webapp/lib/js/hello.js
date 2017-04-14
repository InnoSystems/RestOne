$(function() {
	$("#send1").click(function() {
		$.ajax({
			url : "http://localhost:8080/greeting"
		}).then(function(data) {
			$('.greeting1-id').text("The ID is: " + data.id);
			$('.greeting1-content').text("The content is: " + data.content);
		});
	});
	$("#send2").click(function() {
		var str1 = "http://localhost:8080/greeting?name=";
		var str2 = $("#input1").val();
		var str3 = str1 + str2;
		$.ajax({
			url : str3
		}).then(function(data) {
			$('.greeting2-id').text("The ID is: " + data.id);
			$('.greeting2-content').text("The content is: " + data.content);
		});
	});
});
