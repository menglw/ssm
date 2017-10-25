$(function () {
	getbaseList();
});
function getbaseList(init){
	JY.Model.loading();
	JY.Ajax.doRequest("baseForm",jypath +'/backstage/db/dbManage/findAllTables',null,function(data){
		 $("#baseTable tbody").empty();
    		 var obj=data.obj;
        	 var list=obj.list;
        	 var permitBtn=obj.permitBtn;
        	 var html="";
    		 if(list!=null&&list.length>0){
        		 var leng=0;//计算序号
        		 for(var i = 0;i<list.length;i++){
            		 var l=list[i];
            		 html+="<tr>";
            		 html+="<td class='center'><label> <input type='checkbox' name='ids' value='"+l+"' class='ace' /> <span class='lbl'></span></label></td>";
            		 html+="<td class='center hidden-480'>"+(i+leng+1)+"</td>";
            		 html+="<td class='left '>"+JY.Object.notEmpty(l)+"</td>";
            		 html+=JY.Tags.setFunction(l,permitBtn);
            		 html+="</tr>";		 
            	 } 
        		 $("#baseTable tbody").append(html);
        	 }else{
        		html+="<tr><td colspan='4' class='center'>没有相关数据</td></tr>";
        		$("#baseTable tbody").append(html);
        	 }	
        	 JY.Model.loadingClose();
	});
}
function checkData(tName){
	var width=document.documentElement.clientWidth * 1+"px";
	var height=document.documentElement.clientHeight * 1+"px";
	var index =layer.open({
	    type: 2,
	    title: "查看【"+tName+"】数据",
	    shadeClose: true,
	    area: [width, height],
	    content: jypath+"/backstage/db/dbManage/dbData?tName="+tName
	});
	layer.full(index);
}
function checkStructure(tName){
	var width=document.documentElement.clientWidth * 1+"px";
	var height=document.documentElement.clientHeight * 1+"px";
	var index =layer.open({
	    type: 2,
	    title: "查看【"+tName+"】表结构",
	    shadeClose: true,
	    area: [width, height],
	    content: jypath+"/backstage/db/dbManage/dbStructure?tName="+tName
	});
	layer.full(index);
}