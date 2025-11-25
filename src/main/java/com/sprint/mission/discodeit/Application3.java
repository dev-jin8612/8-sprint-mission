package com.ohgiraffers.section03.filterstream;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Application3 {
    public static void main(String[] args) {
        DataOutputStream dout = null;

        try {
            dout = new DataOutputStream(new FileOutputStream("src/main/java/com/ohgiraffers/section03/filterstream/score.txt"));

            /* 설명. 파일에 자료형별로 기록 */
            dout.writeUTF("홍길동");
            dout.writeInt(95);
            dout.writeChar('A');
            dout.writeUTF("이순신");
            dout.writeInt(87);
            dout.writeChar('B');
            dout.writeUTF("김철수");
            dout.writeInt(73);
            dout.writeChar('C');

        } catch(FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(dout != null) {
                try {
                    dout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        DataInputStream din = null;

        try {
            din = new DataInputStream(new FileInputStream("src/main/java/com/ohgiraffers/section03/filterstream/score.txt"));

            while(true){
                /* 설명. 파일에 기록한 순서대로 읽어들이지 않는 경우 에러 발생함 */
                System.out.println(din.readUTF() + ", " + din.readInt() + ", " + din.readChar());
            }
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (EOFException e) {
            System.out.println("파일 읽기 완료!");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(din != null) {
                try {
                    din.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
