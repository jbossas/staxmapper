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
import javax.xml.stream.XMLStreamWriter;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public interface XMLExtendedStreamWriter extends XMLStreamWriter {
    void writeAttribute(String localName, String[] values) throws XMLStreamException;

    void writeAttribute(String prefix, String namespaceURI, String localName, String[] values) throws XMLStreamException;

    void writeAttribute(String namespaceURI, String localName, String[] values) throws XMLStreamException;

    void writeAttribute(String localName, Iterable<String> value) throws XMLStreamException;

    void writeAttribute(String prefix, String namespaceURI, String localName, Iterable<String> value) throws XMLStreamException;

    void writeAttribute(String namespaceURI, String localName, Iterable<String> value) throws XMLStreamException;

    /**
     * Sets the namespace to use for child element writes when a namespace is not specified.
     * In other words, when {@link #writeStartElement(String)} is called this namespace
     * will be used. Setting this to null will result in the standard behavior, which is
     * usage of the xml default ns (not specifying a prefix)
     *
     * This setting is scoped within the document or element that it was called in. Once
     * called all further child elements will use this namespace. In addition, it is
     * inherited for all levels of nesting. In other words, setting an unspecified namespace
     * on a grandchild will not affect the namespace setting of a child sibling.
     *
     * To clear the effect of this setting, call the method with a value of null.
     *
     * @param namespace The namespace to use when not specified on elements, or null
     *                  if the xml default ns should be used
     */
    void setUnspecifiedElementNamespace(String namespace);
}
