package vascopanigi.MoneyZen.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
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
    private boolean isShared;
// Initially the default behaviour I imagined was to have a bilateral connection between transactions and wallets
//    @JsonManagedReference
@JsonIgnore
    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL, orphanRemoval = true     )
    private List<Transaction> transactions;

    public Wallet(String name) {
        this.name = name;
        this.balance = 0;

    }
}
