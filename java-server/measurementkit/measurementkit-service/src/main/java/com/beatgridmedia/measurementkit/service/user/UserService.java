package com.beatgridmedia.measurementkit.service.user;

import com.beatgridmedia.measurementkit.persistence.entity.UserEntity;
import com.beatgridmedia.measurementkit.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public Optional<UserEntity> findById(long id) {
        return userRepository.findById(id);
    }

    /**
     * @param phoneNumber phone number in E.164 format.
     */
    public Optional<UserEntity> findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    @Transactional
    public UserEntity create(String phoneNumber) {
        return userRepository.save(new UserEntity(phoneNumber));
    }
}
