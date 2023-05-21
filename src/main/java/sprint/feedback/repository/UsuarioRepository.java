package sprint.feedback.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sprint.feedback.models.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Page<Usuario> findByNameContaining(String name, Pageable pageable);
}
