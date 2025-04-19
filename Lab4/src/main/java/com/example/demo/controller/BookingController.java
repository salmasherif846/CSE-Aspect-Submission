package com.example.demo.controller;

import com.example.demo.annotation.Throttle;
import com.example.demo.annotation.RedisLock;
import com.example.demo.entity.Suite;
import com.example.demo.service.SuiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private SuiteService suiteService;

    @Throttle(maxCalls = 2, window = 10, unit = TimeUnit.SECONDS, cacheKeyPrefix = "allBookings")
    @GetMapping
    public ResponseEntity<List<Suite>> listSuites() {
        return ResponseEntity.ok(suiteService.fetchAllSuites());
    }

    @RedisLock(prefix = "suite-lock", key = "#suiteId", duration = 15, unit = TimeUnit.SECONDS)
    @GetMapping("/lock/{suiteId}")
    public ResponseEntity<String> lockAndProcess(@PathVariable Long suiteId) {
        return ResponseEntity.ok(suiteService.simulateSuiteProcessing(suiteId));
    }
}
