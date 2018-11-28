package org.apache.lucene.testframework.test.search;

import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.search.BulkScorer;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.FilterWeight;
import org.apache.lucene.search.Weight;

import java.io.IOException;

public final class BrokenExplainWeight extends FilterWeight {
    public BrokenExplainWeight(BrokenExplainTermQuery q, Weight in) {
      super(q, in);
    }
    public BulkScorer bulkScorer(LeafReaderContext context) throws IOException {
      return in.bulkScorer(context);
    }
    public Explanation explain(LeafReaderContext context, int doc) throws IOException {
      BrokenExplainTermQuery q = (BrokenExplainTermQuery) this.getQuery();
      Explanation result = in.explain(context, doc);
      if (result.isMatch()) {
        if (q.breakExplainScores) {
          result = Explanation.match(-1F * result.getValue(), "Broken Explanation Score", result);
        }
        if (q.toggleExplainMatch) {
          result = Explanation.noMatch("Broken Explanation Matching", result);
        }
      } else {
        if (q.toggleExplainMatch) {
          result = Explanation.match(-42.0F, "Broken Explanation Matching", result);
        }
      }
      return result;
    }
  }