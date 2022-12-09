package com.example.cryptos.data.mappers

import com.example.cryptos.data.models.Alphabet
import com.example.cryptos.data.models.ResultResponse

internal fun Char.isCirilik(): Boolean = (1040 .. 1103).union(listOf(1025,1105)).contains(this.code)
internal fun Char.isLatin(): Boolean = (65 .. 90).union(97 ..122).contains(this.code)
internal fun Char.isDig(): Boolean = (48 .. 57).contains(this.code)
internal fun Int.getReversedValue(alphabetSize: Int): Int{
    var start = 2
    while (true){
        if((start * this) % alphabetSize == 1) return start
        start++
    }
}
internal fun Int.correctDiv(other: Int): Int = if( this < 0) other - (((-1)*this) % other) else this % other
internal fun Char.mapToAlphabet(): ResultResponse<Alphabet> =
    when (val result = Alphabet.values().find { it.value==this.toString() }){
        null -> ResultResponse.Error<Alphabet>("No character in Alphabet")
        else -> ResultResponse.Success<Alphabet>(result)
    }
internal fun String.transformToAlphabet(): List<Alphabet>
    = map {
        when(val result = it.mapToAlphabet()){
            is ResultResponse.Error -> throw Throwable(result.message)
            is ResultResponse.Success -> result.data!!
        }
    }

internal fun List<Alphabet>.transformToString(): String
        = fold(""){result: String, element: Alphabet ->
            "$result${element.value}"
        }

