<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%@include file="../includes/header.jsp" %>
 <div class="row">
     <div class="col-lg-12">
         <h1 class="page-header">Board Modify Page</h1>
     </div>
     <!-- /.col-lg-12 -->
 </div>
 <!-- /.row -->
 
 <div class="row">
     <div class="col-lg-12">
         <div class="panel panel-default">
             <div class="panel-heading">
                 Board Modify Page
             </div>
             <!-- /.panel-heading -->
             <div class="panel-body">
                <form role="form" action="/board/modify" method="post">
                <input type='hidden' name='pageNum' value='<c:out value="${cri.pageNum }"/>'>
                <input type='hidden' name='amount' value='<c:out value="${cri.amount }"/>'>
                <div class="form-group">
                	<label>Bno</label>
                	<input class="form-control" name='bno' value='<c:out value="${board.bno }"/>' readonly="readonly"> 
                </div>
                 <div class="form-group">
                	<label>Title</label>
                	<input class="form-control" name='title' value='<c:out value="${board.title }"/>'> 
                </div>
                 <div class="form-group">
                	<label>Text area</label>
                	<input class="form-control" rows="3" name='content' value='<c:out value="${board.content }"/>'> 
                </div>
                <div class="form-group">
                	<label>Writer</label>
                	<input class="form-control" name='writer' value='<c:out value="${board.writer }"/>' readonly="readonly"> 
                </div>
                <div class="form-group">
                	<label>RegDate</label>
                	<input class="form-control" name='regDate' value='<fmt:formatDate pattern = "yyyy/MM/dd" value="${board.regdate }"/>' readonly="readonly"> 
                </div>
                <div class="form-group">
                	<label>Update Date</label>
                	<input class="form-control" name='updateDate' value='<fmt:formatDate pattern = "yyyy/MM/dd" value="${board.updateDate }"/>' readonly="readonly"> 
                </div>
	                <button type="submit" data-oper='modify' class="btn btn-default" >Modify</button>
	                <button type="submit" data-oper='remove' class="btn btn-danger">Remove</button>
	                <button type="submit" data-oper='list' class="btn btn-info">List</button>
                </form>
             </div>
             <!-- end panel-body -->
         </div>
         <!-- end panel -->
     </div>
     <!-- /.col-lg-12 -->
 </div>
 <!-- /.row -->

<script type="text/javascript">
$(document).ready(function(){
// JavaScript에서는 button 태그의 'data-oper' 속성을 이용해서 원하는 기능을 동작하도록 처리

	var formObj = $("form");
	
	$('button').on("click", function(e){
		// form 태그의 모든 버튼은 기본적으로 submit으로 처리하기 때문에 
		// e.preventDefault()로 기본 동작을 막고 마지막에 직접 submit() 실행
		e.preventDefault();
		
		var operation = $(this).data("oper");
		
		console.log(operation);
		
		if(operation === 'remove'){
			formObj.attr("action", "/board/remove");
		} else if(operation === 'list'){
			formObj.attr("action", "/board/list").attr("method", "get");
			var pageNumTag = $("input[name='pageNum']").clone();
			var amountTag = $("input[name='amount']").clone();
			
			formObj.empty();
			formObj.append(pageNumTag);
			formObj.append(amountTag);
		}
		formObj.submit();
		
	});
	
});
</script>
<%@include file="../includes/footer.jsp" %>