package Indexer;

import java.util.Vector;

class NewString {
    public String str;

    NewString() {
        str = "";
    }
}

/**
 * A class to perform stemming on words (Provided by Lab 3).
 */
public class Porter {
    private String Clean(String str) {
        int last = str.length();

        StringBuilder temp = new StringBuilder();

        for (int i = 0; i < last; i++) {
            if (Character.isLetterOrDigit(str.charAt(i)))
                temp.append(str.charAt(i));
        }

        return temp.toString();
    }

    private boolean hasSuffix(String word, String suffix, NewString stem) {
        if (word.length() <= suffix.length())
            return false;
        if (suffix.length() > 1)
            if (word.charAt(word.length() - 2) != suffix.charAt(suffix.length() - 2))
                return false;

        StringBuilder tmp = new StringBuilder();
        for (int i = 0; i < word.length() - suffix.length(); i++)
            tmp.append(word.charAt(i));
        stem.str = tmp.toString();

        tmp = new StringBuilder(stem.str);
        for (int i = 0; i < suffix.length(); i++)
            tmp.append(suffix.charAt(i));

        return tmp.toString().compareTo(word) == 0;
    }

    private boolean vowel(char ch, char prev) {
        return switch (ch) {
            case 'a', 'e', 'i', 'o', 'u' -> true;
            case 'y' -> switch (prev) {
                case 'a', 'e', 'i', 'o', 'u' -> false;
                default -> true;
            };
            default -> false;
        };
    }

    private int measure(String stem) {
        int i = 0, count = 0;
        int length = stem.length();

        while (i < length) {
            for (; i < length; i++) {
                if (i > 0) {
                    if (vowel(stem.charAt(i), stem.charAt(i - 1)))
                        break;
                } else {
                    if (vowel(stem.charAt(i), 'a'))
                        break;
                }
            }

            for (i++; i < length; i++) {
                if (i > 0) {
                    if (!vowel(stem.charAt(i), stem.charAt(i - 1)))
                        break;
                } else {
                    if (!vowel(stem.charAt(i), '?'))
                        break;
                }
            }
            if (i < length) {
                count++;
                i++;
            }
        }

        return (count);
    }

    private boolean containsVowel(String word) {
        for (int i = 0; i < word.length(); i++)
            if (i > 0) {
                if (vowel(word.charAt(i), word.charAt(i - 1)))
                    return true;
            } else {
                if (vowel(word.charAt(0), 'a'))
                    return true;
            }

        return false;
    }

    private boolean cvc(String str) {
        int length = str.length();

        if (length < 3)
            return false;

        if ((!vowel(str.charAt(length - 1), str.charAt(length - 2)))
                && (str.charAt(length - 1) != 'w')
                && (str.charAt(length - 1) != 'x')
                && (str.charAt(length - 1) != 'y')
                && (vowel(str.charAt(length - 2), str.charAt(length - 3)))) {

            if (length == 3) {
                return !vowel(str.charAt(0), '?');
            } else {
                return !vowel(str.charAt(length - 3), str.charAt(length - 4));
            }
        }

        return false;
    }

    private String step1(String str) {

        NewString stem = new NewString();

        if (str.charAt(str.length() - 1) == 's') {
            if ((hasSuffix(str, "sses", stem)) || (hasSuffix(str, "ies", stem))) {
                StringBuilder tmp = new StringBuilder();
                for (int i = 0; i < str.length() - 2; i++)
                    tmp.append(str.charAt(i));
                str = tmp.toString();
            } else {
                if ((str.length() == 1) && (str.charAt(0) == 's')) {
                    str = "";
                    return str;
                }
                if (str.charAt(str.length() - 2) != 's') {
                    StringBuilder tmp = new StringBuilder();
                    for (int i = 0; i < str.length() - 1; i++)
                        tmp.append(str.charAt(i));
                    str = tmp.toString();
                }
            }
        }

        if (hasSuffix(str, "eed", stem)) {
            if (measure(stem.str) > 0) {
                StringBuilder tmp = new StringBuilder();
                for (int i = 0; i < str.length() - 1; i++)
                    tmp.append(str.charAt(i));
                str = tmp.toString();
            }
        } else {
            if ((hasSuffix(str, "ed", stem)) || (hasSuffix(str, "ing", stem))) {
                if (containsVowel(stem.str)) {

                    StringBuilder tmp = new StringBuilder();
                    for (int i = 0; i < stem.str.length(); i++)
                        tmp.append(str.charAt(i));
                    str = tmp.toString();
                    if (str.length() == 1)
                        return str;

                    if ((hasSuffix(str, "at", stem))
                            || (hasSuffix(str, "bl", stem))
                            || (hasSuffix(str, "iz", stem))) {
                        str += "e";
                    } else {
                        int length = str.length();
                        if ((str.charAt(length - 1) == str.charAt(length - 2))
                                && (str.charAt(length - 1) != 'l')
                                && (str.charAt(length - 1) != 's')
                                && (str.charAt(length - 1) != 'z')) {
                            tmp = new StringBuilder();
                            for (int i = 0; i < str.length() - 1; i++)
                                tmp.append(str.charAt(i));
                            str = tmp.toString();
                        } else if (measure(str) == 1) {
                            if (cvc(str))
                                str += "e";
                        }
                    }
                }
            }
        }

        if (hasSuffix(str, "y", stem))
            if (containsVowel(stem.str)) {
                StringBuilder tmp = new StringBuilder();
                for (int i = 0; i < str.length() - 1; i++)
                    tmp.append(str.charAt(i));
                str = tmp + "i";
            }
        return str;
    }

