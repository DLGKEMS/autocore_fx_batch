package com.hakjin.autocore.processing.scheduler;

import com.hakjin.autocore.processing.facade.FileJobFacade;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.nio.file.*;

@Component
public class BatchStartupRunner implements CommandLineRunner {

    private final FileJobFacade fileJobFacade;
    private final String dirIn;

    public BatchStartupRunner(FileJobFacade fileJobFacade,
                              @Value("${app.dir_in}") String dirIn) {
        this.fileJobFacade = fileJobFacade;
        this.dirIn = dirIn;
    }

    @Override
    public void run(String... args) {
        System.out.println("=== Folder Watcher Started ===");

        new Thread(this::watchFolder).start();
    }

    private void watchFolder() {
        try {
            Path path = Paths.get(dirIn);

            WatchService watchService = FileSystems.getDefault().newWatchService();
            path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

            while (true) {
                WatchKey key = watchService.take(); // 파일 생길 때까지 블로킹

                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();

                    if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                        Path createdFile = path.resolve((Path) event.context());

                        System.out.println("New file detected: " + createdFile);

                        // CSV만 처리
                        if (createdFile.toString().toLowerCase().endsWith(".csv")) {
                            fileJobFacade.start();
                        }
                    }
                }

                key.reset();
            }

        } catch (Exception e) {
            System.out.println("Watcher error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}