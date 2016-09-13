<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Home</title>
</head>
<body>
	<center><h1>Please do not refresh this page...</h1></center>
	<pre>
	</pre>
	<form method="post" action="${paytmUrl}" name='f1'>
		<input type='text' name='REQUEST_TYPE' value='${requestType}'><br>
		<input type='text' name='MID' value='${mid}'><br>
		<input type='text' name='ORDER_ID' value='${orderId}'><br>
		<input type='text' name='CUST_ID' value='${custId}'><br>
		<input type='text' name='TXN_AMOUNT' value='${txnAmt}'><br>
		<input type='text' name='CHANNEL_ID' value='${chanelId}'><br>
		<input type='text' name='INDUSTRY_TYPE_ID' value='${industryTypeId}'><br>
		<input type='text' name='WEBSITE' value='${website}'><br>
		<input type='text' name='MOBILE_NO' value='${mobileNo}'><br>
		<input type='text' name='EMAIL' value='${email}'><br>
		<input type='text' name='CALLBACK_URL' value='${callbackUrl}'><br>
		<input type='text' name='CHECKSUMHASH' value='${CHECKSUMHASH}'><br>
		
		<input type='submit'  value='Pay'>
	</form>
	
	
	<script type='text/javascript'>
		//document.f1.submit();
	</script>
</body>
</html>
