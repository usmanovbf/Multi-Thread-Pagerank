package com.github.usmanovbf;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import org.apache.commons.cli.*;

import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Hello world!
 */
public class Starter {


    private static final int MAX_PAGES_TO_FETCH = 50;
    private static Logger logger = Logger.getLogger(Starter.class.getName());

    public static void main(String[] args) {
        Options options = new Options();
        options.addOption(CliOptions.HOST.getShortOpt(), CliOptions.HOST.getLongOpt(), true, "host to crawl");
        CommandLineParser parser = new PosixParser();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            logger.log(Level.SEVERE, "Error while parsing " + CommandLineParser.class.getName(), e);

        }

        if (cmd.hasOption(CliOptions.HOST.getShortOpt())) {
            String crawlStorageFolder = "data";
            int numberOfCrawlers = 100;

            CrawlConfig config = new CrawlConfig();
            config.setCrawlStorageFolder(crawlStorageFolder);
            config.setResumableCrawling(false);
            config.setMaxPagesToFetch(MAX_PAGES_TO_FETCH);


        /*
         * Instantiate the controller for this crawl.
         */
            PageFetcher pageFetcher = new PageFetcher(config);
            RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
            RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
            CrawlController controller = null;
            try {
                controller = new CrawlController(config, pageFetcher, robotstxtServer);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error while initialising " + CrawlController.class.getName(), e);
            }

            controller.addSeed(cmd.getOptionValue(CliOptions.HOST.shortOpt));
            controller.setCustomData(new WebSite());
            controller.start(Crawler.class, numberOfCrawlers);
            controller.getFrontier().getNumberOfProcessedPages();
            WebSite webSite = (WebSite) controller.getCustomData();
            System.out.println();
        } else
            logger.log(Level.SEVERE, "Not specified necessary option " + CliOptions.HOST);
    }

    private enum CliOptions {
        HOST("h", "host");

        private final String shortOpt;
        private final String longOpt;

        CliOptions(String shortOpt, String longOpt) {
            this.shortOpt = shortOpt;
            this.longOpt = longOpt;
        }

        public String getShortOpt() {
            return shortOpt;
        }

        public String getLongOpt() {
            return longOpt;
        }

        @Override
        public String toString() {
            return "--" + longOpt;
        }
    }
}
