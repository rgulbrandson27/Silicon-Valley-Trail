package com.raina.siliconvalleytrail.service;

import com.raina.siliconvalleytrail.model.DepartureDate;
import com.raina.siliconvalleytrail.model.GameSession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PersistenceServiceTest {

    private PersistenceService persistenceService;
    private GameSession session;

    @BeforeEach
    void setUp() {
        persistenceService = new PersistenceService();
        session = new GameSession(
                "test save", "Founder", "Eng1", "Eng2", "Eng3",
                DepartureDate.OCTOBER_28, 21, 10000
        );
        session.setCurrentLandmark("Lincoln");
        session.setSessionName("test save");
    }

    @AfterEach
    void cleanUp() {
        // delete any test save files after each test
        File saveDir = new File("saves/");
        if (saveDir.exists()) {
            for (File f : saveDir.listFiles()) {
                if (f.getName().contains("test_save")) {
                    f.delete();
                }
            }
        }
    }

    @Test
    void saveGame_shouldCreateSaveFile() {
        persistenceService.saveGame(session);
        List<String> saves = persistenceService.listSaves();
        assertFalse(saves.isEmpty(),
                "Save file should exist after saving");
    }

    @Test
    void loadGame_shouldRestoreSessionData() {
        persistenceService.saveGame(session);
        List<String> saves = persistenceService.listSaves();
        assertFalse(saves.isEmpty(), "Should have at least one save");

        GameSession loaded = persistenceService.loadGame(saves.get(0));
        assertNotNull(loaded, "Loaded session should not be null");
        assertEquals(session.getFounderName(), loaded.getFounderName(),
                "Founder name should match after load");
        assertEquals(session.getCash(), loaded.getCash(),
                "Cash should match after load");
        assertEquals(session.getCurrentLandmark(), loaded.getCurrentLandmark(),
                "Current landmark should match after load");
    }

    @Test
    void listSaves_shouldReturnOnlyJsonFiles() {
        persistenceService.saveGame(session);
        List<String> saves = persistenceService.listSaves();
        for (String save : saves) {
            assertTrue(save.endsWith(".json"),
                    "All saves should be .json files but found: " + save);
        }
    }
}