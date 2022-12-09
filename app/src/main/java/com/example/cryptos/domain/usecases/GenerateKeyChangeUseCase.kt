package com.example.cryptos.domain.usecases

import com.example.cryptos.data.mappers.correctDiv
import com.example.cryptos.data.mappers.getReversedValue
import com.example.cryptos.data.models.Alphabet
import com.example.cryptos.data.models.AthensParameters
import com.example.cryptos.data.models.CipherInfo
import com.example.cryptos.data.models.KeyPart
import com.example.cryptos.domain.models.CipherMode

object GenerateKeyChangeUseCase {
    operator fun invoke(
        cipherInfo: CipherInfo
    ): List<KeyPart> = when (cipherInfo.cipherMode) {
        CipherMode.ORDINARY_CHANGE -> ordinaryChange(
            cipherInfo.alphabet,
            if (cipherInfo.keyIndexes.isEmpty()) cipherInfo.alphabet.indices.shuffled() else cipherInfo.keyIndexes
        )
        CipherMode.ATHENS -> if (!cipherInfo.isReversed)
            athensChange(
                cipherInfo.alphabet,
                cipherInfo.inputData!!,
                cipherInfo.athensParameters!!
            )
        else reversedAthensChange(
            cipherInfo.alphabet,
            cipherInfo.inputData!!,
            cipherInfo.athensParameters!!
        )
        CipherMode.ATHENS_RECURSIVE -> if (!cipherInfo.isReversed) recAthensChange(
            cipherInfo.alphabet,
            cipherInfo.inputData!!,
            cipherInfo.athensParameters!!,
            cipherInfo.nextAthensParameters!!
        ) else reversedRecAthensChange(
            cipherInfo.alphabet,
            cipherInfo.inputData!!,
            cipherInfo.athensParameters!!,
            cipherInfo.nextAthensParameters!!
        )
    }

    private fun ordinaryChange(
        alphabet: List<Alphabet>,
        keyIndexes: List<Int>
    ): List<KeyPart> = MutableList(alphabet.size) {
        KeyPart(alphabet[it], alphabet[keyIndexes[it]])
    }

    private fun athensChange(
        alphabet: List<Alphabet>,
        inputData: List<Alphabet>,
        athensParameters: AthensParameters
    ): List<KeyPart> = MutableList(inputData.size) {
        with(alphabet){
            val index = (athensParameters.alpha * indexOf(inputData[it]) + athensParameters.betta).correctDiv(alphabet.size)
            KeyPart(inputData[it], get(index))
        }
    }

    private fun recAthensChange(
        alphabet: List<Alphabet>,
        inputData: List<Alphabet>,
        athensParameters: AthensParameters,
        nextAthensParameters: AthensParameters
    ): List<KeyPart> = MutableList(inputData.size) {
        with(alphabet){
            val index = when (it) {
                0 -> (athensParameters.alpha * indexOf(inputData[it]) + athensParameters.betta).correctDiv(alphabet.size)
                else -> {
                    val result =
                        (nextAthensParameters.alpha * indexOf(inputData[it]) + nextAthensParameters.betta).correctDiv(alphabet.size)
                    nextAthensParameters.updateParameters(
                        alpha = (athensParameters.alpha * nextAthensParameters.alpha).correctDiv(alphabet.size),
                        betta = (athensParameters.alpha + nextAthensParameters.alpha).correctDiv(alphabet.size),
                    )
                    result
                }
            }
            KeyPart(inputData[it], get(index))
        }
    }

    private fun reversedAthensChange(
        alphabet: List<Alphabet>,
        inputData: List<Alphabet>,
        athensParameters: AthensParameters
    ): List<KeyPart> = MutableList(inputData.size) {
        with(alphabet){
            val index = ((indexOf(inputData[it]) - athensParameters.betta) * athensParameters.alpha.getReversedValue(alphabet.size)).correctDiv(size)
            KeyPart(get(index),inputData[it])
        }
    }

    private fun reversedRecAthensChange(
        alphabet: List<Alphabet>,
        inputData: List<Alphabet>,
        athensParameters: AthensParameters,
        nextAthensParameters: AthensParameters
    ): List<KeyPart> = MutableList(inputData.size) {
        with(alphabet){
            val index = when (it) {
                0 -> ((indexOf(inputData[it]) - athensParameters.betta) * athensParameters.alpha.getReversedValue(alphabet.size)).correctDiv(size)
                else -> {
                    val result =
                        ((indexOf(inputData[it]) - nextAthensParameters.betta) * nextAthensParameters.alpha.getReversedValue(alphabet.size)).correctDiv(size)
                    nextAthensParameters.updateParameters(
                        alpha = (athensParameters.alpha * nextAthensParameters.alpha).correctDiv(alphabet.size),
                        betta = (athensParameters.alpha + nextAthensParameters.alpha).correctDiv(alphabet.size),
                    )
                    result
                }
            }
            KeyPart(get(index),inputData[it])
        }
    }

}