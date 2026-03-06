package com.hakjin.autocore.processing.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;

@Service
public class FileMover {

    private final Path processingDir;
    private final Path doneDir;
    private final Path errorDir;

    public FileMover(
            @Value("${app.dir_processing}") String dirProcessing,
            @Value("${app.dir_done}") String dirDone,
            @Value("${app.dir_error}") String dirError
    ) throws IOException {
        this.processingDir = Paths.get(dirProcessing);
        this.doneDir = Paths.get(dirDone);
        this.errorDir = Paths.get(dirError);

        Files.createDirectories(processingDir);
        Files.createDirectories(doneDir);
        Files.createDirectories(errorDir);
    }

    public Path claimToProcessing(Path inCsvFile) throws IOException {
        Files.createDirectories(processingDir);
        Path target = processingDir.resolve(inCsvFile.getFileName());

        try {
            return Files.move(inCsvFile, target);
        } catch (FileSystemException e) {
            System.out.println("Error moving file: " + inCsvFile.getFileName());
            e.printStackTrace();
            throw new IOException("Cannot claim file (maybe locked or already processing): " + inCsvFile, e);
        }
    }

    public void moveToDone(Path processingFile) throws IOException {
        move(processingFile, doneDir);
    }

    public void moveToError(Path processingFile) throws IOException {
        move(processingFile, errorDir);
    }

    private void move(Path srcFile, Path destDir) throws IOException {
        Path target = destDir.resolve(srcFile.getFileName());
        Files.move(srcFile, target, StandardCopyOption.REPLACE_EXISTING);
    }
}