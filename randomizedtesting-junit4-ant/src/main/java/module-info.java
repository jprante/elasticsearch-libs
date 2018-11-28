module org.xbib.elasticsearch.randomizedtesting.junit.ant {

    exports com.carrotsearch.ant.tasks.junit4;
    exports com.carrotsearch.ant.tasks.junit4.balancers;
    exports com.carrotsearch.ant.tasks.junit4.events;
    exports com.carrotsearch.ant.tasks.junit4.events.aggregated;
    exports com.carrotsearch.ant.tasks.junit4.events.mirrors;
    exports com.carrotsearch.ant.tasks.junit4.gson.stream;
    exports com.carrotsearch.ant.tasks.junit4.listeners;
    exports com.carrotsearch.ant.tasks.junit4.listeners.antxml;
    exports com.carrotsearch.ant.tasks.junit4.listeners.json;
    exports com.carrotsearch.ant.tasks.junit4.runlisteners;
    exports com.carrotsearch.ant.tasks.junit4.slave;
    exports com.carrotsearch.ant.tasks.junit4.tools;

    opens com.carrotsearch.ant.tasks.junit4;

    requires java.xml;
    requires java.management;
    requires junit;
    requires hamcrest.all;
    requires simple.xml;
    requires org.objectweb.asm;
    requires org.xbib.elasticsearch.ant;
    requires org.xbib.elasticsearch.guava;
    requires org.xbib.elasticsearch.randomizedtesting;
}
