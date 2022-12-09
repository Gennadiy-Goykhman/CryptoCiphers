package com.example.cryptos.domain.generators
import com.example.cryptos.data.mappers.isCirilik
import com.example.cryptos.data.mappers.isDig
import com.example.cryptos.data.mappers.isLatin
import com.example.cryptos.domain.generators.AlphabetGenerator
import java.io.File

class Generator: AlphabetGenerator {
    override val packagePath: String
        get() = "com/example/cryptos/data/models"

    override fun generate(className: String){
        val file = File(modulePath+packagePath+"\\${className}.kt").apply { createNewFile() }
            file.bufferedWriter().use { builder ->
                builder.write("package ${packagePath.replace('/','.')}"+"\n")
                builder.newLine()
                builder.write("enum class $className(val value: String, val number: Int, val type: String){")
                builder.newLine()
                var counter = 0
                repeat(16383){
                    val currentChar = it.toChar()
                    if(currentChar.isLatin() ||
                        currentChar.isCirilik() ||
                        currentChar.isDig()){
                        val type = when {
                            currentChar.isLatin() && currentChar.isUpperCase()-> "LC"
                            currentChar.isLatin()->"L"
                            currentChar.isCirilik() && currentChar.isUpperCase() -> "CC"
                            currentChar.isCirilik()->"C"
                            currentChar.isDig() -> "N"
                            else -> "U"
                        }
                        builder.write("\t`${it.toChar()}`(value = \"${it.toChar()}\", " +
                                "number = ${currentChar.code}, type= \"${type}\"),\n")
                    }
                    counter++
                }
                builder.write("}")
                builder.close()
        }
    }
}
