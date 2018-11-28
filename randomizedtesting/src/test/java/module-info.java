module org.xbib.elasticsearch.randomizedtesting.test {

    exports com.carrotsearch.randomizedtesting.test;
    exports com.carrotsearch.randomizedtesting.test.contracts;
    exports com.carrotsearch.randomizedtesting.test.generators;
    exports com.carrotsearch.randomizedtesting.test.inheritance;
    exports com.carrotsearch.randomizedtesting.test.rules;
    exports com.carrotsearch.randomizedtesting.test.snippets;
    exports com.carrotsearch.randomizedtesting.test.timeout;

    opens com.carrotsearch.randomizedtesting.test.rules;

    requires org.xbib.elasticsearch.randomizedtesting;
    requires org.assertj.core;
    requires junit;
    requires java.logging;
    requires jdk.management;

}
