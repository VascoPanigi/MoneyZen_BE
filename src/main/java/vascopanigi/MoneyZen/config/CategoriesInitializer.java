package vascopanigi.MoneyZen.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import vascopanigi.MoneyZen.entities.Category;
import vascopanigi.MoneyZen.enums.transaction.TransactionType;
import vascopanigi.MoneyZen.repositories.CategoryRepository;

import java.util.List;

@Configuration
public class CategoriesInitializer {
    @Autowired
    private CategoryRepository categoryRepository;

    @PostConstruct
    public void initializeCategories() {
        try {
        if (categoryRepository.count() == 0) {
            List<Category> outcomeCategories = List.of(
                    new Category(TransactionType.OUTCOME, "Food & Drink"),
                    new Category(TransactionType.OUTCOME, "Bill & fees"),
                    new Category(TransactionType.OUTCOME, "Family & personal"),
                    new Category(TransactionType.OUTCOME, "Shopping"),
                    new Category(TransactionType.OUTCOME, "Transport"),
                    new Category(TransactionType.OUTCOME, "Travel"),
                    new Category(TransactionType.OUTCOME, "Groceries"),
                    new Category(TransactionType.OUTCOME, "Entertainment"),
                    new Category(TransactionType.OUTCOME, "Home"),
                    new Category(TransactionType.OUTCOME, "Car"),
                    new Category(TransactionType.OUTCOME, "Healthcare"),
                    new Category(TransactionType.OUTCOME, "Education"),
                    new Category(TransactionType.OUTCOME, "Gifts"),
                    new Category(TransactionType.OUTCOME, "Sports"),
                    new Category(TransactionType.OUTCOME, "Beauty"),
                    new Category(TransactionType.OUTCOME, "Work"),
                    new Category(TransactionType.OUTCOME, "Other")
            );

            List<Category> incomeCategories = List.of(
                    new Category(TransactionType.INCOME, "Salary"),
                    new Category(TransactionType.INCOME, "Business"),
                    new Category(TransactionType.INCOME, "Loan"),
                    new Category(TransactionType.INCOME, "Parental Leave"),
                    new Category(TransactionType.INCOME, "Gifts"),
                    new Category(TransactionType.INCOME, "Extra income"),
                    new Category(TransactionType.INCOME, "Insurance"),
                    new Category(TransactionType.INCOME, "Sale"),
                    new Category(TransactionType.INCOME, "Other")
            );

            categoryRepository.saveAll(outcomeCategories);
            categoryRepository.saveAll(incomeCategories);
        }
            System.out.println("Categories successfully initialized.");
        } catch (Exception e) {
            System.out.println("Error during categories initialization: " + e.getMessage());
        }
    }
}
