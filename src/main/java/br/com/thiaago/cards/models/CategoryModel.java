package br.com.thiaago.cards.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "cards")
@Getter
@Setter
public class CategoryModel implements Serializable {

    private static final long serialVersion = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idCategory;

    private String name;
    private Color color;

}
