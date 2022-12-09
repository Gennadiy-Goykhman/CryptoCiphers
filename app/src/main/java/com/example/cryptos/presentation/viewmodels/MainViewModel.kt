package com.example.cryptos.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.example.cryptos.domain.models.AlphabetMode
import com.example.cryptos.domain.usecases.GenerateKeyChangeUseCase
import com.example.cryptos.domain.usecases.GetChangeAlphabetUseCase
import com.example.cryptos.domain.usecases.GetCipherTextUseCase

class MainViewModel: ViewModel() {
    fun getCipherText(){
//        val alphabet = GetChangeAlphabetUseCase(AlphabetMode.LATIN)
//        val keyChange = GenerateKeyChangeUseCase(alphabet)
//        val cipherText = GetCipherTextUseCase(listOf(),keyChange)
    }
}