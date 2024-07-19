package vascopanigi.MoneyZen.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vascopanigi.MoneyZen.entities.PersonalWallet;
import vascopanigi.MoneyZen.entities.User;

import java.util.List;
import java.util.UUID;

@Repository
public interface PersonalWalletRepository extends JpaRepository<PersonalWallet, UUID> {
    List<PersonalWallet> findByUser(User currentUser);
}
