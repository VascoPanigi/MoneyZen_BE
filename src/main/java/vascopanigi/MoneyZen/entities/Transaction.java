package vascopanigi.MoneyZen.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vascopanigi.MoneyZen.enums.transaction.TransactionRecurrence;
import vascopanigi.MoneyZen.enums.transaction.TransactionType;

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
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    @Enumerated(EnumType.STRING)
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

    public Transaction(String name, double amount, TransactionType transactionType, TransactionRecurrence transactionRecurrence, String description, Date date, Wallet wallet, Set<Label> labels) {
        this.name = name;
        this.amount = amount;
        this.transactionType = transactionType;
        this.transactionRecurrence = transactionRecurrence;
        this.description = description;
        this.date = date;
        this.wallet = wallet;
        this.labels = labels;
    }

    
}
