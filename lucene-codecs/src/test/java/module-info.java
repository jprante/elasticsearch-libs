module org.xbib.elasticsearch.lucene.codecs.test {

    exports org.apache.lucene.codecs.test.blockterms;
    exports org.apache.lucene.codecs.test.blocktreeords;
    exports org.apache.lucene.codecs.test.bloom;
    exports org.apache.lucene.codecs.test.memory;
    exports org.apache.lucene.codecs.test.simpletext;

    requires org.xbib.elasticsearch.lucene;
    requires org.xbib.elasticsearch.lucene.codecs;
    requires org.xbib.elasticsearch.lucene.testframework;

    uses org.apache.lucene.codecs.Codec;
    uses org.apache.lucene.codecs.DocValuesFormat;
    uses org.apache.lucene.codecs.PostingsFormat;

}