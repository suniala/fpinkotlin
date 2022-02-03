package chapter9.exercises.ex6

import chapter9.solutions.final.ParseError
import chapter9.solutions.final.Parser
import chapter9.solutions.final.ParserDsl

/**
Using flatMap and any other combinators, write the context-sensitive parser we couldn’t
express earlier. The result should be a Parser<Int> that returns the number of char-
acters read. You can use a new primitive called regex to parse digits, which promotes a
regular expression String to a Parser<String> .

Examples of this kind of input are "0" , "1a" , "2aa" , "4aaaa" , and so on.
 */
abstract class Listing : ParserDsl<ParseError>() {
    init {

        //tag::init1[]
        val parser: Parser<Int> = getParser()
        //end::init1[]
    }

    private fun getParser(): Parser<Int> {
        val a: Parser<String> = regex("\\d")
        val b: Parser<List<String>> = a.flatMap { nstr ->
            val n = nstr.toInt()
            listOfN(n, string("a"))
        }
        val c: Parser<String> = b.slice()
        return c.map { s -> s.length }
    }
}
