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

import static javax.xml.stream.XMLStreamConstants.END_DOCUMENT;
import static javax.xml.stream.XMLStreamConstants.START_DOCUMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
final class XMLMapperImpl implements XMLMapper {
    private final ConcurrentMap<QName, XMLElementReader<?>> rootElements = new ConcurrentHashMap<>();
    private final ConcurrentMap<QName, Supplier<? extends XMLElementReader<?>>> rootElementsSuppliers = new ConcurrentHashMap<>();
    private final ConcurrentMap<QName, XMLAttributeReader<?>> rootAttributes = new ConcurrentHashMap<>();

    public <T> void registerRootElement(QName name, XMLElementReader<T> reader) {
        registerRootElement(name, () -> reader);
    }

    public <T> void registerRootElement(QName name, Supplier<XMLElementReader<T>> supplier) {
        if (rootElementsSuppliers.putIfAbsent(name, supplier) != null) {
            throw new IllegalArgumentException("Root element supplier for " + name + " already registered");
        }
    }

    @Override
    public void unregisterRootElement(QName name) {
        rootElements.remove(name);
    }

    public void registerRootAttribute(QName name, XMLAttributeReader<?> reader) {
        if (rootAttributes.putIfAbsent(name, reader) != null) {
            throw new IllegalArgumentException("Root attribute for " + name + " already registered");
        }
    }

    @Override
    public void unregisterRootAttribute(QName name) {
        rootAttributes.remove(name);
    }

    public void parseDocument(Object rootObject, XMLStreamReader reader) throws XMLStreamException {
        try {
            reader.require(START_DOCUMENT, null, null);
            reader.nextTag();
            reader.require(START_ELEMENT, null, null);
            processNested(new XMLExtendedStreamReaderImpl(this, reader), rootObject);
            while (reader.next() != END_DOCUMENT) {
            }
            reader.close();
            rootElements.clear(); //clear the parsers cache
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
                // log it?
            }
        }
    }

    public void deparseDocument(final XMLElementWriter<?> writer, final Object rootObject, final XMLStreamWriter streamWriter) throws XMLStreamException {
        doDeparse(writer, rootObject, new FormattingXMLStreamWriter(streamWriter));
    }

    @SuppressWarnings( { "unchecked" })
    private <T> void doDeparse(XMLElementWriter<?> writer, final T value, final XMLExtendedStreamWriter streamWriter) throws XMLStreamException {
        ((XMLElementWriter<T>)writer).writeContent(streamWriter, value);
    }

    /**
     * Format the content writer's output on to an XML stream writer.
     *
     * @param contentWriter the content writer
     * @param streamWriter the stream writer
     * @throws XMLStreamException if an exception occurs
     */
    public void deparseDocument(XMLContentWriter contentWriter, XMLStreamWriter streamWriter) throws XMLStreamException {
        // todo: add validation based on the registered root elements?
        contentWriter.writeContent(new FormattingXMLStreamWriter(streamWriter));
    }

    private XMLElementReader<?> getParser(final QName name) {
        return rootElements.computeIfAbsent(name, qName -> rootElementsSuppliers.getOrDefault(name, () -> null).get());
    }

    @SuppressWarnings({"unchecked"})
    <T> void processNested(final XMLExtendedStreamReader streamReader, final T value) throws XMLStreamException {
        final QName name = streamReader.getName();
        final XMLElementReader<T> reader = (XMLElementReader<T>) getParser(name);
        if (reader == null) {
            throw new XMLStreamException("Unexpected element '" + name + "'", streamReader.getLocation());
        }
        reader.readElement(streamReader, value);
    }

    @SuppressWarnings({ "unchecked" })
    <T> void processAttribute(final XMLStreamReader streamReader, final int index, final T value) throws XMLStreamException {
        final QName name = streamReader.getName();
        final XMLAttributeReader<T> reader = (XMLAttributeReader<T>) rootAttributes.get(name);
        if (reader == null) {
            throw new XMLStreamException("Unexpected attribute '" + name + "'", streamReader.getLocation());
        }
        reader.readAttribute(streamReader, index, value);
    }
}
