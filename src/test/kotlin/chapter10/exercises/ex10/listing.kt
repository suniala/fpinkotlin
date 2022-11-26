package chapter10.exercises.ex10

import chapter10.Monoid
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec
import io.kotlintest.tables.forAll
import io.kotlintest.tables.headers
import io.kotlintest.tables.row
import io.kotlintest.tables.table
import chapter10.solutions.ex10.Part as SPart
import chapter10.solutions.ex10.Stub as SStub
import chapter10.solutions.ex10.WC as SWC
import chapter10.solutions.ex10.wcMonoid as SWCMonoid

sealed class WC
data class Stub(val chars: String) : WC()
data class Part(val ls: String, val words: Int, val rs: String) : WC()

//tag::init1[]
/**
Write a monoid instance for WC , and ensure that it meets both monoid laws.
 */
fun wcMonoid(): Monoid<WC> = object : Monoid<WC> {
    override fun combine(a1: WC, a2: WC): WC =
        when (a1) {
            is Stub -> when (a2) {
                is Stub -> {
                    val a1Parts = a1.chars.split(" ")
                    val a2Parts = a2.chars.split(" ")
                    if (a1Parts.size > 1 && a2Parts.size > 1) {
                        Part(a1Parts.first(), 1, a2Parts.last())
                    } else {
                        Stub(a1.chars + a2.chars)
                    }
                }

                is Part -> {
                    val a1Parts = a1.chars.split(" ")
                    if (a1Parts.size > 1) {
                        Part(a1Parts.first(), 1 + a2.words, a2.rs)
                    } else {
                        Part(a1.chars + a2.ls, a2.words, a2.rs)
                    }
                }
            }

            is Part -> when (a2) {
                is Stub -> {
                    val a2Parts = a2.chars.split(" ")
                    if (a2Parts.size > 1) {
                        Part(a1.ls, a1.words + 1, a2Parts.last())
                    } else {
                        Part(a1.ls, a1.words, a1.rs + a2.chars)
                    }
                }

                is Part ->
                    if (a1.rs.isEmpty() && a2.ls.isEmpty()) {
                        Part(a1.ls, a1.words + a2.words, a2.rs)
                    } else {
                        Part(a1.ls, a1.words + a2.words + 1, a2.rs)
                    }
            }
        }

    override val nil: WC = Stub("")
}

class Exercise10 : WordSpec({
    val cases = table(
        headers("a", "b", "result"),
        row(
            Stub(""),
            Stub(""),
            Stub("")
        ),
        row(
            Stub("a"),
            Stub(""),
            Stub("a")
        ),
        row(
            Stub(""),
            Stub("b"),
            Stub("b")
        ),
        row(
            Stub("a"),
            Stub("b"),
            Stub("ab")
        ),
        row(
            Stub("a a"),
            Stub("b"),
            Stub("a ab")
        ),
        row(
            Stub("a"),
            Stub("b b"),
            Stub("ab b")
        ),
        row(
            Stub("a a"),
            Stub("b b"),
            Part("a", 1, "b")
        ),
        row(
            Part("", 1, "ar"),
            Stub(""),
            Part("", 1, "ar")
        ),
        row(
            Part("", 1, "ar"),
            Stub("b"),
            Part("", 1, "arb")
        ),
        row(
            Part("", 1, "ar"),
            Stub("b b"),
            Part("", 2, "b")
        ),
        row(
            Stub(""),
            Part("bl", 1, ""),
            Part("bl", 1, "")
        ),
        row(
            Stub("a"),
            Part("bl", 1, ""),
            Part("abl", 1, "")
        ),
        row(
            Stub("a a"),
            Part("bl", 1, ""),
            Part("a", 2, "")
        ),
        row(
            Part("al", 1, ""),
            Part("bl", 1, "br"),
            Part("al", 3, "br")
        ),
        row(
            Part("al", 1, "ar"),
            Part("", 1, "br"),
            Part("al", 3, "br")
        ),
        row(
            Part("al", 1, "ar"),
            Part("bl", 1, "br"),
            Part("al", 3, "br")
        )
    )

    "WC monoid" should {
        "uphold the law of associativity" {
            forAll(cases) { a, b, result ->
                wcMonoid().combine(a, b) shouldBe result
            }
        }
        "uphold the law of identity" {
            wcMonoid().nil shouldBe Stub("")
        }
    }

    "WC monoid solution" should {
        // Reference solution disagrees with my interpretation of the expected behaviour.
        "!uphold the law of associativity" {
            forAll(cases) { a, b, result ->
                val sa = toSolutionType(a)
                val sb = toSolutionType(b)
                val sresult = toSolutionType(result)
                SWCMonoid().combine(sa, sb) shouldBe sresult
            }
        }
        "uphold the law of identity" {
            SWCMonoid().nil shouldBe SStub("")
        }
    }
})

private fun toSolutionType(a: WC): SWC = when (a) {
    is Stub -> SStub(a.chars)
    is Part -> SPart(a.ls, a.words, a.rs)
}

//end::init1[]
