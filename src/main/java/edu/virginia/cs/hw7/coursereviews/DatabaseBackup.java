package edu.virginia.cs.hw7.coursereviews;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class DatabaseBackup {
    private static final String BACKUP_DB_NAME = "Reviews_backup.sqlite3";

    public void backupDatabase() {
        try {
            Path source = Paths.get(DatabaseManager.DB_NAME);
            Path destination = Paths.get(BACKUP_DB_NAME);

            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Unable to backup database", e);
        }
    }

    public void restoreDatabase() {
        try {
            Path source = Paths.get(BACKUP_DB_NAME);
            Path destination = Paths.get(DatabaseManager.DB_NAME);

            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Unable to restore database", e);
        }
    }
}