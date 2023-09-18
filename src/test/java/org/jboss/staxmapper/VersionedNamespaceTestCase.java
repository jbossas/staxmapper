/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2023 Red Hat, Inc., and individual contributors
 * as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
