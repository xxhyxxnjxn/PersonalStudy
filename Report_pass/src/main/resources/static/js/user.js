
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



