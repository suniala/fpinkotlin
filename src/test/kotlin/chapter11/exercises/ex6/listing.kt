package chapter11.exercises.ex6

import arrow.Kind
import chapter10.Cons
import chapter10.List
import chapter10.ListFoldable.foldRight
import chapter11.ForOption
import chapter11.Functor
import chapter11.None
import chapter11.OptionOf
import chapter11.Some
import chapter11.fix
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

/**
Hard: Here’s an example of a function we haven’t seen before. Implement the function
filterM . It’s a bit like filter , except that instead of a function from (A) -> Boolean , we
have an (A) -> Kind<F, Boolean> . Replacing various ordinary functions like filter
with the monadic equivalent often yields interesting results. Implement this function,
and then think about what it means for various data types such as Par , Option , and Gen .
 */
interface Monad<F> : Functor<F> {

    fun <A> unit(a: A): Kind<F, A>
    fun <A, B> flatMap(fa: Kind<F, A>, f: (A) -> Kind<F, B>): Kind<F, B>

    override fun <A, B> map(fa: Kind<F, A>, f: (A) -> B): Kind<F, B> =
        flatMap(fa) { a -> unit(f(a)) }

    //tag::init[]
    fun <A> filterM(
        ms: List<A>,
        f: (A) -> Kind<F, Boolean>
    ): Kind<F, List<A>> {
        return foldRight(ms, unit(List.empty())) { curr, included ->
            flatMap(f(curr)) { include ->
                if (include) map(included) { l -> Cons(curr, l) }
                else included
            }
        }
    }
//end::init[]
}

object Monads {
    fun optionMonad(): Monad<ForOption> = object : Monad<ForOption> {
        override fun <A> unit(a: A): OptionOf<A> = Some(a)

        override fun <A, B> flatMap(
            fa: OptionOf<A>,
            f: (A) -> OptionOf<B>
        ): OptionOf<B> =
            chapter11.optionMonad.flatMap(fa.fix()) { f(it).fix() }
    }
}

class Exercise6 : WordSpec({
    "option monad" should {
        val monad = Monads.optionMonad()

        "should produce Some as a unit" {
            monad.unit("1") shouldBe Some("1")
        }

        "omit those items that yield Some(false)" {
            monad.filterM(List.of(1, 2, 3, 4, 5)) {
                if (it % 2 == 0) Some(true)
                else Some(false)
            } shouldBe Some(List.of(2, 4))
        }

        "produce a Some empty list if all items yield None" {
            monad.filterM(List.of(1, 2, 3, 4, 5)) {
                Some(false)
            } shouldBe Some(List.empty<Int>())
        }

        "produce None if any item yields None" {
            monad.filterM(List.of(1, 2, 3, 4, 5)) {
                if (it != 4) Some(true)
                else None
            } shouldBe None
        }
    }
})
