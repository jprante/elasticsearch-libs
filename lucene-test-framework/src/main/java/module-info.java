import org.apache.lucene.testframework.codecs.asserting.AssertingCodec;
import org.apache.lucene.testframework.codecs.asserting.AssertingDocValuesFormat;
import org.apache.lucene.testframework.codecs.asserting.AssertingPostingsFormat;
import org.apache.lucene.testframework.codecs.blockterms.LuceneFixedGap;
import org.apache.lucene.testframework.codecs.blockterms.LuceneVarGapDocFreqInterval;
import org.apache.lucene.testframework.codecs.blockterms.LuceneVarGapFixedInterval;
import org.apache.lucene.testframework.codecs.bloom.TestBloomFilteredLucenePostings;
import org.apache.lucene.testframework.codecs.cheapbastard.CheapBastardCodec;
import org.apache.lucene.testframework.codecs.compressing.FastCompressingCodec;
import org.apache.lucene.testframework.codecs.compressing.FastDecompressionCompressingCodec;
import org.apache.lucene.testframework.codecs.compressing.HighCompressionCompressingCodec;
import org.apache.lucene.testframework.codecs.compressing.dummy.DummyCompressingCodec;
import org.apache.lucene.testframework.codecs.mockrandom.MockRandomPostingsFormat;
import org.apache.lucene.testframework.codecs.ramonly.RAMOnlyPostingsFormat;

module org.xbib.elasticsearch.lucene.testframework {

    exports org.apache.lucene.testframework.analysis;
    exports org.apache.lucene.testframework.analysis.standard;
    exports org.apache.lucene.testframework.codecs;
    exports org.apache.lucene.testframework.codecs.asserting;
    exports org.apache.lucene.testframework.codecs.blockterms;
    exports org.apache.lucene.testframework.codecs.bloom;
    exports org.apache.lucene.testframework.codecs.cheapbastard;
    exports org.apache.lucene.testframework.codecs.compressing;
    exports org.apache.lucene.testframework.codecs.compressing.dummy;
    exports org.apache.lucene.testframework.codecs.cranky;
    exports org.apache.lucene.testframework.codecs.mockrandom;
    exports org.apache.lucene.testframework.codecs.ramonly;
    exports org.apache.lucene.testframework.geo;
    exports org.apache.lucene.testframework.index;
    exports org.apache.lucene.testframework.mockfile;
    exports org.apache.lucene.testframework.search;
    exports org.apache.lucene.testframework.search.similarities;
    exports org.apache.lucene.testframework.search.spans;
    exports org.apache.lucene.testframework.store;
    exports org.apache.lucene.testframework.util;
    exports org.apache.lucene.testframework.util.automaton;
    exports org.apache.lucene.testframework.util.fst;

    requires junit;
    requires org.xbib.elasticsearch.lucene;
    requires org.xbib.elasticsearch.lucene.codecs;
    requires org.xbib.elasticsearch.randomizedtesting;

    uses org.apache.lucene.codecs.Codec;
    uses org.apache.lucene.codecs.DocValuesFormat;
    uses org.apache.lucene.codecs.PostingsFormat;

    provides org.apache.lucene.codecs.Codec with
            AssertingCodec,
            CheapBastardCodec,
            FastCompressingCodec,
            FastDecompressionCompressingCodec,
            HighCompressionCompressingCodec,
            DummyCompressingCodec;

    provides org.apache.lucene.codecs.DocValuesFormat with
            AssertingDocValuesFormat;

    provides org.apache.lucene.codecs.PostingsFormat with
            MockRandomPostingsFormat,
            RAMOnlyPostingsFormat,
            LuceneFixedGap,
            LuceneVarGapFixedInterval,
            LuceneVarGapDocFreqInterval,
            TestBloomFilteredLucenePostings,
            AssertingPostingsFormat;
}