package com.example.sdproject.model

enum class NumOffice(val number: Int, val nameOffice: String) {
    BanGiamDoc(1, "Ban Giam Doc"),
    PhongKeToan(2, "Phong Ke Toan"),
    PhongKinhDoanh(3, "Phong Kinh Doanh"),
    PhongThietKe(4, "Phong Thiet Ke");

    companion object {
        fun findOfficeByID(id: Int) = entries.find { it.number == id } ?: PhongThietKe
    }
}

enum class Gender(val express: Boolean, val nameGender: String) {
    NAM(true, "Nam"),
    NU(false, "Nu");

    companion object {
        fun findGenderByValue(gender: Boolean) = entries.find { it.express == gender } ?: NAM
    }
}