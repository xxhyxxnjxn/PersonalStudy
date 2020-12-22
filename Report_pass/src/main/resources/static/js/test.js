/**
 * 
 */
$(document).ready(function() {

	document.getElementById('now_dateTo').valueAsDate = new Date();
	var orderId = $('#orderId').val();
	var site = $('#site').val();

	$("#now_dateTo").on("change", function() {
		var valuenow_dateTo = $("#now_dateTo").val();
		var selectValTo = (valuenow_dateTo.toString().substr(0, 4) + valuenow_dateTo.toString().substr(5, 2) + valuenow_dateTo.toString().substr(8, 2));
		alert(selectValTo);
		var valuenow_dateFrom = $("#now_dateFrom").val();
		var selectValFrom = (valuenow_dateFrom.toString().substr(0, 4) + valuenow_dateFrom.toString().substr(5, 2) + valuenow_dateFrom.toString().substr(8, 2));
		alert(selectValFrom);
		var table, tr, td, i, searchFilter;
		table = document.getElementById("myTable");
		tr = table.getElementsByTagName("tr");

		for (i = 0; i < tr.length; i++) {
			td = tr[i].getElementsByTagName("td")[2];
			searchFilter = td.textContent.toString().substr(0, 4) + td.textContent.toString().substr(6, 2) + td.textContent.toString().substr(10, 2);

			if (td) {
				txtValue = td.textContent || td.innerText;
				if ((searchFilter <= selectValTo) && (searchFilter >= selectValFrom)) {
					tr[i].style.display = "";
				} else {
					tr[i].style.display = "none";
				}
			}

		}






	});
	$("#now_dateFrom").on("change", function() {
		var valuenow_dateTo = $("#now_dateTo").val();
		var selectValTo = (valuenow_dateTo.toString().substr(0, 4) + valuenow_dateTo.toString().substr(5, 2) + valuenow_dateTo.toString().substr(8, 2));

		var valuenow_dateFrom = $("#now_dateFrom").val();
		var selectValFrom = (valuenow_dateFrom.toString().substr(0, 4) + valuenow_dateFrom.toString().substr(5, 2) + valuenow_dateFrom.toString().substr(8, 2));
		alert(selectValTo);
		alert(selectValFrom);
		var tableFrom, tr, td, i, searchFilter;
		table = document.getElementById("myTable");
		tr = table.getElementsByTagName("tr");

		for (i = 0; i < tr.length; i++) {
			td = tr[i].getElementsByTagName("td")[2];
			searchFilter = td.textContent.toString().substr(0, 4) + td.textContent.toString().substr(6, 2) + td.textContent.toString().substr(10, 2);

			if (td) {
				txtValue = td.textContent || td.innerText;
				if ((searchFilter <= selectValTo) && (searchFilter >= selectValFrom)) {
					tr[i].style.display = "";
				} else {
					tr[i].style.display = "none";
				}
			}
		}



	});
	$("#coinSelected").on("change", function() {
		var valueCoin = $("#coinSelected").val().toLowerCase();
		var valuenow_dateTo = $("#now_dateTo").val();
		var selectValTo = (valuenow_dateTo.toString().substr(0, 4) + valuenow_dateTo.toString().substr(5, 2) + valuenow_dateTo.toString().substr(8, 2));
		alert(selectValTo);
		var valuenow_dateFrom = $("#now_dateFrom").val();
		var selectValFrom = (valuenow_dateFrom.toString().substr(0, 4) + valuenow_dateFrom.toString().substr(5, 2) + valuenow_dateFrom.toString().substr(8, 2));
		alert(selectValFrom);
		var table, tr, td, i, searchFilter;
		table = document.getElementById("myTable");
		tr = table.getElementsByTagName("tr");

		for (i = 0; i < tr.length; i++) {
			td = tr[i].getElementsByTagName("td")[2];
			tdCo = tr[i].getElementsByTagName("td")[1];
			searchFilter = td.textContent.toString().substr(0, 4) + td.textContent.toString().substr(6, 2) + td.textContent.toString().substr(10, 2);

			if (td&&tdCo) {
				txtValue = tdCo.textContent || tdCo.innerText;
				if ((searchFilter <= selectValTo) && (searchFilter >= selectValFrom)&&(txtValue.toLowerCase().indexOf(valueCoin) > -1)) {
					tr[i].style.display = "";
				} else {
					tr[i].style.display = "none";
				}
			}

		}
		
	});
	$("#myInput").on("keyup", function() {
		var value = $(this).val().toLowerCase();
		$("#myTable tr").filter(function() {
			$(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
		});
	});

});
function updateLog(orderId) {
	answer = confirm("일지를 저장하시겠습니까??");
	if (answer == true) {
		let data = {
			log: $('#' + "logId" + orderId).val()
		}
		$.ajax({
			type: "PUT",//insert
			url: "/api/report/" + orderId,
			data: JSON.stringify(data),
			contentType: "application/json;charset=utf-8",
			dataType: "json"//요창을 서버로해서 응답이 왔을 때 기본적으로 모든 것이 문자열 (생긴게 json이라면)=>javascript
		}).done(function(r) {
			alert("수정이 완료되었습니다.");

			location.href = "/board/reportForm/" + $('#site').val();
		}).fail(function(r) {
			//var message = JSON.parse(r.responseText);
			//console.log((message));
			alert('서버 오류');
		});



	}
}






/**
 * 
 */
let index = {
	init: function() {
		$("#btn-save").on("click", () => {
			this.save();
		});
		$("#btn-apiCreate").on("click", () => {
			this.create();
		});
		$("#btn-update").on("click", () => {
			this.update();
		});
	},
	save: function() {
		//	alert("user 의 save 함수호출됨");
		let data = {
			memId: $("#username").val(),
			password: $("#password").val(),
			email: $("#email").val(),
			name: $("#name").val(),
			phone: $("#phone").val()
		};
		//console.log(data);
		//ajax통신을 이용해서 세개의 데이터를 json으로 변경하여 insert 요청하기 
		//ajax호출시 default가 비동시 호출
		$.ajax({
			//회원가입수행 요청(100초 가정)
			type: "POST",//insert
			url: "/auth/joinProc",
			data: JSON.stringify(data),
			contentType: "application/json;charset=utf-8",
			dataType: "json"//요창을 서버로해서 응답이 왔을 때 기본적으로 모든 것이 문자열 (생긴게 json이라면)=>javascript
		}).done(function(r) {
			alert("회원가입이 완료되었습니다.");
			location.href = "/auth/loginForm";
		}).fail(function(r) {
			//var message = JSON.parse(r.responseText);
			//console.log((message));
			alert('서버 오류');
		});
	},	
	update: function() {
		//	alert("user 의 save 함수호출됨");
		let data = {
			idx: $("#idx").val(),
			password: $("#password").val(),
			email: $("#email").val(),
			name: $("#name").val(),
			phone: $("#phone").val()
		};
		//console.log(data);
		//ajax통신을 이용해서 세개의 데이터를 json으로 변경하여 insert 요청하기 
		//ajax호출시 default가 비동시 호출
		$.ajax({
			//회원가입수행 요청(100초 가정)
			type: "PUT",//insert
			url: "/user",
			data: JSON.stringify(data),
			contentType: "application/json;charset=utf-8",
			dataType: "json"//요창을 서버로해서 응답이 왔을 때 기본적으로 모든 것이 문자열 (생긴게 json이라면)=>javascript
		}).done(function(r) {
			alert("회원수정이 완료되었습니다.");
			location.href = "/getList";
		}).fail(function(r) {
			//var message = JSON.parse(r.responseText);
			//console.log((message));
			alert('서버 오류');
		});
	},
	create: function() {
	location.href = "/apikey";
	}
	
	
	
	
	
}
index.init();



















