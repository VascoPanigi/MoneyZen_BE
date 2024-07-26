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

    @Autowired
    private WalletService walletService;


//    @Autowired
//    private LabelRepository labelRepository;

    public Transaction saveTransaction(NewTransactionDTO body, User currentUser, UUID walletId){
        Category transactionCategory = categoryService.findByName(body.categoryName());
        Wallet transactionWallet = this.walletService.getWalletById(walletId, currentUser);
        if(transactionCategory.getTransactionType().equals(TransactionType.INCOME)){
        transactionWallet.setBalance(transactionWallet.getBalance() + body.amount());
            System.out.println("Income adds credit to your balance!");
        }else{
            transactionWallet.setBalance(transactionWallet.getBalance() - body.amount());
            System.out.println("With an outcome expense you lose money");
        }

        Transaction newTransaction = new Transaction(body.name(), body.amount(), convertTransactionRecurrenceFromStrToEnum(body.transactionRecurrence()), body.description(), body.date(), transactionWallet, transactionCategory);
        return this.transactionRepository.save(newTransaction);
    }

    public static TransactionRecurrence convertTransactionRecurrenceFromStrToEnum(String transactionRecurrence) {
        try {
            return TransactionRecurrence.valueOf(transactionRecurrence.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid transaction recurrence: " + transactionRecurrence + ". Choose between DAILY, WEEKLY, MONTHLY, YEARLY, NONE. Exception " + e);
        }
    }

    public Transaction findById(UUID id) {
        return this.transactionRepository.findById(id).orElseThrow(() -> new NotFoundException("Transaction with id: " + id +" not found."));
    }


    public void findByIdAndDelete(UUID transactionId, User currentUser) {
        Transaction found = this.findById(transactionId);
        Wallet userWallet = this.walletService.getWalletById(found.getWallet().getId(), currentUser);
        userWallet.setBalance(userWallet.getBalance() - found.getAmount());
        this.transactionRepository.delete(found);
    }
}
