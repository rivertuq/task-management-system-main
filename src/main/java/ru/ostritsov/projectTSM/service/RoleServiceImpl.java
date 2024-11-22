package ru.ostritsov.projectTSM.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ostritsov.projectTSM.model.Role;
import ru.ostritsov.projectTSM.repository.RoleRepository;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }
}
