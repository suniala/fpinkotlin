package chapter10.exercises.ex7

import chapter10.Monoid
import chapter10.stringMonoid
import chapter2.exercises.ex2.head
import chapter7.solutions.ex6.Pars.splitAt
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

//tag::init1[]
/**
Implement foldMap based on the balanced fold technique. Your implementation
should use the strategy of splitting the sequence in two, recursively processing each
half, and then adding the answers together using the provided monoid.
 */
fun <A, B> foldMap(la: List<A>, m: Monoid<B>, f: (A) -> B): B =
    when (la.size) {
        0 -> {
            m.nil
        }

        1 -> {
            f(la.head)
        }

        else -> {
            val (subLa, subRa) = la.splitAt(la.size / 2)
            val lb = foldMap(subLa, m, f)
            val rb = foldMap(subRa, m, f)
            m.combine(lb, rb)
        }
    }
//end::init1[]

class Exercise7 : WordSpec({
    "balanced folding foldMap" should {
        "fold a list with an even number of values" {
            foldMap(
                listOf("lorem", "ipsum", "dolor", "sit"),
                stringMonoid
            ) { it } shouldBe "loremipsumdolorsit"
        }
        "fold a list with an odd number of values" {
            foldMap(
                listOf("lorem", "ipsum", "dolor"),
                stringMonoid
            ) { it } shouldBe "loremipsumdolor"
        }
        "fold a list with a single value" {
            foldMap(
                listOf("lorem"),
                stringMonoid
            ) { it } shouldBe "lorem"
        }
        "fold an empty list" {
            foldMap(
                emptyList<String>(),
                stringMonoid
            ) { it } shouldBe ""
        }
    }
})
