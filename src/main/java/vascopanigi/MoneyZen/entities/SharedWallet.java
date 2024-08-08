package vascopanigi.MoneyZen.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="shared_wallets")
public class SharedWallet extends Wallet {
    @JsonManagedReference
    @ManyToMany
    @JoinTable(
            name = "shared_wallet_user",
            joinColumns = @JoinColumn(name = "shared_wallet_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users;

    public SharedWallet(String name) {
        super(name);
        this.setShared(true);
    }
}
