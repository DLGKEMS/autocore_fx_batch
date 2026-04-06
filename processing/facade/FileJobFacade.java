package com.hakjin.autocore.processing.facade;

//import com.hakjin.autocore.processing.dto.ErrorMessage;
import com.hakjin.autocore.processing.service.FileValidator;
import com.hakjin.autocore.processing.service.OneFileProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Component
public class FileJobFacade {

    private final FileValidator fileValidator;
    private final OneFileProcessor oneFileProcessor;
    private final String dir_in;

    public FileJobFacade(FileValidator fileValidator, OneFileProcessor oneFileProcessor, @Value("${app.dir_in}") String dir_in) {
        this.fileValidator = fileValidator;
        this.oneFileProcessor = oneFileProcessor;
        this.dir_in = dir_in;
    }

    public void start() {
        System.out.println("=== Batch started ===");

        try {
            // 경로, 폴더인지 검증
            Optional<String> validationResult = fileValidator.validateInputDirectory(dir_in);

            if (validationResult.isPresent()) {
                System.err.println(validationResult.get());
                return;
            }
            Path inDirPath = Paths.get(dir_in);

            List<Path> targets;
            try (Stream<Path> s = Files.list(inDirPath)) {
                targets = s.filter(Files::isRegularFile)
                        .filter(p -> p.toString().toLowerCase().endsWith(".csv"))
                        .toList();
            }

            for (Path inFile : targets) {
                oneFileProcessor.process(inFile);
            }

        } catch (Exception e) {
            System.out.println("facade error : " + e.getMessage());
            e.printStackTrace();
        } finally {
            System.out.println("=== Batch finished ===");
        }
    }
}
