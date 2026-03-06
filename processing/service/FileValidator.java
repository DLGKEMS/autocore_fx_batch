package com.hakjin.autocore.processing.service;

import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class FileValidator {

    public Optional<String> validateInputDirectory(String path) {
        Path inputPath = Paths.get(path);

        if (!Files.exists(inputPath)) {
            return Optional.of("입력 경로가 존재하지 않습니다.");
        }

        if (!Files.isDirectory(inputPath)) {
            return Optional.of("입력 경로가 폴더가 아닙니다.");
        }

        return Optional.empty();
    }
}