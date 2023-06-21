package casestudyone


import java.util.regex.Pattern

import static groovy.lang.Closure.DELEGATE_ONLY

class RegexBuilder implements PredefinedTerms {

    private List<RegexExpression> startProperties = [] as ArrayList
    private Followed followed = new Followed()
    private List<RegexExpression> endProperties = [] as ArrayList
    private SemanticModelBuilder semanticModel = new SemanticModelBuilder()

    static Pattern regex(@DelegatesTo(value = RegexBuilder, strategy = DELEGATE_ONLY) final Closure closure) {
        final RegexBuilder dsl = new RegexBuilder()
        closure.delegate = dsl
        closure.resolveStrategy = DELEGATE_ONLY
        closure.call()
        return ~dsl.buildRegex()
    }

    private String buildRegex() { return semanticModel.buildRegex(startProperties, followed, endProperties) }

    static String innerRegex(@DelegatesTo(value = RegexBuilder.InnerRegex, strategy = DELEGATE_ONLY) final Closure closure) {
        final RegexBuilder dsl = new RegexBuilder()
        closure.delegate = dsl
        closure.resolveStrategy = DELEGATE_ONLY
        closure.call()
        return dsl.buildRegex()
    }

    class InnerRegex {
        RegexOrAndOccursCommands followedWith(String input) { RegexBuilder.followedWith(input) }

        static String innerRegex(@DelegatesTo(value = RegexBuilder.InnerRegex, strategy = DELEGATE_ONLY) final Closure closure) {
            final RegexBuilder dsl = new RegexBuilder()
            closure.delegate = dsl
            closure.resolveStrategy = DELEGATE_ONLY
            closure.call()
            return dsl.buildRegex()
        }
    }

    // _________________ starts with _________________
    RegexOrAndOccursCommands startsWith(String input) {
        startProperties.clear()
        startProperties.add(semanticModel.createRegexExpression(input))
        return new StartOrAndOccurs()
    }

    class StartOrAndOccurs implements RegexOrAndOccursCommands {

        @Override
        RegexOrAndOccursCommands or(String input) {
            startProperties.add(semanticModel.createRegexExpression(input))
            return this
        }

        @Override
        RegexOrCommand occurs(IntRange intRange) {
            RegexExpression lastEntry = startProperties[startProperties.size() - 1]
            lastEntry.multiplicity = intRange
            return new StartOr()
        }

        class StartOr implements RegexOrCommand {
            @Override
            RegexOrAndOccursCommands or(String input) {
                startProperties.add(semanticModel.createRegexExpression(input))
                return new StartOrAndOccurs()
            }
        }
    }

    // _________________ followed with _________________
    RegexOrAndOccursCommands followedWith(String input) {
        Followed.SingleFollowed singleFollowed = new Followed.SingleFollowed(followed)
        singleFollowed.followedProperties.add(semanticModel.createRegexExpression(input))
        followed.followedList.add(singleFollowed)
        return new Followed.SingleFollowed.FollowedOrAndOccurs(singleFollowed)
    }

    class Followed {
        private List<SingleFollowed> followedList = [] as ArrayList
        class SingleFollowed {
            private List<RegexExpression> followedProperties = [] as ArrayList

            class FollowedOrAndOccurs implements RegexOrAndOccursCommands {
                @Override
                RegexOrAndOccursCommands or(String input) {
                    followedProperties.add(semanticModel.createRegexExpression(input))
                    return this
                }

                @Override
                RegexOrCommand occurs(IntRange intRange) {
                    RegexExpression lastEntry = followedProperties[followedProperties.size() - 1]
                    lastEntry.multiplicity = intRange
                    return new FollowedOr()
                }

                class FollowedOr implements RegexOrCommand {
                    @Override
                    RegexOrAndOccursCommands or(String input) {
                        followedProperties.add(semanticModel.createRegexExpression(input))
                        return new FollowedOrAndOccurs()
                    }
                }
            }
        }
    }

    // _________________ ends with _________________
    RegexOrAndOccursCommands endsWith(String input) {
        endProperties.clear()
        endProperties.add(semanticModel.createRegexExpression(input))
        return new EndOrAndOccurs()
    }

    class EndOrAndOccurs implements RegexOrAndOccursCommands {

        @Override
        RegexOrAndOccursCommands or(String input) {
            endProperties.add(semanticModel.createRegexExpression(input))
            return this
        }

        @Override
        RegexOrCommand occurs(IntRange intRange) {
            RegexExpression lastEntry = endProperties[endProperties.size() - 1]
            lastEntry.multiplicity = intRange
            return new EndOr()
        }

        class EndOr implements RegexOrCommand {
            @Override
            RegexOrAndOccursCommands or(String input) {
                endProperties.add(semanticModel.createRegexExpression(input))
                return new EndOrAndOccurs()
            }
        }
    }
}