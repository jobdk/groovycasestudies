package casestudytwo.plantuml

import groovy.transform.TypeChecked

import static groovy.lang.Closure.DELEGATE_ONLY

class PlantUmlDslBuilder {
    PlantUmlDslBuilder add = this
    ArrayList<Class> classes = new ArrayList<>()
    ArrayList<AbstractClass> abstractClasses = new ArrayList<>()
    ArrayList<Enum> enums = new ArrayList<>()
    ArrayList<Interface> interfaces = new ArrayList<>()
    ArrayList<Dependency> dependencies = new ArrayList<>()

    static PlantUml uml(@DelegatesTo(value = PlantUmlDslBuilder, strategy = DELEGATE_ONLY) final Closure closure) {
        final PlantUmlDslBuilder dsl = new PlantUmlDslBuilder()
        closure.delegate = dsl
        closure.resolveStrategy = DELEGATE_ONLY
        closure.call()
        return dsl.buildPlantUml()
    }

    PlantUml buildPlantUml() { return new PlantUml(classes, abstractClasses, enums, interfaces, dependencies) }

    ClassBuilder Class(String name) {
        classes += new Class(name, [], [], [])
        return new ClassBuilder()
    }

    ClassBuilder AbstractClass(String name) {
        abstractClasses += new AbstractClass(name, [], [], [])
        return new ClassBuilder()
    }

    InterfaceBuilder Interface(String name) {
        interfaces += new Interface(name, [], [])
        return new InterfaceBuilder()
    }

    EnumBuilder Enum(String name) {
        enums += new Enum(name, List.of())
        return new EnumBuilder()
    }

    void Dependencies(@DelegatesTo(value = DependencyBuilder, strategy = DELEGATE_ONLY) final Closure closure) {
        final DependencyBuilder dependencyBuilder = new DependencyBuilder()
        closure.delegate = dependencyBuilder
        closure.resolveStrategy = DELEGATE_ONLY
        closure.call()
        dependencies += dependencyBuilder.dependencies
    }

    private class ClassBuilder {
        ArrayList<Field> fields = new ArrayList<>()
        ArrayList<Method> methods = new ArrayList<>()
        ArrayList<Constructor> constructors = new ArrayList<>()

        Class containing(@DelegatesTo(value = ClassBuilder, strategy = DELEGATE_ONLY) final Closure closure) {
            final ClassBuilder classBuilder = new ClassBuilder()
            closure.delegate = classBuilder
            closure.resolveStrategy = DELEGATE_ONLY
            closure.call()
            return classBuilder.buildEitherClassOrAbstractClass()
        }

        void field(@DelegatesTo(value = FieldBuilder, strategy = DELEGATE_ONLY) final Closure closure) {
            final FieldBuilder fieldBuilder = new FieldBuilder()
            closure.delegate = fieldBuilder
            closure.resolveStrategy = DELEGATE_ONLY
            closure.call()
            fields.add(fieldBuilder.buildField())
        }

        void method(@DelegatesTo(value = MethodBuilder, strategy = DELEGATE_ONLY) final Closure closure) {
            final MethodBuilder methodBuilder = new MethodBuilder()
            closure.delegate = methodBuilder
            closure.resolveStrategy = DELEGATE_ONLY
            closure.call()
            methods.add(methodBuilder.buildMethod())
        }

        void constructor(@DelegatesTo(value = ConstructorBuilder, strategy = DELEGATE_ONLY) final Closure closure) {
            final ConstructorBuilder constructorBuilder = new ConstructorBuilder()
            closure.delegate = constructorBuilder
            closure.resolveStrategy = DELEGATE_ONLY
            closure.call()
            constructors.add(constructorBuilder.builderConstructor())
        }

        void buildEitherClassOrAbstractClass() {
            if (!this.classes.isEmpty() && this.classes.last().fields.isEmpty() || this.classes.last().methods.isEmpty() || this.classes.last().constructors.isEmpty())
                this.classes[this.classes.size() - 1] = new Class(this.classes.last().name, fields, methods, constructors)
            else
                this.abstractClasses[this.abstractClasses.size() - 1] = new AbstractClass(this.abstractClasses.last().name, fields, methods, constructors)
        }
    }

    private class InterfaceBuilder {
        ArrayList<Field> fields = new ArrayList<>()
        ArrayList<Method> methods = new ArrayList<>()

        Interface containing(@DelegatesTo(value = InterfaceBuilder, strategy = DELEGATE_ONLY) final Closure closure) {
            final InterfaceBuilder interfaceBuilder = new InterfaceBuilder()
            closure.delegate = interfaceBuilder
            closure.resolveStrategy = DELEGATE_ONLY
            closure.call()
            return interfaceBuilder.builderInterface()
        }

        void field(@DelegatesTo(value = FieldBuilder, strategy = DELEGATE_ONLY) final Closure closure) {
            final FieldBuilder fieldBuilder = new FieldBuilder()
            closure.delegate = fieldBuilder
            closure.resolveStrategy = DELEGATE_ONLY
            closure.call()
            fields.add(fieldBuilder.buildField())
        }

