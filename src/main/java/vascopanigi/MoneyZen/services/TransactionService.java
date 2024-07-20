package vascopanigi.MoneyZen.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import vascopanigi.MoneyZen.entities.Label;
import vascopanigi.MoneyZen.entities.Transaction;
import vascopanigi.MoneyZen.enums.transaction.TransactionRecurrence;
import vascopanigi.MoneyZen.enums.transaction.TransactionType;
import vascopanigi.MoneyZen.exceptions.BadRequestException;
import vascopanigi.MoneyZen.payloads.transaction.NewTransactionDTO;
//import vascopanigi.MoneyZen.repositories.LabelRepository;
import vascopanigi.MoneyZen.repositories.TransactionRepository;

import java.util.HashSet;
import java.util.Set;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

//    @Autowired
//    private LabelRepository labelRepository;

//    public Transaction save(NewTransactionDTO body){
//        Transaction newTransaction = new Transaction(body.name(), body.amount(), convertTransactionTypeFromStrToEnum(body.transactionType()), convertTransactionRecurrenceFromStrToEnum(body.transactionRecurrence()), body.description(), body.date());
//
//        Set<Label> labels = new HashSet<>();
//
//        body.labels().forEach(label -> labelRepository.findByName(label.getName()));
//    }

//    public static TransactionType convertTransactionTypeFromStrToEnum(String transactionType) {
//        try {
//            return TransactionType.valueOf(transactionType.toUpperCase());
//        } catch (IllegalArgumentException e) {
//            throw new BadRequestException("Invalid transaction type: " + transactionType + ". Choose between INCOME, OUTCOME. Exception " + e);
//        }
//    }

    public static TransactionRecurrence convertTransactionRecurrenceFromStrToEnum(String transactionRecurrence) {
        try {
            return TransactionRecurrence.valueOf(transactionRecurrence.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid transaction recurrence: " + transactionRecurrence + ". Choose between UNIQUE, WEEKLY, MONTHLY, YEARLY. Exception " + e);
        }
    }
}
