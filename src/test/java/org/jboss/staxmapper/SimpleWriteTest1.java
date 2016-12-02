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

import java.io.StringWriter;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import org.junit.Test;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
@SuppressWarnings( "deprecation" )
public final class SimpleWriteTest1 implements XMLContentWriter {

    public static void main(String[] args) throws XMLStreamException {
        new SimpleWriteTest1().testWriteContent();
    }

    @Test
    public void testWriteContent() throws XMLStreamException {
        final StringWriter writer = new StringWriter(512);
        final XMLMapper mapper = XMLMapper.Factory.create();
        mapper.deparseDocument(this, XMLOutputFactory.newInstance().createXMLStreamWriter(writer));
        System.out.println("Output: " + writer.getBuffer().toString());
    }

    @Override
    public void writeContent(final XMLExtendedStreamWriter streamWriter) throws XMLStreamException {
        streamWriter.writeStartDocument("UTF-8", "1.0");
        streamWriter.writeStartElement("hello");
        streamWriter.writeNamespace("foo", "http://foo");
        streamWriter.writeNamespace("bar", "http://bar");
        streamWriter.writeAttribute("test", "this out");
        streamWriter.setUnspecifiedElementNamespace("http://foo");
        streamWriter.writeStartElement("hello-two");
        streamWriter.writeAttribute("test2", "this out2");
        streamWriter.writeStartElement("helloblah");
        streamWriter.setUnspecifiedElementNamespace("http://bar");
        streamWriter.writeAttribute("test3", "this out3");
        streamWriter.writeCharacters(" test ");
        streamWriter.writeStartElement("helloblah2");
        streamWriter.writeAttribute("test4", "this out4");
        streamWriter.writeEndElement();
        streamWriter.writeEndElement();
        streamWriter.writeStartElement("inner");
        streamWriter.writeEndElement();
        streamWriter.writeEndElement();

        streamWriter.writeStartElement("actually-empty");
        streamWriter.setDefaultNamespace("http://blah");
        streamWriter.writeEndElement();

        streamWriter.setUnspecifiedElementNamespace(null);

        streamWriter.writeComment("this is a comment");
        streamWriter.writeComment("This is a comment\nthat spans multiple\nlines");
        streamWriter.writeEmptyElement("foo");
        streamWriter.writeCharacters("Some characters\n");
        streamWriter.writeCharacters("Some multi-\nline\ncharacters");
        streamWriter.writeEndElement();
        streamWriter.writeEndDocument();
        streamWriter.close();
    }
}
