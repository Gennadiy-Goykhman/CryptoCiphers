package com.example.cryptos.presentation.temp_app

import com.example.cryptos.data.mappers.mapToAlphabet
import com.example.cryptos.data.mappers.transformToString
import com.example.cryptos.data.models.Alphabet
import com.example.cryptos.data.models.AthensParameters
import com.example.cryptos.data.models.CipherInfo
import com.example.cryptos.data.models.KeyPart
import com.example.cryptos.domain.models.AlphabetMode
import com.example.cryptos.domain.models.CipherMode
import com.example.cryptos.domain.usecases.GenerateKeyChangeUseCase
import com.example.cryptos.domain.usecases.GetChangeAlphabetUseCase
import com.example.cryptos.domain.usecases.GetCipherTextUseCase
import com.example.cryptos.domain.usecases.GetOpenTextUseCase
import java.util.Collections.max
import kotlin.math.max
import kotlin.random.Random

fun main() {
    TempApp.init()
}

object TempApp{
    private var openText: String = ""
    private var cipherText: String = ""
    private var cipherMode: CipherMode? = null
    private var alphabet: List<Alphabet>? = null
    private lateinit var keyChange: List<KeyPart>
    private var autoGenerateKey = false
    private var execMode: ExecuteMode = ExecuteMode.ENCODE

    fun init(){
        println("""
            Welcome to the cipher app!
            =========================================================
            """.trimIndent())
        makeChoice()
        chooseAlphabet()
        inputData()
        chooseKey()
        getResult()
    }

    private fun makeChoice(){
        println("Choose the cipher, that you would like to use:")
        CipherMode.values().forEachIndexed { index, cipherMode -> println("${index+1}) ${cipherMode.name}") }
        println("Enter index of your choice: ")
        val choiceNumber = readLine()!!.toInt()
        analyzeCipherChoice(choiceNumber)
    }

    private fun chooseAlphabet(){
        println("Choose the alphabet, that you would like to use:")
        AlphabetMode.values().forEachIndexed { index, cipherMode -> println("${index+1}) ${cipherMode.name}") }
        println("Enter index of your choice: ")
        val choiceNumber = readLine()!!.toInt()
        analyzeAlphabetChoice(choiceNumber)
    }

    private fun inputData(){
        inputDataMode()
        println("Input data to transform: ")
        val inputText = readLine()!!
            .replace(" ","")
            .replace("\n","")
            .replace(",","")
            .replace(".","")
            .replace(";","")
            .trim()
        when(execMode){
            ExecuteMode.ENCODE -> openText = inputText
            ExecuteMode.DECODE -> cipherText = inputText
        }
    }

    private fun getResult(){
        println("""Your key change is: 
                   |${keyChange.map { it.symbolOrigin }.fold("") { str, alph -> "$str $alph" }}
                   |${keyChange.map { it.symbolChange }.fold("") { str, alph -> "$str $alph" }}
            """.trimMargin())
        println("Final result is:")
        val result = when (execMode) {
            ExecuteMode.ENCODE -> GetCipherTextUseCase(
                openText = openText.map { it.mapToAlphabet().data!! },
                keyText = keyChange
            )
            ExecuteMode.DECODE -> GetOpenTextUseCase(
                cipherText = cipherText.map { it.mapToAlphabet().data!! },
                keyText = keyChange
            )
        }
        println(result.transformToString())
    }

    private fun inputDataMode(){
        println("""
            What do you want to execute? Enter Index:
            1) Encoding
            2) Decoding
            """.trimIndent())
        execMode = when(readLine()!!.toInt()){
            1 -> ExecuteMode.ENCODE
            2 -> ExecuteMode.DECODE
            else -> {
                resetData(ResetLevel.INPUTMODE)
                println("Please, try again\n")
                inputData()
                ExecuteMode.ENCODE
            }
        }
    }

    private fun chooseKey(){
        println("""
            Do you want to generate key?
            In other case you should enter it by yourself
            
            Enter YES or NO
            """.trimIndent())
        val choice = readLine()!!
        when(choice){
            "YES" -> {
                autoGenerateKey = true
                keyChange = GenerateKeyChangeUseCase(
                    CipherInfo(
                        alphabet = alphabet!!,
                        cipherMode = cipherMode!!,
                        inputData = when(execMode){
                            ExecuteMode.ENCODE -> openText.map{it.mapToAlphabet().data!!}
                            ExecuteMode.DECODE -> cipherText.map{it.mapToAlphabet().data!!}
                        },
                        isReversed = execMode==ExecuteMode.DECODE,
                        athensParameters = AthensParameters(max(openText.length, cipherText.length)-1,max(openText.length, cipherText.length)-2),
                        nextAthensParameters = AthensParameters(max(openText.length, cipherText.length)+1,max(openText.length, cipherText.length)+2)
                    )
                )
            }
            "NO" -> {
                autoGenerateKey = false
                enterKey()
            }
            else -> {
                resetData(ResetLevel.KEY_CHOOSE)
                println("Sorry, enter your answer again\n")
                chooseKey()
            }
        }
    }


