<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Home</title>
</head>
<body>
<h1>
	Hello world!  
</h1>

<P>  The time on the server is ${serverTime}. </P>
<script type="text/javascript">
/* 로그인 후 기본적으로 '/' 경로로 이동하기 때문에 추가' */
self.location ="/board/list";
</script>
</body>
</html>
