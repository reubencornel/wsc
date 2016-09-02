package com.sforce.ws.parser;

import com.google.appengine.repackaged.com.google.common.collect.ImmutableMap;

import javax.xml.stream.Location;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Map;

/**
 * Created by rcornel on 9/2/16.
 */
public class XmlPullParserImpl implements XmlPullParser {

    XMLInputFactory factory = XMLInputFactory.newFactory();
    Map<String, String> featureMapping = ImmutableMap.of("http://xmlpull.org/v1/doc/features.html#process-namespaces", "javax.xml.stream.isNamespaceAware");
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

    }

    @Override
    public void setInput(InputStream inputStream, String inputEncoding) throws XmlPullParserException {
        try {
            reader = factory.createXMLStreamReader(inputStream, inputEncoding);
        } catch (XMLStreamException e) {
            throw new XmlPullParserException(e.getMessage());
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
        return reader.getNamespaceURI(prefix);
    }

    @Override
    public int getDepth() {
        throw new UnsupportedOperationException();    }

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
        // Not implemented;
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
            int returnTag =reader.next();
            System.out.println("TAG(next): " + returnTag);
            return returnTag;
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
        try {
            reader.next();
            String returnStr = reader.getText();

            reader.next();
            return returnStr;
        } catch (XMLStreamException e) {
            throw new XmlPullParserException(e.getMessage());
        }

    }

    @Override
    public int nextTag() throws XmlPullParserException, IOException {
        try {
            int returnTag =reader.nextTag();
            System.out.println("TAG(nextTag): " + returnTag);
            return returnTag;
        } catch (XMLStreamException e) {
            throw new XmlPullParserException(e.getMessage());
        }
    }
}