    private String step2(String str) {

        String[][] suffixes = {{"ational", "ate"},
                {"tional", "tion"},
                {"enci", "ence"},
                {"anci", "ance"},
                {"izer", "ize"},
                {"iser", "ize"},
                {"abli", "able"},
                {"alli", "al"},
                {"entli", "ent"},
                {"eli", "e"},
                {"ousli", "ous"},
                {"ization", "ize"},
                {"isation", "ize"},
                {"ation", "ate"},
                {"ator", "ate"},
                {"alism", "al"},
                {"iveness", "ive"},
                {"fulness", "ful"},
                {"ousness", "ous"},
                {"aliti", "al"},
                {"iviti", "ive"},
                {"biliti", "ble"}};
        NewString stem = new NewString();
        for (String[] suffix : suffixes) {
            if (hasSuffix(str, suffix[0], stem)) {
                if (measure(stem.str) > 0) {
                    str = stem.str + suffix[1];
                    return str;
                }
            }
        }

        return str;
    }

    private String step3(String str) {

        String[][] suffixes = {{"icate", "ic"},
                {"ative", ""},
                {"alize", "al"},
                {"alise", "al"},
                {"iciti", "ic"},
                {"ical", "ic"},
                {"ful", ""},
                {"ness", ""}};
        NewString stem = new NewString();
        for (String[] suffix : suffixes) {
            if (hasSuffix(str, suffix[0], stem))
                if (measure(stem.str) > 0) {
                    str = stem.str + suffix[1];
                    return str;
                }
        }
        return str;
    }

    private String step4(String str) {

        String[] suffixes = {"al", "ance", "ence", "er", "ic", "able", "ible",
                "ant", "ement", "ment", "ent", "sion", "tion",
                "ou", "ism", "ate", "iti", "ous", "ive", "ize", "ise"};

        NewString stem = new NewString();

        for (String suffix : suffixes) {
            if (hasSuffix(str, suffix, stem)) {
                if (measure(stem.str) > 1) {
                    str = stem.str;
                    return str;
                }
            }
        }
        return str;
    }

    private String step5(String str) {

        if (str.charAt(str.length() - 1) == 'e') {
            if (measure(str) > 1) {/* measure(str)==measure(stem) if ends in vowel */
                StringBuilder tmp = new StringBuilder();
                for (int i = 0; i < str.length() - 1; i++)
                    tmp.append(str.charAt(i));
                str = tmp.toString();
            } else if (measure(str) == 1) {
                StringBuilder stem = new StringBuilder();
                for (int i = 0; i < str.length() - 1; i++)
                    stem.append(str.charAt(i));

                if (!cvc(stem.toString()))
                    str = stem.toString();
            }
        }

        if (str.length() == 1)
            return str;
        if ((str.charAt(str.length() - 1) == 'l')
                && (str.charAt(str.length() - 2) == 'l')
                && (measure(str) > 1))
            if (measure(str) > 1) {/* measure(str)==measure(stem) if ends in vowel */
                StringBuilder tmp = new StringBuilder();
                for (int i = 0; i < str.length() - 1; i++)
                    tmp.append(str.charAt(i));
                str = tmp.toString();
            }
        return str;
    }

    private String stripPrefixes(String str) {

        String[] prefixes = {"kilo", "micro", "milli", "intra", "ultra", "mega",
                "nano", "pico", "pseudo"};

        for (String prefix : prefixes) {
            if (str.startsWith(prefix)) {
                StringBuilder temp = new StringBuilder();
                for (int j = 0; j < str.length() - prefix.length(); j++)
                    temp.append(str.charAt(j + prefix.length()));
                return temp.toString();
            }
        }

        return str;
    }


    private String stripSuffixes(String str) {

        str = step1(str);
        if (!str.isEmpty())
            str = step2(str);
        if (!str.isEmpty())
            str = step3(str);
        if (!str.isEmpty())
            str = step4(str);
        if (!str.isEmpty())
            str = step5(str);

        return str;
    }

    /**
     * Perform stemming on a list of words by striping affixes from the words.
     * @param words The list of words to be stemmed.
     * @return The list of words after stemming.
     */
    public Vector<String> stripAffixes(Vector<String> words) {
        Vector<String> stemmedWords = new Vector<>();
        for (String str : words) {
            str = str.toLowerCase();
            str = Clean(str);
            if ((str.length() > 2)) {
                str = stripPrefixes(str);
                if (!str.isEmpty()) {
                    str = stripSuffixes(str);
                }
            }
            if (!str.isEmpty()) {
                stemmedWords.add(str);
            }
        }
        return stemmedWords;
    }

    /**
     * Perform stemming on a word by striping affixes from the word.
     * @param word The word to be stemmed.
     * @return The word after stemming.
     */
    public String stripAffixes(String word) {
        word = word.toLowerCase();
        word = Clean(word);
        if ((word.length() > 2)) {
            word = stripPrefixes(word);
            if (!word.isEmpty()) {
                word = stripSuffixes(word);
            }
        }
        return word;
    }

    /**
     * Remove symbols from a list of words.
     * @param words The list of words to remove symbols.
     * @return The list of words after removing symbols.
     */
    public Vector<String> removeSymbols(Vector<String> words) {
        Vector<String> result = new Vector<>();
        for (String word : words) {
            result.add(word.replaceAll("[^a-zA-Z0-9]", "").toLowerCase());
        }
        return result;
    }

    /**
     * Remove symbols from a word.
     * @param word The word to remove symbols.
     * @return The word after removing symbols.
     */
    public String removeSymbols(String word) {
        return word.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
    }

}
