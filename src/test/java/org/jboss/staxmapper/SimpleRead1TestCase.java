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

import static javax.xml.stream.XMLStreamConstants.COMMENT;
import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

import java.io.StringReader;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import org.junit.Test;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public final class SimpleRead1TestCase implements XMLElementReader<Object> {

    public static void main(String[] args) throws XMLStreamException {
        new SimpleRead1TestCase().testReadContent();
    }

    @Test
    public void testReadContent() throws XMLStreamException {
        final XMLMapper mapper = XMLMapper.Factory.create();
        mapper.registerRootElement(new QName("urn:test:one", "root"), this);
        mapper.parseDocument(Boolean.TRUE, XMLInputFactory.newInstance().createXMLStreamReader(new StringReader(
                "<root xmlns=\"urn:test:one\">\n" +
                "    <!-- Comment! -->\n" +
                "    <root xmlns=\"urn:test:one\"/>\n" +
                "    <root xmlns=\"urn:test:one\"/>\n" +
                "    <root xmlns=\"urn:test:one\"/>\n" +
                "    <test>    blah blah blah    </test>" +
                "    <test>    blah1 blah1 blah1    </test>" +
                "</root>\n\n"
        )));
    }

    public void readElement(final XMLExtendedStreamReader reader, final Object value) throws XMLStreamException {
        System.out.println("Got my element at " + reader.getLocation());
        while (reader.hasNext()) {
            switch (reader.next()) {
                case COMMENT:
                    System.out.println("Got comment: " + reader.getText());
                    break;
                case END_ELEMENT:
                    return;
                case START_ELEMENT:
                    if ("test".equals(reader.getLocalName())) {
                        System.out.println("Element text: [" + reader.getElementText() + "]");
                        reader.setTrimElementText(false);
                        break;
                    }
                    reader.handleAny(value);
                    break;
            }
        }
    }
}
