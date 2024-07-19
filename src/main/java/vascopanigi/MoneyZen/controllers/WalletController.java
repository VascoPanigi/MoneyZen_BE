package vascopanigi.MoneyZen.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vascopanigi.MoneyZen.entities.User;
import vascopanigi.MoneyZen.entities.Wallet;
import vascopanigi.MoneyZen.exceptions.BadRequestException;
import vascopanigi.MoneyZen.payloads.wallet.NewWalletDTO;
import vascopanigi.MoneyZen.payloads.wallet.NewWalletResponseDTO;
import vascopanigi.MoneyZen.services.WalletService;

import java.util.UUID;

@RestController
@RequestMapping("/wallets")
public class WalletController {
    @Autowired
    private WalletService walletService;

    @PostMapping("/personal-wallets")
    @ResponseStatus(HttpStatus.CREATED)
    public NewWalletResponseDTO savePersonalWallet(@RequestBody @Validated NewWalletDTO body, BindingResult validationResult, @AuthenticationPrincipal User currentUser){
    if (validationResult.hasErrors()) {
        System.out.println(validationResult.getAllErrors());
        throw new BadRequestException(validationResult.getAllErrors());
    }
    System.out.println(body);
    return new NewWalletResponseDTO(this.walletService.savePersonalWallet(body, currentUser).getId());
    }

    @PostMapping("/shared-wallets")
    @ResponseStatus(HttpStatus.CREATED)
    public NewWalletResponseDTO savesSharedWallet(@RequestBody @Validated NewWalletDTO body, BindingResult validationResult, @AuthenticationPrincipal User currentUser){
        if (validationResult.hasErrors()) {
            System.out.println(validationResult.getAllErrors());
            throw new BadRequestException(validationResult.getAllErrors());
        }
        System.out.println(body);
        return new NewWalletResponseDTO(this.walletService.saveSharedWallet(body, currentUser).getId());
    }

    @GetMapping("/{walletId}")
    public Wallet getSpecificWallet(@PathVariable UUID walletId, @AuthenticationPrincipal User currentUser){
        return this.walletService.getWalletById(walletId, currentUser);
    }
}
