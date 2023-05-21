package sprint.feedback.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sprint.feedback.models.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
        Page<Feedback> findByDateContaining(Date data, Pageable pageable);
}
