package com.coltran.ai.springaidesktop.infrastructure.desktop;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.HashMap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.mock.env.MockEnvironment;

class OllamaOrchestratorTest {


    @Test
    @DisplayName("Should correctly start Ollama daemon based on environment variables on ApplicationEnvironmentPreparedEvent")
    void onApplicationEvent_shouldStartOllamaDaemonWithCorrectEnvironment() throws IOException { 

        MockEnvironment mockEnvironment = new MockEnvironment();
        mockEnvironment.setProperty("ai.engine.port", "9999");

        ApplicationEnvironmentPreparedEvent mockEvent = mock(ApplicationEnvironmentPreparedEvent.class);
        when(mockEvent.getEnvironment()).thenReturn(mockEnvironment);

        try(MockedConstruction<ProcessBuilder> mockedProcessBuilder = mockConstruction(ProcessBuilder.class, 
            (mock, context) -> {
                Process fakeProcess = mock(Process.class);
                when(mock.start()).thenReturn(fakeProcess);
                when(mock.environment()).thenReturn(new HashMap<>());
            })){    
                
            OllamaOrchestrator ollamaOrchestrator = new OllamaOrchestrator();
            ollamaOrchestrator.onApplicationEvent(mockEvent);

            assertEquals(1, mockedProcessBuilder.constructed().size(), "ProcessBuilder should have been constructed once.");
            ProcessBuilder constructedBuilder = mockedProcessBuilder.constructed().get(0);

            verify(constructedBuilder, times(1)).start();

            assertEquals("0.0.0.0:9999", constructedBuilder.environment().get("OLLAMA_HOST"));


        }
    }

    

}