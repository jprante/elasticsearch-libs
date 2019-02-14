module org.xbib.elasticsearch.netty {

    exports io.netty.bootstrap;
    exports io.netty.buffer;
    exports io.netty.channel;
    exports io.netty.channel.embedded;
    exports io.netty.channel.epoll;
    exports io.netty.channel.group;
    exports io.netty.channel.local;
    exports io.netty.channel.nio;
    exports io.netty.channel.oio;
    exports io.netty.channel.pool;
    exports io.netty.channel.socket;
    exports io.netty.channel.socket.nio;
    exports io.netty.channel.socket.oio;
    exports io.netty.channel.unix;
    exports io.netty.handler.codec;
    exports io.netty.handler.codec.base64;
    exports io.netty.handler.codec.bytes;
    exports io.netty.handler.codec.compression;
    exports io.netty.handler.codec.http;
    exports io.netty.handler.codec.http.cookie;
    exports io.netty.handler.codec.http.cors;
    exports io.netty.handler.codec.http.multipart;
    exports io.netty.handler.codec.http.websocketx;
    exports io.netty.handler.codec.json;
    exports io.netty.handler.codec.marshalling;
    exports io.netty.handler.codec.protobuf;
    exports io.netty.handler.codec.rtsp;
    exports io.netty.handler.codec.serialization;
    exports io.netty.handler.codec.spdy;
    exports io.netty.handler.codec.string;
    exports io.netty.handler.codec.xml;
    exports io.netty.handler.flow;
    exports io.netty.handler.flush;
    exports io.netty.handler.ipfilter;
    exports io.netty.handler.logging;
    exports io.netty.handler.ssl;
    exports io.netty.handler.stream;
    exports io.netty.handler.timeout;
    exports io.netty.handler.traffic;
    exports io.netty.resolver;
    exports io.netty.util;
    exports io.netty.util.collection;
    exports io.netty.util.concurrent;

    requires jdk.unsupported;
    requires java.management;
    requires java.logging;

    requires org.xbib.elasticsearch.log4j;

    requires static jzlib;
}