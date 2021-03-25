
function roading() {
	if (ck == 0) {
		alert("인증이 필요합니다");
	} else {
		$.ajax({
			url: 'apikey/insert',
			type: 'post',
			dataType: 'text',
			data: {
				site: $('#site').val(),
				apiKey: $('#apiKey').val(),
				secretKey: $('#secretKey').val()
			},
			success: function(result) {

				alert("계정이 생성되었습니다.");
				location.replace("/roading");
			}
		});
	}
};