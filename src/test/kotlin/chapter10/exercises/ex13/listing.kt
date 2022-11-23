package chapter10.exercises.ex13

import chapter10.ForList
import chapter10.ListOf
import chapter10.asConsList
import chapter10.exercises.ex12.Foldable
import chapter10.fix
import chapter10.stringMonoid
import io.kotlintest.properties.assertAll
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

//tag::init1[]
/**
Implement Foldable<ForList> using the Foldable<F> interface from the previous
exercise.
 */
object ListFoldable : Foldable<ForList> {
    override fun <A, B> foldRight(
        fa: ListOf<A>,
        z: B,
        f: (A, B) -> B
    ): B =
        fa.fix().foldRight(z, f)

    override fun <A, B> foldLeft(
        fa: ListOf<A>,
        z: B,
        f: (B, A) -> B
    ): B {
        return fa.fix().foldLeft(z, f)
    }
}
//end::init1[]

class Exercise13 : WordSpec({
    "ListFoldable" should {
        "foldRight" {
            assertAll<List<Int>> { ls ->
                ListFoldable.foldRight(
                    ls.asConsList(),
                    0,
                    { a, b -> a + b }
                ) shouldBe ls.sum()
            }
        }
        "foldLeft" {
            assertAll<List<Int>> { ls ->
                ListFoldable.foldLeft(
                    ls.asConsList(),
                    0,
                    { b, a -> a + b }) shouldBe ls.sum()
            }
        }
        "foldMap" {
            assertAll<List<Int>> { ls ->
                ListFoldable.foldMap(
                    ls.asConsList(),
                    stringMonoid
                ) { it.toString() } shouldBe ls.joinToString("")
            }
        }
    }
})
