package br.com.cardapio.dto;

import br.com.cardapio.domain.TipoPrato;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record PratoRequest(
        @NotNull(message = "tipo é obrigatório")
        TipoPrato tipo,

        @NotBlank(message = "nome é obrigatório")
        @Size(max = 120, message = "nome deve ter no máximo 120 caracteres")
        String nome,

        @NotBlank(message = "descrição é obrigatória")
        @Size(max = 500, message = "descrição deve ter no máximo 500 caracteres")
        String descricao,

        @NotNull(message = "preço é obrigatório")
        @DecimalMin(value = "0.00", message = "preço não pode ser negativo")
        @Digits(integer = 8, fraction = 2, message = "preço deve ter no máximo 8 inteiros e 2 decimais")
        BigDecimal preco,

        @NotBlank(message = "nacionalidade é obrigatória")
        @Size(max = 80, message = "nacionalidade deve ter no máximo 80 caracteres")
        String nacionalidade,

        @NotBlank(message = "detalhe é obrigatório")
        @Size(max = 500, message = "detalhe deve ter no máximo 500 caracteres")
        String detalhe,

        @NotNull(message = "disponível é obrigatório")
        Boolean disponivel
) {
}
