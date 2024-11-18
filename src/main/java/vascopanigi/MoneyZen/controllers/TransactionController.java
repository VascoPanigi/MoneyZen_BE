package vascopanigi.MoneyZen.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import vascopanigi.MoneyZen.entities.Transaction;
import vascopanigi.MoneyZen.entities.User;
import vascopanigi.MoneyZen.exceptions.BadRequestException;
import vascopanigi.MoneyZen.payloads.transaction.NewTransactionDTO;
import vascopanigi.MoneyZen.payloads.transaction.NewTransactionResponseDTO;
import vascopanigi.MoneyZen.payloads.transaction.UpdateTransactionDTO;
import vascopanigi.MoneyZen.services.TransactionService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/{walletId}")
    public NewTransactionResponseDTO saveTransaction(
            @RequestBody @Valid NewTransactionDTO newTransactionDTO,
            @PathVariable UUID walletId,
            BindingResult validationResult,
            @AuthenticationPrincipal User currentUser) {

        if (validationResult.hasErrors()) {
            throw new BadRequestException(validationResult.getAllErrors());
        }
        return new NewTransactionResponseDTO(this.transactionService.saveTransaction(newTransactionDTO, currentUser, walletId).getId());
    }

    @DeleteMapping("/{transactionId}")
    public void deleteTransaction(@PathVariable UUID transactionId, @AuthenticationPrincipal User currentAuthenticatedUser) {
        transactionService.findByIdAndDelete(transactionId, currentAuthenticatedUser);
    }

    @GetMapping("/wallet/{walletId}")
    public Page<Transaction> getTransactionsByWallet(
            @PathVariable UUID walletId,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "15") int pageSize,
            @RequestParam(defaultValue = "date") String sortedBy,
            @RequestParam(required = false) String transactionType,
            @RequestParam(defaultValue = "DESC") String sortOrder,
            @RequestParam(required = false) LocalDateTime startDateTime,
            @RequestParam(required = false) LocalDateTime endDateTime,
            @RequestParam(required = false) Double minAmount,
            @RequestParam(required = false) Double maxAmount,
            @RequestParam(required = false) String name,
            @AuthenticationPrincipal User currentUser) {
        return transactionService.findTransactionsByWallet(walletId, pageNumber, pageSize, sortedBy, transactionType, sortOrder, startDateTime, endDateTime, minAmount, maxAmount,name, currentUser);
    }

    @GetMapping("/wallet/{walletId}/all-transactions")
    public List<Transaction> getTransactionsByWallet(
            @PathVariable UUID walletId,
            @AuthenticationPrincipal User currentUser) {
        return transactionService.getAllTransactions(walletId, currentUser);
    }

    @PutMapping("/{transactionId}")
    public Transaction updateTransaction(
            @PathVariable UUID transactionId,
            @RequestBody @Valid UpdateTransactionDTO updateTransactionDTO,
            BindingResult validationResult,
            @AuthenticationPrincipal User currentUser) {

        if (validationResult.hasErrors()) {
            throw new BadRequestException(validationResult.getAllErrors());
        }
        return transactionService.updateTransaction(transactionId, updateTransactionDTO, currentUser);
    }
}
