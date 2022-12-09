package com.example.cryptos.data.models

data class AthensParameters(private var _alpha:Int,private var _betta: Int){
    val alpha: Int get() = _alpha
    val betta: Int get() = _betta
    fun updateParameters(alpha: Int,betta: Int){
        this._alpha = alpha
        this._betta = betta
    }
}
