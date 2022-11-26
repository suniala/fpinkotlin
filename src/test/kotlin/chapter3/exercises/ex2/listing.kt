package chapter3.exercises.ex2

import chapter3.Cons
import chapter3.List
import chapter3.Nil
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.WordSpec

// tag::init[]
/**
Using the same idea, implement the function setHead for replacing the first element of a List
with a different value.
 */
fun <A> setHead(xs: List<A>, x: A): List<A> = when (xs) {
    is Nil -> throw IllegalStateException()
    is Cons<A> -> Cons(x, xs.tail)
}
// end::init[]

class Exercise2 : WordSpec({
    "list setHead" should {
        "return a new List with a replaced head" {
            setHead(List.of(1, 2, 3, 4, 5), 6) shouldBe
                List.of(6, 2, 3, 4, 5)
        }

        "throw an illegal state exception when no head is present" {
            shouldThrow<IllegalStateException> {
                setHead(Nil, 6)
            }
        }
    }
})
