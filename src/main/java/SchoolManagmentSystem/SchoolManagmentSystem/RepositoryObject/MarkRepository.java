package SchoolManagmentSystem.SchoolManagmentSystem.RepositoryObject;

import SchoolManagmentSystem.SchoolManagmentSystem.Model.Mark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarkRepository  extends JpaRepository<Mark, Long> {
}
