package vascopanigi.MoneyZen.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vascopanigi.MoneyZen.entities.Category;
import vascopanigi.MoneyZen.services.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoriesController {
    @Autowired
    private CategoryService categoryService;


    @GetMapping
    public List<Category> getAllCategories(){
        return categoryService.getAllCategories();
    }
}
