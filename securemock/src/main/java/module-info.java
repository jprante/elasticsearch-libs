module org.xbib.elasticsearch.securemock {

    exports org.mockito;
    exports org.mockito.codegen;
    exports org.mockito.configuration;
    exports org.mockito.creation.instance;
    exports org.mockito.hamcrest;
    exports org.mockito.invocation;
    exports org.mockito.junit;
    exports org.mockito.listeners;
    exports org.mockito.mock;
    exports org.mockito.plugins;
    exports org.mockito.quality;
    exports org.mockito.session;
    exports org.mockito.stubbing;
    exports org.mockito.verification;

    requires org.objectweb.asm;
    requires org.objectweb.asm.commons;
    requires org.objectweb.asm.tree;
    requires junit;
    requires java.instrument; //java.lang.instrument.Instrumentation in InlineByteBuddyMockMaker
    requires java.management; // java.lang.management in ByteBuddyAgent
    requires jdk.unsupported; // sun.reflect.ReflectionFactory in org.xbib.securemock.objenesis.instantiator.sun.SunReflectionFactoryHelper.getReflectionFactoryClass
}
