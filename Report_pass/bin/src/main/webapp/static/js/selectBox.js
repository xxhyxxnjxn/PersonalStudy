/**
 * 
 */

let index ={
		init:function(){
			$("#selectsCoins").on("change",()=>{
				alert($("#selectsCoins").val());
			} );
/*			$("#selectSites").on("change",()=>{
				var site = $("#selectSites").val();
				this.selectedSite(site);
			} );*/
		},
		selectedSite: function(site){
			$.ajax({
				type:"POST",//insert
				url:"/report/selectedSite/"+site,
				contentType:"application/json;charset=utf-8",
				dataType:"json"//요창을 서버로해서 응답이 왔을 때 기본적으로 모든 것이 문자열 (생긴게 json이라면)=>javascript
				,success:function(){
					alert("성공");
				}
				,error:function(){
					alert("실패");
				}
			}).done(function(r){
				alert("수정이 완료되었습니다.");
				alert(r);
				location.href="/board/reportForm";
			}).fail(function(r){
				//var message = JSON.parse(r.responseText);
				//console.log((message));
				//alert('서버 오류');
			}); 
		},
		update: function(){
			let orderId= $("#orderId").val();
			let data= {
				title : $("#title").val(),
			    content : $("#content").val()
			}
			$.ajax({
				type:"PUT",//insert
				url:"/api/board/"+orderId,
				data: JSON.stringify(data),
				contentType:"application/json;charset=utf-8",
				dataType:"json"//요창을 서버로해서 응답이 왔을 때 기본적으로 모든 것이 문자열 (생긴게 json이라면)=>javascript
			}).done(function(r){
				alert("수정이 완료되었습니다.");
				alert(r);
				location.href="/board/"+orderId+"/updateForm";
			}).fail(function(r){
				//var message = JSON.parse(r.responseText);
				//console.log((message));
				//alert('서버 오류');
			}); 
		}
} 
index.init();

$("#excelSubmitCoinoneBank").click(function (event) {
        //preventDefault 는 기본으로 정의된 이벤트를 작동하지 못하게 하는 메서드이다. submit을 막음

	event.preventDefault();
        // Get form
        var form = $('#fileUploadFormCoinoneBank')[0];
	    // Create an FormData object 
        var data = new FormData(form);
	   // disabled the submit button
        $("#excelSubmitCoinoneBank").prop("disabled", true);
        $.ajax({
            type: "POST",
            enctype: 'multipart/form-data',
            url: "/excel/readCoinone/bankstatement",
            data: data,
            processData: false,
            contentType: false,
            cache: false,
            timeout: 600000,
            success: function (data) {
            	alert("제출완료");
                $("#excelSubmitCoinoneBank").prop("disabled", false);
		location.reload();
            },
            error: function (e) {
                console.log("ERROR : ", e);
                $("#excelSubmitCoinoneBank").prop("disabled", false);
                alert("제출실패 : 관리자에게 문의 하세요");
            }
        });



    });

$("#excelSubmitUpbitBank").click(function (event) {
        //preventDefault 는 기본으로 정의된 이벤트를 작동하지 못하게 하는 메서드이다. submit을 막음

	event.preventDefault();
        // Get form
        var form = $('#fileUploadFormUpbitBank')[0];
	    // Create an FormData object 
        var data = new FormData(form);
	   // disabled the submit button
        $("#excelSubmitUpbitBank").prop("disabled", true);
        $.ajax({
            type: "POST",
            enctype: 'multipart/form-data',
            url: "/excel/readUpbit/bankstatement",
            data: data,
            processData: false,
            contentType: false,
            cache: false,
            timeout: 600000,
            success: function (data) {
            	alert("제출완료");
                $("#excelSubmitUpbitBank").prop("disabled", false);
		location.reload();
            },
            error: function (e) {
                console.log("ERROR : ", e);
                $("#excelSubmitUpbitBank").prop("disabled", false);
                alert("제출실패 : 관리자에게 문의 하세요");
            }
        });



    });

	$("#orderby").change(function() {
		var orderby = $("#orderby").val();
		$('#myTable').html("");
		trade();
	});

	$("#type").change(function() {
		var type = $("#type").val();
		$('#myTable').html("");
		trade();
	});
	$("#priceSelect").change(function() {
		var priceSelect = $("#priceSelect").val();
		$('#myTable').html("");
		trade();
	});
