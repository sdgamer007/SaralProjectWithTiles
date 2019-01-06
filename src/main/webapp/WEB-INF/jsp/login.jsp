<html>

<head>
<title>First Web Application</title>
</head>
	<script type="text/javascript" src="http://www.myersdaily.org/joseph/javascript/md5.js"></script>
	<script src="https://code.jquery.com/jquery-3.3.1.js"></script>
	<script type="text/javascript">
	        MyFunction = function(){
	        	 var passwrd = $("#pass").val();
	        	 var newpass = md5(passwrd);
	        	 $("#pass").val(newpass);
	        }
    </script>		

<body>
	
	<form method="post">
		<font color="red">${nameErrorMessage}</font>
		Name : <input type="text" name="sign_no" />
		<font color="red">${passwordErrorMessage}</font>
		Password : <input id="pass" type="password" name="password" /> 
		<font color="red">${errorMessage}</font>
		<input type="submit" onclick="javascript:MyFunction()" />
	</form>
</body>

</html>