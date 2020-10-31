package chapter3.exercises.ex22

import chapter3.Cons
import chapter3.List
import chapter3.List.Companion.empty
import chapter3.Nil
import chapter3.reverse
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

// tag::init[]
/**
Generalize the function you just wrote so that it’s not specific to integers or
addition. Name your generalized function zipWith .
 */
fun <A> zipWith(xa: List<A>, xb: List<A>, f: (A, A) -> A): List<A> {
    tailrec fun go(
        acc: List<A>,
        remA: List<A>,
        remB: List<A>
    ): List<A> =
        when (remA) {
            is Nil -> acc
            is Cons -> when (remB) {
                is Nil -> acc
                is Cons -> go(
                    Cons(f(remA.head, remB.head), acc),
                    remA.tail,
                    remB.tail
                )
            }
        }
    return reverse(go(empty(), xa, xb))
}
// end::init[]

class Exercise22 : WordSpec({
    "list zipWith" should {
        "apply a function to elements of two corresponding lists" {
            zipWith(
                List.of(1, 2, 3),
                List.of(4, 5, 6)
            ) { x, y -> x + y } shouldBe List.of(5, 7, 9)
        }
    }
})
