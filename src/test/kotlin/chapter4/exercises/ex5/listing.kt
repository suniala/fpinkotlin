package chapter4.exercises.ex5

import chapter3.Cons
import chapter3.List
import chapter3.foldRight
import chapter4.None
import chapter4.Option
import chapter4.Some
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

/**
Implement the traverse function. It’s fairly straightforward to do using map
and sequence , but try for a more efficient implementation that only looks at
the list once. When complete, implement sequence by using traverse .
 */
class Exercise5 : WordSpec({

    //tag::traverse[]
    fun <A, B> traverse(
        xa: List<A>,
        f: (A) -> Option<B>
    ): Option<List<B>> = foldRight(
        xa, Some(List.empty<B>()) as Option<List<B>>
    ) { a, b ->
        b.flatMap { acc: List<B> ->
            f(a).map {
                Cons(it, acc)
            }
        }
    }
    //end::traverse[]

    fun <A> sequence(xs: List<Option<A>>): Option<List<A>> =
        traverse(xs) {
            it
        }

    fun <A> catches(a: () -> A): Option<A> =
        try {
            Some(a())
        } catch (e: Throwable) {
            None
        }

    "traverse" should {
        """return some option of an empty list when empty""" {
            val xa = List.empty<Int>()
            traverse(xa) { a: Int ->
                catches { a.toString() }
            } shouldBe Some(List.empty<String>())
        }

        """return some option of a transformed list if all
            transformations succeed""" {
            val xa = List.of(1, 2, 3, 4, 5)
            traverse(xa) { a: Int ->
                catches { a.toString() }
            } shouldBe Some(
                List.of("1", "2", "3", "4", "5")
            )
        }

        "return a none option if any transformations fail" {
            val xa = List.of("1", "2", "x", "4")
            traverse(xa) { a ->
                catches { a.toInt() }
            } shouldBe None
        }
    }

    "sequence" should {
        """turn an empty list of options into an some option of an
            empty list""" {
            val lo = List.empty<Option<Int>>()
            sequence(lo) shouldBe Some(List.empty<Int>())
        }

        "turn a list of some options into an option of list" {
            val lo =
                List.of(Some(10), Some(20), Some(30))
            sequence(lo) shouldBe Some(List.of(10, 20, 30))
        }

        "turn a list of options containing a none into a none" {
            val lo =
                List.of(Some(10), None, Some(30))
            sequence(lo) shouldBe None
        }
    }
})
