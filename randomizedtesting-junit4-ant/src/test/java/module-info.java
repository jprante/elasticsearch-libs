module org.xbib.elasticsearch.randomizedtesting.junit.ant.test {

    exports com.carrotsearch.ant.tasks.junit4.test;
    exports com.carrotsearch.ant.tasks.junit4.test.events;
    exports com.carrotsearch.ant.tasks.junit4.test.it;
    exports com.carrotsearch.ant.tasks.junit4.test.listeners.antxml;
    exports com.carrotsearch.ant.tasks.junit4.test.runlisteners;
    exports com.carrotsearch.ant.tasks.junit4.test.spikes;

    exports com.carrotsearch.ant.tasks.junit4.test.donotexecute;
    exports com.carrotsearch.ant.tasks.junit4.test.donotexecute.bad;
    exports com.carrotsearch.ant.tasks.junit4.test.donotexecute.replication;
    exports com.carrotsearch.ant.tasks.junit4.test.donotexecute.sub1;
    exports com.carrotsearch.ant.tasks.junit4.test.donotexecute.sub2;

    opens com.carrotsearch.ant.tasks.junit4.test;
    opens com.carrotsearch.ant.tasks.junit4.test.events;
    opens com.carrotsearch.ant.tasks.junit4.test.it;
    opens com.carrotsearch.ant.tasks.junit4.test.listeners.antxml;
    opens com.carrotsearch.ant.tasks.junit4.test.runlisteners;
    opens com.carrotsearch.ant.tasks.junit4.test.spikes;

    opens com.carrotsearch.ant.tasks.junit4.test.donotexecute;
    opens com.carrotsearch.ant.tasks.junit4.test.donotexecute.bad;
    opens com.carrotsearch.ant.tasks.junit4.test.donotexecute.replication;
    opens com.carrotsearch.ant.tasks.junit4.test.donotexecute.sub1;
    opens com.carrotsearch.ant.tasks.junit4.test.donotexecute.sub2;

    requires java.xml;
    requires java.management;
    requires junit;
    requires hamcrest.all;
    requires simple.xml;
    requires org.objectweb.asm;
    requires org.xbib.elasticsearch.ant;
    requires org.xbib.elasticsearch.guava;
    requires org.xbib.elasticsearch.randomizedtesting;
    requires org.xbib.elasticsearch.randomizedtesting.junit.ant;
}