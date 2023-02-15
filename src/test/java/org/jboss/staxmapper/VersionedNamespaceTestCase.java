/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2023, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.staxmapper;

import java.util.EnumSet;

import org.junit.Assert;
import org.junit.Test;

/**
 * Validates {@link URN#since(URN)} logic.
 * @author Paul Ferraro
 */
public class VersionedNamespaceTestCase {

    enum TestNamespace implements Versioned<IntVersion, TestNamespace>, Namespace {

        VERSION_1_0(1),
        VERSION_1_1(1, 1),
        VERSION_1_2(1, 2),
        VERSION_3_0(3, 0), // Intentionally out-of-order
        VERSION_2_0(2, 0),
        VERSION_2_1(2, 1),
        VERSION_2_2(2, 2),
        VERSION_2_3(2, 3),
        ;
        private final IntVersion version;

        TestNamespace(int major) {
            this.version = new IntVersion(major);
        }

        TestNamespace(int major, int minor) {
            this.version = new IntVersion(major, minor);
        }

        @Override
        public IntVersion getVersion() {
            return this.version;
        }

        @Override
        public String getUri() {
            return String.format("urn:foo:%d.%d", this.version.major(), this.version.minor());
        }
    }

    @Test
    public void getUri() {
        Assert.assertEquals("urn:foo:1.0", TestNamespace.VERSION_1_0.getUri());
        Assert.assertEquals("urn:foo:1.1", TestNamespace.VERSION_1_1.getUri());
    }

    @Test
    public void test() {
        for (TestNamespace namespace : EnumSet.allOf(TestNamespace.class)) {
            Assert.assertTrue(namespace.since(TestNamespace.VERSION_1_0));
            Assert.assertTrue(namespace.since(namespace));
        }
        for (TestNamespace namespace : EnumSet.of(TestNamespace.VERSION_1_0, TestNamespace.VERSION_1_1, TestNamespace.VERSION_1_2, TestNamespace.VERSION_2_0)) {
            Assert.assertFalse(namespace.since(TestNamespace.VERSION_2_1));
        }
        for (TestNamespace namespace : EnumSet.of(TestNamespace.VERSION_2_1, TestNamespace.VERSION_2_2, TestNamespace.VERSION_2_3, TestNamespace.VERSION_3_0)) {
            Assert.assertTrue(namespace.since(TestNamespace.VERSION_2_1));
        }
        for (TestNamespace namespace : EnumSet.complementOf(EnumSet.of(TestNamespace.VERSION_3_0))) {
            Assert.assertFalse(namespace.since(TestNamespace.VERSION_3_0));
        }
    }
}
