/*
 * Copyright (c) 2009--2012 Red Hat, Inc.
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
package com.redhat.rhn.frontend.security;

import org.apache.commons.collections.CollectionUtils;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

/**
 * BaseAuthenticationService
 */
public abstract class BaseAuthenticationService implements AuthenticationService {

    /**
     * {@inheritDoc}
     */
    public boolean requestURIRequiresAuthentication(final HttpServletRequest request) {
        return !CollectionUtils.exists(getUnprotectedURIs(), uri -> request.getRequestURI().startsWith(uri.toString()));
    }

    protected boolean requestURIdoesLogin(final HttpServletRequest request) {
        return CollectionUtils.exists(getLoginURIs(), uri -> request.getRequestURI().startsWith(uri.toString()));
    }

    protected boolean requestPostCsfrWhitelist(final HttpServletRequest request) {
        return CollectionUtils.exists(getPostUnprotectedURIs(),
                uri -> request.getRequestURI().startsWith(uri.toString()));
    }

    protected abstract Set getUnprotectedURIs();
    protected abstract Set getPostUnprotectedURIs();
    protected abstract Set getLoginURIs();
}
