package uz.pdp.appjparelationships.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.appjparelationships.entity.Address;
import uz.pdp.appjparelationships.entity.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Integer> {

    boolean existsByName(String name);

}
