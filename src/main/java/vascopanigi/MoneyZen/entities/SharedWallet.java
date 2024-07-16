package vascopanigi.MoneyZen.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name="shared_wallets")
public class SharedWallet extends Wallet {
    @JsonBackReference
    @ManyToMany
    @JoinTable(
            name = "shared_wallet_user",
            joinColumns = @JoinColumn(name = "shared_wallet_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users;
}
