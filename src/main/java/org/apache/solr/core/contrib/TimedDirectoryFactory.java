package org.apache.solr.core.contrib;

import org.apache.lucene.store.*;
import org.apache.solr.core.DirectoryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * User: mmattozzi
 * Date: Nov 30, 2010
 * Time: 10:26:32 PM
 */
public class TimedDirectoryFactory extends DirectoryFactory {

    private static Logger logger = LoggerFactory.getLogger(TimedDirectoryFactory.class);

    protected static AtomicLong totalBytesRead = new AtomicLong(0L);
    protected static AtomicLong totalLatency = new AtomicLong(0L);

    @Override
    public Directory open(String path) throws IOException {
        return new TimedNIOFSDirectory(new File(path), null);
    }

    public static class TimedNIOFSDirectory extends NIOFSDirectory {

        public TimedNIOFSDirectory(File path, LockFactory lockFactory) throws IOException {
            super(path, lockFactory);
        }

        /** Creates an IndexInput for the file with the given name. */
        @Override
        public IndexInput openInput(String name, int bufferSize) throws IOException {
            ensureOpen();
            return new InstrumentedNIOFSIndexInput(new File(getFile(), name), bufferSize, getReadChunkSize());
        }

        protected static class InstrumentedNIOFSIndexInput extends NIOFSDirectory.NIOFSIndexInput {

            public InstrumentedNIOFSIndexInput(File path, int bufferSize, int chunkSize) throws IOException {
              super(path, bufferSize, chunkSize);
            }

            @Override
            protected void readInternal(byte[] b, int offset, int len) throws IOException {
                long startTime = System.currentTimeMillis();
                super.readInternal(b, offset, len);
                long totalTime = System.currentTimeMillis() - startTime;
                totalBytesRead.addAndGet(len);
                totalLatency.addAndGet(totalTime);
            }
        }
    }
}

