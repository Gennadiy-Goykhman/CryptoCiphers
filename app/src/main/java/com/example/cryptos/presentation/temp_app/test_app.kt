package com.example.cryptos.presentation.viewmodels

import com.example.cryptos.data.mappers.transformToAlphabet
import com.example.cryptos.data.mappers.transformToString
import com.example.cryptos.data.models.AthensParameters
import com.example.cryptos.data.models.CipherInfo
import com.example.cryptos.domain.models.AlphabetMode
import com.example.cryptos.domain.models.CipherMode
import com.example.cryptos.domain.usecases.GenerateKeyChangeUseCase
import com.example.cryptos.domain.usecases.GetChangeAlphabetUseCase
import com.example.cryptos.domain.usecases.GetCipherTextUseCase
import com.example.cryptos.domain.usecases.GetOpenTextUseCase

fun main() {
    val string = "Late in the evening a young student was writing a practical exercise"
        .replace(" ", "")
        .uppercase()
    val alphabet = GetChangeAlphabetUseCase(AlphabetMode.LATIN_CAPITALS)
    val keyChange = GenerateKeyChangeUseCase(
        CipherInfo(
            alphabet = alphabet,
            cipherMode = CipherMode.ORDINARY_CHANGE
        )
    )
    val ordinaryCipherText = GetCipherTextUseCase(
        openText = string.transformToAlphabet(),
        keyText = keyChange,
    )
    println(ordinaryCipherText.transformToString())
    val openText = GetOpenTextUseCase(ordinaryCipherText, keyChange)
    println(openText.transformToString())

    val athensKeyChange = GenerateKeyChangeUseCase(
        CipherInfo(
            alphabet = alphabet,
            cipherMode = CipherMode.ATHENS,
            athensParameters = AthensParameters(17, 5),
            inputData = string.transformToAlphabet()
        )
    )
    val athensCipherText = GetCipherTextUseCase(
        openText = string.transformToAlphabet(),
        keyText = athensKeyChange
    )
    println(athensCipherText.transformToString())

    val athensReversedKeyChange = GenerateKeyChangeUseCase(
        CipherInfo(
            alphabet = alphabet,
            cipherMode = CipherMode.ATHENS,
            isReversed = true,
            athensParameters = AthensParameters(17, 5),
            inputData = athensCipherText
        )
    )

    val athensOpenText = GetOpenTextUseCase(
        athensCipherText,
        keyText = athensReversedKeyChange
    )
    println(athensOpenText.transformToString())

    val athensRecursiveKeyChange = GenerateKeyChangeUseCase(
        CipherInfo(
            alphabet = alphabet,
            cipherMode = CipherMode.ATHENS_RECURSIVE,
            athensParameters = AthensParameters(17, 5),
            inputData = string.transformToAlphabet(),
            nextAthensParameters = AthensParameters(19, 3)
        )
    )

    val athensRecursiveCipherText = GetCipherTextUseCase(
        openText = string.transformToAlphabet(),
        keyText = athensRecursiveKeyChange
    )

    println(athensRecursiveCipherText.transformToString())

    val athensRecursiveReversedKeyChange = GenerateKeyChangeUseCase(
        CipherInfo(
            alphabet = alphabet,
            cipherMode = CipherMode.ATHENS_RECURSIVE,
            isReversed = true,
            athensParameters = AthensParameters(17, 5),
            inputData = athensRecursiveCipherText,
            nextAthensParameters = AthensParameters(19, 3)
        )
    )

    val athensRecursiveOpenText = GetOpenTextUseCase(
        athensRecursiveCipherText,
        keyText = athensRecursiveReversedKeyChange
    )

    println(athensRecursiveOpenText.transformToString())
}