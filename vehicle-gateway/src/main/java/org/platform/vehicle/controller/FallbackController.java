package org.platform.vehicle.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author gejiawei
 * @Date 2020/10/30 11:51 上午
 */

@RestController
public class FallbackController {

    @GetMapping("/fallbackA")
    public ResponseEntity<String> fallbackA() {
        return new ResponseEntity<>("服务暂时不可用", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
