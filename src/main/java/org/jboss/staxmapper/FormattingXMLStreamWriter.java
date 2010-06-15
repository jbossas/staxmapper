/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
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

import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * An XML stream writer which nicely formats the XML for configuration files.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
final class FormattingXMLStreamWriter implements XMLStreamWriter {
    private final XMLStreamWriter delegate;
    private int level;
    private boolean afterStart;

    public FormattingXMLStreamWriter(final XMLStreamWriter delegate) {
        this.delegate = delegate;
    }

    private void indent() throws XMLStreamException {
        if (afterStart) {
            delegate.writeCharacters("\n");
        }
        int level = this.level;
        final XMLStreamWriter delegate = this.delegate;
        for (int i = 0; i < level; i ++) {
            delegate.writeCharacters("    ");
        }
    }

    public void writeStartElement(final String localName) throws XMLStreamException {
        indent();
        delegate.writeStartElement(localName);
        level++;
        afterStart = true;
    }

    public void writeStartElement(final String namespaceURI, final String localName) throws XMLStreamException {
        indent();
        delegate.writeStartElement(namespaceURI, localName);
        level++;
        afterStart = true;
    }

    public void writeStartElement(final String prefix, final String localName, final String namespaceURI) throws XMLStreamException {
        indent();
        delegate.writeStartElement(prefix, namespaceURI, localName);
        level++;
        afterStart = true;
    }

    public void writeEmptyElement(final String namespaceURI, final String localName) throws XMLStreamException {
        indent();
        delegate.writeEmptyElement(namespaceURI, localName);
        delegate.writeCharacters("\n");
    }

    public void writeEmptyElement(final String prefix, final String localName, final String namespaceURI) throws XMLStreamException {
        indent();
        delegate.writeEmptyElement(prefix, namespaceURI, localName);
        delegate.writeCharacters("\n");
    }

    public void writeEmptyElement(final String localName) throws XMLStreamException {
        indent();
        delegate.writeEmptyElement(localName);
        delegate.writeCharacters("\n");
    }

    public void writeEndElement() throws XMLStreamException {
        if (! afterStart) {
            indent();
        }
        delegate.writeEndElement();
        level--;
        delegate.writeCharacters("\n");
    }

    public void writeEndDocument() throws XMLStreamException {
        delegate.writeEndDocument();
    }

    public void close() throws XMLStreamException {
        delegate.close();
    }

    public void flush() throws XMLStreamException {
        delegate.flush();
    }

    public void writeAttribute(final String localName, final String value) throws XMLStreamException {
        delegate.writeAttribute(localName, value);
    }

    public void writeAttribute(final String prefix, final String namespaceURI, final String localName, final String value) throws XMLStreamException {
        delegate.writeAttribute(prefix, namespaceURI, localName, value);
    }

    public void writeAttribute(final String namespaceURI, final String localName, final String value) throws XMLStreamException {
        delegate.writeAttribute(namespaceURI, localName, value);
    }

    public void writeNamespace(final String prefix, final String namespaceURI) throws XMLStreamException {
        delegate.writeNamespace(prefix, namespaceURI);
    }

    public void writeDefaultNamespace(final String namespaceURI) throws XMLStreamException {
        delegate.writeDefaultNamespace(namespaceURI);
    }

    public void writeComment(final String data) throws XMLStreamException {
        indent();
        final StringBuilder b = new StringBuilder(data.length());
        int s = 0;
        int i = data.indexOf('\n') + 1;
        if (i != 0) {
            // Format multi-line comment
            final int level = this.level;
            do {
                b.append('\n');
                for (int q = 0; q < level; q ++) {
                    b.append("    ");
                }
                b.append("  ~ ");
                b.append(data.substring(s, i - 1));
                s = i;
                i = data.indexOf('\n', s) + 1;
            } while (i != 0);
            for (int q = 0; q < level; q ++) {
                b.append("      ");
            }
            delegate.writeComment(b.toString());
        } else {
            delegate.writeComment(" " + data + " ");
        }
    }

    public void writeProcessingInstruction(final String target) throws XMLStreamException {
        indent();
        delegate.writeProcessingInstruction(target);
    }

    public void writeProcessingInstruction(final String target, final String data) throws XMLStreamException {
        indent();
        delegate.writeProcessingInstruction(target, data);
    }

    public void writeCData(final String data) throws XMLStreamException {
        delegate.writeCData(data);
    }

    public void writeDTD(final String dtd) throws XMLStreamException {
        indent();
        delegate.writeDTD(dtd);
    }

    public void writeEntityRef(final String name) throws XMLStreamException {
    }

    public void writeStartDocument() throws XMLStreamException {
        delegate.writeStartDocument();
        delegate.writeCharacters("\n\n");
    }

    public void writeStartDocument(final String version) throws XMLStreamException {
        delegate.writeStartDocument(version);
    }

    public void writeStartDocument(final String encoding, final String version) throws XMLStreamException {
        delegate.writeStartDocument(encoding, version);
    }

    public void writeCharacters(final String text) throws XMLStreamException {
        delegate.writeCharacters(text);
    }

    public void writeCharacters(final char[] text, final int start, final int len) throws XMLStreamException {
        delegate.writeCharacters(text, start, len);
    }

    public String getPrefix(final String uri) throws XMLStreamException {
        return delegate.getPrefix(uri);
    }

    public void setPrefix(final String prefix, final String uri) throws XMLStreamException {
        delegate.setPrefix(prefix, uri);
    }

    public void setDefaultNamespace(final String uri) throws XMLStreamException {
        delegate.setDefaultNamespace(uri);
    }

    public void setNamespaceContext(final NamespaceContext context) throws XMLStreamException {
        delegate.setNamespaceContext(context);
    }

    public NamespaceContext getNamespaceContext() {
        return delegate.getNamespaceContext();
    }

    public Object getProperty(final String name) throws IllegalArgumentException {
        return delegate.getProperty(name);
    }
}
