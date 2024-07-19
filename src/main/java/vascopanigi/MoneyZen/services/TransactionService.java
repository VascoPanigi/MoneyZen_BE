package vascopanigi.MoneyZen.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vascopanigi.MoneyZen.repositories.TransactionRepository;

@Service
public class TransactionService {
@Autowired
    private TransactionRepository transactionRepository;
}
