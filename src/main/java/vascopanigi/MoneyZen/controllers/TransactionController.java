package vascopanigi.MoneyZen.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vascopanigi.MoneyZen.entities.Transaction;
import vascopanigi.MoneyZen.entities.User;
import vascopanigi.MoneyZen.exceptions.BadRequestException;
import vascopanigi.MoneyZen.payloads.transaction.NewTransactionDTO;
import vascopanigi.MoneyZen.payloads.transaction.NewTransactionResponseDTO;
import vascopanigi.MoneyZen.services.TransactionService;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public NewTransactionResponseDTO saveTransaction(
            @RequestBody @Valid NewTransactionDTO newTransactionDTO,
            BindingResult validationResult,
            @AuthenticationPrincipal User currentUser) {

        if (validationResult.hasErrors()) {
            throw new BadRequestException(validationResult.getAllErrors());
        }
        return new NewTransactionResponseDTO(this.transactionService.saveTransaction(newTransactionDTO, currentUser).getId());
    }


}
