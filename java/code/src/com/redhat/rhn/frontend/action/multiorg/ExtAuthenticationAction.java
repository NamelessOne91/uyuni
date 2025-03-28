/*
 * Copyright (c) 2009--2014 Red Hat, Inc.
 *
 * This software is licensed to you under the GNU General Public License,
 * version 2 (GPLv2). There is NO WARRANTY for this software, express or
 * implied, including the implied warranties of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. You should have received a copy of GPLv2
 * along with this software; if not, see
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt.
 *
 * Red Hat trademarks are not licensed under GPLv2. No permission is
 * granted to use or replicate Red Hat trademarks that are incorporated
 * in this software or its documentation.
 */
package com.redhat.rhn.frontend.action.multiorg;

import com.redhat.rhn.common.db.datasource.DataList;
import com.redhat.rhn.common.localization.LocalizationService;
import com.redhat.rhn.domain.common.RhnConfiguration;
import com.redhat.rhn.domain.common.RhnConfigurationFactory;
import com.redhat.rhn.domain.org.OrgFactory;
import com.redhat.rhn.domain.org.usergroup.UserGroupFactory;
import com.redhat.rhn.domain.user.User;
import com.redhat.rhn.frontend.dto.OrgDto;
import com.redhat.rhn.frontend.struts.RequestContext;
import com.redhat.rhn.frontend.struts.RhnAction;
import com.redhat.rhn.frontend.struts.RhnHelper;
import com.redhat.rhn.manager.org.OrgManager;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ExtAuthenticationAction
 */
public class ExtAuthenticationAction extends RhnAction {

    /** {@inheritDoc} */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm formIn,
            HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm daForm = (DynaActionForm) formIn;
        RequestContext ctx = new RequestContext(request);

        if (ctx.isSubmitted()) {
            // store the value
            RhnConfigurationFactory factory = RhnConfigurationFactory.getSingleton();
            String toOrgString = daForm.getString("to_org");
            if (!StringUtils.isEmpty(toOrgString)) {
                // just check the org id is valid
                OrgFactory.lookupById(Long.parseLong(toOrgString));
            }
            // store the value
            factory.updateConfigurationValue(RhnConfiguration.KEYS.EXTAUTH_USE_ORGUNIT, toOrgString);

            Boolean keepRoles = (Boolean) daForm.get("keep_roles");
            boolean keepTempRoles = factory.getBooleanConfiguration(RhnConfiguration.KEYS.EXTAUTH_KEEP_TEMPROLES)
                    .getValue();
            if (keepTempRoles && !BooleanUtils.toBoolean(keepRoles)) {
                // if the option was turned off, delete temporary roles
                // across the whole satellite
                UserGroupFactory.deleteTemporaryRoles();
            }
            // store the value
            factory.updateConfigurationValue(RhnConfiguration.KEYS.EXTAUTH_KEEP_TEMPROLES, keepRoles);

            createSuccessMessage(request, "message.ext_auth_updated", null);
            return mapping.findForward("success");
        }

        // not submitted
        setupForm(request, daForm);
        return mapping.findForward(RhnHelper.DEFAULT_FORWARD);
    }

    private void setupForm(HttpServletRequest request, DynaActionForm form) {
        RequestContext ctx = new RequestContext(request);
        User user = ctx.getCurrentUser();
        RhnConfigurationFactory factory = RhnConfigurationFactory.getSingleton();
        Boolean useOrgUnit = factory.getBooleanConfiguration(RhnConfiguration.KEYS.EXTAUTH_USE_ORGUNIT).getValue();
        form.set("use_ou", useOrgUnit);

        DataList<OrgDto> dr = OrgManager.activeOrgs(user);

        List<LabelValueBean> orgs = new LinkedList<>();
        orgs.add(lv(
                LocalizationService.getInstance().getMessage("message.ext_auth_disable"),
                null));
        for (OrgDto orgDto : dr) {
            orgs.add(lv(orgDto.getName(), orgDto.getId().toString()));
        }
        request.setAttribute("orgs", orgs);

        Long actOrgId = factory.getLongConfiguration(RhnConfiguration.KEYS.EXTAUTH_DEFAULT_ORGID).getValue();
        if (actOrgId != null) {
            form.set("to_org", actOrgId.toString());
        }
        else {
            form.set("to_org", null);
        }

        Boolean keepRoles = factory.getBooleanConfiguration(RhnConfiguration.KEYS.EXTAUTH_KEEP_TEMPROLES).getValue();
        form.set("keep_roles", keepRoles);
    }
}
