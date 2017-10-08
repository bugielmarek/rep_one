<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<script type="text/javascript"
	src="<c:url value='/static/script/jquery.js'/>"></script>
<!-- ========================================= <><><><><><><><><><><><><><><><><><><><><><><><><><><><><><> ========================================= -->


<!-- ========================================= SCRIPT FOCUS =========================================  -->

<script type="text/javascript">
				$(document).ready(function() {
					document.formCreateUser.username.focus();
				});				
			</script>



<!-- ========================================= SCRIPT PASSWORDS MATCH =========================================  -->

<script type="text/javascript">
	function onLoad() {

		$("#password").keyup(checkPasswordsMatch);
		$("#confirmpass").keyup(checkPasswordsMatch);

		$("#details").submit(canSubmit);
	}

	function canSubmit() {
		var password = $("#password").val();
		var confirmpass = $("#confirmpass").val();

		if (password != confirmpass) {
			alert("<fmt:message key='UnmatchedPassword.user.password'/>")
			return false;
		} else {
			return true;
		}
	}

	function checkPasswordsMatch() {
		var password = $("#password").val();
		var confirmpass = $("#confirmpass").val();

		if (password.length > 3 || confirmpass.length > 3) {

			if (password == confirmpass) {
				$("#matchpass").text(
						"<fmt:message key='MatchedPassword.user.password'/>");
				$("#matchpass").addClass("valid");
				$("#matchpass").removeClass("errors");
			} else {
				$("#matchpass").text(
						"<fmt:message key='UnmatchedPassword.user.password'/>");
				$("#matchpass").addClass("errors");
				$("#matchpass").removeClass("valid");
			}
		}
	}

	$(document).ready(onLoad);
</script>

<!-- ========================================= <><><><><><><><><><><><><><><><><><><><><><><><><><><><><><> ========================================= -->