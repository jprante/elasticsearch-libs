module org.xbib.elasticsearch.jackson {
    exports com.fasterxml.jackson.annotation;
    exports com.fasterxml.jackson.core;
    exports com.fasterxml.jackson.core.base;
    exports com.fasterxml.jackson.core.filter;
    exports com.fasterxml.jackson.core.io;
    exports com.fasterxml.jackson.core.json;
    exports com.fasterxml.jackson.core.type;
    exports com.fasterxml.jackson.core.util;
    exports com.fasterxml.jackson.databind;
    exports com.fasterxml.jackson.databind.annotation;
    exports com.fasterxml.jackson.databind.ext;
    exports com.fasterxml.jackson.databind.deser.std;
    exports com.fasterxml.jackson.databind.module;
    exports com.fasterxml.jackson.databind.node;
    exports com.fasterxml.jackson.databind.ser.std;
    exports com.fasterxml.jackson.databind.ser.impl;
    exports com.fasterxml.jackson.databind.util;
    exports com.fasterxml.jackson.dataformat.cbor;
    exports com.fasterxml.jackson.dataformat.smile;
    exports com.fasterxml.jackson.dataformat.yaml;
    // required by log4j2 core jackson
    exports com.fasterxml.jackson.dataformat.xml;
    exports com.fasterxml.jackson.dataformat.xml.annotation;
    exports com.fasterxml.jackson.dataformat.xml.util;

    provides com.fasterxml.jackson.core.JsonFactory with
            com.fasterxml.jackson.core.JsonFactory;

    provides com.fasterxml.jackson.core.ObjectCodec with
            com.fasterxml.jackson.databind.ObjectMapper;

    provides com.fasterxml.jackson.databind.Module with
            com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

    requires org.xbib.elasticsearch.snakeyaml;

    //requires static java.desktop; // we removed java.beans.Introspector from JaxbAnnotationInspector
    requires static java.xml;
    requires static java.sql; // for java.sql.Timestamp in com.fasterxml.jackson.databind.ser.std.StdJdkSerializers
    // XML is completely optional at runtime but required at compile time by log4j2
    requires static java.xml.bind;
    requires static org.codehaus.stax2;
}
