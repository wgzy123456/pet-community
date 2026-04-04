package com.petcommunity.service;

import com.petcommunity.entity.Pet;
import com.petcommunity.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;

    public List<Pet> getByUserId(Long userId) {
        return petRepository.findByUserId(userId);
    }

    public Optional<Pet> getById(Long id) {
        return petRepository.findById(id);
    }

    public Pet save(Pet pet) {
        return petRepository.save(pet);
    }

    public void delete(Long id) {
        petRepository.deleteById(id);
    }
}
