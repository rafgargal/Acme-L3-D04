<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="student.lecture.form.label.title" path="title"/>
	<acme:input-textbox code="student.lecture.form.label.lAbstract" path="lAbstract"/>
	<acme:input-textbox code="student.lecture.form.label.learningTime" path="learningTime"/>
	<acme:input-textbox code="student.lecture.form.label.body" path="body"/>
	<acme:input-textbox code="student.lecture.form.label.activityType" path="activityType"/>
	<acme:input-textbox code="student.lecture.form.label.furtherInfo" path="furtherInfo"/>
	
</acme:form>