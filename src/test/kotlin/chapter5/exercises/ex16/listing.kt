package chapter5.exercises.ex16

import chapter3.List
import chapter5.Stream
import chapter5.toList
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec
import utils.SOLUTION_HERE

/**
Generalize tails to the function scanRight , which is like a foldRight that
returns a stream of the intermediate results. For example:

>>> Stream.of(1, 2, 3).scanRight(0, { a, b -> a + b }).toList()
res1: chapter3.List<kotlin.Int> = Cons(head=6, tail=Cons(head=5,
tail=Cons(head=3,
tail=Cons(head=0,
tail=Nil))))
 */
class Exercise16 : WordSpec({

    //tag::scanright[]
    fun <A, B> Stream<A>.scanRight(z: B, f: (A, () -> B) -> B): Stream<B> =

        SOLUTION_HERE()
    //end::scanright[]

    "Stream.scanRight" should {
        "!behave like foldRight" {
            Stream.of(1, 2, 3)
                .scanRight(0, { a, b ->
                    a + b()
                }).toList() shouldBe List.of(6, 5, 3, 0)
        }
    }
})
