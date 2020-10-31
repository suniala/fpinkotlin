package chapter3.exercises.ex12

import chapter3.List
import chapter3.exercises.ex11.reverse
import chapter3.exercises.ex9.foldLeft
import chapter3.foldRight
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

// tag::init[]
/**
Can you write foldLeft in terms of foldRight ? How about the other way
around? Implementing foldRight via foldLeft is useful because it lets us
implement foldRight tail-recursively, which means it works even for large
lists without overflowing the stack.
 */
fun <A, B> foldLeftR(xs: List<A>, z: B, f: (B, A) -> B): B =
    foldRight(reverse(xs), z) { a, b -> f(b, a) }

fun <A, B> foldRightL(xs: List<A>, z: B, f: (A, B) -> B): B =
    foldLeft(reverse(xs), z) { b, a -> f(a, b) }
// end::init[]

class Exercise12 : WordSpec({
    "list foldLeftR" should {
        "implement foldLeft functionality using foldRight" {
            foldLeftR(
                List.of(1, 2, 3, 4, 5),
                0,
                { x, y -> x + y }) shouldBe 15
        }
    }

    "list foldRightL" should {
        "implement foldRight functionality using foldLeft" {
            foldRightL(
                List.of(1, 2, 3, 4, 5),
                0,
                { x, y -> x + y }) shouldBe 15
        }
    }
})
