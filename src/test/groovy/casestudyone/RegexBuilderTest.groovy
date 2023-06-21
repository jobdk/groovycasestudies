package casestudyone

import casestudyone.RegexBuilder as build
import groovy.test.GroovyTestCase
import org.junit.jupiter.api.Assertions

import java.util.regex.Pattern

class RegexBuilderTest extends GroovyTestCase implements PredefinedTerms {
    // _________________ starts with _________________

    void 'test starts with anything'() {
        Pattern startsWith = build.regex {
            startsWith anything
        }
        assertEquals("^(${anything})", startsWith.pattern())
        assertTrue(startsWith.matcher("Should match.").matches())
        assertTrue(startsWith.matcher("").matches())
    }

    void 'test startsWith something'() {
        def startsWith = build.regex {
            startsWith something
        }
        assertEquals("^(${something})", startsWith.pattern())
        assertTrue(startsWith.matcher("Should match.").matches())
        assertFalse(startsWith.matcher("").matches())
    }

    void 'test starts with letters'() {
        def startsWith = build.regex {
            startsWith letters
        }
        assertEquals("^(${letters})", startsWith.pattern())
        assertTrue(startsWith.matcher("Should match").matches())
        assertFalse(startsWith.matcher("123").matches())
    }

    void 'test starts with numbers'() {
        def startsWith = build.regex {
            startsWith numbers
        }
        assertEquals("^(${numbers})", startsWith.pattern())
        assertTrue(startsWith.matcher("123").matches())
        assertFalse(startsWith.matcher("Test").matches())
    }

    void 'test starts with Hello'() {
        def startsWith = build.regex {
            startsWith "Hello"
        }
        assertEquals("^(Hello)", startsWith.pattern())
        assertTrue(startsWith.matcher("Hello").matches())
        assertFalse(startsWith.matcher("hello").matches())
    }

    void 'test starts with @'() {
        def startsWith = build.regex {
            startsWith "@"
        }
        assertEquals("^(@)", startsWith.pattern())
        assertTrue(startsWith.matcher("@").matches())
    }

    void 'test starts with first or second'() {
        def startsWith = build.regex {
            startsWith "first" or "second"
        }
        assertEquals("^(first|second)", startsWith.pattern())
        assertTrue(startsWith.matcher("first").matches())
        assertTrue(startsWith.matcher("second").matches())
        assertFalse(startsWith.matcher("third").matches())
        assertFalse(startsWith.matcher("fourth").matches())
    }

    void 'test starts with whitespace between words'() {
        Pattern startsWith = build.regex {
            startsWith "Hello World!" or "Hello"
        }
        assertEquals("^(Hello World!|Hello)", startsWith.pattern())
        assertTrue(startsWith.matcher("Hello World!").matches())
        assertTrue(startsWith.matcher("Hello").matches())
        assertFalse(startsWith.matcher("Test").matches())
    }

    // _________________ followed with _________________

    void 'test followedWith anything'() {
        // GIVEN
        // WHEN
        Pattern followedWith = build.regex { followedWith anything }

        // THEN
        assertEquals("(${anything})", followedWith.pattern())
        assertTrue(followedWith.matcher("Should match.").matches())
        assertTrue(followedWith.matcher("").matches())
    }


    void 'test followedWith something'() {
        Pattern followedWith = build.regex { followedWith something }
        assertEquals("(${something})", followedWith.pattern())
        assertTrue(followedWith.matcher("Should match.").matches())
        assertFalse(followedWith.matcher("").matches())
    }


    void 'test followed with letters'() {
        Pattern followedWith = build.regex { followedWith letters }
        assertEquals("(${letters})", followedWith.pattern())
        assertTrue(followedWith.matcher("Test").matches())
        assertFalse(followedWith.matcher("123").matches())
    }


    void 'test followed with numbers'() {
        // GIVEN
        // WHEN
        Pattern followedWith = build.regex { followedWith numbers }

        // THEN
        assertEquals("(${numbers})", followedWith.pattern())
        assertTrue(followedWith.matcher("123").matches())
        assertFalse(followedWith.matcher("Test").matches())
    }


