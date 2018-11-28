module org.xbib.elasticsearch.lucene.testframework.test {

    exports org.apache.lucene.testframework.test.analysis;
    exports org.apache.lucene.testframework.test.codecs.asserting;
    exports org.apache.lucene.testframework.test.codecs.compressing;
    exports org.apache.lucene.testframework.test.index;
    exports org.apache.lucene.testframework.test.mockfile;
    exports org.apache.lucene.testframework.test.search;
    exports org.apache.lucene.testframework.test.store;
    exports org.apache.lucene.testframework.test.util;

    requires org.xbib.elasticsearch.lucene;
    requires org.xbib.elasticsearch.lucene.testframework;
    requires org.xbib.elasticsearch.randomizedtesting;
    requires junit;
    requires hamcrest.all;
}