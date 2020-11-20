package chapter4.exercises.ex4

import chapter3.Cons
import chapter3.List
import chapter3.Nil
import chapter3.append
import chapter4.None
import chapter4.Option
import chapter4.Some
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

//tag::init[]
/**
Write a function, sequence that combines a list of Options into one Option
containing a list of all the Some values in the original list. If the original list
contains None even once, the result of the function should be None ;
otherwise the result should be Some with a list of all the values.
 */
fun <A> sequence(xs: List<Option<A>>): Option<List<A>> {
    fun seq(rem: List<Option<A>>, acc: List<A>): Option<List<A>> = when (rem) {
        is Cons<Option<A>> -> rem.head.flatMap { seq(rem.tail, append(acc, List.of(it))) }
        is Nil -> Some(acc)
    }

    return seq(xs, List.empty())
}
//end::init[]

class Exercise4 : WordSpec({

    "sequence" should {
        "turn a list of some options into an option of list" {
            val lo =
                List.of(Some(10), Some(20), Some(30))
            sequence(lo) shouldBe Some(List.of(10, 20, 30))
        }
        "turn a list of options containing none into a none" {
            val lo =
                List.of(Some(10), None, Some(30))
            sequence(lo) shouldBe None
        }
    }
})