    void 'test followed with @'() {
        Pattern followedWith = build.regex { followedWith "@" }
        assertEquals("(@)", followedWith.pattern())
        assertTrue(followedWith.matcher("@").matches())
        assertFalse(followedWith.matcher("@@").matches())
    }


    void 'test followed with @ or email'() { // Scala, Groovy,
        Pattern followedWith = build.regex { followedWith "@" or "email" }
        assertEquals("(@|email)", followedWith.pattern())
        assertTrue(followedWith.matcher("@").matches())
        assertTrue(followedWith.matcher("email").matches())
        assertFalse(followedWith.matcher("@email").matches())
        assertFalse(followedWith.matcher("emailtest").matches())
    }


    void 'test followed with multiple times in correct order'() { // Scala, Groovy
        // GIVEN
        // WHEN
        Pattern followedWith = build.regex {
            followedWith "gmail" or "gmx"
            followedWith "."
            followedWith "com" or "de"
        }

        // THEN
        assertEquals("(gmail|gmx)(\\.)(com|de)", followedWith.pattern())
        assertTrue(followedWith.matcher("gmail.com").matches())
        assertTrue(followedWith.matcher("gmail.de").matches())
        assertTrue(followedWith.matcher("gmx.com").matches())
        assertTrue(followedWith.matcher("gmx.de").matches())
        assertFalse(followedWith.matcher("gmxde").matches())
        assertFalse(followedWith.matcher("gmailgmx.de").matches())
        assertFalse(followedWith.matcher("gmail.").matches())
    }

    // _________________ ends with _________________

    void 'test ends with anything'() {
        // GIVEN
        // WHEN
        Pattern endsWith = build.regex { endsWith anything }

        // THEN
        assertEquals("(${anything})\$", endsWith.pattern())
        assertTrue(endsWith.matcher("Should match.").matches())
        assertTrue(endsWith.matcher("").matches())
    }


    void 'test ends with something'() {
        // GIVEN
        // WHEN
        Pattern endsWith = build.regex { endsWith something }

        // THEN
        assertEquals("(${something})\$", endsWith.pattern())
        assertTrue(endsWith.matcher("Should match.").matches())
        assertFalse(endsWith.matcher("").matches())
    }


    void 'test ends with letters'() {
        // GIVEN
        // WHEN
        Pattern endsWith = build.regex { endsWith letters }

        // THEN
        assertEquals("(${letters})\$", endsWith.pattern())
        assertTrue(endsWith.matcher("Should match").matches())
        assertFalse(endsWith.matcher("123").matches())
    }


    void 'test ends with numbers'() {
        // GIVEN
        // WHEN
        Pattern endsWith = build.regex { endsWith numbers }

        // THEN
        assertEquals("(${numbers})\$", endsWith.pattern())
        assertTrue(endsWith.matcher("123").matches())
        assertFalse(endsWith.matcher("Test").matches())
    }


    void 'test ends with Hello'() {
        // GIVEN
        // WHEN
        Pattern endsWith = build.regex { endsWith "Hello" }

        // THEN
        assertEquals("(Hello)\$", endsWith.pattern())
        assertTrue(endsWith.matcher("Hello").matches())
        assertFalse(endsWith.matcher("Hello World").matches())
    }


    void 'test ends with first or second'() {
        // GIVEN
        // WHEN
        Pattern endsWith = build.regex { endsWith "first" or "second" }

        // THEN
        assertEquals("(first|second)\$", endsWith.pattern())
        assertTrue(endsWith.matcher("first").matches())
        assertTrue(endsWith.matcher("second").matches())
        assertFalse(endsWith.matcher("third").matches())
        assertFalse(endsWith.matcher("fourth").matches())
    }

    // _________________ Multiplicities  _________________

