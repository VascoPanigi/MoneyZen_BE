package vascopanigi.MoneyZen.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vascopanigi.MoneyZen.enums.transaction.TransactionRecurrence;
import vascopanigi.MoneyZen.enums.transaction.TransactionType;

import java.time.LocalDateTime;
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
    private TransactionRecurrence transactionRecurrence;
    private String description;
    private LocalDateTime date;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    @ManyToOne

    @JoinColumn(name = "category_id")
    private Category category;

//    @ManyToMany
//    @JoinTable(
//            name = "transaction_labels",
//            joinColumns = @JoinColumn(name = "transaction_id"),
//            inverseJoinColumns = @JoinColumn(name = "label_id")
//    )
//    private Set<Label> labels;

    public Transaction(String name, double amount, TransactionRecurrence transactionRecurrence, String description, LocalDateTime date) {
        this.name = name;
        this.amount = amount;
        this.transactionRecurrence = transactionRecurrence;
        this.description = description;
        this.date = date;
    }

    public Transaction(String name, double amount, TransactionRecurrence transactionRecurrence, String description, LocalDateTime date, Wallet wallet, Category category) {
        this.name = name;
        this.amount = amount;
        this.transactionRecurrence = transactionRecurrence;
        this.description = description;
        this.date = date;
        this.wallet = wallet;
        this.category = category;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", amount=" + amount +
                ", transactionRecurrence=" + transactionRecurrence +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", wallet=" + wallet +
                ", category=" + category +
                '}';
    }
}
