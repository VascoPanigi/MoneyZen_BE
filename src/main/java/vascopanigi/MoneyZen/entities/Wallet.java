package vascopanigi.MoneyZen.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Wallet {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    private String name;
    private double balance;

    @JsonManagedReference
    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL)
    private Set<Transaction> transactions;

    public Wallet(String name, double balance) {
        this.name = name;
        this.balance = balance;
    }
}
