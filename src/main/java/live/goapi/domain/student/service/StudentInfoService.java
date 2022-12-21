package live.goapi.domain.student.service;

import live.goapi.domain.api_key.service.CheckApiKeyService;
import live.goapi.domain.club.entity.Club;
import live.goapi.domain.club.repository.ClubRepository;
import live.goapi.domain.student.entity.Student;
import live.goapi.domain.student.exception.NotFoundStudentException;
import live.goapi.domain.student.presentation.dto.request.RequestStudentClub;
import live.goapi.domain.student.presentation.dto.request.RequestStudentMajor;
import live.goapi.domain.student.presentation.dto.request.RequestStudentName;
import live.goapi.domain.student.presentation.dto.request.RequestStudentNumber;
import live.goapi.domain.student.presentation.dto.response.ResponseStudent;
import live.goapi.domain.student.repository.StudentRepository;
import live.goapi.domain.teacher.exception.NotFoundTeacherException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentInfoService {

    private final StudentRepository studentRepository;
    private final ClubRepository clubRepository;
    private final CheckApiKeyService checkApiKeyService;

    public ResponseStudent getStudentInfoByStudentName (RequestStudentName request) {

        checkApiKeyService.checkApiKey(request.getRandomKey());

        Student findStudent = studentRepository.findByStudentName(request.getStudentName()).orElseThrow(
                () -> new NotFoundStudentException("존재하지 않는 학생입니다"));
        return makeResponseStudent(findStudent);
    }

    public ResponseStudent getStudentInfoByStudentNumber(RequestStudentNumber request) {

        checkApiKeyService.checkApiKey(request.getRandomKey());

        Student findStudent = studentRepository.findByStudentNumber(request.getStudentNumber())
                .orElseThrow( ()-> new NotFoundTeacherException("존재하지 않는 학생입니다"));

        return makeResponseStudent(findStudent);
    }

    public List<ResponseStudent> getStudentsInfoByMajor(RequestStudentMajor request) {

        checkApiKeyService.checkApiKey(request.getRandomKey());

        List<Student> students = studentRepository.findByStudentMajor(request.getMajor());
        
        
        if(students.isEmpty()){
            throw new NotFoundStudentException("존재하지 않는 학생들입니다.");
        }

        List<ResponseStudent> responseList = makeResponseStudentList(students);

        return responseList;
    }
    
    public List<ResponseStudent> getStudentsByClub(RequestStudentClub request) {

        checkApiKeyService.checkApiKey(request.getRandomKey());

        Optional<Club> club = clubRepository.findByClubName(request.getClubName());
        List<Student> clubStudents = studentRepository.findByClub(club.get());
        
        
        if(clubStudents.isEmpty()) {
            throw new NotFoundStudentException("존재하지 않는 학생들입니다.");
        }

        List<ResponseStudent> responseList = makeResponseStudentList(clubStudents);

        return responseList;
    }

    private ResponseStudent makeResponseStudent(Student findStudent) {
        return ResponseStudent.builder()
                .studentName(findStudent.getStudentName())
                .studentNumber(findStudent.getStudentNumber())
                .studentMajor(findStudent.getStudentMajor())
                .club(findStudent.getClub().getClubName())
                .build();
    }
    
    private List<ResponseStudent> makeResponseStudentList(List<Student> students) {
        List<ResponseStudent> responseList = new ArrayList<>();
        for (Student clubStudent : students) {
            responseList.add(makeResponseStudent(clubStudent));
        }
        return responseList;
    }
}
