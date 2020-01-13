package com.dpoints.datasource.model

class DashboardModel {
    private var title: String? = null
    private var image: Int = 0

    fun getTitle(): String? {
        return title
    }

    fun setTitle(title: String) {
        this.title = title
    }

    fun getImage(): Int {
        return image
    }

    fun setImage(image: Int) {
        this.image = image
    }
}