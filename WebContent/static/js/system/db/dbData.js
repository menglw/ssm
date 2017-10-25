$(function () {
	getbaseList();
});
function getbaseList(init){
	JY.Model.loading();
	JY.Ajax.doRequest("baseForm",jypath +'/backstage/db/dbManage/getData',null,function(data){
		$("#baseTable thead").empty(); 
		$("#baseTable tbody").empty();
    		 var obj=data.obj;
        	 var list=obj.list;
        	 var results=list.results;
        	 var thHtml ="",tbHtml="";
         	 var pageNum=list.pageNum,pageSize=list.pageSize,totalRecord=list.totalRecord;
    		 if(results!=null&&results.length>0){
    			 var leng=(pageNum-1)*pageSize;//计算序号		
        		 for(var i = 0;i<results.length;i++){
        			 var datas=results[i];
        			 if(i==0){
        				 thHtml+="<tr><th style='width:3%' class='center'>序号</th>";
        				 var wh=parseInt(97/datas.length);
        				 for(var j = 0;j<datas.length;j++){		
            				 thHtml+="<th style='width:"+wh+"%' class='center' >"+datas[j]+"</th>";         				
            			 }
        				 thHtml+="</tr>";	
        			 }else{	
        				 tbHtml+="<tr>";
        				 tbHtml+="<td class='center hidden-480'>"+(i+leng)+"</td>";
        				 for(var j = 0;j<datas.length;j++){		
            				 tbHtml+="<td class='center'>"+datas[j]+"</td>";
            			 }   
        				 tbHtml+="</tr>";
        			 }      			
            	 } 
        		 if(list.length==1){
        			 tbHtml+="<tr><td colspan='30' class='center'>没有相关数据</td></tr>";
        		 }
        		 $("#baseTable thead").append(thHtml);
        		 $("#baseTable tbody").append(tbHtml);
        		 JY.Page.setPage("baseForm","pageing",pageSize,pageNum,totalRecord,"getbaseList");
        	 }else{
        		tbHtml+="<tr><td colspan='30' class='center'>没有相关数据</td></tr>";
        		$("#baseTable tbody").append(tbHtml);
        	 }	
        	 JY.Model.loadingClose();
	});
}