import java.util.*;

/**
 * A class that defines a new permutation code, as well as methods for encoding
 * and decoding of the messages that use this code.
 **/
class PermutationCode {
  // The original list of characters to be encoded
  ArrayList<String> alphabet = 
      new ArrayList<String>(Arrays.asList(
                  "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", 
                  "k", "l", "m", "n", "o", "p", "q", "r", "s", 
                  "t", "u", "v", "w", "x", "y", "z"));

  // The encoded alphabet: the 1-string at index 0 is the encoding of "a",
  // the 1-string at index 1 is the encoding of "b", etc.
  ArrayList<String> code;

  // A random number generator
  Random rand;

  // Create a new random instance of the encoder/decoder with a new permutation code 
  PermutationCode() {
    this(new Random());
  }
  // Create a particular random instance of the encoder/decoder
  PermutationCode(Random r) {
    this.rand = r;
    this.code = this.initEncoder();
  }

  // Create a new instance of the encoder/decoder with the given code 
  PermutationCode(ArrayList<String> code) {
    this.code = code;
  }

  // Initialize the encoding permutation of the characters
  ArrayList<String> initEncoder() {
    return null; // NOTE: You should fix this!
  }

  // produce an encoded String from the given String
  // You can assume the given string consists only of lowercase characters
  String encode(String source) {
    return null; // NOTE: You should fix this!
  }

  // produce a decoded String from the given String
  // You can assume the given string consists only of lowercase characters
  String decode(String code) {
    return null; // NOTE: You should fix this!
  }
}