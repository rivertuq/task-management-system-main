package ru.ostritsov.projectTSM.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ostritsov.projectTSM.model.Task;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findById(Long id);

    List<Task> findByAuthorId(Long userId, Pageable page);
}
