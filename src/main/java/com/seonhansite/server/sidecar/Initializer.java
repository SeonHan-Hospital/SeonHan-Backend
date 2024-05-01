package com.seonhansite.server.sidecar;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Initializer {
    @PostConstruct
    public void init() {
        log.info("SeonHan Hospital Server Bean 초기화 완료");
    }
}
