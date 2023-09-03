package br.com.thiaago.cards.controllers;

import br.com.thiaago.cards.dtos.CardRecordDTO;
import br.com.thiaago.cards.models.CardModel;
import br.com.thiaago.cards.repository.CardRepository;
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

@RestController
public class CardController {

    @Autowired
    CardRepository cardRepository;

    @PostMapping("/cards")
    public ResponseEntity<CardModel> saveCard(@RequestBody @Valid CardRecordDTO cardRecordDTO) {
        CardModel cardModel = new CardModel();
        BeanUtils.copyProperties(cardRecordDTO, cardModel);

        return ResponseEntity.status(HttpStatus.CREATED).body(cardRepository.save(cardModel));
    }

    @GetMapping("/cards")
    public ResponseEntity<List<CardModel>> getAllCards() {
        List<CardModel> cards = cardRepository.findAll();

        if (!cards.isEmpty()) {
            cards.forEach(cardModel -> cardModel.add(linkTo(methodOn(CardController.class).getOneCard(cardModel.getIdCard())).withSelfRel()));
        }

        return ResponseEntity.status(HttpStatus.OK).body(cards);
    }

    @GetMapping("/cards/{id}")
    public ResponseEntity<Object> getOneCard(@PathVariable(value = "id") UUID uuid) {
        Optional<CardModel> optionalCardModel = cardRepository.findById(uuid);

        if (optionalCardModel.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Card not found");

        optionalCardModel.get().add(linkTo(methodOn(CardController.class).getAllCards()).withRel("Cards list"));

        return ResponseEntity.status(HttpStatus.OK).body(optionalCardModel.get());
    }

    @GetMapping("/cards/{id}")
    public ResponseEntity<Object> updateCard(@PathVariable(value = "id") UUID uuid, @RequestBody @Valid CardRecordDTO cardRecordDTO) {
        Optional<CardModel> optionalCardModel = cardRepository.findById(uuid);

        if (!optionalCardModel.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Card not found");

        CardModel cardModel = optionalCardModel.get();

        BeanUtils.copyProperties(cardRecordDTO, cardModel);

        return ResponseEntity.status(HttpStatus.OK).body(cardRepository.save(cardModel));
    }

    @DeleteMapping("/cards/{id}")
    public ResponseEntity<String> deleteCard(@PathVariable(value = "id") UUID uuid, @RequestBody @Valid CardRecordDTO cardRecordDTO) {
        Optional<CardModel> optionalCardModel = cardRepository.findById(uuid);

        if (!optionalCardModel.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Card not found");

        cardRepository.delete(optionalCardModel.get());

        return ResponseEntity.status(HttpStatus.OK).body("Card deleted!");
    }

}