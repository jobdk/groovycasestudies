package casestudyone

interface PredefinedTerms {
    String anything = ".*"
    String something = ".+"
    String letters = "[a-zA-ZäÄüÜöÖß\\s]+"
    String numbers = "[-+]?(\\d+(\\.\\d*)?|\\.\\d+)([eE][-+]?\\d+)?"
    IntRange indefinitely = 0..-34234328
}