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

import javax.xml.stream.XMLStreamException;

/**
 * A reader which pulls an object out of some XML element and appends it
 * to a provided object model.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 *
 * @param <T> the type that this reader can operate on
 */
public interface XMLElementReader<T> {

    /**
     * Parse an element and all of its nested content.
     *
     * @param reader the stream reader
     * @param value the value passed in
     * @throws XMLStreamException if an error occurs
     */
    void readElement(XMLExtendedStreamReader reader, T value) throws XMLStreamException;
}
