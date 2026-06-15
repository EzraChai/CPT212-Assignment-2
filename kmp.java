/*
 * Knuth-Morris-Pratt (KMP) String Matching Algorithm
 * Uses Failure Function (LPS table) to avoid rechecking characters
 * Time Complexity: O(n + m)
 */

public class kmp {

    // Algorithm: Failure(pattern)
    private static int[] Failure(char[] pattern) {
        int m = pattern.length;
        int[] fail = new int[m]; // all overlaps initialized to zero
        int j = 1;
        int k = 0;
        while (j < m) {
            if (pattern[j] == pattern[k]) { // k+1 characters match
                fail[j] = k + 1;
                j++;
                k++;
            } else if (k > 0) { // k follows a matching prefix
                k = fail[k - 1];
            } else { // no match found starting at j
                j++;
            }
        }
        return fail;
    }

    // Algorithm: FindKMP(text, pattern)
    public static int findKMP(char[] text, char[] pattern) {
        int n = text.length;
        int m = pattern.length;
        if (m == 0)
            return 0; // trivial case: empty pattern
        int[] fail = Failure(pattern);
        int j = 0; // index into text
        int k = 0; // index into pattern
        while (j < n) {
            if (text[j] == pattern[k]) { // characters match
                if (k == m - 1)
                    return j - m + 1; // full match found
                j++;
                k++;
            } else if (k > 0) {
                k = fail[k - 1]; // reuse matched prefix
            } else {
                j++; // no reuse possible, advance text
            }
        }
        return -1; // pattern not found
    }

    public static void main(String[] args) {
        System.out.println(findKMP("ababcabcabababd".toCharArray(), "ababd".toCharArray()));
    }

}
