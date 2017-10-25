$(function () {
	JY.Model.loading();
	$("#displayPdfIframe").attr("src",jypath+"/static/plugins/pdfJs/web/viewer.html?file="+jypath+"/static/apidoc/db/db.pdf");
	JY.Model.loadingClose();
});