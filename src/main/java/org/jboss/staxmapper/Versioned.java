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

/**
 * A versioned object.
 * @author Paul Ferraro
 * <V> the version type
 * <N> the versioned object type
 */
public interface Versioned<V extends Comparable<V>, T extends Versioned<V, T>> {

    /**
     * Returns the version of this object.
     * @return the version of this object.
     */
    V getVersion();

    /**
     * Indicates whether the version of this object is greater than or equal to the version of the specified versioned object.
     * @param object a versioned object
     * @return true, if the version of this object is greater than or equal to the version of the specified versioned object, false otherwise.
     */
    default boolean since(T object) {
        return this.getVersion().compareTo(object.getVersion()) >= 0;
    }
}
