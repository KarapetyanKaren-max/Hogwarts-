package ru.hogwarts.school.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.repository.AvatarRepository;

@Service
public class AvatarService {

    private final AvatarRepository avatarRepository;

    public AvatarService(AvatarRepository avatarRepository) {
        this.avatarRepository = avatarRepository;
    }

    public Page<Avatar> getAvatarsPaginated(Pageable pageable) {
        return avatarRepository.findAll(pageable);
    }

    public void saveAvatar(Avatar avatar) {
        avatarRepository.save(avatar);
    }

    public Avatar getAvatarById(Long id) {
        return avatarRepository.findById(id).orElse(null);
    }
}