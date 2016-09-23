/*
 * Copyright (c) 2013, salesforce.com, inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided
 * that the following conditions are met:
 *
 *    Redistributions of source code must retain the above copyright notice, this list of conditions and the
 *    following disclaimer.
 *
 *    Redistributions in binary form must reproduce the above copyright notice, this list of conditions and
 *    the following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 *    Neither the name of salesforce.com, inc. nor the names of its contributors may be used to endorse or
 *    promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED
 * TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.sforce.ws.parser;

import com.sun.org.apache.xerces.internal.util.NamespaceContextWrapper;

import javax.xml.stream.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is a wrapper around XmlStreamReader. All most all calls are delegated to the stream reader.
 *
 * @author rcornel
 */
public class XmlPullParserImpl implements XmlPullParser {

    private static XMLInputFactory factory = null;
    static {
        factory = XMLInputFactory.newFactory();
        factory.setProperty("javax.xml.stream.isCoalescing", true);
        factory.setProperty("javax.xml.stream.isReplacingEntityReferences", false);
        factory.setProperty("javax.xml.stream.supportDTD", false);
    }

    Map<String, String> featureMapping = new HashMap<String, String>(){{
        put("http://xmlpull.org/v1/doc/features.html#process-namespaces", "javax.xml.stream.isNamespaceAware");
    }};

    XMLStreamReader reader = null;

    @Override
    public void setFeature(String name, boolean state) throws XmlPullParserException {

        String staxName = featureMapping.get(name);
        if (staxName == null) {
            throw new XmlPullParserException("Unrecognized feature");
        }
        factory.setProperty(staxName, state);
    }

    @Override
    public boolean getFeature(String name) {
         Object propertyValue = factory.getProperty(name);
        if (propertyValue instanceof Boolean) {
            return (Boolean)propertyValue;
        }
        return false;
    }

    @Override
    public void setProperty(String name, Object value) throws XmlPullParserException {
        factory.setProperty(name, value);
    }

    @Override
    public Object getProperty(String name) {
        return factory.getProperty(name);
    }

    @Override
    public void setInput(Reader in) throws XmlPullParserException {
        try {
            reader = factory.createXMLStreamReader(in);
        } catch (XMLStreamException e) {
            throw new XmlPullParserException(e.getMessage(), this, e);
        }
    }

    @Override
    public void setInput(InputStream inputStream, String inputEncoding) throws XmlPullParserException {
        try {
            reader = factory.createXMLStreamReader(inputStream, inputEncoding);
        } catch (XMLStreamException e) {
            throw new XmlPullParserException(e.getMessage(), this, e);
        }
    }

    @Override
    public String getInputEncoding() {
        return reader.getEncoding();
    }

    @Override
    public void defineEntityReplacementText(String entityName, String replacementText) throws XmlPullParserException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getNamespaceCount(int depth) throws XmlPullParserException {
        return reader.getNamespaceCount();
    }

    @Override
    public String getNamespacePrefix(int pos) throws XmlPullParserException {
        return reader.getNamespacePrefix(pos);
    }

    @Override
    public String getNamespaceUri(int pos) throws XmlPullParserException {
        return reader.getNamespaceURI(pos);
    }

    @Override
    public String getNamespace(String prefix) {
        if (prefix == null) {
            return reader.getNamespaceContext().getNamespaceURI("");
        }
        return reader.getNamespaceContext().getNamespaceURI(prefix);
    }

    @Override
    public int getDepth() {
        // Depth is not supported by xml stream reader
        throw new UnsupportedOperationException();
    }

    @Override
    public String getPositionDescription() {
        Location l = reader.getLocation();
        return l.getLineNumber() + " " + l.getColumnNumber();
    }

    @Override
    public int getLineNumber() {
        return reader.getLocation().getLineNumber();
    }

    @Override
    public int getColumnNumber() {
        return reader.getLocation().getColumnNumber();
    }

    @Override
    public boolean isWhitespace() throws XmlPullParserException {
        return reader.isWhiteSpace();
    }

    @Override
    public String getText() {
        String ret = reader.getText();
        try {
            reader.next();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public char[] getTextCharacters(int[] holderForStartAndLength) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getNamespace() {
        return reader.getNamespaceURI();
    }

    @Override
    public String getName() {
        return reader.getName().getLocalPart();
    }

    @Override
    public String getPrefix() {
        return reader.getPrefix();
    }

    @Override
    public boolean isEmptyElementTag() throws XmlPullParserException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getAttributeCount() {
        return reader.getAttributeCount();
    }

    @Override
    public String getAttributeNamespace(int index) {
        return reader.getAttributeNamespace(index);
    }

    @Override
    public String getAttributeName(int index) {
        return reader.getAttributeName(index).toString();
    }

    @Override
    public String getAttributePrefix(int index) {
        return reader.getAttributePrefix(index);
    }

    @Override
    public String getAttributeType(int index) {
        return reader.getAttributeType(index);
    }

    @Override
    public boolean isAttributeDefault(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getAttributeValue(int index) {
        return reader.getAttributeValue(index);
    }

    @Override
    public String getAttributeValue(String namespace, String name) {
        return reader.getAttributeValue(namespace, name);
    }

    @Override
    public int getEventType() throws XmlPullParserException {
        return reader.getEventType();
    }

    @Override
    public int next() throws XmlPullParserException, IOException {
        try {
            return reader.next();
        } catch (XMLStreamException e) {
            throw new XmlPullParserException(e.getMessage());
        }
    }

    @Override
    public int nextToken() throws XmlPullParserException, IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void require(int type, String namespace, String name) throws XmlPullParserException, IOException {
        try {
            reader.require(type, namespace, name);
        } catch (XMLStreamException e) {
            throw new XmlPullParserException(e.getMessage());
        }
    }

    @Override
    public String nextText() throws XmlPullParserException, IOException {
            if (getEventType() != XMLStreamConstants.START_ELEMENT) {
                throw new XmlPullParserException("parser must be on start tag");
            }
            int eventType = next();
            if (eventType == XMLStreamConstants.CDATA || eventType == XMLStreamConstants.CHARACTERS) {
                String result = reader.getText();
                eventType = next();
                if (eventType != XMLStreamConstants.END_ELEMENT) {
                    throw new XmlPullParserException("Event Text must be followed by end tag");
                }
                return result;
            } else if (eventType == XMLStreamConstants.END_ELEMENT) {
                return "";
            } else {
                throw new XmlPullParserException("Event must be on start tag to read text.");
            }
    }

    @Override
    public int nextTag() throws XmlPullParserException, IOException {
        int eventType = next();
        if(eventType == XMLStreamConstants.CHARACTERS &&  isWhitespace()) {   // skip whitespace
            eventType = next();
        }
        if (eventType != XMLStreamConstants.START_ELEMENT &&  eventType != XMLStreamConstants.END_ELEMENT) {
            throw new XmlPullParserException("expected start or end tag", this, null);
        }
        return eventType;
    }

    @Override
    public String toString(){
        int eventType = reader.getEventType();
        switch (eventType) {
            case XMLStreamConstants.START_ELEMENT : return " Start Element: " + reader.getName().toString() + " : " + getPositionDescription();
            case XMLStreamConstants.END_ELEMENT: return "End Element: " + reader.getName().toString( )+ ":" + getPositionDescription();
            case XMLStreamConstants.START_DOCUMENT: return "Document Start";
            case XMLStreamConstants.END_DOCUMENT: return "End Document";
            default: return "TEXT:" + reader.getText();
        }
    }
}
