package org.apache.solr.core.contrib;

import org.apache.solr.handler.RequestHandlerBase;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.request.SolrQueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: mmattozzi
 * Date: Nov 30, 2010
 * Time: 10:26:55 PM
 */
public class DirectoryStatsHandler extends RequestHandlerBase {

    private static Logger logger = LoggerFactory.getLogger(DirectoryStatsHandler.class);

    @Override
    public void handleRequestBody(SolrQueryRequest req, SolrQueryResponse rsp) throws Exception {
        Boolean reset = req.getParams().getBool("reset");
        if (reset != null && reset.equals(Boolean.TRUE)) {
            TimedDirectoryFactory.totalBytesRead.set(0L);
            TimedDirectoryFactory.totalLatency.set(0L);
        }

        rsp.add("Total bytes read", TimedDirectoryFactory.totalBytesRead.get());
        rsp.add("Total latency ms", TimedDirectoryFactory.totalLatency.get());
        if (TimedDirectoryFactory.totalLatency.get() > 0) {
            rsp.add("MB/s", ( (float) TimedDirectoryFactory.totalBytesRead.get() / 1048576) /
                    ((float) TimedDirectoryFactory.totalLatency.get() * 0.001) ) ;
        }

        rsp.setHttpCaching(false);
    }

    @Override
    public String getDescription() {
        return "DirectoryStatsHandler";
    }

    @Override
    public String getSourceId() {
        return "DirectoryStatsHandler";
    }

    @Override
    public String getSource() {
        return "none";
    }

    @Override
    public String getVersion() {
        return "1";
    }
}
