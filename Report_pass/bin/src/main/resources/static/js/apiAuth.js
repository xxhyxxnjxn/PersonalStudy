/**
 * 
 */

var ck = 0;

function auth() {
	$.ajax({
		url: 'apikey/auth',
		type: 'post',
		dataType: 'text',
		data: {
			site: $('#site').val(),
			apiKey: $('#apiKey').val(),
			secretKey: $('#secretKey').val()
		},
		success: function(result) {
					alert(result);
			if (result == "인증에 성공했습니다.") {

				$('#apiKey').attr("readonly", true);
				$('#secretKey').attr("readonly", true);
				$('#btn-auth').attr("disabled", true);

				ck = 1;
			}

		},
		error: function(result) {
			alert('실패');
		}
	});
};

function insert() {
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
				location.replace("/getList");
			}
		});
	}
};

$("#site").change(function() {

	$('#apiKey').attr("readonly", false);
	$('#secretKey').attr("readonly", false);
	$('#btn-auth').attr("disabled", false);
	$.ajax({
		url: 'apikey/site',
		type: 'post',
		dataType: 'text',
		data: {
			site: $('#site').val(),
		},
		success: function(result) {

			if (result == "null") {
				$('#apiKey').val("");
				$('#secretKey').val("");
				$('#apiKey').attr("readonly", false);
				$('#secretKey').attr("readonly", false);
				$('#btn-auth').attr("disabled", false);
				$('#btn-insert').attr("disabled", false);

			} else {
				alert("이미 등록된 거래소 입니다");
				$('#apiKey').attr("readonly", true);
				$('#secretKey').attr("readonly", true);
				$('#btn-auth').attr("disabled", true);
				$('#btn-insert').attr("disabled", true);
			}
		}
	});
	ck = 0;
});

function update() {
	if (ck == 0) {
		alert("인증이 필요합니다");
	} else {
		$.ajax({
			url: 'apikey/update',
			type: 'post',
			dataType: 'text',
			data: {
				site: $('#site').val(),
				apiKey: $('#apiKey').val(),
				secretKey: $('#secretKey').val()
			},
			success: function(result) {

				alert("apiKey가 수정되었습니다.");
				location.replace("/getList");
			}
		});
	}
};


