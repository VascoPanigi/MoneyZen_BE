package vascopanigi.MoneyZen.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vascopanigi.MoneyZen.entities.PersonalWallet;
import vascopanigi.MoneyZen.entities.SharedWallet;
import vascopanigi.MoneyZen.entities.Wallet;

import java.util.List;
import java.util.UUID;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, UUID> {
    @Query("SELECT pw FROM PersonalWallet pw WHERE pw.user.id = :userId")
    List<PersonalWallet> findPersonalWalletsByUserId(UUID userId);

    @Query("SELECT sw FROM SharedWallet sw JOIN sw.users u WHERE u.id = :userId")
    List<SharedWallet> findSharedWalletsByUserId(UUID userId);
}
