package org.apache.lucene.testframework.test.analysis;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.testframework.analysis.LookaheadTokenFilter;

import java.io.IOException;

public final class NeverPeeksLookaheadTokenFilter extends LookaheadTokenFilter<LookaheadTokenFilter.Position> {
    public NeverPeeksLookaheadTokenFilter(TokenStream input) {
        super(input);
    }

    @Override
    public Position newPosition() {
        return new Position();
    }

    @Override
    public final boolean incrementToken() throws IOException {
        return nextToken();
    }
}
