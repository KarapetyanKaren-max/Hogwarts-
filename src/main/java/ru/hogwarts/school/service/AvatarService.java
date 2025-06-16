package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.repository.AvatarRepository;

@Service
public class AvatarService {

    private final AvatarRepository avatarRepository;

    public AvatarService(AvatarRepository avatarRepository) {
        this.avatarRepository = avatarRepository;
    }

    public void saveAvatar(Avatar avatar) {
        avatarRepository.save(avatar);
    }

    public Avatar getAvatarById(Long id) {
        return avatarRepository.findById(id).orElse(null);
    }
}