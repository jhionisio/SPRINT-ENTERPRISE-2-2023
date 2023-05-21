package sprint.feedback.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sprint.feedback.models.Empresa;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
        Page<Empresa> findByNameContaining(String name, Pageable pageable);
}
