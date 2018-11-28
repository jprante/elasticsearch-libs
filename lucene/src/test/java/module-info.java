module org.xbib.elasticsearch.lucene.test {

    exports org.apache.lucene.test.analysis;
    exports org.apache.lucene.test.analysis.ar;
    exports org.apache.lucene.test.analysis.br;
    exports org.apache.lucene.test.analysis.compound;
    exports org.apache.lucene.test.analysis.core;
    exports org.apache.lucene.test.analysis.cz;
    exports org.apache.lucene.test.analysis.da;
    exports org.apache.lucene.test.analysis.fi;
    exports org.apache.lucene.test.analysis.fr;
    exports org.apache.lucene.test.analysis.hunspell;
    exports org.apache.lucene.test.analysis.nl;
    exports org.apache.lucene.test.analysis.snowball;
    exports org.apache.lucene.test.analysis.tokenattributes;
    exports org.apache.lucene.test.analysis.util;

    opens org.apache.lucene.test.analysis.ar;
    opens org.apache.lucene.test.analysis.br;
    opens org.apache.lucene.test.analysis.compound;
    opens org.apache.lucene.test.analysis.core;
    opens org.apache.lucene.test.analysis.cz;
    opens org.apache.lucene.test.analysis.da;
    opens org.apache.lucene.test.analysis.fi;
    opens org.apache.lucene.test.analysis.fr;
    opens org.apache.lucene.test.analysis.hunspell;
    opens org.apache.lucene.test.analysis.nl;
    opens org.apache.lucene.test.analysis.snowball;
    opens org.apache.lucene.test.analysis.util;

    requires junit;
    requires java.logging;
    requires org.xbib.elasticsearch.lucene;
    requires org.xbib.elasticsearch.lucene.testframework;

}
