package br.com.thiaago.cards.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "cards")
@Getter
@Setter
public class CardModel extends RepresentationModel<CardModel> implements Serializable {

    private static final long serialVersion = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idCard;

    private String text;
    private UUID categoryUUID;

}
