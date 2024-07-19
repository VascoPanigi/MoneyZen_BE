package vascopanigi.MoneyZen.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vascopanigi.MoneyZen.entities.User;
import vascopanigi.MoneyZen.exceptions.BadRequestException;
import vascopanigi.MoneyZen.payloads.user.NewUserResponseDTO;
import vascopanigi.MoneyZen.payloads.wallet.NewPersonalWalletDTO;
import vascopanigi.MoneyZen.payloads.wallet.NewPersonalWalletReponseDTO;
import vascopanigi.MoneyZen.services.WalletService;

@RestController
@RequestMapping("/wallets")
public class WalletController {
    @Autowired
    private WalletService walletService;

    @PostMapping("/personal-wallets")
    @ResponseStatus(HttpStatus.CREATED)
    public NewPersonalWalletReponseDTO savePersonalWallet(@RequestBody @Validated NewPersonalWalletDTO body, BindingResult validationResult,  @AuthenticationPrincipal User currentUser){
    if (validationResult.hasErrors()) {
        System.out.println(validationResult.getAllErrors());
        throw new BadRequestException(validationResult.getAllErrors());
    }
    System.out.println(body);
    return new NewPersonalWalletReponseDTO(this.walletService.savePersonalWallet(body, currentUser).getId());
    }
}
