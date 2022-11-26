package chapter11.exercises.ex1

import arrow.Kind
import arrow.core.ForListK
import arrow.core.ForSequenceK
import arrow.core.ListK
import arrow.core.SequenceK
import arrow.core.fix
import chapter10.ForList
import chapter10.ForOption
import chapter10.List
import chapter10.Option
import chapter10.OptionOf
import chapter10.fix
import chapter11.ForPar
import chapter11.Par
import chapter11.fix
import chapter11.sec2.Monad
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

//tag::init1[]
/**
Write monad instances for Par , Option , and List . Additionally, provide monad instances
for arrow.core.ListK and arrow.core.SequenceK .
 */
object Monads {

    fun parMonad(): Monad<ForPar> = object : Monad<ForPar> {
        override fun <A> unit(a: A): Kind<ForPar, A> = Par.unit(a)

        override fun <A, B> flatMap(
            fa: Kind<ForPar, A>,
            f: (A) -> Kind<ForPar, B>
        ): Kind<ForPar, B> = fa.fix().flatMap { f(it).fix() }
    }

    fun optionMonad(): Monad<ForOption> = object : Monad<ForOption> {
        override fun <A> unit(a: A): OptionOf<A> = Option.of(a)

        override fun <A, B> flatMap(
            fa: OptionOf<A>,
            f: (A) -> OptionOf<B>
        ): OptionOf<B> = fa.fix().flatMap { f(it).fix() }
    }

    fun listMonad(): Monad<ForList> = object : Monad<ForList> {
        override fun <A> unit(a: A): Kind<ForList, A> = List.of(a)

        override fun <A, B> flatMap(
            fa: Kind<ForList, A>,
            f: (A) -> Kind<ForList, B>
        ): Kind<ForList, B> = fa.fix().flatMap { f(it).fix() }
    }

    fun listKMonad(): Monad<ForListK> = object : Monad<ForListK> {
        override fun <A> unit(a: A): Kind<ForListK, A> = ListK.just(a)

        override fun <A, B> flatMap(
            fa: Kind<ForListK, A>,
            f: (A) -> Kind<ForListK, B>
        ): Kind<ForListK, B> = fa.fix().flatMap { f(it).fix() }
    }

    fun sequenceKMonad(): Monad<ForSequenceK> =
        object : Monad<ForSequenceK> {
            override fun <A> unit(a: A): Kind<ForSequenceK, A> =
                SequenceK.just(a)

            override fun <A, B> flatMap(
                fa: Kind<ForSequenceK, A>,
                f: (A) -> Kind<ForSequenceK, B>
            ): Kind<ForSequenceK, B> = fa.fix().flatMap { f(it).fix() }
        }
}
//end::init1[]

class Exercise1 : WordSpec({
    "monads" should {
        "option" {
            val monad = Monads.optionMonad()
            monad.unit("1") shouldBe Option.of("1")
            monad.map(Option.of(42)) { v -> "foo-$v" }
                .shouldBe(Option.of("foo-42"))
            monad.flatMap(Option.of(42)) { v -> Option.of("foo-$v") }
                .shouldBe(Option.of("foo-42"))
            monad.flatMap(Option.of(42)) { Option.empty<String>() }
                .shouldBe(Option.empty())
            monad
                .flatMap(Option.empty<Int>()) { v ->
                    Option.of("foo-$v")
                }
                .shouldBe(Option.empty())
        }
    }
})
