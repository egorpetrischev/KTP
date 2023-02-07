public class Palindrome {
    public static void main(String[] args) {
        for (int i = 0; i < args.length; i++){
            String s = args[i];
            System.out.println(isPalindrome(s));
        }
    }

    // Check if str is palindrome
    public static boolean isPalindrome(String str){
        return str.equals(reverseString(str));
    }

    // Reverses str
    public static String reverseString(String str){
        String revStr = "";
        for (int i = str.length() - 1; i >= 0; i--){
            revStr += str.charAt(i);
        }
        return revStr;
    }
}