import org.apache.lucene.codecs.blocktreeords.BlockTreeOrdsPostingsFormat;
import org.apache.lucene.codecs.bloom.BloomFilteringPostingsFormat;
import org.apache.lucene.codecs.memory.DirectDocValuesFormat;
import org.apache.lucene.codecs.memory.DirectPostingsFormat;
import org.apache.lucene.codecs.memory.FSTOrdPostingsFormat;
import org.apache.lucene.codecs.memory.FSTPostingsFormat;
import org.apache.lucene.codecs.memory.MemoryDocValuesFormat;
import org.apache.lucene.codecs.memory.MemoryPostingsFormat;
import org.apache.lucene.codecs.simpletext.SimpleTextCodec;
import org.apache.lucene.codecs.simpletext.SimpleTextDocValuesFormat;
import org.apache.lucene.codecs.simpletext.SimpleTextPostingsFormat;

module org.xbib.elasticsearch.lucene.codecs {

    exports org.apache.lucene.codecs.blockterms;
    exports org.apache.lucene.codecs.blocktreeords;
    exports org.apache.lucene.codecs.bloom;
    exports org.apache.lucene.codecs.memory;
    exports org.apache.lucene.codecs.simpletext;

    requires org.xbib.elasticsearch.lucene;

    uses org.apache.lucene.codecs.Codec;
    uses org.apache.lucene.codecs.DocValuesFormat;
    uses org.apache.lucene.codecs.PostingsFormat;

    provides org.apache.lucene.codecs.Codec with
            SimpleTextCodec;

    provides org.apache.lucene.codecs.DocValuesFormat with
            DirectDocValuesFormat,
            MemoryDocValuesFormat,
            SimpleTextDocValuesFormat;

    provides org.apache.lucene.codecs.PostingsFormat with
            BlockTreeOrdsPostingsFormat,
            BloomFilteringPostingsFormat,
            DirectPostingsFormat,
            MemoryPostingsFormat,
            FSTOrdPostingsFormat,
            FSTPostingsFormat,
            SimpleTextPostingsFormat;
}