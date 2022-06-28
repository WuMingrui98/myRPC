package xyz.raygetoffer.serialize;

import lombok.*;

import java.io.Serializable;

/**
 * @author mingruiwu
 * @create 2022/6/27 22:15
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Student implements Serializable {
    private static final long serialVersionUID = 947247088365372891L;
    private String name;
    private int age;
    private int score;
}
