/**
 * 
 */

function go_next() {
	if (document.formm.okon1[0].checked == true) {  // 동의함 버튼 클릭의 경우
		document.formm.action = "join_form";
		document.formm.submit();
	} else if (document.formm.okon1[1].checked == true) {
		alert("약관에 동의하셔야만 합니다.");
	}
}

function idcheck() {
	
	// 회원가입 화면에 ID 입력 여부 확인
	if(document.getElementById("id").value=="") {
		alert("아이디를 입력해 주세요.");
		document.getElementById("id").focus();
		return false;
	}
	
	var url = "id_check_form?id="+document.getElementById("id").value;
	window.open(url, "_blank_", "toolbar=no, menubar=no, scrollbars=yes, resizable=no, width=350, height=250");
}

/*
** 회원 가입시, 필수입력 확인
*/
function go_save() {
	if (document.getElementById("id").value == "") {
		alert("아이디를 입력해 주세요.");
		document.getElementById("id").focus();
		return false;
	} else if (document.getElementById("id").value != document.getElementById("reid").value) {
		alert("아이디 중복 체크를 해주세요!");
		document.getElementById("id").focus();
		return false;
	} else if (document.getElementById("pwd").value == "") {
		alert("비밀번호를 입력해 주세요.");
		document.getElementById("pwd").focus();
		return false;
	} else if (document.getElementById("pwd").value != document.getElementById("pwdCheck").value ) {
		alert("비밀번호가 일치하지 않습니다. 다시 입력해 주세요.");
		document.getElementById("pwd").focus();
		return false;
	} else if (document.getElementById("name").value == "") {
		alert("이름을 입력해 주세요.");
		document.getElementById("name").focus();
		return false;
	} else if (document.getElementById("email").value == "") {
		alert("이메일을 입력해 주세요.");
		document.getElementById("email").focus();
		return false;
	} else { // 모든 필수입력이 입력된 경우 URL 요청
		document.getElementById("join").action = "join";
		document.getElementById("join").submit();
	}
	
}

/*
** 우편번호 찾기 화면 출력 요청
*/
function post_zip() {
	var url = "find_zip_num";
	window.open(url, "_blank_", "toolbar=no, menubar=no, scrollbars=yes, resizable=no, width=600, height=350");
}

/*
** 아이디 찾기 화면 출력 요청
*/
function find_id_form() {
	var url = "find_id_form";
	
	window.open(url, "_blank_", "toolbar=no, menubar=no, scrollbars=yes, resizable=no, width=550, height=450");
}

/*
** 아이디 찾기 URL 요청
*/
function findMemberId() {
	if (document.getElementById("name").value=="") {
		alert("이름을 입력해 주세요.");
		return false;
	} else if (document.getElementById("email").value=="") {
		alert("이메일을 입력해 주세요.");
		return false;
	} else {
		document.getElementById("findId").action="find_id";  // 컨트롤러로 요청 URL설정
		document.getElementById("findId").submit();
	}
}

function findPassword() {
	if (document.getElementById("id2").value=="") {
		alert("아이디를 입력해 주세요.");
		return false;
	} else if (document.getElementById("name2").value=="") {
		alert("이름을 입력해 주세요.");
		return false;
	} else if (document.getElementById("email2").value=="") {
		alert("이메일을 입력해 주세요.");
		return false;
	} else {
		document.getElementById("findPW").action="find_pwd";  // 비밀번호 찾기 URL
		document.getElementById("findPW").submit();
	}
}


function changePassword() {
	if (document.getElementById("pwd").value == "") {
		alert("비밀번호를 입력해 주세요");
		document.getElementById("pwd").focus();
		return false;
	} else if (document.getElementById("pwdcheck").value != document.getElementById("pwd").value) {
		alert("비밀번호가 맞지 않습니다. 다시 입력해 주세요.");
		document.getElementById("pwd").focus();
		return false;
	} else {
		document.getElementById("pwd_form").action="change_pwd";
		document.getElementById("pwd_form").submit();
	}
}


















