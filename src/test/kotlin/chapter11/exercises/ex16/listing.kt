package chapter11.exercises.ex16

import arrow.Kind
import chapter11.Functor

interface Monad<F> : Functor<F> {
    fun <A> unit(a: A): Kind<F, A>
    fun <A, B> flatMap(fa: Kind<F, A>, f: (A) -> Kind<F, B>): Kind<F, B>
}

//tag::init1[]
/**
Implement map , flatMap , and unit as methods on this class, and give an implementa-
tion for Monad<Id> :
 */
data class Id<out A>(val a: A) : IdOf<A> {
    companion object {
        fun <A> unit(a: A): Id<A> = Id(a)
    }

    fun <B> flatMap(f: (A) -> Id<B>): Id<B> = f(a)

    fun <B> map(f: (A) -> B): Id<B> = unit(f(a))
}
//end::init1[]

class ForId private constructor() {
    companion object
}

typealias IdOf<A> = Kind<ForId, A>

fun <A> IdOf<A>.fix() = this as Id<A>

//tag::init2[]
fun idMonad(): Monad<ForId> = object : Monad<ForId> {
    override fun <A, B> map(
        fa: IdOf<A>,
        f: (A) -> B
    ): IdOf<B> = fa.fix().map(f)

    override fun <A> unit(a: A): IdOf<A> = Id.unit(a)

    override fun <A, B> flatMap(
        fa: IdOf<A>,
        f: (A) -> IdOf<B>
    ): IdOf<B> = fa.fix().flatMap { a -> f(a).fix() }
}
//end::init2[]

fun main() {
    val IM: Monad<ForId> = idMonad()
    val id: Id<String> = IM.flatMap(Id("Hello, ")) { a: String ->
        IM.flatMap(Id("monad!")) { b: String ->
            Id(a + b)
        }
    }.fix()
    println(IM)
    println(id)
}
