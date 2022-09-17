package omid.ro.civil_registration.repositories;

import omid.ro.civil_registration.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person,Long> {
}
