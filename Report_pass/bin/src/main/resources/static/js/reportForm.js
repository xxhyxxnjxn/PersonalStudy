/**
 * 
 */
$(document).ready(function() {

	$("#myInput").on("keyup", function() {
		var value = $(this).val().toLowerCase();
		$("#myTable tr").filter(function() {
			$(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
		});
	});

});

function updateLately(site) {
	
		$.ajax({
		url: '/board/reportForm/state',
		type: 'post',
		data: {
			site: site
		},
		dataType: 'json',
		//global: false,
		success: function(j) {
			console.log(j.state);
			if (j.state == false) {

	answer = confirm("최근내역을 불러오시겠습니까?");
/*	if(site == "coinone"){
		var currency = $('#coinone_currency').val();
		if(currency == ""){
			alert("코인을 입력해주세요");
		}else{
			if (answer == true) {
					$.ajax({
						url: '/board/updateLately/coinone',
						type: 'post',
						data: {
							site: site,
							currency : currency
						},
						dataType: 'text',
						global:false,
						success: function(j) {
							
								alert('업데이트 되었습니다.');
								location.reload();
						},
						error: function(j) {
							alert('실패');
							location.reload();
						}
					});
					
				}
		}
		
				
	}else{*/
		
		if (answer == true) {
			timerStart();
					$.ajax({
						url: '/board/updateLately',
						type: 'post',
						data: {
							site: site
						},
						dataType: 'text',
						global:false,
						success: function(j) {
								
								alert('업데이트 되었습니다.');
								location.reload();
						},
						error: function(j) {
							alert('실패');
							location.reload();
						}
					});
					
		}
	//}
		

		
		} else {
						alert('데이터 불러오는중 .... 기다려주세요 ..... 누르지말고 ... ');
					}
		
				},
				error: function(j) {
		
					alert('실패');
		
				}
			});
		
}
function refresh(site) {
		$.ajax({
		url: '/board/reportForm/state',
		type: 'post',
		data: {
			site: site
		},
		dataType: 'json',
		//global: false,
		success: function(j) {
			console.log(j.state);
			if (j.state == false) {

	answer = confirm("초기화하시겠습니까?");
		if (answer == true) {
			timerStart();
					$.ajax({
						url: '/board/refresh',
						type: 'post',
						data: {
							site: site
						},
						dataType: 'text',
						global:false,
						success: function(j) {
							
								alert('업데이트 되었습니다.');
								location.reload();
						},
						error: function(j) {
							alert('실패');
							location.reload();
						}
					});
					
				}

		
		} else {
						alert('데이터 불러오는중 .... 기다려주세요 ..... 누르지말고 ... ');
					}
		
				},
				error: function(j) {
		
					alert('실패');
		
				}
			});
		
}

function updateLog(orderId) {
	answer = confirm("일지를 저장하시겠습니까??");
	if (answer == true) {
		let data = {
			log: $('#' + "logId" + orderId).val(),
			site: $('#site').val()
		}
		$.ajax({
			type: "PUT",//insert
			url: "/api/report/" + $('#site').val() + "/" + orderId,
			data: JSON.stringify(data),
			contentType: "application/json;charset=utf-8",
			dataType: "json"//요창을 서버로해서 응답이 왔을 때 기본적으로 모든 것이 문자열 (생긴게 json이라면)=>javascript
		}).done(function(r) {
			alert("수정이 완료되었습니다.");

			//   location.href = "/board/reportForm/" + $('#site').val();
		}).fail(function(r) {
			//var message = JSON.parse(r.responseText);
			//console.log((message));
			alert('서버 오류');
		});
	}
}
$(document).ready(function() {
 $("#example").DataTable();
	$("#coinSelected").on("change", function() {
		var valueCoin = $("#coinSelected").val().toLowerCase();
		var valuenow_dateTo = $("#now_dateTo").val();
		var selectValTo = (valuenow_dateTo.toString().substr(0, 4) + valuenow_dateTo.toString().substr(5, 2) + valuenow_dateTo.toString().substr(8, 2));

		var valuenow_dateFrom = $("#now_dateFrom").val();
		var selectValFrom = (valuenow_dateFrom.toString().substr(0, 4) + valuenow_dateFrom.toString().substr(5, 2) + valuenow_dateFrom.toString().substr(8, 2));

		var table, tr, td, i, searchFilter;
		table = document.getElementById("myTable");
		tr = table.getElementsByTagName("tr");

		for (i = 0; i < tr.length; i++) {
			td = tr[i].getElementsByTagName("td")[2];
			tdCo = tr[i].getElementsByTagName("td")[1];
			searchFilter = td.textContent.toString().substr(0, 4) + td.textContent.toString().substr(6, 2) + td.textContent.toString().substr(10, 2);

			if (td && tdCo) {
				txtValue = tdCo.textContent || tdCo.innerText;
				if ((searchFilter <= selectValTo) && (searchFilter >= selectValFrom) && (txtValue.toLowerCase().indexOf(valueCoin) > -1)) {
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
			log: $('#' + "logId" + orderId).val(),
			site: $('#site').val()
		}
		$.ajax({
			type: "PUT",//insert
			url: "/api/report/" + $('#site').val() + "/" + orderId,
			data: JSON.stringify(data),
			contentType: "application/json;charset=utf-8",
			dataType: "json"//요창을 서버로해서 응답이 왔을 때 기본적으로 모든 것이 문자열 (생긴게 json이라면)=>javascript
		}).done(function(r) {
			alert("수정이 완료되었습니다.");

			//   location.href = "/board/reportForm/" + $('#site').val();
		}).fail(function(r) {
			//var message = JSON.parse(r.responseText);
			//console.log((message));
			alert('서버 오류');
		});

	}
}

$("#excelSubmit").click(function (event) {
        //preventDefault 는 기본으로 정의된 이벤트를 작동하지 못하게 하는 메서드이다. submit을 막음

        event.preventDefault();
        // Get form
        var form = $('#fileUploadForm')[0];
	    // Create an FormData object 
        var data = new FormData(form);
	   // disabled the submit button
        $("#excelSubmit").prop("disabled", true);
		timerStart();
        $.ajax({
            type: "POST",
            enctype: 'multipart/form-data',
            url: "/excel/read",
            data: data,
            processData: false,
            contentType: false,
            cache: false,
            timeout: 600000,
            success: function (data) {
            	alert("제출완료");
                $("#excelSubmit").prop("disabled", false);
		location.reload();
            },
            error: function (e) {
                console.log("ERROR : ", e);
                $("#excelSubmit").prop("disabled", false);
                alert("제출실패 : 관리자에게 문의 하세요");
            }
        });

    });


$("#excelSubmitCoinone").click(function (event) {
        //preventDefault 는 기본으로 정의된 이벤트를 작동하지 못하게 하는 메서드이다. submit을 막음

	event.preventDefault();
        // Get form
        var form = $('#fileUploadFormCoinone')[0];
	    // Create an FormData object 
        var data = new FormData(form);
	   // disabled the submit button
        $("#excelSubmitCoinone").prop("disabled", true);
        $.ajax({
            type: "POST",
            enctype: 'multipart/form-data',
            url: "/excel/readCoinone",
            data: data,
            processData: false,
            contentType: false,
            cache: false,
            timeout: 600000,
            success: function (data) {
            	alert("제출완료");
                $("#excelSubmitCoinone").prop("disabled", false);
		location.reload();
            },
            error: function (e) {
                console.log("ERROR : ", e);
                $("#excelSubmitCoinone").prop("disabled", false);
                alert("제출실패 : 관리자에게 문의 하세요");
            }
        });



    });











