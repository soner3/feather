package com.feather.gateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/csrf")
public class CsrfControllerImpl implements CsrfController {

    @GetMapping
    @Override
    public ResponseEntity<Void> csrf() {
        return ResponseEntity.noContent().build();
    }

}
