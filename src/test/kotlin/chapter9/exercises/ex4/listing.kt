package chapter9.exercises.ex4

import chapter9.solutions.final.ParseError
import chapter9.solutions.final.Parser
import chapter9.solutions.final.ParserDsl

/**
Hard: Implement the listOfN combinator introduced earlier using map2 and succeed .
 */
abstract class Listing : ParserDsl<ParseError>() {

    fun <A, B, C> map2(
        pa: Parser<A>,
        pb: Parser<B>,
        f: (A, B) -> C
    ): Parser<C> = TODO()

    init {
        //tag::init1[]
        fun <A> listOfN(n: Int, pa: Parser<A>): Parser<List<A>> =
            if (n < 1) succeed(emptyList())
            else map2(pa, listOfN(n - 1, pa)) { a, al ->
                a.cons(al)
            }
        //end::init1[]
    }
}
