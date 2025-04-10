/*
 * Copyright (c) 2009--2010 Red Hat, Inc.
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
package com.redhat.rhn.manager.entitlement.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.redhat.rhn.domain.entitlement.Entitlement;
import com.redhat.rhn.manager.entitlement.EntitlementManager;
import com.redhat.rhn.testing.RhnBaseTestCase;

import org.junit.jupiter.api.Test;

/**
 * EntitlementManagerTest
 */
public class EntitlementManagerTest extends RhnBaseTestCase {

    @Test
    public void testGetEntitlementByName() {
        assertNull(EntitlementManager.getByName(null));
        assertNull(EntitlementManager.getByName("foo"));

        Entitlement ent = EntitlementManager.getByName(
                    EntitlementManager.ENTERPRISE_ENTITLED);
        assertNotNull(ent);
        assertEquals(EntitlementManager.MANAGEMENT, ent);

        ent = EntitlementManager.getByName("enterprise_entitled");
        assertNotNull(ent);
        assertEquals(EntitlementManager.MANAGEMENT, ent);

        ent = EntitlementManager.getByName(
                EntitlementManager.VIRTUALIZATION_ENTITLED);
        assertNotNull(ent);
        assertEquals(EntitlementManager.VIRTUALIZATION, ent);

        ent = EntitlementManager.getByName(
                EntitlementManager.CONTAINER_BUILD_HOST_ENTITLED);
        assertNotNull(ent);
        assertEquals(EntitlementManager.CONTAINER_BUILD_HOST, ent);

        ent = EntitlementManager.getByName(
                EntitlementManager.OSIMAGE_BUILD_HOST_ENTITLED);
        assertNotNull(ent);
        assertEquals(EntitlementManager.OSIMAGE_BUILD_HOST, ent);

        ent = EntitlementManager.getByName(
                EntitlementManager.ANSIBLE_CONTROL_NODE_ENTITLED);
        assertNotNull(ent);
        assertEquals(EntitlementManager.ANSIBLE_CONTROL_NODE, ent);

        ent = EntitlementManager.getByName(
                EntitlementManager.ANSIBLE_MANAGED_ENTITLED);
        assertNotNull(ent);
        assertEquals(EntitlementManager.ANSIBLE_MANAGED, ent);
    }
}
