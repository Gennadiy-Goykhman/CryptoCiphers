package com.example.cryptos.domain.generators

import java.nio.file.FileSystems

interface AlphabetGenerator{
    val modulePath: String get() = FileSystems.getDefault().getPath("").toAbsolutePath().toString().plus("\\app\\src\\main\\java\\")
    val packagePath : String get() = this::class.java.`package`!!.name.replace('.','\\')
    fun generate(className: String = "Alphabet")
}