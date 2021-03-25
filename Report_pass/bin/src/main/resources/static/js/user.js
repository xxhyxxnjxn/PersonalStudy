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
      $("#btn-confirm-id").on("click", () => {
         this.confirmId();
      });
   },
   confirmId: function() {
       var Id_RegExp = /^[a-zA-Z0-9]{4,12}$/; //id와 유효성 검사 정규식
      
      if ($('#username').val() == "") {
         alert("아이디를 입력하세요");
      } else {
         $.ajax({
            url: '/auth/userId',
            type: 'GET',
            dataType: 'json',
			 enctype: 'multipart/form-data',
  			contentType: "application/json;charset=utf-8",
            data: {
               memId: $('#username').val()
            },
            success: function(result) {
               alert("이미 사용중인 아이디 입니다");
            },
            error: function(result) {
         var usernameOri  = $("#username").val();
            
            if(!Id_RegExp.test(usernameOri)){ //아이디 유효성검사
              alert("ID는 4~12자의 영문 대소문자와 숫자로만 입력하여 주세요.");        
              return false;
          }
               if (result.status == 500) {
                  alert("사용가능한 아이디입니다");
               }
            }
         });
      }
   },
   save: function() {
      //   alert("user 의 save 함수호출됨");
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

       var RegExp =  /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,15}$/; // pwassword 유효성 검사 정규식
       var Id_RegExp = /^[a-zA-Z0-9]{4,12}$/; //id와 유효성 검사 정규식

      var e_RegExp = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;

      var regExp = /^(01[016789]{1}|02|0[3-9]{1}[0-9]{1})-?[0-9]{3,4}-?[0-9]{4}$/;

      var usernameOri  = $("#username").val();
      var passOri  = $("#password").val();
      var passReOri  = $("#passwordRe").val();
      var emailOri  = $("#email").val();
      var nameOri  = $("#name").val();
      var phoneOri  = $("#phone").val();
      
          if(!Id_RegExp.test(usernameOri)){ //아이디 유효성검사
            alert("ID는 4~12자의 영문 대소문자와 숫자로 입력하여 주세요.");        
            return false;
        }
  // ================ PASSWORD 유효성검사 ===============//
        if(passOri==''){ // 비밀번호 입력여부 검사
            alert("Password를 입력해주세요.");
            return false;
        }
        if(!RegExp.test(passOri)){ //패스워드 유효성검사
            alert("Password는 8~15자의 영문 대문자와 소문자, 숫자, 특수문자가 포함되어야 합니다.");
            return false;
        }
        if(passOri==usernameOri){ //패스워드와 ID가 동일한지 검사
            alert("Password는 ID와 동일하면 안됩니다.");
            return false;
        }

        if(passOri!=passReOri){ //비밀번호와 비밀번호확인이 동일한지 검사
            alert("비밀번호가 틀립니다. 다시 확인하여 입력해주세요.");
            return false;
        }

  // ================ email 유효성검사 ================ //
        if(emailOri== ''){ // 이메일 입력여부 검사
            alert("이메일을 입력해주세요.");
            return false;
        }
        
        if(!e_RegExp.test(emailOri)){ //이메일 유효성 검사
            alert("올바른 이메일 형식이 아닙니다.");
            return false;
        }
