//package com.emanuelvictor.api.functional.accessmanager.others;
//
//import lombok.SneakyThrows;
//import org.junit.jupiter.api.Test;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.util.HashSet;
//import java.util.Set;
//
////@SpringBootTest
//class Nana {
//
//    @Test
//    @SneakyThrows
//    void contextLoads() {
//        final StringBuilder nonDistinctLinesString = new StringBuilder();
//        final StringBuilder distinctLinesString = new StringBuilder();
//
//        final Set<String> nonDistinctLines = new HashSet<>();
//        final Set<String> distinctLines = new HashSet<>();
//
//        final File file = new File("raw_sem_pontos_distinct");
//        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
//            String line;
//            while ((line = br.readLine()) != null) {
//                nonDistinctLines.add(line);
//                distinctLines.add(removeDots(line));
//            }
//        }
//
//
//        distinctLines.forEach(distinctLinesString::append);
//        nonDistinctLines.forEach(nonDistinctLinesString::append);
//
//        System.out.println("-----------");
//        distinctLines.forEach(System.out::println);
//        System.out.println(nonDistinctLines.size());
//        System.out.println(nonDistinctLinesString.length());
//        System.out.println(distinctLines.size());
//        System.out.println(distinctLinesString.length());
//    }
//
//
//    private static String removeDots(final String line) {
//        return line.replace(line.substring(line.indexOf(".")), "");
//    }
//
//}
