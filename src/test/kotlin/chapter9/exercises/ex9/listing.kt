package chapter9.exercises.ex9

sealed class JSON

object JNull : JSON()
data class JNumber(val get: Double) : JSON()
data class JString(val get: String) : JSON()
data class JBoolean(val get: Boolean) : JSON()
data class JArray(val get: List<JSON>) : JSON()
data class JObject(val get: Map<String, JSON>) : JSON()

object ParseError

interface Parser<A>

/**
Hard: At this point, you are going to take over the design process. You’ll be creating
Parser<JSON> from scratch using the primitives we’ve defined. You don’t need to
worry about the representation of Parser just yet. As you go, you’ll undoubtedly dis-
cover additional combinators and idioms, notice and factor out common patterns,
and so on. Use the skills you’ve been developing throughout this book, and have fun!
If you get stuck, you can always consult the tips in appendix A or the final solution in
appendix B.

Here are some basic guidelines to help you in the exercise:
 Any general-purpose combinators you discover can be declared in the Parsers
abstract class directly. These are top-level declarations with no implementation.
 Any syntactic sugar can be placed in another abstract class called ParsersDsl
that extends from Parsers . Make generous use of infix , along with anything
else in your Kotlin bag of tricks to make the final JSONParser as easy to use as
possible. The functions implemented here should all delegate to declarations
in Parsers .
 Any JSON-specific combinators can be added to JSONParser , which extends
ParsersDsl .
 You’ll probably want to introduce combinators that make it easier to parse the
tokens of the JSON format (like string literals and numbers). For this, you can
use the regex primitive we introduced earlier. You can also add a few primitives
like letter , digit , whitespace , and so on to build up your token parsers.

NOTE: This exercise is about defining the algebra consisting of primitive and
combinator declarations only. No implementations should appear in the final
solution.
 */
//tag::init[]
@Suppress("UNUSED_PARAMETER")
abstract class Parsers<PE> {

    // primitives

    internal fun string(s: String): Parser<String> = TODO()

    internal fun regex(r: String): Parser<String> = TODO()

    internal fun <A> slice(p: Parser<A>): Parser<String> = TODO()

    internal fun <A> succeed(a: A): Parser<A> = TODO()

    internal fun <A, B> flatMap(
        p1: Parser<A>,
        f: (A) -> Parser<B>
    ): Parser<B> = TODO()

    internal fun <A> or(
        p1: Parser<out A>,
        p2: () -> Parser<out A>
    ): Parser<A> = TODO()

    // other combinators here

    internal fun <A> many(pa: Parser<A>): Parser<List<A>> = TODO()

    internal fun <A, B> map(pa: Parser<A>, f: (A) -> B): Parser<B> = TODO()

    internal fun <A, B> product(
        pa: Parser<A>,
        pb: () -> Parser<B>
    ): Parser<Pair<A, B>> = TODO()

    internal fun <A, B> takeLeft(pab: Parser<Pair<A, B>>): Parser<A> =
        TODO()

    internal fun <A, B> takeRight(pab: Parser<Pair<A, B>>): Parser<B> =
        TODO()

    internal fun whitespace(): Parser<Char> = TODO()

    /**
     * Match a string value in quotes but return it without quotes
     */
    internal fun quotedString(): Parser<String> = TODO()
}

abstract class ParsersDsl<PE> : Parsers<PE>() {
    // syntactic sugar here
    fun <A, B> Parser<A>.flatMap(f: (A) -> Parser<B>): Parser<B> =
        flatMap(this, f)

    fun <A, B> Parser<A>.map(f: (A) -> B): Parser<B> = map(this, f)

    infix fun <A, B> Parser<A>.product(pb: () -> Parser<B>): Parser<Pair<A, B>> =
        product(this, pb)

    fun <A, B> Parser<Pair<A, B>>.takeLeft() = takeLeft(this)

    fun <A, B> Parser<Pair<A, B>>.takeRight() = takeRight(this)

    infix fun <A> Parser<A>.or(that: Parser<A>): Parser<A> =
        or(this) { that }

    fun <A> Parser<A>.many(): Parser<List<A>> = many(this)
}

@Suppress("unused")
abstract class JSONParsers : ParsersDsl<ParseError>() {
    /**
     * Matches the given token and consumes any preceding or following whitespace.
     */
    private fun token(token: String): Parser<Unit> =
        whitespace().many()
            .product { string(token) }
            .product { whitespace().many() }
            .map {}

    private val keyValuePair: Parser<Pair<String, JSON>> =
        quotedString()
            .product { token(":") }
            .takeLeft()
            .product { jsonParser }
            .map { (key, value) ->
                Pair(key, value)
            }

    private val jnull: Parser<JSON> =
        token("null").map { JNull as JSON }

    private val jnumber: Parser<JSON> =
        regex("\\d+").map { JNumber(it.toDouble()) as JSON }

    private val jstring: Parser<JSON> =
        quotedString().map { withoutQuotes ->
            JString(withoutQuotes) as JSON
        }

    private val jboolean: Parser<JSON> =
        token("true").map { JBoolean(true) as JSON } or
            token("false").map { JBoolean(false) as JSON }

    private val jarray: Parser<JSON> =
        token("[")
            .product { jsonParser }
            .takeRight()
            .product {
                token(",")
                    .product { jsonParser }
                    .takeRight()
                    .many()
            }
            .product {
                token("]")
            }
            .takeLeft()
            .map { (first, following) -> JArray(listOf(first) + following) as JSON }

    private val jobject: Parser<JSON> =
        token("{")
            .product { keyValuePair }
            .takeRight()
            .product {
                token(",")
                    .product { keyValuePair }
                    .takeRight()
                    .many()
            }
            .map { (first, following) -> listOf(first) + following }
            .product { token("}") }
            .takeLeft()
            .map { keyValuePairList ->
                JObject(keyValuePairList.toMap()) as JSON
            }

    @Suppress("MemberVisibilityCanBePrivate")
    val jsonParser: Parser<JSON> =
        jnull or jnumber or jstring or jboolean or jarray or jobject
}
//end::init[]
