package com.tyganeutronics.myratecalculator.models

import com.tyganeutronics.myratecalculator.R

class BOND constructor(id: String, rate: Double) : Currency(id, rate) {

    constructor(rate: Double) : this("BOND", rate)

    override fun getSign(): String {
        return "$"
    }

    override fun getName(): Int {
        return R.string.currency_bond
    }
}