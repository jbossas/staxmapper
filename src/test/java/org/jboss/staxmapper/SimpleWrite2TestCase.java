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

import java.io.StringWriter;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import org.junit.Test;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public final class SimpleWrite2TestCase implements XMLElementWriter<Object> {

    public static void main(String[] args) throws XMLStreamException {
        new SimpleWrite2TestCase().testWriteContent();
    }

    @Test
    public void testWriteContent() throws XMLStreamException {
        final StringWriter writer = new StringWriter(512);
        final XMLMapper mapper = XMLMapper.Factory.create();
        mapper.deparseDocument(this, new Object(), XMLOutputFactory.newInstance().createXMLStreamWriter(writer));
        System.out.println("Output: " + writer.getBuffer().toString());
    }

    public void writeContent(final XMLExtendedStreamWriter streamWriter, final Object object) throws XMLStreamException {
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
