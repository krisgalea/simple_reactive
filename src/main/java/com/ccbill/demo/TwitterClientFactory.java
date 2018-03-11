package com.ccbill.demo;

import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.httpclient.BasicClient;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Builds a Twitter Client to query terms from the Twitter Stream.
 * see: https://developer.twitter.com/en/docs/tutorials/consuming-streaming-data
 *
 * @author Kris Galea
 */
public class TwitterClientFactory {

    private static final String CONSUMER_KEY        = "ADD_YOURS";
    private static final String CONSUMER_SECRET     = "ADD_YOURS";
    private static final String TOKEN               = "ADD_YOURS";
    private static final String SECRET              = "ADD_YOURS";

    private static final List<String> DEFAULT_TERMS = List.of("the");

    /**
     * Manufactures a {@link BasicClient} from credentials passed in param and opens a stream connection for
     * the search terms specified
     *
     * @param consumerKey       The consumer key of the target Twitter account
     * @param consumerSecret    The consumer secret of the target Twitter account
     * @param token             The token of the target Twitter account
     * @param secret            The secret of the target Twitter account
     * @param terms             A {@link List} of search terms
     * @return                  A concurrently accessible {@link BlockingQueue} of {@link SimpleTweet}
     */
    public static BlockingQueue<SimpleTweet> manufactureClientAndGetQueue(String consumerKey, String consumerSecret, String token, String secret, List<String> terms) {
        BlockingQueue<SimpleTweet> msgQueue = new LinkedBlockingQueue<SimpleTweet>(100000);

        /** Declare the host you want to connect to, the endpoint, and authentication (basic auth or oauth) */
        Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
        StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();

        // Add auth'n details
        Authentication hosebirdAuth = new OAuth1(consumerKey, consumerSecret, token, secret);

        // Add search terms
        hosebirdEndpoint.trackTerms(terms);

        // Generate client
        BasicClient basicClient = new ClientBuilder()
                .name("Demo-Client")
                .hosts(hosebirdHosts)
                .authentication(hosebirdAuth)
                .endpoint(hosebirdEndpoint)
                .processor(new CustomHosebirdProcessor(msgQueue))
                .build();

        //initiate connection
        basicClient.connect();

        return msgQueue;
    }

    /**
     * Uses statically defined credentials specified in this class to open a connection to Twitter
     *
     * @return      A concurrently accessible {@link BlockingQueue} of {@link SimpleTweet}
     */
    public static BlockingQueue<SimpleTweet> manufactureWithDefaults() {
        BlockingQueue<SimpleTweet> tweets = manufactureClientAndGetQueue(CONSUMER_KEY, CONSUMER_SECRET, TOKEN, SECRET, DEFAULT_TERMS);
        return tweets;
    }

}
