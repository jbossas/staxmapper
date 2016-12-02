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

import java.util.function.Supplier;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

/**
 * An XML mapper.  Allows the creation of extensible streaming XML parsers.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public interface XMLMapper {

    /**
     * Add a known root element which can be read by {@link XMLExtendedStreamReader#handleAny(Object)}.
     *
     * @param <T> the type of the expected object
     * @param name the element name
     * @param reader the reader which handles the element
     */
    <T> void registerRootElement(QName name, XMLElementReader<T> reader);

    /**
     * Add a known root element which can be read by {@link XMLExtendedStreamReader#handleAny(Object)}.
     *
     * @param <T> the type of the expected object
     * @param name the element name
     * @param supplier provider for the reader which handles the element
     *
     * It is recommended that supplier always creates new instance of the {@link XMLElementReader}
     * instead of caching and returning always same instance. This way unused parsers can get GC-ed
     * when not needed.
     *
     */
    <T> void registerRootElement(QName name, Supplier<XMLElementReader<T>> supplier);


    /**
     * Removes a {@link #registerRootElement(QName, XMLElementReader) previously registered root element}.
     *
     * @param name the element name
     */
    void unregisterRootElement(QName name);

    /**
     * Add a known root attribute which can be read by {@link XMLExtendedStreamReader#handleAttribute(Object, int)}.
     *
     * @param name the attribute name
     * @param reader the reader which handles the attribute
     */
    void registerRootAttribute(QName name, XMLAttributeReader<?> reader);

    /**
     * Removes a {@link #registerRootAttribute(QName, XMLAttributeReader) previously registered root attribute}.
     *
     * @param name the element name
     */
    void unregisterRootAttribute(QName name);

    /**
     * Parse a document.  The document must have a known, registered root element which can accept the given root object.
     *
     * @param rootObject the root object to send in
     * @param reader the reader from which the document should be read
     * @throws XMLStreamException if an error occurs
     */
    void parseDocument(Object rootObject, XMLStreamReader reader) throws XMLStreamException;

    /**
     * Format the element writer's output on to an XML stream writer.
     *
     * @param writer the element writer
     * @param rootObject the root object to send in
     * @param streamWriter the stream writer
     * @throws XMLStreamException if an exception occurs
     */
    void deparseDocument(XMLElementWriter<?> writer, Object rootObject, XMLStreamWriter streamWriter) throws XMLStreamException;

    /**
     * Format the content writer's output on to an XML stream writer.
     *
     * @param contentWriter the content writer
     * @param streamWriter the stream writer
     * @throws XMLStreamException if an exception occurs
     */
    @Deprecated
    void deparseDocument(XMLContentWriter contentWriter, XMLStreamWriter streamWriter) throws XMLStreamException;

    /**
     * A factory for creating an instance of {@link XMLMapper}.
     */
    class Factory {

        private Factory() {
        }

        /**
         * Create a new mapper instance.
         *
         * @return the new instance
         */
        public static XMLMapper create() {
            return new XMLMapperImpl();
        }
    }
}
