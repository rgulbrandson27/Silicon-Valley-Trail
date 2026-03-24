// SaveService.java in service package
package com.raina.siliconvalleytrail.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raina.siliconvalleytrail.model.GameSession;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PersistenceService {

    private static final String SAVE_DIR = "saves/";
    private final ObjectMapper objectMapper;

    public PersistenceService() {
        this.objectMapper = new ObjectMapper();
        File saveDir = new File(SAVE_DIR);
        if (!saveDir.exists()) {
            boolean created = saveDir.mkdirs();
            if (!created) {
                System.out.println("⚠️  Warning: could not create saves directory.");
            }
        }
    }

    // save game to file named after session
    public void saveGame(GameSession session) {
        try {
            String fileName = SAVE_DIR + session.getSessionName()
                    .replaceAll("\\s+", "_") + "_"
                    + session.getSessionId() + ".json";
            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(fileName), session);
            System.out.println("✅ Game saved as: " + fileName);
        } catch (Exception e) {
            System.out.println("❌ Save failed: " + e.getMessage());
        }
    }

    // list all saved games
    public List<String> listSaves() {
        List<String> saves = new ArrayList<>();
        File saveDir = new File(SAVE_DIR);
        if (saveDir.exists()) {
            File[] files = saveDir.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.getName().endsWith(".json")) {
                        saves.add(f.getName());
                    }
                }
            }
        }
        return saves;
    }

    // load a saved game by filename
    public GameSession loadGame(String fileName) {
        try {
            return objectMapper.readValue(
                    new File(SAVE_DIR + fileName), GameSession.class);
        } catch (Exception e) {
            System.out.println("❌ Load failed: " + e.getMessage());
            return null;
        }
    }
}