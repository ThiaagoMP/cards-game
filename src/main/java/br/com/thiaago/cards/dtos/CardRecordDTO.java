package br.com.thiaago.cards.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CardRecordDTO(@NotBlank @NotNull String text, @NotNull UUID categoryUUID) {
}
