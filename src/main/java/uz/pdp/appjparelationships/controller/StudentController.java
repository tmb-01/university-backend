package uz.pdp.appjparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appjparelationships.entity.Address;
import uz.pdp.appjparelationships.entity.Group;
import uz.pdp.appjparelationships.entity.Student;
import uz.pdp.appjparelationships.payload.StudentDto;
import uz.pdp.appjparelationships.repository.AddressRepository;
import uz.pdp.appjparelationships.repository.GroupRepository;
import uz.pdp.appjparelationships.repository.StudentRepository;

import java.util.Optional;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    StudentRepository studentRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    GroupRepository groupRepository;

    //1. VAZIRLIK
    @GetMapping("/forMinistry")
    public Page<Student> getStudentListForMinistry(@RequestParam int page) {
        //1-1=0     2-1=1    3-1=2    4-1=3
        //select * from student limit 10 offset (0*10)
        //select * from student limit 10 offset (1*10)
        //select * from student limit 10 offset (2*10)
        //select * from student limit 10 offset (3*10)
        Pageable pageable = PageRequest.of(page, 10);
        Page<Student> studentPage = studentRepository.findAll(pageable);
        return studentPage;
    }

    //2. UNIVERSITY
    @GetMapping("/forUniversity/{universityId}")
    public Page<Student> getStudentListForUniversity(@PathVariable Integer universityId,
                                                     @RequestParam int page) {
        //1-1=0     2-1=1    3-1=2    4-1=3
        //select * from student limit 10 offset (0*10)
        //select * from student limit 10 offset (1*10)
        //select * from student limit 10 offset (2*10)
        //select * from student limit 10 offset (3*10)
        Pageable pageable = PageRequest.of(page, 10);
        Page<Student> studentPage = studentRepository.findAllByGroup_Faculty_UniversityId(universityId, pageable);
        return studentPage;
    }

    //3. FACULTY DEKANAT

    @GetMapping("/faculty/{faculty_id}")
    public Page<Student> getByFacultyId(@PathVariable Integer faculty_id,
                                        @RequestParam int page) {
        PageRequest pageRequest = PageRequest.of(page, page);
        return studentRepository.findByGroup_FacultyId(faculty_id, pageRequest);
    }

    //4. GROUP OWNER
    @GetMapping("/group/{group_id}")
    public Page<Student> getByGroupId(@PathVariable Integer group_id,
                                      @RequestParam int page) {
        PageRequest pageRequest = PageRequest.of(page, page);
        return studentRepository.findByGroup_FacultyId(group_id, pageRequest);
    }

    @PostMapping
    public void add(@RequestBody StudentDto studentDto) {

        Optional<Group> groupById = groupRepository.findById(studentDto.getGroupId());

        if (groupById.isPresent()) {
            Group group = groupById.get();

            Address address = new Address();
            address.setDistrict(studentDto.getDistrict());
            address.setCity(studentDto.getCity());
            address.setStreet(studentDto.getStreet());
            Address savedAddress = addressRepository.save(address);

            Student student = new Student();
            student.setFirstName(studentDto.getFirstName());
            student.setLastName(studentDto.getLastName());
            student.setAddress(savedAddress);
            student.setGroup(group);
            student.setSubjects(studentDto.getSubjects());

            studentRepository.save(student);

        }
    }

    @PutMapping("{id}")
    public void update(@PathVariable Integer id, @RequestBody Student student) {
        Optional<Student> studentById = studentRepository.findById(id);

        if (studentById.isPresent()) {
            Optional<Address> addressById = addressRepository.findById(student.getAddress().getId());
            if (addressById.isPresent()) {
                Address address = addressById.get();
                address.setDistrict(student.getAddress().getDistrict());
                address.setCity(student.getAddress().getCity());
                address.setStreet(student.getAddress().getStreet());

                Address savedAddress = addressRepository.save(address);

                Student studentData = studentById.get();
                studentData.setFirstName(student.getFirstName());
                studentData.setLastName(student.getLastName());
                studentData.setAddress(savedAddress);
                studentData.setGroup(student.getGroup());
                studentData.setSubjects(student.getSubjects());

                studentRepository.save(studentData);
            }
        }
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Integer id) {
        Optional<Student> studentById = studentRepository.findById(id);

        if (studentById.isPresent()) {
            studentRepository.deleteById(id);
        }

    }

}
