package vascopanigi.MoneyZen.config;


import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vascopanigi.MoneyZen.entities.Category;
import vascopanigi.MoneyZen.entities.PersonalWallet;
import vascopanigi.MoneyZen.entities.Transaction;
import vascopanigi.MoneyZen.entities.User;
import vascopanigi.MoneyZen.enums.transaction.TransactionRecurrence;
import vascopanigi.MoneyZen.enums.transaction.TransactionType;
import vascopanigi.MoneyZen.exceptions.NotFoundException;
import vascopanigi.MoneyZen.repositories.PersonalWalletRepository;
import vascopanigi.MoneyZen.repositories.SharedWalletRepository;
import vascopanigi.MoneyZen.repositories.TransactionRepository;
import vascopanigi.MoneyZen.repositories.UserRepository;
import vascopanigi.MoneyZen.services.CategoryService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Configuration
public class TransactionInitializer {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SharedWalletRepository sharedWalletRepository;

    @Autowired
    private PersonalWalletRepository personalWalletRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CategoryService categoryService;

    @PostConstruct
    public void initializeTransactions() {
        UUID userId = UUID.fromString("fc1fa784-939a-4b89-a562-1ac952ef45b9");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id: " + userId + " not found."));

        String testWalletName = "test";
        PersonalWallet testWallet = user.getPersonalWallets().stream()
                .filter(wallet -> testWalletName.equals(wallet.getName()))
                .findFirst()
                .orElse(null);

        if (testWallet == null) {
            testWallet = new PersonalWallet(testWalletName, user);
            personalWalletRepository.save(testWallet);
            addRandomTransactions(testWallet);
        }
    }

    private void addRandomTransactions(PersonalWallet wallet) {
        List<Category> categories = categoryService.getAllCategories();
        Random random = new Random();
        AtomicReference<Double> totalAmount = new AtomicReference<>((double) 0);

        List<Transaction> transactions = IntStream.range(0, 50)
                .mapToObj(i -> {
                    String name = "Transaction " + (i + 1);
                    double amount = random.nextDouble() * 100 * (random.nextBoolean() ? 1 : -1);
                    TransactionType transactionType = amount > 0 ? TransactionType.INCOME : TransactionType.OUTCOME;
                    TransactionRecurrence transactionRecurrence = TransactionRecurrence.NONE;
                    String description = "Description for " + name;
                    LocalDateTime date = LocalDateTime.now().minusDays(random.nextInt(180));
                    Category category = categories.get(random.nextInt(categories.size()));

                    totalAmount.updateAndGet(v -> new Double((double) (v + amount)));
                    return new Transaction(name, Math.abs(amount), transactionRecurrence, description, date, wallet, category);
                })
                .collect(Collectors.toList());

        if (totalAmount.get() < 0) {
            Transaction lastTransaction = transactions.getLast();
            lastTransaction.setAmount(lastTransaction.getAmount() - totalAmount.get() + 100);
        }

        wallet.setTransactions(transactions);
        wallet.setBalance(wallet.getTransactions().stream().mapToDouble(Transaction::getAmount).sum());
        personalWalletRepository.save((PersonalWallet) wallet);
    }
}