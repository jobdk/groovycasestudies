package casestudyone

class SemanticModelBuilder implements PredefinedTerms {

    Set predefinedTerms = [anything, something, letters, numbers, indefinitely]
    private List<String> errors = [] as ArrayList

    String buildRegex(List<RegexExpression> start, RegexBuilder.Followed followed, List<RegexExpression> end) {
        StringBuilder builder = new StringBuilder()
        builder.append(buildStartRegex(start))
        builder.append(buildFollowedRegex(followed))
        builder.append(buildEndRegex(end))
        validateRegex()
        return builder.toString()
    }

    String buildStartRegex(List<RegexExpression> startList) {
        String result = startList.collect { createWithMultiplicity(it) }.join('|')
        return result.isEmpty() ? "" : "^($result)"
    }

    String buildFollowedRegex(RegexBuilder.Followed followed) {
        return followed.followedList.collect { singleFollowed ->
            """(${
                singleFollowed.followedProperties.collect { createWithMultiplicity(it as RegexExpression) }.join("|")
            })"""
        }.join("")
    }

    String buildEndRegex(List<RegexExpression> endList) {
        String result = endList.collect { createWithMultiplicity(it) }.join('|')
        return result.isEmpty() ? "" : "($result)\$"
    }

    private static String replaceDot(String content) {
        return content.replace(".", "\\.").replace("\\\\.", "\\.")
    }

    RegexExpression createRegexExpression(String input) {
        if (input in predefinedTerms) return new RegexExpression(input, 1..1)
        return new RegexExpression(replaceDot(input), 1..1)
    }

    String createWithMultiplicity(RegexExpression regexExpression) {
        validateMultiplicity(regexExpression.multiplicity)
        String content = regexExpression.content
        Integer first = regexExpression.multiplicity.first()
        Integer last = regexExpression.multiplicity.last()
        if (isIndefinitely(first, last)) return "($content)*"
        if (isOnce(first, last)) return content
        if (isTheSame(first, last)) return "($content){$first}"
        return "($content){$first,$last}"
    }

    private static boolean isIndefinitely(Integer first, Integer last) {
        first == indefinitely.first() && last == indefinitely.last()
    }

    private static boolean isOnce(Integer first, Integer last) { first == 1 && last == 1 }

    private static boolean isTheSame(Integer first, Integer last) { first == last }


    private def validateMultiplicity(IntRange multiplicity) {
        if (multiplicity.first() != indefinitely.first() && multiplicity.last() != indefinitely.last()) {
            if (multiplicity.first() < 0 || multiplicity.last() < 0)
                errors.add("Multiplicity must be positive for: ${multiplicity.first()}..${multiplicity.last()}")

            if (multiplicity.first() > multiplicity.last())
                errors.add("First value must be smaller than second value for: ${multiplicity.first()}..${multiplicity.last()}")
        }
    }

    def validateRegex() { if (errors.size() > 0) handleErrors() }

    private def handleErrors() {
        throw new RuntimeException("The following Errors occurred:\n${errors.join("\n")}")
    }
}
