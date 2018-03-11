package com.ccbill.demo;

import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Stream;

/**
*
* @author Kris Galea
*
*/
public class TwitterMain {

    public static void main(String[] args) throws InterruptedException {
        //open a connection with twitter to begin filling a concurrent data structure BlockingQueue 
        BlockingQueue<SimpleTweet> tweetBlockingQueue = TwitterClientFactory.manufactureWithDefaults();

        //generate a per second ticker reactive stream 
        Flux<Long> tickerFlux = Flux.interval(Duration.ofSeconds(1));
        
        //generate a reactive stream from the concurrent data structure blocking queue 
        Flux<SimpleTweet> tweetFlux = Flux.fromIterable(tweetBlockingQueue);

        //use functional combination operator .zip to combine the streams of different speeds 
        Flux<Tuple2<SimpleTweet, Long>> fluxTuple = Flux.zip(tweetFlux, tickerFlux);
        
        //use functional operator 'map' to output a stream of values from the first element in the tuple outputted by zip
        Flux<SimpleTweet> slowSimpleTweet = fluxTuple.map(tuple -> tuple.getT1());

        //print the tweets after a delay of 5 seconds at intervals of 1 second
        slowSimpleTweet.delaySubscription(Duration.ofSeconds(5))
                .subscribe(simpleTweet -> System.out.println(simpleTweet));

        //keep the main thread running while Flux is doing its thing
        while(true) Thread.sleep(Integer.MAX_VALUE);
    }

}
