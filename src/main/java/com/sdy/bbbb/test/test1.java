package com.sdy.bbbb.test;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class test1 {

    private final Test2 test2;

    @GetMapping("/api/test")
    public void api () throws IOException {
        test2.call();
    }
}
