<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html >
<html lang="en">
<head>
<%@include file="../../common/includeBaseSet.jsp" %>
<%@include file="../../common/includeSystemSet.jsp" %>
</head>
<body>
	<div class="page-content">
		<div class="row-fluid">	
			<div class="col-xs-12">
				<form id="baseForm" class="form-inline" method="POST" onsubmit="return false;">
				<div class="row">
					<div class="widget-main">【${tName}】表</div>
				</div>
				<input type='hidden' name='tName' value='${tName}'/>
				</form>
				<table id="baseTable" class="table table-striped table-bordered table-hover" >
					<thead>
						<tr>
							<th style="width:20%" class="center">字段名</th>
							<th style="width:20%" class="center">类型</th>
							<th style="width:20%" class="center">长度</th>
							<th style="width:20%" class="center hidden-480">精度</th>
							<th style="width:20%" class="center hidden-480">不是null</th>
						</tr>
					</thead>
					<tbody></tbody>
				</table>		
			<!-- #dialog-confirm -->
			<%@include file="../../common/dialog.jsp" %>	
			</div>
		</div>
	</div>
	<script src="${jypath}/static/js/system/db/dbStructure.js"></script>
</body>
</html>