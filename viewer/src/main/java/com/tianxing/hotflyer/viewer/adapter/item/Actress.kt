package com.tianxing.hotflyer.viewer.adapter.item

/**
 * Project: JAViewer
 */
class Actress : Linkable() {

    lateinit var name: String
        protected set
    lateinit var imageUrl: String
        protected set

    override fun equals(obj: Any?): Boolean {
        if (super.equals(obj)) {
            return true
        }

        return if (obj is Actress) {
            this.name == obj.name
        } else false

    }

    companion object {

        fun create(name: String, imageUrl: String, detailUrl: String): Actress {
            val actress = Actress()
            actress.name = name
            actress.imageUrl = imageUrl
            actress.link = detailUrl
            return actress
        }
    }
}
