$(function () {
	getbaseList();
});
function getbaseList(init){
	JY.Model.loading();
	JY.Ajax.doRequest("baseForm",jypath +'/backstage/db/dbManage/getStructure',null,function(data){
		 $("#baseTable tbody").empty();
    		 var obj=data.obj;
        	 var list=obj.list;
        	 var html="";
    		 if(list!=null&&list.length>0){   
        		 for(var i = 0;i<list.length;i++){
            		 var l=list[i];
            		 html+="<tr>";
            		 html+="<td class='left '>"+JY.Object.notEmpty(l.name)+"</td>";
            		 html+="<td class='left '>"+JY.Object.notEmpty(l.type)+"</td>";
            		 html+="<td class='left '>"+JY.Object.notEmpty(l.precision)+"</td>";
            		 html+="<td class='left hidden-480'>"+JY.Object.notEmpty(l.scale)+"</td>";
            		 if(l.isNullable==0){
            			 html+="<td class='left hidden-480'><i class='icon-check bigger-110 hidden-480'></i> </td>";
            		 }else{
            			 html+="<td class='left hidden-480'><i class='icon-check-empty  bigger-110 hidden-480'></i> </td>";
            		 }
            		 html+="</tr>";		 
            	 } 
        		 $("#baseTable tbody").append(html);
        	 }else{
        		html+="<tr><td colspan='5' class='center'>没有相关数据</td></tr>";
        		$("#baseTable tbody").append(html);
        	 }	
        	 JY.Model.loadingClose();
	});
}