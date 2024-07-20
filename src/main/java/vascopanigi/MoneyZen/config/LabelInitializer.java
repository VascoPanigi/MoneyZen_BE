//package vascopanigi.MoneyZen.config;
//
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import vascopanigi.MoneyZen.entities.Label;
//import vascopanigi.MoneyZen.repositories.LabelRepository;
//
//@Configuration
//public class LabelInitializer {
//
//    @Autowired
//    private LabelRepository labelRepository;
//
//    @PostConstruct
//    public void initializeLabels() {
//        String[] labels = {
//                "Groceries", "Utilities", "Rent", "Transportation", "Dining", "Entertainment",
//                "Healthcare", "Insurance", "Savings", "Investments", "Education", "Personal Care",
//                "Travel", "Clothing", "Gifts", "Charity", "Subscriptions", "Electronics",
//                "Home Improvement", "Pets", "Fitness", "Loan Payments", "Taxes", "Business Expenses",
//                "Miscellaneous", "Books", "Music", "Hobbies", "Software", "Garden", "Children",
//                "Baby Supplies", "Beauty", "Laundry", "Cleaning", "Stationery", "Groceries-Organic",
//                "Fuel", "Parking", "Car Maintenance", "Public Transport", "Bike Maintenance",
//                "Food Delivery", "Coffee", "Alcohol", "Snacks", "Online Shopping", "Gadgets",
//                "Tools", "Memberships"
//        };
//
//        try {
//            if (labelRepository.count() == 0) {
//                for (int i = 1; i < labels.length + 1; i++) {
//                    String labelName = labels[i] ;
//                    labelRepository.save(new Label(labelName));
//                }
//                System.out.println("Labels successfully initialized.");
//            }
//        } catch (Exception e) {
//            System.out.println("Error during label initialization: " + e.getMessage());
//        }
//    }
//}
