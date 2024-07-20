package vascopanigi.MoneyZen.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vascopanigi.MoneyZen.entities.Category;
import vascopanigi.MoneyZen.exceptions.NotFoundException;
import vascopanigi.MoneyZen.repositories.CategoryRepository;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public Category findByName(String name){
           return categoryRepository.findByName(name).orElseThrow(()-> new NotFoundException("Category " + name + " not found!"));
    }
}
