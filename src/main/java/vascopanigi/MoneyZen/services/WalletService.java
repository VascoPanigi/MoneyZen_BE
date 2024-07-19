package vascopanigi.MoneyZen.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import vascopanigi.MoneyZen.entities.PersonalWallet;
import vascopanigi.MoneyZen.entities.SharedWallet;
import vascopanigi.MoneyZen.entities.User;
import vascopanigi.MoneyZen.payloads.wallet.NewWalletDTO;
import vascopanigi.MoneyZen.repositories.WalletRepository;
import vascopanigi.MoneyZen.security.JWTTools;

import java.util.HashSet;
import java.util.Set;

@Service
public class WalletService {
    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private JWTTools jwtTools;

    public PersonalWallet savePersonalWallet(NewWalletDTO body, @AuthenticationPrincipal User currentUser){
        PersonalWallet personalWallet = new PersonalWallet(body.name(), currentUser);
        // Save the wallet
        return walletRepository.save(personalWallet);
    }

    public SharedWallet saveSharedWallet(NewWalletDTO body, @AuthenticationPrincipal User currentUser){
        SharedWallet sharedWallet = new SharedWallet(body.name());
        // Save the wallet
        Set<User> usersSet = new HashSet<>();
        usersSet.add(currentUser);
        sharedWallet.setUsers(usersSet);
        return walletRepository.save(sharedWallet);
    }

}
