package mk.ukim.finki.wp.kol2022.g1.service.impl;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.wp.kol2022.g1.model.Employee;
import mk.ukim.finki.wp.kol2022.g1.model.EmployeeType;
import mk.ukim.finki.wp.kol2022.g1.model.Skill;
import mk.ukim.finki.wp.kol2022.g1.model.exceptions.InvalidEmployeeIdException;
import mk.ukim.finki.wp.kol2022.g1.model.exceptions.InvalidSkillIdException;
import mk.ukim.finki.wp.kol2022.g1.repository.EmployeeRepository;
import mk.ukim.finki.wp.kol2022.g1.repository.SkillRepository;
import mk.ukim.finki.wp.kol2022.g1.service.EmployeeService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static mk.ukim.finki.wp.kol2022.g1.service.FieldFilterSpecification.*;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final SkillRepository skillRepository;

    @Override
    public List<Employee> listAll() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee findById(Long id) {
        return employeeRepository.findById(id).orElseThrow(InvalidEmployeeIdException::new);
    }

    @Override
    public Employee create(String name, String email, String password, EmployeeType type, List<Long> skillId, LocalDate employmentDate) {
        List<Skill> skills = skillId
                .stream()
                .map(skillRepository::findById)
                .map(skill -> skill.orElseThrow(InvalidSkillIdException::new))
                .toList();
        return employeeRepository.save(new Employee(name, email, password, type, skills, employmentDate));
    }

    @Override
    public Employee update(Long id, String name, String email, String password, EmployeeType type, List<Long> skillId, LocalDate employmentDate) {
        Employee e = employeeRepository.findById(id).orElseThrow(InvalidEmployeeIdException::new);
        List<Skill> skills = skillId
                .stream()
                .map(skillRepository::findById)
                .map(skill -> skill.orElseThrow(InvalidSkillIdException::new))
                .collect(Collectors.toCollection(ArrayList::new));
        e.setName(name);
        e.setEmail(email);
        e.setPassword(password);
        e.setType(type);
        e.setSkills(skills);
        e.setEmploymentDate(employmentDate);
        return employeeRepository.save(e);
    }

    @Override
    public Employee delete(Long id) {
        Employee e = this.findById(id);
        employeeRepository.delete(e);
        return e;
    }

    @Override
    public List<Employee> filter(Long skillId, Integer yearsOfService) {
        Skill skill = null;
        if(skillId != null){
            skill = skillRepository.findById(skillId).orElseThrow(InvalidSkillIdException::new);
        }
        LocalDate date = null;
        if (yearsOfService != null) {
            date = LocalDate.of(LocalDate.now().getYear() - yearsOfService, LocalDate.now().getMonth(), LocalDate.now().getDayOfMonth());
        }
        Specification<Employee> specification = Specification
                .where(isMember(Employee.class, "skills", skill))
                .and(lessThan(Employee.class, "employmentDate", date));

        return employeeRepository.findAll(specification);
    }
}
