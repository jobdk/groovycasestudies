package casestudytwo.plantuml
import casestudytwo.experiments.classes.PlantUmlDslBuilder as experimentPlantUmlDslBuilder

import static groovy.lang.Closure.DELEGATE_ONLY


class PlantUml {
    List<Class> classes
    List<AbstractClass> abstractClasses
    List<Enum> enums
    List<Interface> interfaces
    List<Dependency> dependencies

    PlantUml(List<Class> classes, List<AbstractClass> abstractClasses, List<Enum> enums, List<Interface> interfaces, List<Dependency> dependencies) {
        this.classes = classes
        this.abstractClasses = abstractClasses
        this.enums = enums
        this.interfaces = interfaces
        this.dependencies = dependencies
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

    // Only needed for experiment inside classes - is ignored in NCLOC count.
    static void containing(@DelegatesTo(value = experimentPlantUmlDslBuilder.ClassBuilder, strategy = DELEGATE_ONLY) final Closure closure) {
        experimentPlantUmlDslBuilder a = new experimentPlantUmlDslBuilder()
        final experimentPlantUmlDslBuilder.ClassBuilder dsl = new experimentPlantUmlDslBuilder.ClassBuilder(a)
        closure.delegate = dsl
        closure.resolveStrategy = DELEGATE_ONLY
        closure.call()
    }
}

class AbstractClass {
    String name
    List<Field> fields
    List<Method> methods
    List<Constructor> constructors

    AbstractClass(String name, List<Field> fields, List<Method> methods, List<Constructor> constructors) {
        this.name = name
        this.fields = fields
        this.methods = methods
        this.constructors = constructors
    }
}

class Enum {
    String name
    List<String> values

    Enum(String name, List<String> values) {
        this.name = name
        this.values = values
    }
}

class Interface {
    String name
    List<Field> fields
    List<Method> methods

    Interface(String name, List<Field> fields, List<Method> methods) {
        this.name = name
        this.fields = fields
        this.methods = methods
    }
}

class Field {
    NonAccessModifier nonAccessModifier
    Access access
    String name
    String type

    Field(NonAccessModifier nonAccessModifier, Access access, String name, String type) {
        this.nonAccessModifier = nonAccessModifier
        this.access = access
        this.name = name
        this.type = type
    }
}

class Method {
    NonAccessModifier nonAccessModifier
    Access access
    String name
    String type
    List<Parameter> parameters

    Method(NonAccessModifier nonAccessModifier, Access access, String name, String type, List<Parameter> parameters) {
        this.nonAccessModifier = nonAccessModifier
        this.access = access
        this.name = name
        this.type = type
        this.parameters = parameters
    }
}

class Constructor {
    Access access
    List<Parameter> parameters

    Constructor(Access access, List<Parameter> parameters) {
        this.access = access
        this.parameters = parameters
    }
}

class Parameter {
    String name
    String type

    Parameter(String name, String type) {
        this.name = name
        this.type = type
    }
}

enum Access {
    Public, Private, Protected, PackagePrivate
}

enum NonAccessModifier {
    Static, Abstract, None
}

class DependencyConcept {
    String name
    String cardinality

    DependencyConcept(String name, String cardinality) {
        this.name = name
        this.cardinality = cardinality
    }
}

class Dependency {
    DependencyConcept from
    DependencyConcept to
    DependencyType dependencyType
    String label

    Dependency(DependencyConcept from, DependencyConcept to, DependencyType dependencyType, String label) {
        this.from = from
        this.to = to
        this.dependencyType = dependencyType
        this.label = label
    }
}

enum DependencyType {
    EXTENSION, COMPOSITION, AGGREGATION
}

