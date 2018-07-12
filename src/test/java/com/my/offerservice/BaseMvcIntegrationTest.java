package com.my.offerservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles(profiles = "test")
public abstract class BaseMvcIntegrationTest extends BaseSpringIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;
}
