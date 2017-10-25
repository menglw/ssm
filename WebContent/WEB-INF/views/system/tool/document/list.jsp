<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html >
<html lang="en">
<head>
<%@include file="../../common/includeBaseSet.jsp"%>
<%@include file="../../common/includeSystemSet.jsp"%>
</head>
<body>

	<div class="page-content">
		<div class="row" >
			<div class="col-xs-12">
			<iframe id="displayPdfIframe" width="100%" height="800" src=""></iframe>
			</div>
		</div>	
		<%@include file="../../common/dialog.jsp" %>
	</div>
<script src="${jypath}/static/js/system/tool/document.js"></script>
</body>
</html>