package org.example.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Wine management",
        description = "Endpoints for managing wine")
@RequiredArgsConstructor
@RestController
@RequestMapping("/wine")
public class WineController {
    // private final WineService wineService;
}
