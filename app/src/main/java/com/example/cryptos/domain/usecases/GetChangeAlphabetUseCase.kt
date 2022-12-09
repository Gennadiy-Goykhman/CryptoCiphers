package com.example.cryptos.domain.usecases

import com.example.cryptos.data.models.Alphabet
import com.example.cryptos.domain.models.AlphabetMode

object GetChangeAlphabetUseCase {
    operator fun invoke(
        mode: AlphabetMode
    ): List<Alphabet> = when(mode){
        AlphabetMode.CYRILIK -> Alphabet.values().filter { it.type=="C"|| it.type=="CC" }.sortedBy { it.number }
        AlphabetMode.LATIN -> Alphabet.values().filter { it.type=="L" || it.type=="LC"}.sortedBy { it.number }
        AlphabetMode.NUMBERS -> Alphabet.values().filter { it.type=="N" }.sortedBy { it.number }
        AlphabetMode.LATIN_CAPITALS -> Alphabet.values().filter { it.type=="LC" }.sortedBy { it.number }
        AlphabetMode.ALL -> Alphabet.values().toList()
    }
}