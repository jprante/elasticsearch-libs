module org.xbib.randomizedtesting {

    exports com.carrotsearch.randomizedtesting;
    exports com.carrotsearch.randomizedtesting.annotations;
    exports com.carrotsearch.randomizedtesting.generators;
    exports com.carrotsearch.randomizedtesting.listeners;
    exports com.carrotsearch.randomizedtesting.rules;

    requires junit;
    requires java.management;
    requires java.logging;
}