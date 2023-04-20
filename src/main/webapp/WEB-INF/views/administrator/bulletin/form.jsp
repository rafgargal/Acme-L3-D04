<%--
- form.jsp
-
- Copyright (C) 2012-2023 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>



<acme:form>
	<acme:input-moment code="administrator.bulletin.form.label.moment" path="moment"/>	
	<acme:input-textbox code="administrator.bulletin.form.label.title" path="title"/>
	<acme:input-textarea code="administrator.bulletin.form.label.message" path="message"/>
	<acme:input-checkbox code="administrator.bulletin.form.label.critical" path="critical"/>
	<acme:input-url code="administrator.bulletin.form.label.moreInfoLink" path="moreInfoLink"/>

  	<a onclick="changeDisplay();" class="btn btn-dark">
  		<acme:message code="administrator.bulletin.form.button.create"/>								
	</a>
	<jstl:set var="action" value="${acme:pushIdParameter('/administrator/bulletin/create', id)}"/>
	<button id="confirmButton" style="display: none;" type="submit" formmethod="post" onclick="javascript: form.action = getAbsoluteUrl('${action}')" class="btn btn-dark">
		<acme:message code="administrator.bulletin.form.submit.confirm"/>
	</button>		
</acme:form>

<script>
	function changeDisplay() {
	  var element = document.getElementById("confirmButton");
	  element.style.display = "inline-block";
	}
</script>