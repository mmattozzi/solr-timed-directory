package org.apache.solr.core.contrib;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.solr.core.DirectoryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;

/**
 * User: mmattozzi
 * Date: Nov 30, 2010
 * Time: 10:27:07 PM
 */
public class RAMDirectoryFactory extends DirectoryFactory {

    private static Logger logger = LoggerFactory.getLogger(RAMDirectoryFactory.class);

    @Override
    public Directory open(String path) throws IOException {
        return new RAMDirectory(NIOFSDirectory.open(new File(path)));
    }
}
