package simple.com.liss.domain;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class DemoEvent {

    private String name;

    private String message;
}
