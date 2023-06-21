package casestudytwo.experiments.annotation

import groovy.test.GroovyTestCase

import static casestudytwo.experiments.annotation.PlantUmlDslBuilder.startuml
/** The following code is commented to not infer with other test executives.
 * Uncomment to test.
 */
class AnnotationSyntaxTest extends GroovyTestCase {

    void testFirstExperiment() {
        @startuml1
        def a = startuml {
            addClass()
        }
        @enduml1 // Syntax can be created but not enforced.

        // Works with any local variable.
        @startuml1
        def variable = "Variable"
    }

    void testSecondExperiment() {
//        @startuml2(PlantUmlDslBuilder.class)
//        def a = startuml {
//            addClass()
//        }
    }

//    Solution: Using a Closure as a parameter.
    void testSolution() {
        def a = startuml {
        }
    }
}