// ================ 이름 유효성검사 ================ //        
        if(nameOri ==''){
            alert("이름을 입력해주세요.");
            return false;
        }
   if( !regExp.test(phoneOri)) {
        alert("핸드폰번호를 정확하게 입력해주세요");
        return false;
   }


      if ($('#username').val() == "") {
         alert("아이디를 입력하세요");
      } else if($('#password').val() == ""){
         
         alert("비밀번호를 입력하세요");
      } else if($('#name').val() == ""){
         alert("이름을 입력하세요");

      } else if($('#email').val() == ""){
         
         alert("이메일을 입력하세요");
      } else if($('#phone').val() == ""){
         alert("전화번호를 입력하세요");
         
      }
      else {
         $.ajax({
            url: '/auth/userId',
            type: 'GET',
            dataType: 'text',
 enctype: 'multipart/form-data',
			contentType: "application/json;charset=utf-8",
            data: {
               memId: $('#username').val()
            },
            success: function(result) {
               alert("이미 사용중인 아이디 입니다");
               $('#username').val(memId);
            },
            error: function(result) {

               if (result.status == 500) {
                  $.ajax({
                     //회원가입수행 요청(100초 가정)
                     type: "POST",//insert
                     url: "/auth/joinProc",
                     data: JSON.stringify(data),
                     contentType: "application/json;charset=utf-8",
                     dataType: "json"//요창을 서버로해서 응답이 왔을 때 기본적으로 모든 것이 문자열 (생긴게 json이라면)=>javascript
                  }).done(function(r) {
                     if (r.status === 500) {
                        alert("회원가입에 실패하었습니다.");
                     } else {
                        alert("회원가입이 완료되었습니다.");
                        location.href = "/auth/loginForm";
                     }
                  }).fail(function(r) {
                     //var message = JSON.parse(r.responseText);
                     //console.log((message));
                     alert('서버 오류');
                  });
               }
            }
         });
      }
   },
   update: function() {
      //   alert("user 의 save 함수호출됨");




       var RegExp =  /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,15}$/; // pwassword 유효성 검사 정규식
       var Id_RegExp = /^[a-zA-Z0-9]{4,12}$/; //id와 유효성 검사 정규식

      var e_RegExp = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;

      var regExp = /^(01[016789]{1}|02|0[3-9]{1}[0-9]{1})-?[0-9]{3,4}-?[0-9]{4}$/;

      var usernameOri  = $("#username").val();
      var passOri  = $("#password").val();
      var passReOri  = $("#passwordRe").val();
      var emailOri  = $("#email").val();
      var nameOri  = $("#name").val();
      var phoneOri  = $("#phone").val();
      
          if(!Id_RegExp.test(usernameOri)){ //아이디 유효성검사
            alert("ID는 4~12자의 영문 대소문자와 숫자로 입력해주세요.");        
            return false;
        }
  // ================ PASSWORD 유효성검사 ===============//
        if(passOri==''){ // 비밀번호 입력여부 검사
            alert("Password를 입력해주세요.");
            return false;
        }
        if(!RegExp.test(passOri)){ //패스워드 유효성검사
            alert("Password는 8~15자의 영문 대문자와 소문자, 숫자, 특수문자가 포함되어야 합니다.");
            return false;
        }
        if(passOri==usernameOri){ //패스워드와 ID가 동일한지 검사
            alert("Password는 ID와 동일하면 안됩니다.");
            return false;
        }


  // ================ email 유효성검사 ================ //
        if(emailOri== ''){ // 이메일 입력여부 검사
            alert("이메일을 입력해주세요.");
            return false;
        }
        
        if(!e_RegExp.test(emailOri)){ //이메일 유효성 검사
            alert("올바른 이메일 형식이 아닙니다.");
            return false;
        }
// ================ 이름 유효성검사 ================ //        
        if(nameOri ==''){
            alert("이름을 입력해주세요.");
            return false;
        }
   if( !regExp.test(phoneOri)) {
        alert("핸드폰번호를 정확하게 입력해주세요");
        return false;
   }


      if ($('#username').val() == "") {
         alert("아이디를 입력하세요");
      } else if($('#password').val() == ""){
         
         alert("비밀번호를 입력하세요");
      } else if($('#name').val() == ""){
         alert("이름을 입력하세요");

      } else if($('#email').val() == ""){
         
         alert("이메일을 입력하세요");
      } else if($('#phone').val() == ""){
         alert("전화번호를 입력하세요");
         
      }
      else {
	
	
	

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
 enctype: 'multipart/form-data',
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
	
	}


   },
   create: function() {
      location.href = "/apikey";
   }
}
index.init();

