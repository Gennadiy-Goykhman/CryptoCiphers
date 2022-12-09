package com.example.cryptos.domain.usecases

import com.example.cryptos.data.models.Alphabet
import com.example.cryptos.data.models.KeyPart

object GetOpenTextUseCase {
    operator fun invoke(
        cipherText: List<Alphabet>,
        keyText:  List<KeyPart>
    ): List<Alphabet> =
        cipherText.map { letter ->
            keyText.find { it.symbolChange==letter }!!.symbolOrigin
        }
}