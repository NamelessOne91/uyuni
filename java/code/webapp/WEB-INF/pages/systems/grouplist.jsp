<%@ taglib uri="http://rhn.redhat.com/rhn" prefix="rhn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://rhn.redhat.com/tags/list" prefix="rl" %>

<html>
<head>
</head>
<body>
<rhn:toolbar base="h1" icon="header-system-groups" imgAlt="system.common.groupAlt"
 helpUrl="/docs/${rhn:getDocsLocale(pageContext)}/reference/systems/system-groups.html"
 creationUrl="/rhn/groups/EditGroup.do"
 creationType="group"
 creationAcl="authorized_for(systems.groups.details, W)">
  <bean:message key="grouplist.jsp.header"/>
</rhn:toolbar>

<rl:listset  name="groups" legend="system-group">
<rhn:csrf />
 <%@ include file="/WEB-INF/pages/common/fragments/systems/group_listdisplay.jspf" %>
</rl:listset>

</body>
</html>
