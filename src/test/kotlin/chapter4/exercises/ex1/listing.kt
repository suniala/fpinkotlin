package chapter4.exercises.ex1

import chapter4.None
import chapter4.Option
import chapter4.Some
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

//tag::init[]
/**
Implement all of the preceding functions on Option . As you implement each
function, try to think about what it means and in what situations you’d use
it. We’ll explore when to use each of these functions next. Here are a few
hints for solving this exercise:

It’s fine to use matching, though you should be able to implement all the
functions besides map and getOrElse without resorting to this
technique.

For map and flatMap , the type signature should be enough to determine
the implementation.

getOrElse returns the result inside the Some case of the Option , or if the
Option is None , returns the given default value.

orElse returns the first Option if it’s defined; otherwise, it returns the
second Option .
 */
fun <A, B> Option<A>.extensionMap(f: (A) -> B): Option<B> = when (this) {
    is Some<A> -> Some(f(this.get))
    else -> None
}

fun <A, B> Option<A>.extensionFlatMap(f: (A) -> Option<B>): Option<B> =
    map(f).getOrElse { None }

fun <A> Option<A>.getOrElse(default: () -> A): A = when (this) {
    is Some<A> -> this.get
    else -> default()
}

fun <A> Option<A>.orElse(ob: () -> Option<A>): Option<A> = when (this) {
    is Some<A> -> this
    else -> ob()
}

fun <A> Option<A>.filter(f: (A) -> Boolean): Option<A> =
    this.extensionFlatMap { if (f(it)) Some(it) else None }
//end::init[]

//tag::alternate[]
fun <A, B> Option<A>.flatMap_2(
    f: (A) -> Option<B>
): Option<B> = this.extensionFlatMap(f)

fun <A> Option<A>.orElse_2(
    ob: () -> Option<A>
): Option<A> = this.orElse(ob)

fun <A> Option<A>.filter_2(
    f: (A) -> Boolean
): Option<A> = this.filter(f)
//end::alternate[]

class Exercise1 : WordSpec({

    val none = Option.empty<Int>()

    val some = Some(10)

    "option map" should {
        "transform an option of some value" {
            some.extensionMap { it * 2 } shouldBe Some(20)
        }
        "pass over an option of none" {
            none.extensionMap { it * 10 } shouldBe None
        }
    }

    "option flatMap" should {
        """apply a function yielding an option to an
            option of some value""" {
            some.extensionFlatMap { a ->
                Some(a.toString())
            } shouldBe Some("10")

            some.flatMap_2 { a ->
                Some(a.toString())
            } shouldBe Some("10")
        }
        "pass over an option of none" {
            none.extensionFlatMap { a ->
                Some(a.toString())
            } shouldBe None

            none.flatMap_2 { a ->
                Some(a.toString())
            } shouldBe None
        }
    }

    "option getOrElse" should {
        "extract the value of some option" {
            some.getOrElse { 0 } shouldBe 10
        }
        "return a default value if the option is none" {
            none.getOrElse { 10 } shouldBe 10
        }
    }

    "option orElse" should {
        "return the option if the option is some" {
            some.orElse { Some(20) } shouldBe some
            some.orElse_2 { Some(20) } shouldBe some
        }
        "return a default option if the option is none" {
            none.orElse { Some(20) } shouldBe Some(20)
            none.orElse_2 { Some(20) } shouldBe Some(20)
        }
    }

    "option filter" should {
        "return some option if the predicate is met" {
            some.filter { it > 0 } shouldBe some
            some.filter_2 { it > 0 } shouldBe some
        }
        "return a none option if the predicate is not met" {
            some.filter { it < 0 } shouldBe None
            some.filter_2 { it < 0 } shouldBe None
        }
    }
})
