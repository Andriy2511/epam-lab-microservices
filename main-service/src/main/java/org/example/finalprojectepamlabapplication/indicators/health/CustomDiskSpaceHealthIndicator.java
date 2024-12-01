package org.example.finalprojectepamlabapplication.indicators.health;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.system.DiskSpaceHealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataSize;

import java.io.File;

@Component
public class CustomDiskSpaceHealthIndicator extends DiskSpaceHealthIndicator {

    private final File path;

    public CustomDiskSpaceHealthIndicator(
            @Value("${health.diskspace.path:${user.dir}}") File path,
            @Value("${health.diskspace.threshold:500MB}") DataSize threshold) {
        super(path, threshold);
        this.path = path;
    }

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        super.doHealthCheck(builder);
        addCustomDetails(builder);
    }

    private void addCustomDetails(Health.Builder builder) {
        long totalSpace = path.getTotalSpace();
        long freeSpace = path.getFreeSpace();
        long usableSpace = path.getUsableSpace();

        double usedPercentage = ((double)(totalSpace - freeSpace) / totalSpace) * 100;

        long projectSize = calculateDirectorySize(path);
        double projectPercentage = ((double) projectSize / totalSpace) * 100;

        builder.withDetail("usedPercentage", String.format("%.2f%%", usedPercentage))
                .withDetail("usableSpace", DataSize.ofBytes(usableSpace).toMegabytes() + " MB")
                .withDetail("projectSize", formatDataSize(projectSize))
                .withDetail("projectPercentage", String.format("%.8f%%", projectPercentage));
    }

    private long calculateDirectorySize(File directory) {
        long length = 0;
        for (File file : directory.listFiles()) {
            if (file.isFile())
                length += file.length();
            else
                length += calculateDirectorySize(file);
        }
        return length;
    }

    private String formatDataSize(long sizeInBytes) {
        if (sizeInBytes >= DataSize.ofMegabytes(1).toBytes()) {
            return DataSize.ofBytes(sizeInBytes).toMegabytes() + " MB";
        } else {
            return DataSize.ofBytes(sizeInBytes).toKilobytes() + " KB";
        }
    }
}
