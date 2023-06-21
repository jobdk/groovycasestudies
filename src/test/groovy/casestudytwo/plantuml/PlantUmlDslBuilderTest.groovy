package casestudytwo.plantuml

import casestudytwo.plantuml.PlantUmlDslBuilder as start
import groovy.test.GroovyTestCase

class PlantUmlDslBuilderTest extends GroovyTestCase {

    void testClassWithFieldsMethodsAndConstructor() {
// Given
// When
        PlantUml plantUml = start.uml {
            Class "ClassName" containing {
                field {
                    nonAccessModifier = NonAccessModifier.Static
                    access = Access.Private
                    name = "privateField"
                    type = "String"
                }
                constructor {
                    access = Access.Public
                    parameter {
                        name = "parameter"
                        type = "String"
                    }
                }
                method {
                    nonAccessModifier = NonAccessModifier.Static
                    access = Access.Public
                    name = "publicMethod"
                    type = "void"
                    parameter {
                        name = "parameter"
                        type = "String"
                    }
                }
            }
            Enum "EnumName" containing {
                values = ["value1", "value2", "value3"]
            }


            Dependencies {
                add "ClassName" isExtending "MyEnum"
                add "ClassName" of "1" isComposing "MyEnum" of "many" labeled "test label"
                add "ClassName" isAggregating "MyEnum" of "*"
                add "ClassName" isAggregating "MyEnum" labeled "test"
            }
        }

        // THEN
        Class clazz = plantUml.classes.first()
        assertEquals("ClassName", clazz.name)
        assertEquals(1, clazz.fields.size())
        assertEquals(1, clazz.constructors.size())
        assertEquals(1, clazz.methods.size())
        assertEquals(NonAccessModifier.Static, clazz.fields.first().nonAccessModifier)
        assertEquals(Access.Private, clazz.fields.first().access)
        assertEquals("privateField", clazz.fields.first().name)
        assertEquals("String", clazz.fields.first().type)
        assertEquals(Access.Public, clazz.constructors.first().access)
        assertEquals(1, clazz.constructors.first().parameters.size())
        assertEquals("parameter", clazz.constructors.first().parameters.first().name)
        assertEquals("String", clazz.constructors.first().parameters.first().type)
        assertEquals(NonAccessModifier.Static, clazz.methods.first().nonAccessModifier)
        assertEquals(Access.Public, clazz.methods.first().access)
        assertEquals("publicMethod", clazz.methods.first().name)
        assertEquals("void", clazz.methods.first().type)
        assertEquals(1, clazz.methods.first().parameters.size())
        assertEquals("parameter", clazz.methods.first().parameters.first().name)
        assertEquals("String", clazz.methods.first().parameters.first().type)

        // Dependenciy assertions
        assertEquals(4, plantUml.dependencies.size())
        assertEquals("ClassName", plantUml.dependencies.first().from.name)
        assertEquals("MyEnum", plantUml.dependencies.first().to.name)
        assertEquals(DependencyType.EXTENSION, plantUml.dependencies.first().dependencyType)
        assertEquals("", plantUml.dependencies.first().label)
        assertEquals("", plantUml.dependencies.first().from.cardinality)
        assertEquals("", plantUml.dependencies.first().to.cardinality)
        assertEquals("ClassName", plantUml.dependencies.get(1).from.name)
        assertEquals("MyEnum", plantUml.dependencies.get(1).to.name)
        assertEquals(DependencyType.COMPOSITION, plantUml.dependencies.get(1).dependencyType)
        assertEquals("test label", plantUml.dependencies.get(1).label)
        assertEquals("1", plantUml.dependencies.get(1).from.cardinality)
        assertEquals("many", plantUml.dependencies.get(1).to.cardinality)
        assertEquals("ClassName", plantUml.dependencies.get(2).from.name)
        assertEquals("MyEnum", plantUml.dependencies.get(2).to.name)
        assertEquals(DependencyType.AGGREGATION, plantUml.dependencies.get(2).dependencyType)
        assertEquals("", plantUml.dependencies.get(2).label)
        assertEquals("", plantUml.dependencies.get(2).from.cardinality)
        assertEquals("*", plantUml.dependencies.get(2).to.cardinality)
        assertEquals("ClassName", plantUml.dependencies.get(3).from.name)
        assertEquals("MyEnum", plantUml.dependencies.get(3).to.name)
        assertEquals(DependencyType.AGGREGATION, plantUml.dependencies.get(3).dependencyType)
        assertEquals("test", plantUml.dependencies.get(3).label)
        assertEquals("", plantUml.dependencies.get(3).from.cardinality)
        assertEquals("", plantUml.dependencies.get(3).to.cardinality)
    }

