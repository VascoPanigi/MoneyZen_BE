package vascopanigi.MoneyZen.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vascopanigi.MoneyZen.enums.expense.TransactionRecurrence;
import vascopanigi.MoneyZen.enums.expense.TransactionType;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    private String name;
    private double amount;
    private TransactionType transactionType;
    private TransactionRecurrence transactionRecurrence;
    private String description;
    private Date date;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    @ManyToMany
    @JoinTable(
            name = "transaction_labels",
            joinColumns = @JoinColumn(name = "transaction_id"),
            inverseJoinColumns = @JoinColumn(name = "label_id")
    )
    private Set<Label> labels;



}
