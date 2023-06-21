package casestudytwo.experiments.body.enumaration

import casestudytwo.plantuml.Enum
import casestudytwo.plantuml.PlantUml

import static groovy.lang.Closure.DELEGATE_ONLY

class PlantUmlDslBuilder {
    ArrayList<Enum> enums = new ArrayList<>()

    static PlantUml startuml(@DelegatesTo(value = PlantUmlDslBuilder, strategy = DELEGATE_ONLY) final Closure closure) {
        final PlantUmlDslBuilder dsl = new PlantUmlDslBuilder()
        closure.delegate = dsl
        closure.resolveStrategy = DELEGATE_ONLY
        closure.call()
        return dsl.buildPlantUml()
    }

    PlantUml buildPlantUml() {
        return new PlantUml(null, null, enums, null, null)
    }

    EnumBuilder Enum(String name) {
        enums += new Enum(name, List.of())
        return new EnumBuilder()
    }

    class EnumBuilder {
        ArrayList<String> values = new ArrayList<>()

        void containing1(@DelegatesTo(value = Map, strategy = DELEGATE_ONLY) final Closure closure) {
            // With a List
            // Enum containing(@DelegatesTo(value = List, strategy = DELEGATE_ONLY) final Closure closure) {
            // List env = []
            EnumBuilder enumBuilder = new EnumBuilder()
            closure.delegate = enumBuilder
            closure.resolveStrategy = DELEGATE_ONLY
            Map env = [:]
            env.with(closure)
            this.enums[this.enums.size() - 1] = new Enum(this.enums.last().name, env.values().asList())
        }


        void containing(@DelegatesTo(value = EnumBuilder, strategy = DELEGATE_ONLY) final Closure closure) {
            final EnumBuilder enumBuilder = new EnumBuilder()
            closure.delegate = enumBuilder
            closure.resolveStrategy = DELEGATE_ONLY
            closure.call()
            this.enums[this.enums.size() - 1] = new Enum(this.enums.last().name, enumBuilder.values)
        }

        void and(String value) {
            values += value
        }
    }
}
