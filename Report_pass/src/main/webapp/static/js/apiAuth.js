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
		global:false,
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
			 global:false,
		success: function(result) {
				
			
			}
		})	;
		alert("계정이 생성되었습니다.");
			location.replace("/getList");
		
	}
};

$("#site").change(function() {

	$('#apiKey').attr("readonly", false);
	$('#secretKey').attr("readonly", false);
	$('#btn-auth').attr("disabled", false);
	if($('#site').val()=="upbit"){
		$("#upbit_comment").text("업비트 apiKey 발급 할 때 출금, 입금 조회를 꼭 체크 해주세요!");
		$("#upbit_comment").css("color", "red");
	}else{
		$("#upbit_comment").text("");
	}
	$.ajax({
		url: 'apikey/site',
		type: 'post',
		dataType: 'text',
		data: {
			site: $('#site').val(),
		},
		global:false,
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


$(document).ready(function() {
 $('#loading').hide();
});
/*
$(document).ajaxStart(function() {
					$('#loading').show();

					});
$(document).ajaxStop(function() {
					$('#loading').hide();
				alert("계정이 생성되었습니다.");
				location.replace("/getList");
					});
*/
function update() {
	if (ck == 0) {
		alert("인증이 필요합니다");
	} else {
		$.ajax({
			url: 'apikey/update',
			type: 'post',
			dataType: 'text',
			global:false,
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


