package vascopanigi.MoneyZen.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import vascopanigi.MoneyZen.entities.Transaction;
import vascopanigi.MoneyZen.entities.User;
import vascopanigi.MoneyZen.exceptions.BadRequestException;
import vascopanigi.MoneyZen.payloads.transaction.NewTransactionDTO;
import vascopanigi.MoneyZen.payloads.transaction.NewTransactionResponseDTO;
import vascopanigi.MoneyZen.services.TransactionService;

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

}