    void 'test starts multiplicity'() {
        // GIVEN
        // WHEN
        Pattern regex = build.regex {
            startsWith "aa" occurs 1..2 or "b" occurs 3..3
        }

        // THEN
        assertEquals("^((aa){1,2}|(b){3})", regex.pattern())
        assertTrue(regex.matcher("aa").matches())
        assertTrue(regex.matcher("aaaa").matches())
        assertTrue(regex.matcher("bbb").matches())
        assertFalse(regex.matcher("aaa").matches())
        assertFalse(regex.matcher("bb").matches())
        assertFalse(regex.matcher("b").matches())
    }


    void 'test followed with repeated and multiplicities'() {
        // GIVEN
        // WHEN
        Pattern followedWith = build.regex {
            followedWith "gmail" occurs 1..2 or "gmx"
            followedWith "." occurs 3..3
            followedWith "com" or "de"
        }

        // THEN
        assertEquals("((gmail){1,2}|gmx)((\\.){3})(com|de)", followedWith.pattern())
        assertTrue(followedWith.matcher("gmailgmail...com").matches())
        assertTrue(followedWith.matcher("gmail...de").matches())
        assertTrue(followedWith.matcher("gmx...com").matches())
        assertTrue(followedWith.matcher("gmx...de").matches())
        assertFalse(followedWith.matcher("gmxde").matches())
        assertFalse(followedWith.matcher("gmailgmx.de").matches())
        assertFalse(followedWith.matcher("gmail.").matches())
        assertFalse(followedWith.matcher("gmail.").matches())
    }


    void 'test ends multiplicity'() {
        // GIVEN
        // WHEN
        Pattern endsWith = build.regex {
            endsWith "aa" occurs 1..2 or "b" occurs 3..3
        }

        // THEN
        assertEquals("((aa){1,2}|(b){3})\$", endsWith.pattern())
        assertTrue(endsWith.matcher("aa").matches())
        assertTrue(endsWith.matcher("aaaa").matches())
        assertTrue(endsWith.matcher("bbb").matches())
        assertFalse(endsWith.matcher("aaa").matches())
        assertFalse(endsWith.matcher("bb").matches())
        assertFalse(endsWith.matcher("b").matches())
    }


    void 'test ends with multiplicity indefinitely'() {
        // GIVEN
        // WHEN
        Pattern endsWith = build.regex {
            endsWith "aa" occurs indefinitely or "b" occurs 3..3
        }

        // THEN
        assertEquals("((aa)*|(b){3})\$", endsWith.pattern())
        assertTrue(endsWith.matcher("aaaaaaaaaa").matches())
        assertTrue(endsWith.matcher("aaaa").matches())
        assertTrue(endsWith.matcher("bbb").matches())
        assertFalse(endsWith.matcher("aaa").matches())
        assertFalse(endsWith.matcher("bb").matches())
        assertFalse(endsWith.matcher("b").matches())
    }

    // _________________ integration _________________


    void 'test simple regex'() {
        // GIVEN
        // WHEN
        Pattern regex = build.regex {
            startsWith "T"
            followedWith anything
            endsWith "s."
        }

        // THEN
        assertEquals("^(T)(.*)(s\\.)\$", regex.pattern())
        assertTrue(regex.matcher("This matches.").matches())
        assertFalse(regex.matcher("This does not match.").matches())
    }


    void 'test simple regex 2'() {
        // GIVEN
        // WHEN
        Pattern regex = build.regex {
            startsWith "This m"
            followedWith "atche"
            endsWith "s."
        }

        // THEN
        assertEquals("^(This m)(atche)(s\\.)\$", regex.pattern())
        assertTrue(regex.matcher("This matches.").matches())
        assertFalse(regex.matcher("Does not match.").matches())
    }


