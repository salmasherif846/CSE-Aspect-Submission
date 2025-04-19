package com.example.demo.service;

import com.example.demo.annotation.RedisLock;
import com.example.demo.entity.Suite;
import com.example.demo.repo.SuiteRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Service
public class SuiteService {

    private static final Logger logger = LoggerFactory.getLogger(SuiteService.class);
    private static final String ALL_SUITES_CACHE_KEY = "suites:all";
    private static final Duration CACHE_DURATION = Duration.ofMinutes(10);

    @Autowired
    private SuiteRepository suiteRepository;

    @Autowired
    private RedisService redisService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Suite> fetchAllSuites() {
        try {
            String cachedData = redisService.fetch(ALL_SUITES_CACHE_KEY);
            if (cachedData != null) {
                logger.info(" Cache hit – loading suites from Redis.");
                return objectMapper.readValue(cachedData, new TypeReference<List<Suite>>() {});
            }
        } catch (Exception e) {
            logger.error(" Failed to deserialize cached suite data", e);
        }

        logger.info("🛠 Cache miss – querying database for suites.");
        List<Suite> suites = suiteRepository.findAll();

        try {
            String json = objectMapper.writeValueAsString(suites);
            redisService.put(ALL_SUITES_CACHE_KEY, json, CACHE_DURATION);
            logger.info(" Suites cached in Redis for {}", CACHE_DURATION);
        } catch (Exception e) {
            logger.error(" Failed to write suites to Redis cache", e);
        }

        return suites;
    }


    @RedisLock(prefix = "lock-suite", key = "#suiteId", duration = 10, unit = TimeUnit.SECONDS)
    public String simulateSuiteProcessing(Long suiteId) {
        try {
            System.out.println(" Processing suite ID: " + suiteId + " | Thread: " + Thread.currentThread().getName());
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return " Finished processing suite: " + suiteId;
    }
}
