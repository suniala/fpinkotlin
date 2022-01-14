package chapter9.exercises.ex1

import chapter9.ParseError
import chapter9.solutions.final.Parser
import chapter9.solutions.final.ParserDsl

/**
Using product , implement the now-familiar combinator map2 . In turn, use this to
implement many1 in terms of many .
 */
abstract class Listing : ParserDsl<ParseError>() {

    //tag::init1[]
    override fun <A, B, C> map2(
        pa: Parser<A>,
        pb: () -> Parser<B>,
        f: (A, B) -> C
    ): Parser<C> =
        pa.product(pb).map { (a, b) -> f(a, b) }
    //end::init1[]

    //tag::init2[]
    override fun <A> many1(p: Parser<A>): Parser<List<A>> =
        map2(p, { many(p) }) { a, b ->
            a.cons(b)
        }
    //end::init2[]
}
