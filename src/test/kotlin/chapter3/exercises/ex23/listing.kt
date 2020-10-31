package chapter3.exercises.ex23

import chapter3.Cons
import chapter3.List
import chapter3.Nil
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

// tag::startsWith[]
/**
As an example, implement hasSubsequence for checking whether a List
contains another List as a subsequence. For instance, List(1,2,3,4)
would have List(1,2) , List(2,3) , and List(4) as subsequences, among
others. You may have some difficulty finding a concise purely functional
implementation that is also efficient. That’s okay. Implement the function
however comes most naturally. We’ll return to this implementation in
chapter 5 and hopefully improve on it.
 */
tailrec fun <A> startsWith(l1: List<A>, l2: List<A>): Boolean =
    when (l1) {
        is Nil -> true
        is Cons -> when (l2) {
            is Nil -> true
            is Cons -> if (l1.head != l2.head) false
            else startsWith(l1.tail, l2.tail)
        }
    }
// end::startsWith[]

// tag::init[]
tailrec fun <A> hasSubsequence(xs: List<A>, sub: List<A>): Boolean {
    return when (xs) {
        is Nil -> false
        is Cons -> when (sub) {
            is Nil -> true
            is Cons -> if (startsWith(xs, sub)) true
            else hasSubsequence(xs.tail, sub)
        }
    }
}
// end::init[]

class Exercise23 : WordSpec({
    "list subsequence" should {
        "determine if a list starts with" {
            val xs = List.of(1, 2, 3)
            startsWith(xs, List.of(1)) shouldBe true
            startsWith(xs, List.of(1, 2)) shouldBe true
            startsWith(xs, xs) shouldBe true
            startsWith(xs, List.of(2, 3)) shouldBe false
            startsWith(xs, List.of(3)) shouldBe false
            startsWith(xs, List.of(6)) shouldBe false
        }

        "identify subsequences of a list" {
            val xs = List.of(1, 2, 3, 4, 5)
            hasSubsequence(xs, List.of(1)) shouldBe true
            hasSubsequence(xs, List.of(1, 2)) shouldBe true
            hasSubsequence(xs, List.of(2, 3)) shouldBe true
            hasSubsequence(xs, List.of(3, 4)) shouldBe true
            hasSubsequence(xs, List.of(3, 4, 5)) shouldBe true
            hasSubsequence(xs, List.of(4)) shouldBe true

            hasSubsequence(xs, List.of(1, 4)) shouldBe false
            hasSubsequence(xs, List.of(1, 3)) shouldBe false
            hasSubsequence(xs, List.of(2, 4)) shouldBe false
        }
    }
})
