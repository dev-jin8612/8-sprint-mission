package com.ohgiraffers.section03.filterstream.dto;

import java.io.Serializable;

/* 설명.
 *  객체를 입출력을 하기 위해서는 반드시 직렬화(Serialization) 처리를 해야 한다.
 *  직렬화 대상 클래스에 java.io.Serializable 인터페이스를 구현하면 JVM은 해당 클래스의 객체를 직렬화 가능하다고 간주한다.
 *  Serializable 인터페이스의 API를 살펴보면 그 어떠한 메서드도 존재하지 않는 것을 볼 수 있는데,
 *  이러한 인터페이스를 마커(marker) 인터페이스라 부르며, 그저 직렬화를 지원한다는 것을 표시(mark)하는 역할을 한다.
 *  ----------------------------------------------------------------------------------------------------------------------
 *  주의할 점:
 *  1. 직렬화에서 제외할 필드는 transient 키워드를 사용한다.
 *  2. 클래스 변경 시 충돌을 방지하려면 serialVersionUID를 명시적으로 선언하는 것이 좋다.(필수는 아님)
 *  3. 역직렬화(바이트 스트림을 객체로 복원) 과정에서 클래스 구조가 변경되었다면 오류가 발생할 수 있다.
 * */
public class MemberDTO implements Serializable {

    /* 설명.
     *  직렬화 작업 시 사용될 고유한 ID로, staitc final long 타입으로 선언해야 한다.
     *  직렬화 작업에서 클래스 고유 식별자로 사용되며, 클래스 수정 시에도 일관성 있는 역직렬화를 보장한다.
     *  1L, 2L, 100L 등 아무렇게나 값을 설정해도 되지만,
     *  선언하지 않을 경우, JVM은 클래스 구조를 기반으로 유니크 값을 계산해 자동 생성해준다.
     * */
    private static final long serialVersionUID = 1L;

    private String id;
    private String pwd;
    private String name;
    private String email;
    private int age;
    private char gender;
    /* 설명. transient는 특정 필드를 직렬화에서 제외하기 위한 키워드이다. */
    private /* transient */ double point;

    public MemberDTO() {}

    public MemberDTO(String id, String pwd, String name, String email, int age, char gender, double point) {
        super();
        this.id = id;
        this.pwd = pwd;
        this.name = name;
        this.email = email;
        this.age = age;
        this.gender = gender;
        this.point = point;
    }

    public String getId() {
        return id;
    }

    public String getPwd() {
        return pwd;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }

    public char getGender() {
        return gender;
    }

    public double getPoint() {
        return point;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public void setPoint(double point) {
        this.point = point;
    }

    @Override
    public String toString() {
        return "MemberDTO [id=" + id + ", pwd=" + pwd + ", name=" + name + ", email=" + email + ", age=" + age
                + ", gender=" + gender + ", point=" + point + "]";
    }
}
