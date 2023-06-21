package casestudytwo.experiments.annotation

import java.lang.annotation.ElementType
import java.lang.annotation.Target

/** The following code is commented to not infer with other test executives.
 * Uncomment to test.
 */

/**Experiment 1: Using the annotation on top and below of building the uml in order to see if a context can be provided.
 *
 * Result:
 *
 * - The annotation is optional and, therefore, not a good practice and it can also be associated with different objects.
 *
 * - Annotation does not need to have a target below it but it cannot be enforced.*/
@Target([ElementType.LOCAL_VARIABLE])
@interface startuml1 {}

@interface enduml1 {}

/**Experiment 2: Creating the object inside the annotation
 Result:
 Object cannot be created inside the annotation-parameters as it must be a compile-time constant.
 The annotation still needs to be above a target even if the uml can be created inside the parameters.*/
@interface startuml2 {
    Class appliesTo() // Can only be used with Class, cannot be used with PlantUml class.
}





