package casestudytwo.experiments.classes

import casestudytwo.experiments.classes.PlantUmlDslBuilder as start
import casestudytwo.plantuml.PlantUml
import groovy.test.GroovyTestCase

class ClassesTest extends GroovyTestCase {

    void testConcepts() {
        // Does not work as words are already in use.
        /*start.uml{
            class "ClassName"
            abstract class "AbstractClassName"
            interface "InterfaceName"
            enum "EnumName"
        }*/

        PlantUml result = start.uml {
            Class "ClassName" containing {}
            AbstractClass "AbstractClassName"
            Interface "InterfaceName"
            Enum "EnumName"
        }

        assertEquals(result.classes.first().name, "ClassName")
        assertEquals(result.abstractClasses.first().name, "AbstractClassName")
        assertEquals(result.interfaces.first().name, "InterfaceName")
        assertEquals(result.enums.first().name, "EnumName")
    }
}