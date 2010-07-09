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

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
final class FixedXMLStreamReader implements XMLStreamReader {

    private final XMLStreamReader delegate;

    FixedXMLStreamReader(final XMLStreamReader delegate) {
        this.delegate = delegate;
    }

    public Object getProperty(final String name) throws IllegalArgumentException {
        return delegate.getProperty(name);
    }

    public int next() throws XMLStreamException {
        throw new UnsupportedOperationException();
    }

    public void require(final int type, final String namespaceURI, final String localName) throws XMLStreamException {
        delegate.require(type, namespaceURI, localName);
    }

    public String getElementText() throws XMLStreamException {
        return delegate.getElementText();
    }

    public int nextTag() throws XMLStreamException {
        throw new UnsupportedOperationException();
    }

    public boolean hasNext() throws XMLStreamException {
        return delegate.hasNext();
    }

    public void close() throws XMLStreamException {
        throw new UnsupportedOperationException();
    }

    public String getNamespaceURI(final String prefix) {
        return delegate.getNamespaceURI(prefix);
    }

    public boolean isStartElement() {
        return delegate.isStartElement();
    }

    public boolean isEndElement() {
        return delegate.isEndElement();
    }

    public boolean isCharacters() {
        return delegate.isCharacters();
    }

    public boolean isWhiteSpace() {
        return delegate.isWhiteSpace();
    }

    public String getAttributeValue(final String namespaceURI, final String localName) {
        return delegate.getAttributeValue(namespaceURI, localName);
    }

    public int getAttributeCount() {
        return delegate.getAttributeCount();
    }

    public QName getAttributeName(final int index) {
        return delegate.getAttributeName(index);
    }

    public String getAttributeNamespace(final int index) {
        return delegate.getAttributeNamespace(index);
    }

    public String getAttributeLocalName(final int index) {
        return delegate.getAttributeLocalName(index);
    }

    public String getAttributePrefix(final int index) {
        return delegate.getAttributePrefix(index);
    }

    public String getAttributeType(final int index) {
        return delegate.getAttributeType(index);
    }

    public String getAttributeValue(final int index) {
        return delegate.getAttributeValue(index);
    }

    public boolean isAttributeSpecified(final int index) {
        return delegate.isAttributeSpecified(index);
    }

    public int getNamespaceCount() {
        return delegate.getNamespaceCount();
    }

    public String getNamespacePrefix(final int index) {
        return delegate.getNamespacePrefix(index);
    }

    public String getNamespaceURI(final int index) {
        return delegate.getNamespaceURI(index);
    }

    public NamespaceContext getNamespaceContext() {
        return delegate.getNamespaceContext();
    }

    public int getEventType() {
        return delegate.getEventType();
    }

    public String getText() {
        return delegate.getText();
    }

    public char[] getTextCharacters() {
        return delegate.getTextCharacters();
    }

    public int getTextCharacters(final int sourceStart, final char[] target, final int targetStart, final int length) throws XMLStreamException {
        return delegate.getTextCharacters(sourceStart, target, targetStart, length);
    }

    public int getTextStart() {
        return delegate.getTextStart();
    }

    public int getTextLength() {
        return delegate.getTextLength();
    }

    public String getEncoding() {
        return delegate.getEncoding();
    }

    public boolean hasText() {
        return delegate.hasText();
    }

    public Location getLocation() {
        return delegate.getLocation();
    }

    public QName getName() {
        return delegate.getName();
    }

    public String getLocalName() {
        return delegate.getLocalName();
    }

    public boolean hasName() {
        return delegate.hasName();
    }

    public String getNamespaceURI() {
        return delegate.getNamespaceURI();
    }

    public String getPrefix() {
        return delegate.getPrefix();
    }

    public String getVersion() {
        return delegate.getVersion();
    }

    public boolean isStandalone() {
        return delegate.isStandalone();
    }

    public boolean standaloneSet() {
        return delegate.standaloneSet();
    }

    public String getCharacterEncodingScheme() {
        return delegate.getCharacterEncodingScheme();
    }

    public String getPITarget() {
        return delegate.getPITarget();
    }

    public String getPIData() {
        return delegate.getPIData();
    }
}
