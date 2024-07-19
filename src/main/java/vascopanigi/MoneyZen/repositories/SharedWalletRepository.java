package vascopanigi.MoneyZen.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vascopanigi.MoneyZen.entities.SharedWallet;
import vascopanigi.MoneyZen.entities.User;

import java.util.List;
import java.util.UUID;

@Repository
public interface SharedWalletRepository extends JpaRepository<SharedWallet, UUID> {
    List<SharedWallet> findByUsersContains(User currentUser);
}
