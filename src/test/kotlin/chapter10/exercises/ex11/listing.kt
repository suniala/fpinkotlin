package chapter10.exercises.ex11

import chapter10.exercises.ex10.Part
import chapter10.exercises.ex10.Stub
import chapter10.exercises.ex10.wcMonoid
import chapter10.exercises.ex7.foldMap
import io.kotlintest.properties.Gen
import io.kotlintest.properties.assertAll
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec
import java.lang.Integer.min

//tag::init1[]
/**
Use the WC monoid to implement a function that counts words in a String by recur-
sively splitting it into substrings and counting the words in those substrings.
 */
fun wordCount(s: String): Int {
    val wc = foldMap(s.toList(), wcMonoid()) {
        Stub(it.toString())
    }
    return when (wc) {
        is Stub ->
            if (wc.chars.isNotEmpty())
                wc.chars.split(" ").size
            else
                0
        is Part -> wc.words +
            min(wc.ls.length, 1) +
            min(wc.rs.length, 1)
    }
}
//end::init1[]

class Exercise11 : WordSpec({

    val words: List<String> =
        "lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do"
            .split(" ")

    "word count" should {
        "count words using balanced folding" {
            assertAll(Gen.list(Gen.from(words))) { ls ->
                val text = ls.joinToString(" ")
                println("${ls.size}: $text")
                wordCount(text) shouldBe ls.size
            }
        }
    }
})
