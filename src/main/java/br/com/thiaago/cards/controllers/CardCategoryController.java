package br.com.thiaago.cards.controllers;

import br.com.thiaago.cards.dtos.CardCategoryRecordDTO;
import br.com.thiaago.cards.models.CategoryModel;
import br.com.thiaago.cards.repository.CategoryRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class CardCategoryController {

    @Autowired
    CategoryRepository categoryRepository;

    @PostMapping("/categories")
    public ResponseEntity<CategoryModel> saveCategory(@RequestBody @Valid CardCategoryRecordDTO cardCategoryRecordDTO) {
        CategoryModel categoryModel = new CategoryModel();
        BeanUtils.copyProperties(cardCategoryRecordDTO, categoryModel);

        return ResponseEntity.status(HttpStatus.CREATED).body(categoryRepository.save(categoryModel));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryModel>> getAllCategories() {
        List<CategoryModel> cards = categoryRepository.findAll();

        if (!cards.isEmpty()) {
            cards.forEach(categoryModel -> categoryModel.add(linkTo(methodOn(CardController.class).getOneCard(categoryModel.getIdCategory())).withSelfRel()));
        }

        return ResponseEntity.status(HttpStatus.OK).body(cards);
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<Object> getOneCategory(@PathVariable(value = "id") UUID uuid) {
        Optional<CategoryModel> optionalCategoryModel = categoryRepository.findById(uuid);

        if (optionalCategoryModel.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found");

        optionalCategoryModel.get().add(linkTo(methodOn(CardCategoryController.class).getAllCategories()).withRel("Categories list"));

        return ResponseEntity.status(HttpStatus.OK).body(optionalCategoryModel.get());
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<Object> updateCategory(@PathVariable(value = "id") UUID uuid, @RequestBody @Valid CardCategoryRecordDTO cardCategoryRecordDTO) {
        Optional<CategoryModel> optionalCategoryModel = categoryRepository.findById(uuid);

        if (!optionalCategoryModel.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found");

        CategoryModel categoryModel = optionalCategoryModel.get();

        BeanUtils.copyProperties(cardCategoryRecordDTO, categoryModel);

        return ResponseEntity.status(HttpStatus.OK).body(categoryRepository.save(categoryModel));
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable(value = "id") UUID uuid, @RequestBody @Valid CardCategoryRecordDTO cardCategoryRecordDTO) {
        Optional<CategoryModel> optionalCategoryModel = categoryRepository.findById(uuid);

        if (!optionalCategoryModel.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found");

        categoryRepository.delete(optionalCategoryModel.get());

        return ResponseEntity.status(HttpStatus.OK).body("Category deleted!");
    }

}
