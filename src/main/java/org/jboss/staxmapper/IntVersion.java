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

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * An integer based version supporting a variable number of segments.
 * @author Paul Ferraro
 */
public final class IntVersion implements Comparable<IntVersion> {
    public static final CharSequence DEFAULT_DELIMITER = ".";

    private final int[] segments;

    /**
     * Constructs a version with a variable number of segments.
     * @param segments the version segments
     */
    public IntVersion(int... segments) {
        // Ignore trailing zero segments
        int length = segments.length;
        while ((length > 0) && (segments[length -1] == 0)) {
            length -= 1;
        }
        this.segments = Arrays.copyOf(segments, length);
    }

    /**
     * Returns the version segment at the specified index.
     * @param index a version segment index
     * @return the version segment at the specified index, or 0, if the index is out-of-bounds.
     * @throws ArrayIndexOutOfBoundsException if index is negative
     */
    public int segment(int index) {
        return (index < this.segments.length) ? this.segments[index] : 0;
    }

    /**
     * Returns the number of segments with which this version was constructed, excluding any trailing zeros.
     * @return the number of segments with which this version was constructed, excluding any trailing zeros.
     */
    public int segments() {
        return this.segments.length;
    }

    /**
     * Convenience method that returns the 1st segment of this version.
     * Equivalent to {@code this.segment(0)}.
     * @return the 1st segment of this version.
     */
    public int major() {
        return this.segment(0);
    }

    /**
     * Convenience method that returns the 2nd segment of this version.
     * Equivalent to {@code this.segment(1)}.
     * @return the 2nd segment of this version.
     */
    public int minor() {
        return this.segment(1);
    }

    /**
     * Convenience method that returns the 3rd segment of this version.
     * Equivalent to {@code this.segment(2)}.
     * @return the 3rd segment of this version.
     */
    public int micro() {
        return this.segment(2);
    }

    /**
     * Returns a stream of the segments of this version, excluding any trailing zeros.
     * Use {@link #stream(int)} to obtain a stream of a specific length.
     * @return a stream of version segments, excluding any trailing zeros.
     */
    public IntStream stream() {
        return IntStream.of(this.segments);
    }

    /**
     * Returns a stream of the segments of this version, zero padded (or truncated) to the specified number of segments.
     * @return a stream of version segments
     */
    public IntStream stream(int segments) {
        return (this.segments.length >= segments) ? IntStream.of(this.segments).limit(segments) : IntStream.range(0, segments).map(this::segment);
    }

    @Override
    public int compareTo(IntVersion version) {
        for (int i = 0; i < Math.max(this.segments.length, version.segments.length); ++i) {
            int result = Integer.compare(this.segment(i), version.segment(i));
            if (result != 0) return result;
        }
        return 0;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.segments);
    }

    @Override
    public boolean equals(Object object) {
        return (this == object) || (object instanceof IntVersion) && (this.compareTo((IntVersion) object) == 0);
    }

    /**
     * {@inheritDoc}
     * Equivalent to {@code this.toString(this.segments())}
     */
    @Override
    public String toString() {
        return this.toString(this.segments.length);
    }

    /**
     * Prints this version using the {@link #DEFAULT_DELIMITER} segment delimiter, zero padded (or truncated) to the specified number of segments.
     * Equivalent to {@code this.toString(segments, DEFAULT_DELIMITER)}
     * @param the number of segments with which to zero pad this version's string representation
     * @return a string representation of this version
     */
    public String toString(int segments) {
        return this.toString(segments, DEFAULT_DELIMITER);
    }

    /**
     * Prints this version using the specified segment delimiter, zero padded (or truncated) to the specified number of segments.
     * @param the number of segments with which to zero pad this version's string representation
     * @param a version segment delimiter
     * @return a string representation of this version
     */
    public String toString(int segments, CharSequence delimiter) {
        return this.stream(segments).mapToObj(String::valueOf).collect(Collectors.joining(delimiter));
    }
}
