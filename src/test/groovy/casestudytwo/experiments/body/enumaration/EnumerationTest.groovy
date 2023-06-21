package casestudytwo.experiments.body.enumaration

import casestudytwo.plantuml.PlantUml
import groovy.test.GroovyTestCase

import static casestudytwo.experiments.body.enumaration.PlantUmlDslBuilder.startuml

/** Only researches ways to implement the PlantUml-Syntax for the Enum-Concept-Body, as the other
 concepts are already checked in the FieldSyntaxTest.kt.*/
class EnumerationTest extends GroovyTestCase {
    /**
     * Full syntax is not achievable since the values cannot be directly separated by a new line within the enum-body.
     * Instead, they must be assigned to a variable of some sort and separated using a different delimiter.
     * As a result, an alternative approach is presented, using the native map syntax and assigning it to values.
     */
    /*Needed syntax:
      enum enumName {
       value1,
       value2,
       value3,
       value4
      }*/

    void testEnumBody() {
        // Experiment 1: Invoking the body of the closure on to a List.
        // Result: Unsuccessful, as it is not recognized as a List. Does work with a map.
        PlantUml result = startuml {
            Enum "enumName" containing1 {
                // Trying it with a map -> works
                value1 = "value1"
                value2 = "value2"

                // Trying it with a list -> does not work
                // ["value1"]
            }
        }

        assertEquals(result.enums.first().values.first(), "value1")
        assertEquals(result.enums.first().values.last(), "value2")


        // Solution: Using native map syntax and assigning it to values.
        PlantUml solution = startuml {
            Enum "enumName" containing {
                values = ["value1", "value2", "value3", "value4"]
            }
        }

        assertEquals(solution.enums.first().values.first(), "value1")
        assertEquals(solution.enums.first().values.last(), "value4")
    }
}