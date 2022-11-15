package com.sdy.bbbb.data;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DataController {

    private final DataService dataService;

    @GetMapping("/api/test")
    public void callApi() throws Exception {
        dataService.call();
    }
}
