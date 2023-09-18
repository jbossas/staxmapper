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

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link IntVersion}.
 * @author Paul Ferraro
 */
public class IntVersionTestCase {

    @Test
    public void compareTo() {
        IntVersion v100 = new IntVersion(1);
        IntVersion v101 = new IntVersion(1, 0, 1);
        IntVersion v110 = new IntVersion(1, 1);
        IntVersion v111 = new IntVersion(1, 1, 1);
        IntVersion v200 = new IntVersion(2, 0, 0);

        Assert.assertEquals(0, v100.compareTo(v100));
        Assert.assertEquals(0, v101.compareTo(v101));
        Assert.assertEquals(0, v110.compareTo(v110));
        Assert.assertEquals(0, v111.compareTo(v111));
        Assert.assertEquals(0, v200.compareTo(v200));

        Assert.assertTrue(v100.compareTo(v200) < 0);
        Assert.assertTrue(v200.compareTo(v100) > 0);
        Assert.assertTrue(v100.compareTo(v110) < 0);
        Assert.assertTrue(v110.compareTo(v100) > 0);
        Assert.assertTrue(v100.compareTo(v101) < 0);
        Assert.assertTrue(v101.compareTo(v100) > 0);
        Assert.assertTrue(v111.compareTo(v200) < 0);
        Assert.assertTrue(v200.compareTo(v111) > 0);
    }

    @Test
    public void equals() {
        Assert.assertEquals(new IntVersion(), new IntVersion());
        Assert.assertNotEquals(new IntVersion(), new IntVersion(1));
        Assert.assertNotEquals(new IntVersion(1), new IntVersion());
        Assert.assertEquals(new IntVersion(1), new IntVersion(1));
        Assert.assertEquals(new IntVersion(1), new IntVersion(1, 0));
        Assert.assertEquals(new IntVersion(1), new IntVersion(1, 0, 0));
        Assert.assertEquals(new IntVersion(1), new IntVersion(1, 0, 0, 0));
        Assert.assertNotEquals(new IntVersion(1), new IntVersion(1, 0, 0, 1));
        Assert.assertNotEquals(new IntVersion(1, 0, 0, 1), new IntVersion(1));
        Assert.assertEquals(new IntVersion(1, 0, 0, 0), new IntVersion(1));
        Assert.assertEquals(new IntVersion(1, 0, 0), new IntVersion(1));
        Assert.assertEquals(new IntVersion(1, 0), new IntVersion(1));
    }

    @Test
    public void string() {
        Assert.assertEquals("", new IntVersion().toString());
        Assert.assertEquals("1", new IntVersion(1).toString());
        Assert.assertEquals("1", new IntVersion(1, 0).toString());
        // Validate padding
        Assert.assertEquals("0.0.0", new IntVersion().toString(3));
        Assert.assertEquals("1.0.0", new IntVersion(1).toString(3));
        Assert.assertEquals("1:1:0", new IntVersion(1, 1).toString(3, ":"));
        // Validate truncation
        Assert.assertEquals("1_2", new IntVersion(1, 2, 3).toString(2, "_"));
    }

    @Test
    public void segment() {
        IntVersion version = new IntVersion(1, 2, 3, 4, 5);
        for (int i = 0; i < 5; ++i) {
            Assert.assertEquals(i + 1, version.segment(i));
        }
        // Should return 0, rather than throw ArrayIndexOutOfBoundsException
        Assert.assertEquals(0, version.segment(5));

        Assert.assertEquals(1, version.major());
        Assert.assertEquals(2, version.minor());
        Assert.assertEquals(3, version.micro());

        Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> version.segment(-1));
    }

    @Test
    public void hash() {
        Assert.assertEquals(1, new IntVersion().hashCode());
        Assert.assertEquals(1, new IntVersion(0, 0, 0).hashCode());
        Assert.assertEquals(new IntVersion(1).hashCode(), new IntVersion(1, 0, 0).hashCode());
        Assert.assertNotEquals(new IntVersion(1).hashCode(), new IntVersion(1, 0, 1).hashCode());
        Assert.assertNotEquals(new IntVersion(1).hashCode(), new IntVersion(0, 1).hashCode());
        Assert.assertNotEquals(new IntVersion(1).hashCode(), new IntVersion(2).hashCode());
    }
}
