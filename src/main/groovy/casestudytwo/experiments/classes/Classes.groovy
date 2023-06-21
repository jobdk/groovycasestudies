package casestudytwo.experiments.classes


import casestudytwo.plantuml.AbstractClass
import casestudytwo.plantuml.Class
import casestudytwo.plantuml.Constructor
import casestudytwo.plantuml.Dependency
import casestudytwo.plantuml.Enum
import casestudytwo.plantuml.Field
import casestudytwo.plantuml.FieldBuilder
import casestudytwo.plantuml.Interface
import casestudytwo.plantuml.Method
import casestudytwo.plantuml.PlantUml

import static groovy.lang.Closure.DELEGATE_ONLY
/**This class should only test possible ways to implement the PlantUML-Syntax for adding the concepts class, abstract class, interface and enum */
class PlantUmlDslBuilder {
    List<Class> classes = List.of()
    List<AbstractClass> abstractClasses = List.of()
    List<Enum> enums = List.of()
    List<Interface> interfaces = List.of()
    List<Dependency> dependencies = List.of()

    static PlantUml uml(@DelegatesTo(value = PlantUmlDslBuilder, strategy = DELEGATE_ONLY) final Closure closure) {
        final PlantUmlDslBuilder dsl = new PlantUmlDslBuilder()
        closure.delegate = dsl
        closure.resolveStrategy = DELEGATE_ONLY
        closure.call()
        return dsl.buildPlantUml()
    }

    PlantUml buildPlantUml() {
        return new PlantUml(classes, abstractClasses, enums, interfaces, dependencies)
    }

    Class Class(String name) {
        Class clazz = new Class(name, List.of(), List.of(), List.of())
        classes += clazz
        return clazz
    }

    AbstractClass AbstractClass(String name) {
        AbstractClass abstractClass = new AbstractClass(name, List.of(), List.of(), List.of())
        abstractClasses += abstractClass
        return abstractClass
    }

    Interface Interface(String name) {
        Interface interface_ = new Interface(name, List.of(), List.of())
        interfaces += interface_
        return interface_
    }

    Enum Enum(String name) {
        Enum enum_ = new Enum(name, List.of())
        enums += enum_
        return enum_
    }

    class ClassBuilder {
        String name
        List<Field> fields
        List<Method> methods
        List<Constructor> constructors

        def name(String name) {
            this.name = name
        }

        static Field field(@DelegatesTo(value = FieldBuilder, strategy = DELEGATE_ONLY) final Closure closure) {
            final FieldBuilder fieldBuilder = new FieldBuilder()
            closure.delegate = fieldBuilder
            closure.resolveStrategy = DELEGATE_ONLY
            closure.call()
            return fieldBuilder.buildField()
        }
    }
}
