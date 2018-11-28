package org.apache.lucene.testframework.test.store;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FilterDirectory;

// just shields the wrapped directory from being closed
class PreventCloseDirectoryWrapper extends FilterDirectory {
    public PreventCloseDirectoryWrapper(Directory in) {
        super(in);
    }

    @Override
    public void close() {
    }
}
