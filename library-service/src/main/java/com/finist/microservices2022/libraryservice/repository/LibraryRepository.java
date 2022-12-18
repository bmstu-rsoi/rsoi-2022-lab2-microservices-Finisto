package com.finist.microservices2022.libraryservice.repository;

import com.finist.microservices2022.libraryservice.model.Library;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LibraryRepository extends JpaRepository<Library, Integer> {

    List<Library> findAllByCity(String city);

    Library getLibraryByLibraryUid(UUID libraryUid);
}
