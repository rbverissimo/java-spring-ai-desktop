package com.coltran.ai.springaidesktop.infrastructure.desktop;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.awt.Desktop;
import java.net.URI;

@Component
public class DesktopUIOrchestrator {

    @EventListener({ApplicationReadyEvent.class})
    public void launchBrowserWindow(){
        System.setProperty("java.awt.headless", "false");
        try {
            if(Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)){
                Desktop.getDesktop().browse(new URI("http://localhost:8080"));
                System.out.println("DESKTOP LAUNCHED SUCCESSFULLY!");
            }
        } catch (Exception e) {
            System.err.println("DESKTOP COULD NOT LAUNCH. ERROR: " + e.getMessage());
        }
    }
}
