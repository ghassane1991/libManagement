<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="style.css" type="text/css">
<title>Connexion</title>
</head>
<body>
<div class="header">
	<img alt="Breizh libary" src="img/lb.png">
</div>
<form name="connexion" method="POST" action="index.jsp">
<table>
	<tr>
		<td>
		<label>User name:</label>
		</td>
		<td>
			<input type="text" name="name"/>
		</td>
	</tr>
	<tr>
		<td>
		<label>Password:</label>
		</td>
		<td>
			<input type="password" name="pass"/>
		</td>
	</tr>
</table>
	<input class="submit" type="submit" name="conn" value="Connexion">
</form>
<div class="footer">
	Developed by LATFI Ghassane and LGUENSAT Redouane. BreizhLibrary is distributed under GPL V3 License.
</div>
</body>
</html>