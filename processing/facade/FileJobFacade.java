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

    @Value("${app.dir_in}")
    private String dir_in;

    private final FileValidator fileValidator;
    private final OneFileProcessor oneFileProcessor;

    public FileJobFacade(FileValidator fileValidator, OneFileProcessor oneFileProcessor) {
        this.fileValidator = fileValidator;
        this.oneFileProcessor = oneFileProcessor;
    }

    public void start() {
        System.out.println("=== Batch started ===");

        try {
            Optional<String> validationResult = fileValidator.validateInputDirectory(dir_in);

            if (validationResult.isPresent()) {
                System.err.println(validationResult.get());
                return;
            }
            Path inDir = Paths.get(dir_in);

            List<Path> targets;
            try (Stream<Path> s = Files.list(inDir)) {
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
