package com.licenta.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SportMapperTest {

    private SportMapper sportMapper;

    @BeforeEach
    public void setUp() {
        sportMapper = new SportMapperImpl();
    }
}
