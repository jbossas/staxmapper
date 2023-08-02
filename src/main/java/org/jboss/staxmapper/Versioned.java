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

import java.util.Comparator;

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
     * Returns the {@link Comparator} used by {@link #since(Versioned)}.
     * @return a version comparator
     */
    default Comparator<V> getComparator() {
        return Comparator.naturalOrder();
    }

    /**
     * Indicates whether the version of this object is greater than or equal to the version of the specified versioned object.
     * @param object a versioned object
     * @return true, if the version of this object is greater than or equal to the version of the specified versioned object, false otherwise.
     */
    default boolean since(T object) {
        return this.getComparator().compare(this.getVersion(), object.getVersion()) >= 0;
    }
}
