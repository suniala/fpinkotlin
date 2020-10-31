package chapter3.exercises.ex9

import chapter3.Cons
import chapter3.List
import chapter3.Nil
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

// tag::init[]
/**
Our implementation of foldRight is not tail-recursive and will result in a
StackOverflowError for large lists (we say it’s not stack-safe). Convince
yourself that this is the case, and then write another general list-recursion
function, foldLeft, that is tail-recursive, using the techniques we discussed
in the previous chapter.
 */
tailrec fun <A, B> foldLeft(xs: List<A>, z: B, f: (B, A) -> B): B =
    when (xs) {
        is Nil -> z
        is Cons<A> -> foldLeft(xs.tail, f(z, xs.head), f)
    }
// end::init[]

class Exercise9 : WordSpec({
    "list foldLeft" should {
        """apply a function f providing a zero accumulator from tail
            recursive position""" {
            foldLeft(
                List.of(1, 2, 3, 4, 5),
                0,
                { x, y -> x + y }) shouldBe 15
        }
    }
})
