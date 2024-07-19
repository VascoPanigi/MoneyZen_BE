package vascopanigi.MoneyZen.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vascopanigi.MoneyZen.repositories.WalletRepository;

@Service
public class WalletService {
    @Autowired
    private WalletRepository walletRepository;
}