    void 'test email regex'() {
        // GIVEN
        // WHEN
        Pattern regex = build.regex {
            startsWith something
            followedWith "@"
            followedWith something
            followedWith "."
            endsWith "com" or "de" or "net"
        }

        // THEN
        assertEquals("^(.+)(@)(.+)(\\.)(com|de|net)\$", regex.pattern())
        assertTrue(regex.matcher("name@gmail.com").matches())
        assertTrue(regex.matcher("name@gmx.de").matches())
        assertTrue(regex.matcher("name@gmx.de").matches())
        assertTrue(regex.matcher("name@gmx.net").matches())
        assertFalse(regex.matcher("@gmx.de").matches())
        assertFalse(regex.matcher("name@.de").matches())
        assertFalse(regex.matcher("name@.ch").matches())
        assertFalse(regex.matcher("name.de").matches())
    }

    // _________________ inner regex _________________

    void 'test Regex Email inner regex'() {
        // GIVEN
        // WHEN
        Pattern regex = build.regex {
            startsWith innerRegex {
                followedWith "John" or "Steve"
            } or "Hello"
            followedWith "Doe" or "Mustermann"
            followedWith "@"
            endsWith "gmail.com"
        }

        // THEN
        assertEquals("^((John|Steve)|Hello)(Doe|Mustermann)(@)(gmail\\.com)\$", regex.pattern())
        assertTrue(regex.matcher("JohnDoe@gmail.com").matches())
        assertTrue(regex.matcher("JohnMustermann@gmail.com").matches())
        assertTrue(regex.matcher("SteveMustermann@gmail.com").matches())
        assertTrue(regex.matcher("HelloMustermann@gmail.com").matches())
        assertFalse(regex.matcher("JohnD@gmail.com").matches())
        assertFalse(regex.matcher("Demian@gmail.com").matches())
    }


    void 'test Inner regex followed with'() {
        // GIVEN
        // WHEN
        Pattern regex = build.regex {
            startsWith innerRegex {
                followedWith "John"
            }
            followedWith "Doe" or "Mustermann"
            followedWith innerRegex {

                followedWith "a"
                followedWith "b" or "c"
            }
            endsWith "end"
        }

        // THEN
        assertEquals("^((John))(Doe|Mustermann)((a)(b|c))(end)\$", regex.pattern())
        assertTrue(regex.matcher("JohnDoeabend").matches())
        assertTrue(regex.matcher("JohnMustermannacend").matches())
        assertFalse(regex.matcher("JohnMustermannaend").matches())
    }


    void 'test Inner regex followed with with alternative'() {
        // GIVEN
        // WHEN
        Pattern regex = build.regex {
            startsWith innerRegex {
                followedWith "1" or "2"
            } or "4"
            followedWith innerRegex {
                followedWith "a"
                followedWith "b" or "c"
            }
            endsWith "end"
        }

        // THEN
        assertEquals("^((1|2)|4)((a)(b|c))(end)\$", regex.pattern())
        assertTrue(regex.matcher("1abend").matches())
        assertTrue(regex.matcher("2abend").matches())
        assertTrue(regex.matcher("4acend").matches())
        assertFalse(regex.matcher("4aend").matches())
    }

    // _________________ semantic validation _________________

    void 'test Multiplicity is negative'() {
        // GIVEN
        // WHEN
        RuntimeException result = Assertions.assertThrows(RuntimeException.class) {
            build.regex {
                startsWith "first" occurs 1..-1
                followedWith "second" occurs -1..-3
                endsWith "third" occurs 1000..992
            }
        }

        // THEN
        assertEquals(
                """The following Errors occurred:
Multiplicity must be positive for: 1..-1
First value must be smaller than second value for: 1..-1
Multiplicity must be positive for: -1..-3
First value must be smaller than second value for: -1..-3
First value must be smaller than second value for: 1000..992""", result.message
        )
    }

    void 'test for loc'() {
        // GIVEN
        // WHEN

        Pattern regex = build.regex {
            startsWith "domain "
            followedWith innerRegex {
                followedWith "specific "
                followedWith innerRegex {
                    followedWith "modeling" or "design"
                }
            } or "driven design"
            endsWith anything
        }

        // THEN
        assertEquals("^(domain )((specific )((modeling|design))|driven design)(.*)\$", regex.pattern())
    }
}



