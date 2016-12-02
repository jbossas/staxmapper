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

    @Override
    public <T> void registerRootElement(QName name, XMLElementReader<T> reader) {
        registerRootElement(name, () -> reader);
    }

    @Override
    public <T> void registerRootElement(QName name, Supplier<XMLElementReader<T>> supplier) {
        if (rootElementsSuppliers.putIfAbsent(name, supplier) != null) {
            throw new IllegalArgumentException("Root element supplier for " + name + " already registered");
        }
    }

    @Override
    public void unregisterRootElement(QName name) {
        rootElements.remove(name);
    }

    @Override
    public void registerRootAttribute(QName name, XMLAttributeReader<?> reader) {
        if (rootAttributes.putIfAbsent(name, reader) != null) {
            throw new IllegalArgumentException("Root attribute for " + name + " already registered");
        }
    }

    @Override
    public void unregisterRootAttribute(QName name) {
        rootAttributes.remove(name);
    }

    @Override
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

    @Override
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
    @Override
    @SuppressWarnings( "deprecation" )
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
