package vascopanigi.MoneyZen.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vascopanigi.MoneyZen.enums.transaction.TransactionType;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    private String name;

    public Category(TransactionType transactionType, String name) {
        this.transactionType = transactionType;
        this.name = name;
    }
}