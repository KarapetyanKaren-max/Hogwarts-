package ru.hogwarts.school.controller;


import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.service.AvatarService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/api/avatars")
public class AvatarController {

    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadAvatar(@RequestPart("file") MultipartFile file) throws IOException {
        String uniqueFilename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path directory = Paths.get("uploads"); // Директория для хранения картинок на диске
        Path destination = directory.resolve(uniqueFilename);

        Files.copy(file.getInputStream(), destination);

        Avatar avatar = new Avatar();
        avatar.setFilePath(destination.toAbsolutePath().toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setData(file.getBytes());

        avatarService.saveAvatar(avatar);

        return ResponseEntity.ok("Картинка успешно загружена!");
    }

    @GetMapping("/from-db/{id}")
    public ResponseEntity<byte[]> getAvatarFromDB(@PathVariable Long id) {
        Avatar avatar = avatarService.getAvatarById(id);
        if (avatar == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(avatar.getMediaType()))
                .body(avatar.getData());
    }

    @GetMapping("/from-file-system/{filename:.+}")
    public ResponseEntity<Resource> getAvatarFromFS(@PathVariable String filename) throws MalformedURLException {
        Path filePath = Paths.get("uploads").resolve(filename);
        Resource resource = new UrlResource(filePath.toUri());
        if (!resource.exists() || !resource.isReadable()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .header("Content-Type", MediaType.APPLICATION_OCTET_STREAM_VALUE)
                .body(resource);
    }
}
