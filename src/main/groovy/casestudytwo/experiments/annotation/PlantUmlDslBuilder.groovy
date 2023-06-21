package casestudytwo.experiments.annotation


import casestudytwo.plantuml.Class
import casestudytwo.plantuml.PlantUml

import static groovy.lang.Closure.DELEGATE_ONLY

class PlantUmlDslBuilder {
    List<Class> classes = List.of()

    static PlantUml startuml(@DelegatesTo(value = PlantUmlDslBuilder, strategy = DELEGATE_ONLY) final Closure closure) {
        final PlantUmlDslBuilder dsl = new PlantUmlDslBuilder()
        closure.delegate = dsl
        closure.resolveStrategy = DELEGATE_ONLY
        closure.call()
        return dsl.buildPlantUml()
    }

    Class addClass(String name) {
        Class clazz = new Class(name, List.of(), List.of(), List.of())
        classes += clazz
        return clazz
    }


    PlantUml buildPlantUml() {
        return new PlantUml(classes, List.of(), List.of(), List.of(), List.of())
    }
}