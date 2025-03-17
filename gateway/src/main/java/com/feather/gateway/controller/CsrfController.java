package com.feather.gateway.controller;

import org.springframework.http.ResponseEntity;

public interface CsrfController {

    ResponseEntity<Void> csrf();
}
