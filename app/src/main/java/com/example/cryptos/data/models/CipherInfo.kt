package com.example.cryptos.data.models

import com.example.cryptos.domain.models.CipherMode

data class CipherInfo(
    val alphabet: List<Alphabet>,
    val cipherMode: CipherMode,
    val inputData: List<Alphabet>? = null,
    val isReversed: Boolean =  false,
    val keyIndexes: List<Int> = listOf(),
    val athensParameters: AthensParameters? = null,
    val nextAthensParameters: AthensParameters? = null
)
