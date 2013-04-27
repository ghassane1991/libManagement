<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.ArrayList"  %>
<%@page import="java.util.List"  %>
<%@page import="booksManagement.Book"  %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="style.css" type="text/css">
<title>New reservation</title>
</head>
<body>
<div class="header">
	<img alt="Breizh libary" src="img/lb.png">
</div>
<form name="reservation" method="POST" action="reservation">
	<select name="book">
	<%
		ArrayList<Book> bs = (ArrayList<Book>) request.getAttribute("bookList");
	    for (Book b : bs){
	%>
		<option>
		<% out.print(b.getIsbn() + " " + b.getTitle()); %>
		</option>
	<% } %>
	</select>
	<% 
	if (bs.size() != 0){	
	%>
	<input type="submit" name="reserve" value="Reserve">
	<%
	}
	else{%>
	<input type="submit" name="reserve" value="Reserve" disabled>
	<% } %>
</form>
<div class="footer">
	Developed by LATFI Ghassane and LGUENSAT Redouane. BreizhLibrary is distributed under GPL V3 License.
</div>
</body>
</html>