package vascopanigi.MoneyZen.services;

import jakarta.persistence.criteria.Join;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

//import vascopanigi.MoneyZen.entities.Label;
import vascopanigi.MoneyZen.entities.*;
import vascopanigi.MoneyZen.enums.transaction.TransactionRecurrence;
import vascopanigi.MoneyZen.enums.transaction.TransactionType;
import vascopanigi.MoneyZen.exceptions.BadRequestException;
import vascopanigi.MoneyZen.exceptions.NotFoundException;
import vascopanigi.MoneyZen.payloads.transaction.NewTransactionDTO;
//import vascopanigi.MoneyZen.repositories.LabelRepository;
import vascopanigi.MoneyZen.payloads.transaction.UpdateTransactionDTO;
import vascopanigi.MoneyZen.repositories.TransactionRepository;
import vascopanigi.MoneyZen.repositories.WalletRepository;

import java.time.LocalDateTime;
import java.util.*;

import jakarta.persistence.criteria.Predicate;

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
        }else{
            transactionWallet.setBalance(transactionWallet.getBalance() - body.amount());
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

    public List<Transaction> getAllTransactions(UUID walletId, User currentUser){
        Wallet wallet = this.walletService.getWalletById(walletId, currentUser);
        return transactionRepository.findByWalletId(walletId);

    }

    public Page<Transaction> findTransactionsByWallet(UUID walletId,
                                                      int pageNumber,
                                                      int pageSize,
                                                      String sortedBy,
                                                      String transactionType,
                                                      String sortOrder,
                                                      LocalDateTime startDate,
                                                      LocalDateTime endDate,
                                                      Double minAmount,
                                                      Double maxAmount,
                                                      String name,
                                                      User currentUser) {

        Wallet wallet = this.walletService.getWalletById(walletId, currentUser);

        if (pageSize > 100) pageSize = 100;

        Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortedBy);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        return transactionRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal(root.get("wallet").get("id"), walletId));

            if (transactionType != null) {
                Join<Transaction, Category> categoryJoin = root.join("category");
                TransactionType type = TransactionType.valueOf(transactionType.toUpperCase());
                predicates.add(criteriaBuilder.equal(categoryJoin.get("transactionType"), type));
            }
            if (startDate != null && endDate != null) {
                predicates.add(criteriaBuilder.between(root.get("date"), startDate, endDate));
            }
            if (startDate != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("date"), startDate));
            }
            if (endDate != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("date"), endDate));
            }
            if (minAmount != null && maxAmount != null) {
                predicates.add(criteriaBuilder.between(root.get("amount"), minAmount, maxAmount));
            }
            if (minAmount != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("amount"), minAmount));
            }
            if (maxAmount != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("amount"), maxAmount));
            }
            if (name != null && !name.isEmpty()) { // Check if name parameter is not null or empty
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }, pageable);
    }

    public Transaction updateTransaction(UUID transactionId, UpdateTransactionDTO body, User currentUser) {
        Transaction existingTransaction = findById(transactionId);

        Wallet transactionWallet = this.walletService.getWalletById(existingTransaction.getWallet().getId(), currentUser);

        Category transactionCategory = categoryService.findByName(body.categoryName());

        double balanceAdjustment = body.amount() - existingTransaction.getAmount();
        if (transactionCategory.getTransactionType().equals(TransactionType.INCOME)) {
            transactionWallet.setBalance(transactionWallet.getBalance() + balanceAdjustment);
        } else {
            transactionWallet.setBalance(transactionWallet.getBalance() - balanceAdjustment);
        }

        existingTransaction.setName(body.name());
        existingTransaction.setAmount(body.amount());
        existingTransaction.setTransactionRecurrence(convertTransactionRecurrenceFromStrToEnum(body.transactionRecurrence()));
        existingTransaction.setDescription(body.description());
        existingTransaction.setDate(body.date());
        existingTransaction.setCategory(transactionCategory);

        return transactionRepository.save(existingTransaction);
    }
}
