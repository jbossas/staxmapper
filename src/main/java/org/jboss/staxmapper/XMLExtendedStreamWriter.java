/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
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
