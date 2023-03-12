package com.jiwondev.trep.ui.activity
import com.jiwondev.trep.ui.activity.Color.*

class test2(val height: Int, val width: Int) {
    val list = arrayListOf(11, 22, 33)

    fun test() {
        for((a, b) in list.withIndex()) {

        }
    }
}

interface Name
class Minsu(val mbti: String): Name
class Chulsu: Name




enum class Color(
    val r: Int,
    val g: Int,
    val b: Int
) {
    RED(255, 0, 0), ORANGE(255, 165, 0),
    YELLOW(255, 255, 0), GREEN(0, 255, 0), BLUE(0, 0, 255),
    INDIGO(75, 0, 130), VIOLET(238, 130, 238);

    fun rgb() = (r * 256 + g) * 256 + b
}