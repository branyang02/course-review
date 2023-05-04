package edu.virginia.cs.hw7.coursereviews;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.*;

public class DatabaseBackup {
    private static final String BACKUP_DB_NAME = "Reviews_backup.sqlite3";

    public void backupDatabase() {
        try {
            Path source = Paths.get(DatabaseManager.DB_NAME);
            Path destination = Paths.get(BACKUP_DB_NAME);

            // Make sure the database file is closed before copying
            try (FileChannel sourceChannel = FileChannel.open(source);
                 FileChannel destChannel = FileChannel.open(destination, StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
                sourceChannel.transferTo(0, sourceChannel.size(), destChannel);
            }

        } catch (IOException e) {
            throw new RuntimeException("Unable to backup database", e);
        }
    }

    public void restoreDatabase() {
        try {
            Path source = Paths.get(BACKUP_DB_NAME);
            Path destination = Paths.get(DatabaseManager.DB_NAME);

            // Make sure the database file is closed before copying
            try (FileChannel sourceChannel = FileChannel.open(source);
                 FileChannel destChannel = FileChannel.open(destination, StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
                sourceChannel.transferTo(0, sourceChannel.size(), destChannel);
            }

        } catch (IOException e) {
            throw new RuntimeException("Unable to restore database", e);
        }
    }
}