    private fun enterKey(){
        keyChange = when(cipherMode){
            CipherMode.ORDINARY_CHANGE ->{
                println("Enter key change - one to one conformity")
                alphabet!!.forEach { print("${it.value} ") }
                println()
                readLine()!!
                    .split(" ")
                    .mapIndexed { index, changeLettter ->
                        KeyPart(
                            symbolOrigin = alphabet!!.get(index),
                            symbolChange = changeLettter[0].mapToAlphabet().data ?: throw Throwable("There is no letter like this in alphabet")
                        )
                    }
            }
            CipherMode.ATHENS -> {
                println("Enter key pair with a space")
                readLine()!!
                    .split(" ")
                    .map { it.toInt() }
                    .run {
                        val params = AthensParameters(first(), last())
                        GenerateKeyChangeUseCase(
                            CipherInfo(
                                alphabet = alphabet!!,
                                cipherMode = cipherMode!!,
                                inputData = when (execMode) {
                                    ExecuteMode.ENCODE -> openText.map { it.mapToAlphabet().data!! }
                                    ExecuteMode.DECODE -> cipherText.map { it.mapToAlphabet().data!! }
                                },
                                isReversed = execMode == ExecuteMode.DECODE,
                                athensParameters = params
                            )
                        )
                    }
            }
            CipherMode.ATHENS_RECURSIVE ->{
                MutableList(2){
                    println("Enter ${it+1} pair with a space")
                    readLine()!!
                } .map {
                    it.split(" ")
                        .map { it.toInt() }
                        .run {
                            AthensParameters(first(),last())
                        }
                }.run {
                    GenerateKeyChangeUseCase(
                        CipherInfo(
                            alphabet = alphabet!!,
                            cipherMode = cipherMode!!,
                            inputData = when (execMode) {
                                ExecuteMode.ENCODE -> openText.map { it.mapToAlphabet().data!! }
                                ExecuteMode.DECODE -> cipherText.map { it.mapToAlphabet().data!! }
                            },
                            isReversed = execMode == ExecuteMode.DECODE,
                            athensParameters = first(),
                            nextAthensParameters = last()
                        )
                    )
                }
            }
            else -> {
                throw Throwable("Error occurred while generating key")
            }
        }
    }

    private fun analyzeAlphabetChoice(choiceNumber: Int){
        alphabet = GetChangeAlphabetUseCase(when(choiceNumber){
                1 -> AlphabetMode.CYRILIK
                2 -> AlphabetMode.LATIN
                3 -> AlphabetMode.NUMBERS
                4 -> AlphabetMode.ALL
                5 -> AlphabetMode.LATIN_CAPITALS
                else -> {
                    resetData(ResetLevel.ALPHABET)
                    println("Please? make another choice")
                    chooseAlphabet()
                    AlphabetMode.ALL
                }
            })
    }

    private fun analyzeCipherChoice(choiceNumber: Int){
        cipherMode =
            when(choiceNumber){
                1 -> CipherMode.ATHENS
                2 -> CipherMode.ATHENS_RECURSIVE
                3 -> CipherMode.ORDINARY_CHANGE
                else -> {
                    resetData(ResetLevel.OPENTEXT)
                    println("Please? make another choice")
                    makeChoice()
                    null
                }
            }
    }


    private fun resetData(level: ResetLevel) =
        when(level){
            ResetLevel.OPENTEXT ->{
                openText = ""
                cipherMode = null
                cipherText = ""
                alphabet = listOf()
                execMode = ExecuteMode.ENCODE
            }
            ResetLevel.ALPHABET ->{
                openText = ""
                cipherText = ""
                alphabet = listOf()
                execMode = ExecuteMode.ENCODE
            }
            ResetLevel.INPUTMODE ->{
                openText = ""
                cipherText = ""
                execMode = ExecuteMode.ENCODE
            }
            ResetLevel.KEY_CHOOSE ->{

            }
            ResetLevel.KEY ->{

            }
            ResetLevel.CIPHERTEXT ->{

            }
    }

    private enum class ResetLevel {
        OPENTEXT, ALPHABET,INPUTMODE, KEY, KEY_CHOOSE, CIPHERTEXT;
    }

    private enum class ExecuteMode {
        ENCODE, DECODE
    }

}

