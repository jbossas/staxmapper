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

import java.util.Set;
import java.util.function.Function;
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
     * @param name the element name
     * @param reader the reader which handles the element
     */
    <T> void registerRootElement(QName name, XMLElementReader<T> reader);

    /**
     * Add a known root element which can be read by {@link XMLExtendedStreamReader#handleAny(Object)}.
     *
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
     * Convenience method that registers a root element associated with a known set of namespaces, whose reader can be created from the specified factory.
     * @param <T> the operating type of the reader
     * @param <N> the namespace type
     * @param localName the local name of the known root element
     * @param namespaces a set of known namespaces for the specified the root element
     * @param factory a factory for creating an element reader for the specified the root element
     */
    default <T, N extends Namespace> void registerRootElement(String localName, Set<N> namespaces, Function<N, XMLElementReader<T>> readerFactory) {
        for (N namespace : namespaces) {
            this.registerRootElement(new QName(namespace.getUri(), localName), () -> readerFactory.apply(namespace));
        }
    }

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
