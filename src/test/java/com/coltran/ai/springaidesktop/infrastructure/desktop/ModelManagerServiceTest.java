package com.coltran.ai.springaidesktop.infrastructure.desktop;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

public class ModelManagerServiceTest {
    

    private ModelManagerService modelManagerService;
    private MockRestServiceServer mockRestServiceServer;

    @BeforeEach
    void setUp() {
        RestClient.Builder restClientBuilder = RestClient.builder();
        this.mockRestServiceServer = MockRestServiceServer.bindTo(restClientBuilder).build();

        this.modelManagerService = new ModelManagerService(restClientBuilder, "11435");
    }

    @Test 
    @DisplayName("Should trigger model download if it's missing")
    void WhenModelMissing_triggerDownload() {
        mockRestServiceServer.expect(requestTo("http://127.0.0.1:11435/api/tags"))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess("{\"models\": [{\"name\":\"phi3:mini\"}]}", MediaType.APPLICATION_JSON));

        mockRestServiceServer.expect(requestTo("http://127.0.0.1:11435/api/pull"))
            .andExpect(method(HttpMethod.POST))
            .andExpect(jsonPath("$.name").value("llama3.2"))
            .andExpect(jsonPath("$.stream").value(false))
            .andRespond(withSuccess());

        modelManagerService.ensureModelIsDownloaded();

        mockRestServiceServer.verify();

    }


    @Test
    @DisplayName("Should do nothing if model exists")
    void whenModelExists_thenDoNothing() {

        mockRestServiceServer.expect(requestTo("http://127.0.0.1:11435/api/tags"))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess("{\"models\": [{\"name\":\"llama3.2\"}]}", MediaType.APPLICATION_JSON));

        modelManagerService.ensureModelIsDownloaded();
        mockRestServiceServer.verify();
    }

}
