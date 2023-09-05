package br.com.thiaago.cards.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.awt.*;
import java.util.UUID;

public record CardCategoryRecordDTO(@NotNull @NotBlank UUID idCategory, @NotNull String name, @NotNull Color color) {
}
