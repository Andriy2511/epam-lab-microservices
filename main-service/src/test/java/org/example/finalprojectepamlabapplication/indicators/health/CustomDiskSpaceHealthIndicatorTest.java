package org.example.finalprojectepamlabapplication.indicators.health;

import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.health.Health;
import org.springframework.util.unit.DataSize;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class CustomDiskSpaceHealthIndicatorTest {

    @Test
    void testDoHealthCheck() throws Exception {
        Path tempDir = Files.createTempDirectory("testDir");

        Files.createTempFile(tempDir, "file1", ".txt");
        Files.createTempFile(tempDir, "file2", ".txt");

        File path = tempDir.toFile();
        DataSize threshold = DataSize.ofMegabytes(1);

        CustomDiskSpaceHealthIndicator indicator = new CustomDiskSpaceHealthIndicator(path, threshold);

        Health.Builder builder = new Health.Builder();

        indicator.doHealthCheck(builder);

        Health health = builder.build();

        assertTrue(health.getDetails().containsKey("usedPercentage"));
        assertTrue(health.getDetails().containsKey("usableSpace"));
        assertTrue(health.getDetails().containsKey("projectSize"));
        assertTrue(health.getDetails().containsKey("projectPercentage"));

        assertNotNull(health.getDetails().get("usedPercentage"));
        assertNotNull(health.getDetails().get("usableSpace"));
        assertNotNull(health.getDetails().get("projectSize"));
        assertNotNull(health.getDetails().get("projectPercentage"));

        Files.walk(tempDir)
                .sorted(Comparator.reverseOrder())
                .forEach(p -> p.toFile().delete());
    }
}

