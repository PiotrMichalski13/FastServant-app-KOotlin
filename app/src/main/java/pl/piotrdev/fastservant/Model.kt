package pl.piotrdev.fastservant

class Model {
    var price: String? = null
    var name : String? = null
    var id: String? = null
    var uwaga: String? = null




    constructor():this("","","") {

    }


    constructor(price: String?, name: String?, uwaga:String? ) {
        this.price = price
        this.name = name
        this.uwaga = uwaga

    }
}