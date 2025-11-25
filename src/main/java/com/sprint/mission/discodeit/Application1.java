package com.sprint.mission.discodeit;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Application1 {
    public static void main(String[] args) {
        BufferedWriter bw = null;

        try {
            bw = new BufferedWriter(new FileWriter("src/main/java/com/sprint/mission/discodeit/testBuffered.txt"));
            bw.write("안녕하세요\n");
            bw.write("반갑습니다\n");
            bw.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            /* 설명. close()를 호출하면 내부적으로 flush()를 하고 나서 자원을 반납한다. */
            if(bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader("src/main/java/com/sprint/mission/discodeit/testBuffered.txt"));
            String temp;

            while((temp = br.readLine()) != null) {
                System.out.println(temp);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
