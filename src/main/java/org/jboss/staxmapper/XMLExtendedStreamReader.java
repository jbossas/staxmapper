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

import java.util.List;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * An XML stream reader that can read nested {@code <xs:any>} content using a registered set of root elements.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public interface XMLExtendedStreamReader extends XMLStreamReader {
    /**
     * Handle an {@code <xs:any>}-type nested element, passing in the given value, returning after the end of the element.
     * Must be positioned on a {@code START_ELEMENT} or an exception will occur.  On return the cursor will be positioned
     * on the corresponding {@code END_ELEMENT}.
     *
     * @param value the value to pass in
     * @throws XMLStreamException if an error occurs (e.g. the given value
     * does not match the type of the handler for the element, or the element is
     * unknown)
     */
    void handleAny(Object value) throws XMLStreamException;

    /**
     * Handle an extended attribute, passing in the given value.
     * Must be positioned on a {@code START_ELEMENT} or an exception will occur.
     * On return the cursor will be pointing at the same {@code START_ELEMENT}.
     *
     * @param value the value to pass in
     * @param index the index of the attribute to process
     * @throws XMLStreamException if an error occurs
     */
    void handleAttribute(Object value, int index) throws XMLStreamException;

    /**
     * Discard the remaining content of an element.  Runs until a {@code END_ELEMENT} is
     * encountered.  If a {@code START_ELEMENT} is encountered, then recursively consume and ignore
     * its content as well.
     *
     * @throws XMLStreamException if an error occurs.
     */
    void discardRemainder() throws XMLStreamException;

    /**
     * Get the value of an attribute as an integer.
     *
     * @param index the index of the attribute
     * @return the integer value
     * @throws XMLStreamException if an error occurs
     */
    int getIntAttributeValue(int index) throws XMLStreamException;

    /**
     * Get the value of an attribute as an integer list.
     *
     * @param index the index of the attribute
     * @return the integer values
     * @throws XMLStreamException if an error occurs
     */
    int[] getIntListAttributeValue(int index) throws XMLStreamException;

    /**
     * Get the value of an attribute as a space-delimited string list.
     *
     * @param index the index of the attribute
     * @return the values
     * @throws XMLStreamException if an error occurs
     */
    List<String> getListAttributeValue(int index) throws XMLStreamException;

    /**
     * Get the value of an attribute as a long.
     *
     * @param index the index of the attribute
     * @return the long value
     * @throws XMLStreamException if an error occurs
     */
    long getLongAttributeValue(int index) throws XMLStreamException;

    /**
     * Get the value of an attribute as a long integer list.
     *
     * @param index the index of the attribute
     * @return the long values
     * @throws XMLStreamException if an error occurs
     */
    long[] getLongListAttributeValue(int index) throws XMLStreamException;

    /**
     * Get the attribute value using intelligent type conversion.  Numeric types
     * will be parsed; enum types will be mapped.
     *
     * @param index the index of the attribute
     * @param kind the class of the expected object
     * @param <T> the type of the expected object
     * @return the object equivalent
     * @throws XMLStreamException if an error occurs
     */
    <T> T getAttributeValue(int index, Class<T> kind) throws XMLStreamException;

    /**
     * Get the attribute value as a list using intelligent type conversion.  Numeric types
     * will be parsed; enum types will be mapped.
     *
     * @param index the index of the attribute
     * @param kind the class of the expected object
     * @return the list of object equivalents
     * @throws XMLStreamException if an error occurs
     */
    <T> List<? extends T> getListAttributeValue(int index, Class<T> kind) throws XMLStreamException;

    /**
     * Get the XML ID attribute, if any.
     *
     * @return the attribute value
     * @throws XMLStreamException if an error occurs
     */
    String getId() throws XMLStreamException;

    /**
     * Gets the {@link XMLMapper} used to handle
     * {@link #handleAttribute(Object, int) extended attributes} and
     * {@link #handleAny(Object) xs:any-type nested elements}.
     *
     * @return the XMLMapper. Will not return {@code null}
     */
    XMLMapper getXMLMapper();

    /**
     * Whether or not {@link #getElementText} should trim content.
     * The default is true.
     *
     * @param trim trim if true, don't if false
     */
    void setTrimElementText(boolean trim);
}
