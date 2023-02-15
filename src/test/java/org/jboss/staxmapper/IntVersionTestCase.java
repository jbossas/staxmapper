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
