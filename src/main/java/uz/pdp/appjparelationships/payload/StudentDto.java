package uz.pdp.appjparelationships.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.appjparelationships.entity.Subject;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StudentDto {
    private String firstName;
    private String lastName;

    private String city;

    private String district;

    private String street;

    private Integer groupId;

    private List<Subject> subjects;
}