    void testForLoc() {
// Given
// When
        PlantUml plantUml = start.uml {
            Class "ClassName" containing {
                field {
                    nonAccessModifier = NonAccessModifier.Static
                    access = Access.Private
                    name = "privateField"
                    type = "String"
                }
                constructor {
                    access = Access.Public
                    parameter {
                        name = "privateField"
                        type = "String"
                    }
                }
                method {
                    nonAccessModifier = NonAccessModifier.Static
                    access = Access.Public
                    name = "publicMethod"
                    type = "void"
                    parameter {
                        name = "parameter"
                        type = "String"
                    }
                }
            }
            AbstractClass "AbstractClassName" containing {
                field {
                    nonAccessModifier = NonAccessModifier.Abstract
                    access = Access.Protected
                    name = "protectedField"
                    type = "int"
                }
                constructor {
                    access = Access.Public
                    parameter {
                        name = "protectedField"
                        type = "int"
                    }
                }
                method {
                    nonAccessModifier = NonAccessModifier.Static
                    access = Access.Private
                    name = "privateMethod"
                    type = "void"
                    parameter {
                        name = "parameter"
                        type = "String"
                    }
                }
            }
            Interface "InterfaceName" containing {
                field {
                    access = Access.Public
                    name = "publicField"
                    type = "String"
                }
                method {
                    access = Access.Public
                    name = "publicMethod"
                    type = "Map"
                    parameter {
                        name = "parameter"
                        type = "String"
                    }
                }
            }
            Enum "EnumName" containing {
                values = ["VALUE1", "VALUE2", "VALUE3"]
            }
            Dependencies {
                add "ClassName" isExtending "EnumName"
                add "ClassName" of "1" isComposing "AbstractClassName" of "many" labeled "test label"
                add "AbstractClassName" isAggregating "ClassName" of "*"
                add "ClassName" isAggregating "AbstractClassName" labeled "test"
            }
        }
        // THEN
        String plantUmlString = GroovyToPlantUml.buildPlantUml(plantUml)

        // assert plantUml
        assertEquals("@startuml\n" +
                "class ClassName {\n" +
                "- {static} privateField: String\n" +
                "+ ClassName(privateField: String) <<Constructor>>\n" +
                "+ {static} publicMethod(parameter: String): void\n" +
                "}\n" +
                "abstract class AbstractClassName {\n" +
                "# {abstract} protectedField: int\n" +
                "+ AbstractClassName(protectedField: int) <<Constructor>>\n" +
                "- {static} privateMethod(parameter: String): void\n" +
                "}\n" +
                "enum EnumName {\n" +
                "VALUE1\n" +
                "VALUE2\n" +
                "VALUE3\n" +
                "}\n" +
                "interface InterfaceName {\n" +
                "+ publicField: String\n" +
                "+ publicMethod(parameter: String): Map\n" +
                "}\n" +
                "ClassName <|-- EnumName\n" +
                "ClassName \"1\" *-- \"many\" AbstractClassName : test label\n" +
                "AbstractClassName o-- \"*\" ClassName\n" +
                "ClassName o-- AbstractClassName : test\n" +
                "@enduml", plantUmlString)
    }
}


