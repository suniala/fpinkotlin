package chapter3.exercises.ex1

import chapter3.Cons
import chapter3.List
import chapter3.Nil
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.WordSpec

// tag::init[]
/**
Implement the function tail for removing the first element of a List . Note that the function
takes constant time. What are different choices you could make in your implementation if the
List is Nil ? We’ll return to this question in the next chapter.
 */
fun <A> tail(xs: List<A>): List<A> = when (xs) {
    is Nil -> throw IllegalStateException()
    is Cons<A> -> xs.tail
}
// end::init[]

class Exercise1 : WordSpec({
    "list tail" should {
        "return the the tail when present" {
            tail(List.of(1, 2, 3, 4, 5)) shouldBe
                List.of(2, 3, 4, 5)
        }

        "throw an illegal state exception when no tail is present" {
            shouldThrow<IllegalStateException> {
                tail(Nil)
            }
        }
    }
})
