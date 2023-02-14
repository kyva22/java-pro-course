package homeworks.VKS_07;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class BookManager {

    private File findBook(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the title of the book in camelCase format");
        String bookTitle = scanner.nextLine();

        File file = new File("java\\homeworks\\VKS_07\\books\\" + bookTitle);

        if(!file.exists()){
            System.out.println("Book not found, please try again");
            file = findBook();
        }

        return file;
    }

    public void getBookStatistic(){
        StringBuilder bookBuild = new StringBuilder();
        File book = findBook();
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(book))) {
            while(bufferedReader.ready()){
                bookBuild.append(bufferedReader.readLine()).append(" ");
            }
        } catch (IOException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            return;
        }
        String withoutSpecialChar = bookBuild.toString().replaceAll("(?U)[^\\p{L}\\p{N}\\s]+(\\n)", "").toLowerCase();

        int uniqueWords = Arrays.stream(withoutSpecialChar.split(" ")).collect(Collectors.toSet()).size();

        Map<String, Integer> mostPopularWord = tenMostPopularWord(withoutSpecialChar);

        String bookTitle = Arrays.stream(book.getName().split("\\.")).toList().get(0);

        statisticLog(mostPopularWord, uniqueWords, bookTitle);
    }

    private Map<String, Integer> tenMostPopularWord(String sentence){
        List<String> separatedWords = Arrays.stream(sentence.split(" ")).toList();
        HashMap<String, Integer> popularWords = new HashMap<>();

        separatedWords.stream().filter(word -> word.length() > 2).forEach(word -> {
            if(popularWords.containsKey(word)){
                popularWords.put(word, popularWords.get(word)+1);
            }else{
                popularWords.put(word, 1);
            }
        });

        HashMap<String, Integer> result = new HashMap<>();

        popularWords.entrySet().stream().sorted(Map.Entry.<String, Integer>comparingByValue().reversed()).limit(10).forEach(e -> result.put(e.getKey(), e.getValue()));

        return result;
    }

    private void statisticLog(Map<String, Integer> mostPopularWord, int uniqueWord, String bookTitle){
        StringBuilder bookLog = new StringBuilder();
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("java\\homeworks\\VKS_07\\books\\" + bookTitle + "_statistic.txt"))){
            mostPopularWord.forEach((key, value) -> {
                    bookLog.append(key).append(" -> ").append(value).append("\n");
            });
            bookLog.append("Number of unique words - ").append(uniqueWord);
            bufferedWriter.write(bookLog.toString());
            bufferedWriter.flush();
        } catch (IOException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            return;
        }
        System.out.println(bookLog);
    }

}