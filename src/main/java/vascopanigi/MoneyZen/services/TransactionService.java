package vascopanigi.MoneyZen.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import vascopanigi.MoneyZen.entities.Label;
import vascopanigi.MoneyZen.entities.*;
import vascopanigi.MoneyZen.enums.transaction.TransactionRecurrence;
import vascopanigi.MoneyZen.enums.transaction.TransactionType;
import vascopanigi.MoneyZen.exceptions.BadRequestException;
import vascopanigi.MoneyZen.exceptions.NotFoundException;
import vascopanigi.MoneyZen.exceptions.UnauthorizedException;
import vascopanigi.MoneyZen.payloads.transaction.NewTransactionDTO;
//import vascopanigi.MoneyZen.repositories.LabelRepository;
import vascopanigi.MoneyZen.repositories.CategoryRepository;
import vascopanigi.MoneyZen.repositories.TransactionRepository;
import vascopanigi.MoneyZen.repositories.WalletRepository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private CategoryService categoryService;


//    @Autowired
//    private LabelRepository labelRepository;


    public Transaction saveTransaction(NewTransactionDTO body, User currentUser){

        Category transactionCategory = categoryService.findByName(body.categoryName());
        Wallet transactionWallet = walletRepository.findById(body.walletId()).orElseThrow(() -> new NotFoundException("Wallet with id " + body.walletId() + " not found!"));
        if (transactionWallet instanceof PersonalWallet) {
            if (!((PersonalWallet) transactionWallet).getUser().getId().equals(currentUser.getId())) {
                throw new UnauthorizedException("You are not authorized to add transactions to this wallet");
            }
        } else if (transactionWallet instanceof SharedWallet) {
            if (!((SharedWallet) transactionWallet).getUsers().contains(currentUser)) {
                throw new UnauthorizedException("You are not authorized to add transactions to this wallet");
            }
        }

        Transaction newTransaction = new Transaction(body.name(), body.amount(), convertTransactionRecurrenceFromStrToEnum(body.transactionRecurrence()), body.description(), body.date(), transactionWallet, transactionCategory);
        return this.transactionRepository.save(newTransaction);

    }

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
            throw new BadRequestException("Invalid transaction recurrence: " + transactionRecurrence + ". Choose between DAILY, WEEKLY, MONTHLY, YEARLY, NONE. Exception " + e);
        }
    }
}