        void method(@DelegatesTo(value = MethodBuilder, strategy = DELEGATE_ONLY) final Closure closure) {
            final MethodBuilder methodBuilder = new MethodBuilder()
            closure.delegate = methodBuilder
            closure.resolveStrategy = DELEGATE_ONLY
            closure.call()
            methods.add(methodBuilder.buildMethod())
        }

        void builderInterface() {
            this.interfaces[this.interfaces.size() - 1] = new Interface(this.interfaces.last().name, fields, methods)
        }
    }

    class EnumBuilder {
        ArrayList<String> values = new ArrayList<>()

        Enum containing(@DelegatesTo(value = EnumBuilder, strategy = DELEGATE_ONLY) final Closure closure) {
            final EnumBuilder enumBuilder = new EnumBuilder()
            closure.delegate = enumBuilder
            closure.resolveStrategy = DELEGATE_ONLY
            closure.call()
            return enumBuilder.buildEnum()
        }

        void buildEnum() {
            this.enums[this.enums.size() - 1] = new Enum(this.enums.last().name, values)
        }
    }

    class DependencyBuilder {
        ArrayList<Dependency> dependencies = new ArrayList<>()

        DependencyOrCardinalityMethods add(String name) {
            dependencies.add(new Dependency(new DependencyConcept(name, ""), null, null, ""))
            return new DependencyOrCardinalityMethods()
        }

        class DependencyMethods {
            CardinalityOrLabelMethods isExtending(String to) {
                return addToToLastDependency(to, DependencyType.EXTENSION)
            }

            CardinalityOrLabelMethods isAggregating(String to) {
                return addToToLastDependency(to, DependencyType.AGGREGATION)
            }

            CardinalityOrLabelMethods isComposing(String to) {
                return addToToLastDependency(to, DependencyType.COMPOSITION)
            }
        }

        class DependencyOrCardinalityMethods {
            DependencyMethods of(String cardinality) {
                dependencies[dependencies.size() - 1] = new Dependency(new DependencyConcept(dependencies.last().from.name, cardinality), null, null, "")
                return new DependencyMethods()
            }

            CardinalityOrLabelMethods isExtending(String to) {
                return addToToLastDependency(to, DependencyType.EXTENSION)
            }

            CardinalityOrLabelMethods isAggregating(String to) {
                return addToToLastDependency(to, DependencyType.AGGREGATION)
            }

            CardinalityOrLabelMethods isComposing(String to) {
                return addToToLastDependency(to, DependencyType.COMPOSITION)
            }
        }

        class CardinalityOrLabelMethods {
            LabelMethod of(String cardinality) {
                dependencies[dependencies.size() - 1] = new Dependency(dependencies.last().from, new DependencyConcept(dependencies.last().to.name, cardinality), dependencies.last().dependencyType, "")
                return new LabelMethod()
            }

            void labeled(String label) { addLabel(label) }
        }

        class LabelMethod {
            void labeled(String label) { addLabel(label) }
        }

        CardinalityOrLabelMethods addToToLastDependency(String to, DependencyType type) {
            DependencyConcept toDependencyConcept = new DependencyConcept(to, "")
            dependencies[dependencies.size() - 1] = new Dependency(dependencies.last().from, toDependencyConcept, type, "")
            return new CardinalityOrLabelMethods()
        }

        void addLabel(String label) {
            dependencies[dependencies.size() - 1]
                    = new Dependency(dependencies.last().from, dependencies.last().to, dependencies.last().dependencyType, label)
        }
    }
}

class FieldBuilder {
    NonAccessModifier nonAccessModifier = NonAccessModifier.None
    Access access = Access.Public
    String name = ""
    String type = ""

    Field buildField() { return new Field(nonAccessModifier, access, name, type) }
}

class MethodBuilder {
    NonAccessModifier nonAccessModifier = NonAccessModifier.None
    Access access = Access.Public
    String name = ""
    String type = ""
    private ArrayList parameters = new ArrayList<Parameter>()

    void parameter(@DelegatesTo(value = ParameterBuilder, strategy = DELEGATE_ONLY) final Closure closure) {
        final ParameterBuilder parameterBuilder = new ParameterBuilder()
        closure.delegate = parameterBuilder
        closure.resolveStrategy = DELEGATE_ONLY
        closure.call()
        parameters.add(parameterBuilder.builderParameter())
    }

    Method buildMethod() { return new Method(nonAccessModifier, access, name, type, parameters) }
}

class ParameterBuilder {
    String name = ""
    String type = ""

    Parameter builderParameter() { return new Parameter(name, type) }
}

class ConstructorBuilder {
    Access access = Access.Public
    private ArrayList parameters = new ArrayList<Parameter>()

    void parameter(@DelegatesTo(value = ParameterBuilder, strategy = DELEGATE_ONLY) final Closure closure) {
        final ParameterBuilder parameterBuilder = new ParameterBuilder()
        closure.delegate = parameterBuilder
        closure.resolveStrategy = DELEGATE_ONLY
        closure.call()
        parameters.add(parameterBuilder.builderParameter())
    }

    Constructor builderConstructor() {
        return new Constructor(access, parameters)
    }
}