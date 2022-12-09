package com.example.cryptos.domain.usecases

import com.example.cryptos.data.models.Alphabet
import com.example.cryptos.data.models.AthensParameters
import com.example.cryptos.data.models.KeyPart
import com.example.cryptos.domain.models.CipherMode

object GetCipherTextUseCase {
   operator fun invoke(
       openText: List<Alphabet>,
       keyText:  List<KeyPart> = listOf(),
   ): List<Alphabet> =
        openText.map { letter ->
            keyText.find { it.symbolOrigin==letter }!!.symbolChange
        }
}