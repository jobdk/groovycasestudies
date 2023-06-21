package casestudytwo.plantuml

class GroovyToPlantUml {
    private static final def accessMap = [
            (Access.Public)        : '+',
            (Access.Private)       : '-',
            (Access.Protected)     : '#',
            (Access.PackagePrivate): '~'
    ]

    private static final Map<NonAccessModifier, String> nonAccessModifierMap = [
            (NonAccessModifier.Static)  : ' {static}',
            (NonAccessModifier.Abstract): ' {abstract}',
            (NonAccessModifier.None)    : ''
    ]

    private static final Map<DependencyType, String> dependencyMap = [
            (DependencyType.EXTENSION)  : '<|--',
            (DependencyType.COMPOSITION): '*--',
            (DependencyType.AGGREGATION): 'o--'
    ]

    static String buildPlantUml(PlantUml plantUml) {
        String string = new StringBuilder().with {
            append("@startuml\n")
            append(convertConcept(plantUml))
            append(convertDependencies(plantUml))
            append("@enduml")
        }.toString()
        return string
    }

    private static String convertConcept(PlantUml plantUml) {
        StringBuilder stringBuilder = new StringBuilder()
        plantUml.classes.each { clazz ->
            stringBuilder.append(mapClassName(clazz))
            stringBuilder.append(mapFields(clazz.fields))
            stringBuilder.append(mapConstructors(clazz.name, clazz.constructors))
            stringBuilder.append(mapMethods(clazz.methods))
            isBodyLess(clazz) ? stringBuilder.append("\n") : stringBuilder.append("}\n")
        }

        plantUml.abstractClasses.each {
            abstractClass ->
                stringBuilder.append(mapAbstractClassName(abstractClass))
                stringBuilder.append(mapFields(abstractClass.fields))
                stringBuilder.append(mapConstructors(abstractClass.name, abstractClass.constructors))
                stringBuilder.append(mapMethods(abstractClass.methods))
                isBodyLess(abstractClass) ? stringBuilder.append("\n") : stringBuilder.append("}\n")
        }

        plantUml.enums.each {
            enumEntry ->
                stringBuilder.append(mapEnumName(enumEntry))
                stringBuilder.append(mapValues(enumEntry.values))
                isBodyLess(enumEntry) ? stringBuilder.append("\n") : stringBuilder.append("\n}\n")
        }

        plantUml.interfaces.each {
            interfaceEntry ->
                stringBuilder.append(mapInterfaceName(interfaceEntry))
                stringBuilder.append(mapFields(interfaceEntry.fields))
                stringBuilder.append(mapMethods(interfaceEntry.methods))
                isBodyLess(interfaceEntry) ? stringBuilder.append("\n") : stringBuilder.append("}\n")
        }
        return stringBuilder.toString()
    }

    private static String mapValues(List<String> values) {
        return values.join('\n')
    }

    private static String mapConstructors(String className, List<Constructor> constructors) {
        StringBuilder stringBuilder = new StringBuilder()
        constructors.each { constructor ->
            def parameters = constructor.parameters.collect { parameter ->
                "${parameter.name}: ${parameter.type}"
            }.join(', ')
            stringBuilder.append("${accessMap[constructor.access]} $className($parameters) <<Constructor>>\n")
        }
        return stringBuilder.toString()
    }

    private static String mapMethods(List<Method> methods) {
        StringBuilder stringBuilder = new StringBuilder()
        methods.each { method ->
            def parameters = method.parameters.collect { parameter ->
                "${parameter.name}: ${parameter.type}"
            }.join(', ')
            stringBuilder.append("${accessMap[method.access]}${nonAccessModifierMap[method.nonAccessModifier]} ${method.name}($parameters): ${method.type}\n")
        }
        return stringBuilder.toString()
    }

    private static String mapFields(List<Field> fields) {
        StringBuilder stringBuilder = new StringBuilder()
        fields.each { field ->
            stringBuilder.append(accessMap[field.access])
            stringBuilder.append(nonAccessModifierMap[field.nonAccessModifier])
            stringBuilder.append(" ${field.name}: ${field.type}\n")
        }
        return stringBuilder.toString()
    }

    private static String mapClassName(Class clazz) {
        return "class ${clazz.name}" + (isBodyLess(clazz) ? "\n" : " {\n")
    }

    private static String mapAbstractClassName(AbstractClass abstractClass) {
        return "abstract class ${abstractClass.name}" + (isBodyLess(abstractClass) ? "\n" : " {\n")
    }

    private static String mapEnumName(Enum enumEntry) {
        return "enum ${enumEntry.name}" + (isBodyLess(enumEntry) ? "\n" : " {\n")
    }

    private static String mapInterfaceName(Interface interfaceEntry) {
        return "interface ${interfaceEntry.name}" + (isBodyLess(interfaceEntry) ? "\n" : " {\n")
    }

    private static boolean isBodyLess(Interface interfaceEntry) {
        return interfaceEntry.methods.isEmpty() && interfaceEntry.fields.isEmpty()
    }

    private static boolean isBodyLess(Class clazz) {
        return clazz.methods.isEmpty() && clazz.fields.isEmpty() && clazz.constructors.isEmpty()
    }

    private static boolean isBodyLess(AbstractClass abstractClass) {
        return abstractClass.methods.isEmpty() && abstractClass.fields.isEmpty() && abstractClass.constructors.isEmpty()
    }

    private static boolean isBodyLess(Enum enumEntry) {
        return enumEntry.values.isEmpty()
    }

    private static String convertDependencies(PlantUml plantUml) {
        StringBuilder stringBuilder = new StringBuilder()
        plantUml.dependencies.each { dependency ->
            stringBuilder.append("${dependency.from.name}${getFromCardinality(dependency.from.cardinality)} ${dependencyMap[dependency.dependencyType]} ${getToCardinality(dependency.to.cardinality)}${dependency.to.name}${getLabel(dependency.label)}\n")
        }
        return stringBuilder.toString()
    }

    private static String getLabel(String label) {
        return label != "" ? " : $label" : ""
    }

    private static String getFromCardinality(String cardinality) {
        return cardinality != "" ? " \"$cardinality\"" : ""
    }

    private static String getToCardinality(String cardinality) {
        return cardinality != "" ? "\"$cardinality\" " : ""
    }

}