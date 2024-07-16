package vascopanigi.MoneyZen.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vascopanigi.MoneyZen.entities.Label;

import java.util.Optional;
import java.util.UUID;

public interface LabelRepository extends JpaRepository<Label, UUID> {
    Optional<Label> findByName(String name);

}
