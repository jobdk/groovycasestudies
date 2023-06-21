package casestudytwo.experiments.body.field

import casestudytwo.experiments.body.field.PlantUmlDslBuilder as start
import casestudytwo.plantuml.Access
import casestudytwo.plantuml.NonAccessModifier
import casestudytwo.plantuml.PlantUml
import casestudytwo.plantuml.Class
import groovy.test.GroovyTestCase
/**
 * Tries to implement the plantUml field syntax, which is not possible in Groovy as methods are needed between
 * fieldName and type. Colon and other operators like unaryOperators cannot be overloaded or used as existing shorthand.
 * */
class FieldSyntaxTest extends GroovyTestCase {

    void testFieldSyntax() {
        // Needs to be called with field as unaryOperators cannot be overloaded
        PlantUml plantUml = start.uml {
            Class "ClassName" containing {
                field + [publicField: "String"]
                field + { nonAccessModifier = NonAccessModifier.Static } & ["publicField2": "int"]
            }
        }

        def clazz = plantUml.classes.first()
        assertEquals(clazz.name, "ClassName")
        assertEquals(clazz.fields.size(), 2)
        assertEquals(clazz.fields.first().access, Access.Public)
        assertEquals(clazz.fields.first().nonAccessModifier, NonAccessModifier.None)
        assertEquals(clazz.fields.first().name, "publicField")
        assertEquals(clazz.fields.first().type, "String")
        assertEquals(clazz.fields.last().access, Access.Public)
        assertEquals(clazz.fields.last().nonAccessModifier, NonAccessModifier.Static)
        assertEquals(clazz.fields.last().name, "publicField2")
        assertEquals(clazz.fields.last().type, "int")

    }
}

