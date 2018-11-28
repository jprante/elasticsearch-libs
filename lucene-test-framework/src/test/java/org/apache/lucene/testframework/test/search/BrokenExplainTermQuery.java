package org.apache.lucene.testframework.test.search;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.Weight;

import java.io.IOException;

public final class BrokenExplainTermQuery extends TermQuery {
    public final boolean toggleExplainMatch;
    public final boolean breakExplainScores;
    public BrokenExplainTermQuery(Term t, boolean toggleExplainMatch, boolean breakExplainScores) {
      super(t);
      this.toggleExplainMatch = toggleExplainMatch;
      this.breakExplainScores = breakExplainScores;
    }
    public Weight createWeight(IndexSearcher searcher, boolean needsScores, float boost) throws IOException {
      return new BrokenExplainWeight(this, super.createWeight(searcher,needsScores, boost));
    }
  }
  