/*
 * Boyer-Moore String Matching Algorithm
 * Uses:
 * 1. Bad Character Heuristic
 * 2. Good Suffix Heuristic
 * Shift = max(Bad Character Shift, Good Suffix Shift)
 */

public class bm {
    static int ALPHABET = 256;

    // Algorithm: BuildBadChar(pattern)
    private static int[] buildBadChar(String pattern) {
        int m = pattern.length();
        int[] badChar = new int[ALPHABET];

        // Initialize all characters as not present
        for (int i = 0; i < ALPHABET; i++)
            badChar[i] = -1;

        // Store last occurrence of each character
        for (int i = 0; i < m; i++)
            badChar[(int) pattern.charAt(i)] = i;
        return badChar;
    }

    // Algorithm: BuildGoodSuffix(pattern)
    // Suffix array for good suffix table
    private static int[] computeSuffix(String pattern) {
        int m = pattern.length();
        int[] suffix = new int[m];
        suffix[m - 1] = m;
        int g = m - 1, f = 0;
        for (int i = m - 2; i >= 0; i--) {
            if (i > g && suffix[i + m - 1 - f] < i - g) {
                suffix[i] = suffix[i + m - 1 - f];
            } else {
                if (i < g)
                    g = i;
                f = i;
                while (g >= 0 && pattern.charAt(g) == pattern.charAt(g + m - 1 - f))
                    g--;
                suffix[i] = f - g;
            }
        }
        return suffix;
    }

    // Good suffix table
    private static int[] buildGoodSuffix(String pattern) {
        int m = pattern.length();
        int[] suffix = computeSuffix(pattern);
        int[] goodSuffix = new int[m];

        // Default: full skip
        for (int i = 0; i < m; i++)
            goodSuffix[i] = m;

        // Case 2: prefix of pattern matches suffix of good suffix
        int j = 0;
        for (int i = m - 1; i >= 0; i--) {
            if (suffix[i] == i + 1) {
                for (; j < m - 1 - i; j++) {
                    if (goodSuffix[j] == m)
                        goodSuffix[j] = m - 1 - i;
                }
            }
        }

        // Case 1: another occurrence of good suffix exists in pattern
        for (int i = 0; i <= m - 2; i++)
            goodSuffix[m - 1 - suffix[i]] = m - 1 - i;

        return goodSuffix;
    }

    // Algorithm: SearchBM(text, pattern)
    public static void search(String text, String pattern) {
        int n = text.length();
        int m = pattern.length();

        int[] badChar = buildBadChar(pattern);
        int[] goodSuffix = buildGoodSuffix(pattern);

        int s = 0; // current shift of pattern relative to text

        while (s <= n - m) {
            int j = m - 1;

            // Compare pattern right to left
            while (j >= 0 && pattern.charAt(j) == text.charAt(s + j))
                j--;

            // Pattern found
            if (j < 0) {
                System.out.println("Pattern found at index " + s);
                return; // single match — stop after first occurrence
            } else {
                // Calculate shift using Bad Character heuristic
                int bcShift = j - badChar[(int) text.charAt(s + j)];

                // Calculate shift using Good Suffix heuristic
                int gsShift = goodSuffix[j];

                // Use the larger shift value
                int moveBy = Math.max(bcShift, gsShift);
                s += moveBy;
            }
        }

        System.out.println("Pattern not found.");
    }

    public static void main(String[] args) {
        search("AABCBCABCBCD", "ABCBCD");
        search("AABCBCXBCBCD", "ABCBCD");
    }
}