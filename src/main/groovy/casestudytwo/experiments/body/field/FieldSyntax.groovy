package casestudytwo.experiments.body.field


import casestudytwo.plantuml.Access
import casestudytwo.plantuml.Constructor
import casestudytwo.plantuml.Field
import casestudytwo.plantuml.Method
import casestudytwo.plantuml.NonAccessModifier
import casestudytwo.plantuml.PlantUml

import static groovy.lang.Closure.DELEGATE_ONLY

/**This class should only test possible ways to implement the PlantUML-Syntax for adding the attributs, method and constructor concept */
class PlantUmlDslBuilder {
    ArrayList<Class> classes = [] as ArrayList

    static PlantUml uml(@DelegatesTo(value = PlantUmlDslBuilder, strategy = DELEGATE_ONLY) final Closure closure) {
        final  plantUmlDslBuilder = new PlantUmlDslBuilder()
//        static PlantUml uml(@DelegatesTo(value = casestudytwo.experiments.body.enumaration.PlantUmlDslBuilder, strategy = DELEGATE_ONLY) final Closure closure) {
//        final casestudytwo.experiments.body.enumaration.PlantUmlDslBuilder plantUmlDslBuilder = new casestudytwo.experiments.body.enumaration.PlantUmlDslBuilder()
        closure.delegate = plantUmlDslBuilder
        closure.resolveStrategy = DELEGATE_ONLY
        closure.call()
        return plantUmlDslBuilder.buildPlantUml()
    }

    PlantUml buildPlantUml() {
        return new PlantUml(classes as List<casestudytwo.plantuml.Class>, List.of(), List.of(), List.of(), List.of())
    }

    Class Class(String name) {
        Class clazz = new Class(name, List.of(), List.of(), List.of())
        classes += clazz
        return clazz
    }

    // removed unnecessary methods for the sake of simplicity

    class ClassBuilder {
        ArrayList<Field> fields = [] as ArrayList
        ArrayList<Method> methods = [] as ArrayList
        ArrayList<Constructor> constructors = [] as ArrayList

        FieldBuilder field = new FieldBuilder()

        Class buildClass() {
            classes[classes.size() - 1] = new Class(classes.last().name, fields, methods, constructors)
        }

        class FieldBuilder {
            Field plus(LinkedHashMap<String, String> stringStringLinkedHashMap) {
                Field field = new Field(NonAccessModifier.None, Access.Public, stringStringLinkedHashMap.keySet().first(), stringStringLinkedHashMap.values().first())
                fields.add(field)
                return field
            }

            NonAccessModifierBuilder plus(@DelegatesTo(value = NonAccessModifierBuilder, strategy = DELEGATE_ONLY) final Closure closure) {
                Field field = new Field(NonAccessModifier.None, Access.Public, "", "")
                final NonAccessModifierBuilder nonAccessModifierBuilder = new NonAccessModifierBuilder()
                closure.delegate = nonAccessModifierBuilder
                closure.resolveStrategy = DELEGATE_ONLY
                closure.call()
                fields.add(nonAccessModifierBuilder.changeNonAccessModifier(field))
                return nonAccessModifierBuilder
            }


            class NonAccessModifierBuilder {
                NonAccessModifier nonAccessModifier = NonAccessModifier.None

                def and(LinkedHashMap<String, String> stringStringLinkedHashMap) {
                    Field field = new Field(fields.last().nonAccessModifier, fields.last().access, stringStringLinkedHashMap.keySet().first(), stringStringLinkedHashMap.values().first())
                    fields[fields.size() - 1] = field
                    return this
                }

                Field changeNonAccessModifier(Field field) {
                    return new Field(nonAccessModifier, field.access, field.name, field.type)
                }
            }
        }
    }

    class Class {
        String name
        List<Field> fields
        List<Method> methods
        List<Constructor> constructors

        Class(String name, List<Field> fields, List<Method> methods, List<Constructor> constructors) {
            this.name = name
            this.fields = fields
            this.methods = methods
            this.constructors = constructors
        }

        Class containing(@DelegatesTo(value = ClassBuilder, strategy = DELEGATE_ONLY) final Closure closure) {
            final ClassBuilder classBuilder = new ClassBuilder()
            closure.delegate = classBuilder
            closure.resolveStrategy = DELEGATE_ONLY
            closure.call()
            return classBuilder.buildClass()
        }
    }
}

// Testing if Closure can be extend -> Result: Yes. but cannot access state.
/*Closure.metaClass.and = { String a ->
    return new Field(NonAccessModifier.none, Access.public_, "dsfsd", "dsfsd")
}*/




