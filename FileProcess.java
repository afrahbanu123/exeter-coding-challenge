package com.example.demo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileProcess {
	public static void main(String[] args) throws IOException {
		
		//To find the time
		long startTime = System.currentTimeMillis();
	
		//To find the memory used in program
        Runtime runtime = Runtime.getRuntime();
        long initialMemory = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Initial memory used: " + (initialMemory / (1024 * 1024)) + " MB");

		String inputFilePath = "E:\\Afrah\\TranslateWordsChallenge\\t8.shakespeare.txt";
		String outputFilePath = "E:\\Afrah\\TranslateWordsChallenge\\output\\t8.shakespeare.txt";

		String availableWordsFilePath = "E:\\Afrah\\TranslateWordsChallenge\\find_words.txt";

		String inputExcelFilePath = "E:\\Afrah\\TranslateWordsChallenge\\french_dictionary.csv";

		Map<String, String> wordMap = new HashMap<>();
		Map<String, String> equivalentValuesMap = new HashMap<>();
		Map<String, Integer> wordsReplacedMap = new HashMap<>();

		mapValueAddition(inputExcelFilePath, equivalentValuesMap);
		
		mapWordAddition(availableWordsFilePath, wordMap);
		
		fileValueReplace(inputFilePath, outputFilePath, equivalentValuesMap, wordMap, wordsReplacedMap);

		System.out.println(wordsReplacedMap);
		
		long endTime = System.currentTimeMillis();
        long timeElapsed = endTime - startTime;
        System.out.println("Execution time in milliseconds: " + timeElapsed);
        
        long finalMemory = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Final memory used: " + ((finalMemory - initialMemory) / (1024 * 1024)) + " MB");

	}

	public static void mapValueAddition(String equivalentValuesFilePath, Map<String, String> equivalentValues)
			throws IOException {

		try (BufferedReader br = new BufferedReader(new FileReader(equivalentValuesFilePath))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(",");
				if (parts.length >= 2) {
					equivalentValues.put(parts[0], parts[1]);
				}
			}
		} catch (IOException e) {
			System.err.format("IOException: %s%n", e);
		}
	}

	public static void mapWordAddition(String availableWordsFilePath, Map<String, String> wordMap)
			throws IOException {

		try (BufferedReader br = new BufferedReader(new FileReader(availableWordsFilePath))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] words = line.split("\\s+");
				for (String word : words) {
					wordMap.put(word.toLowerCase(), word.toLowerCase());
				}
			}
		} catch (IOException e) {
			System.err.format("IOException: %s%n", e);
		}
	}
	
	public static void fileValueReplace(String inputFilePath, String outputFilePath, Map<String, String> equivalentValuesMap, 
			Map<String, String> wordMap, Map<String, Integer> wordsReplacedMap) {
		try (BufferedReader br = new BufferedReader(new FileReader(inputFilePath));
				BufferedWriter bw = new BufferedWriter(new FileWriter(outputFilePath))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] words = line.split("\\s+");
				for (String word : words) {
					String value = word.toLowerCase();
					if (wordMap.containsKey(value)) {
						String equivalentValue = equivalentValuesMap.get(value);
						String mapValue=value.concat("+").concat(equivalentValue);
						if (equivalentValue != null) {
							if (wordsReplacedMap.containsKey(mapValue)) {
								wordsReplacedMap.put(mapValue, wordsReplacedMap.get(mapValue) + 1);
							} else {
								wordsReplacedMap.put(mapValue, 1);
							}
							
							line = line.replace(word, equivalentValue);
						}
					}
				}
				bw.write(line);
				bw.newLine();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
