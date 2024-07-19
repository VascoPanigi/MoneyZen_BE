package vascopanigi.MoneyZen.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import vascopanigi.MoneyZen.entities.PersonalWallet;
import vascopanigi.MoneyZen.entities.User;
import vascopanigi.MoneyZen.payloads.wallet.NewPersonalWalletDTO;
import vascopanigi.MoneyZen.repositories.WalletRepository;
import vascopanigi.MoneyZen.security.JWTTools;

@Service
public class WalletService {
    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private JWTTools jwtTools;

    public PersonalWallet savePersonalWallet(NewPersonalWalletDTO body, @AuthenticationPrincipal User currentUser){
        PersonalWallet personalWallet = new PersonalWallet(body.name(), currentUser);
        // Save the wallet
        return walletRepository.save(personalWallet);
    }


}
