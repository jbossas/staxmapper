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
import javax.xml.stream.XMLStreamReader;

/**
 * A reader which pulls an object information out of some XML attribute and appends it
 * to a provided object model.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 *
 * @param <T> the type that this reader can operate on
 */
public interface XMLAttributeReader<T> {

    /**
     * Parse an attribute.
     *
     * @param reader the stream reader
     * @param index the attribute index
     * @param value the value passed in
     * @throws javax.xml.stream.XMLStreamException if an error occurs
     */
    void readAttribute(XMLStreamReader reader, int index, T value) throws XMLStreamException;
}