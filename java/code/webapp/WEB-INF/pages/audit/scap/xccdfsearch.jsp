<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://rhn.redhat.com/rhn" prefix="rhn" %>
<%@ taglib uri="http://rhn.redhat.com/tags/list" prefix="rl" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<html>
    <head>
    </head>
    <body>
        <script language="javascript">
            jQuery(window).on("load", function() {
                scanDateSearchOptions();
            });
            function scanDateSearchOptions() {
                if (document.getElementById("scanDateOptionsCheckBox").checked) {
                    jQuery("#scanDateOptions").show();
                } else {
                    jQuery("#scanDateOptions").hide();
                }
            }
        </script>
        <rhn:toolbar base="h1" icon="header-search"
                     helpUrl="/docs/${rhn:getDocsLocale(pageContext)}/reference/audit/openscap-advanced-search.html">
            <bean:message key="scapsearch.jsp.toolbar"/>
        </rhn:toolbar>
        <p><bean:message key="scapsearch.jsp.summary"/></p>
        <div class="panel panel-default">
          <div class="panel-heading">
            <h4><bean:message key="scapsearch.jsp.instructions"/></h4>
          </div>
          <div class="panel-body">
            <html:form styleClass="form-horizontal" action="/audit/scap/Search.do">
              <rhn:csrf/>
              <table class="table">
                <tr>
                  <td>
                    <label class="w-100 control-label" for="searchfor">
                      <bean:message key="scapsearch.jsp.searchfor"/>:
                    </label>
                  </td>
                  <td>
                    <div class="row-0">
                      <div class="col-md-6">
                        <div class="input-group">
                          <html:text property="search_string" styleClass="form-control" name="search_string"
                            value="${search_string}" maxlength="100" accesskey="4"/>
                            <div class="input-group-btn">
                              <button type="submit" class="btn btn-default">
                                <rhn:icon type="header-search" />
                                <bean:message key="button.search"/>
                              </button>
                            </div>
                        </div>
                      </div>
                      <div class="col-md-6"><bean:message key="scapsearch.jsp.whatsearch.tip"/></div>
                    </div>
                  </td>
                </tr>
                <tr>
                  <td>
                    <label class="w-100 control-label" for="withresult">
                      <bean:message key="scapsearch.jsp.withresult"/>:
                    </label>
                  </td>
                  <td>
                    <div class="row-0">
                      <div class="col-md-6">
                        <html:select styleClass="form-control" property="result_filter">
                          <html:options collection="allResults" property="label" labelProperty="label"/>
                        </html:select>
                      </div>
                    </div>
                  </td>
                </tr>
                <tr>
                  <td>
                    <label class="w-100 control-label" for="wheretosearch">
                      <bean:message key="systemsearch.jsp.wheretosearch"/>
                    </label>
                  </td>
                  <td>
                    <div class="radio">
                      <label for="whereToSearch-all">
                        <html:radio property="whereToSearch" value="all" styleId="whereToSearch-all"/>
                        <bean:message key="systemsearch.jsp.searchallsystems"/>
                      </label>
                    </div>
                    <div class="radio">
                      <label for="whereToSearch-system_list">
                        <html:radio property="whereToSearch" value="system_list" styleId="whereToSearch-system_list"/>
                        <bean:message key="systemsearch.jsp.searchSSM"/>
                      </label>
                    </div>
                  </td>
                </tr>
                <tr>
                  <td>
                    <label class="w-100 control-label" for="scanDate">
                      <bean:message key="scapsearch.jsp.scan_date"/>:
                    </label>
                  </td>
                  <td>
                    <div class="checkbox">
                      <label for="scanDateOptionsCheckBox">
                        <html:checkbox styleId="scanDateOptionsCheckBox" property="optionScanDateSearch" onclick="javascript:scanDateSearchOptions()">
                        </html:checkbox>
                        <bean:message key="scapsearch.jsp.search_by_scan_dates"/>
                      </label>
                    </div>
                    </br>
                    <div id="scanDateOptions" class="indent">
                      <table class="table">
                        <tr>
                          <td><bean:message key="scapsearch.jsp.start_date"/>:</td>
                          <td><jsp:include page="/WEB-INF/pages/common/fragments/date-picker.jsp">
                                <jsp:param name="widget" value="start"/>
                              </jsp:include>
                          </td>
                        </tr>
                        <tr>
                          <td><bean:message key="scapsearch.jsp.end_date"/>:</td>
                          <td><jsp:include page="/WEB-INF/pages/common/fragments/date-picker.jsp">
                                  <jsp:param name="widget" value="end"/>
                              </jsp:include>
                          </td>
                        </tr>
                      </table>
                    </div>
                  </td>
                </tr>
                <tr>
                  <td>
                    <label class="w-100 control-label" for="showAs">
                      <bean:message key="scapsearch.jsp.show_as"/>:
                    </label>
                  </td>
                  <td>
                    <div class="radio">
                      <label for="show_as-rr">
                      <html:radio property="show_as" value="rr" styleId="show_as-rr"/>
                        <bean:message key="scapsearch.jsp.list_rr"/>
                      </label>
                    </div>
                    <div class="radio">
                      <label for="show_as-tr">
                      <html:radio property="show_as" value="tr" styleId="show_as-tr"/>
                        <bean:message key="scapsearch.jsp.list_tr"/>
                      </label>
                    </div>
                  </td>
                </tr>
              </table>
              <html:hidden property="submitted" value="true"/>
            </html:form>
          </div>
        </div>
        <c:if test="${(search_string != null && search_string != '')}">
            <c:set var="pageList" value="${requestScope.pageList}"/>
            <rl:listset name="searchSet">
                <rhn:csrf/>
                <c:choose>
                    <c:when test="${param.show_as == 'tr'}">
                        <rl:list emptykey="generic.jsp.none" name="searchResultsTr" dataset="pageList">
                            <%@ include file="/WEB-INF/pages/common/fragments/audit/xccdf-easy-list.jspf" %>
                        </rl:list>
                        <rl:csv dataset="pageList" name="searchResultsTr"
                                exportColumns="id,sid,serverName,profile,satisfied,dissatisfied,satisfactionUnknown"/>
                    </c:when>

                    <c:otherwise>
                        <rl:list emptykey="generic.jsp.none" name="searchResults" dataset="pageList">
                            <rl:decorator name="PageSizeDecorator"/>
                            <%@ include file="/WEB-INF/pages/common/fragments/audit/rule-common-columns.jspf" %>
                        </rl:list>
                        <rl:csv dataset="pageList" name="searchResults"
                                exportColumns="id,documentIdref,identsString,evaluationResult"/>
                    </c:otherwise>
                </c:choose>

                <!-- there are two forms here, need to keep the formvars around for pagination -->
                <html:hidden property="submitted" value="true"/>
                <html:hidden property="search_string" value="${search_string}"/>
                <html:hidden property="whereToSearch" value="${param.whereToSearch}"/>
                <html:hidden property="show_as" value="${param.show_as}"/>
                <html:hidden property="result_filter" value="${param.result_filter}"/>
                <html:hidden property="optionScanDateSearch" value="${param.optionScanDateSearch}"/>
                <html:hidden property="start_year" value="${param.start_year}"/>
                <html:hidden property="start_month" value="${param.start_month}"/>
                <html:hidden property="start_day" value="${param.start_day}"/>
                <html:hidden property="start_hour" value="${param.start_hour}"/>
                <html:hidden property="start_minute" value="${param.start_minute}"/>
                <html:hidden property="start_am_pm" value="${param.start_am_pm}"/>
                <html:hidden property="end_year" value="${param.end_year}"/>
                <html:hidden property="end_month" value="${param.end_month}"/>
                <html:hidden property="end_day" value="${param.end_day}"/>
                <html:hidden property="end_hour" value="${param.end_hour}"/>
                <html:hidden property="end_minute" value="${param.end_minute}"/>
                <html:hidden property="end_am_pm" value="${param.end_am_pm}"/>
            </rl:listset>
        </c:if>
    </body>
</html>
