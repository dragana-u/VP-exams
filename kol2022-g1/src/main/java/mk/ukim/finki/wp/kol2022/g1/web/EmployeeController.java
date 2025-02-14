package mk.ukim.finki.wp.kol2022.g1.web;

import mk.ukim.finki.wp.kol2022.g1.model.Employee;
import mk.ukim.finki.wp.kol2022.g1.model.EmployeeType;
import mk.ukim.finki.wp.kol2022.g1.service.EmployeeService;
import mk.ukim.finki.wp.kol2022.g1.service.SkillService;
import net.sourceforge.htmlunit.xpath.operations.Mod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping(path = {"/", "/employees"})
public class EmployeeController {

    private final EmployeeService service;
    private final SkillService skillService;

    public EmployeeController(EmployeeService service, SkillService skillService) {
        this.service = service;
        this.skillService = skillService;
    }

    /**
     * This method should use the "list.html" template to display all entities.
     * The method should be mapped on paths '/' and '/employees'.
     * The arguments that this method takes are optional and can be 'null'.
     *
     * @return The view "list.html".
     */
    @GetMapping()
    public String showList(@RequestParam (required = false) Long skillId,
                           @RequestParam (required = false) Integer yearsOfService,
                           Model model) {
//        List<Employee> employees;
//        if (skillId == null && yearsOfService == null) {
//            employees = this.service.listAll();
//        } else {
//            employees = this.service.filter(skillId, yearsOfService);
//        }

        List<Employee> employees = this.service.filter(skillId, yearsOfService);
        model.addAttribute("employees", employees);
        model.addAttribute("skills",skillService.listAll());
        return "list";
    }

    /**
     * This method should display the "form.html" template.
     * The method should be mapped on path '/employees/add'.
     *
     * @return The view "form.html".
     */
    @GetMapping("/add")
    public String showAdd(Model model) {
        model.addAttribute("skills",skillService.listAll());
        model.addAttribute("types", Arrays.stream(EmployeeType.values()).toArray());
        return "form";
    }

    /**
     * This method should display the "form.html" template.
     * However, in this case all 'input' elements should be filled with the appropriate value for the entity that is updated.
     * The method should be mapped on path '/employees/[id]/edit'.
     *
     * @return The view "form.html".
     */
    @GetMapping("/{id}/edit")
    public String showEdit(@PathVariable Long id, Model model) {
        Employee employee = this.service.findById(id);
        model.addAttribute("employee",employee);
        model.addAttribute("skills",skillService.listAll());
        model.addAttribute("types", Arrays.stream(EmployeeType.values()).toArray());
        return "form";
    }

    /**
     * This method should create an entity given the arguments it takes.
     * The method should be mapped on path '/employees'.
     * After the entity is created, the list of entities should be displayed.
     *
     * @return The view "list.html".
     */
    @PostMapping()
    public String create(@RequestParam String name,
                         @RequestParam String email,
                         @RequestParam String password,
                         @RequestParam EmployeeType type,
                         @RequestParam List<Long> skillId,
                         @RequestParam LocalDate employmentDate) {
        this.service.create(name, email, password, type, skillId, employmentDate);
        return "redirect:/employees";
    }

    /**
     * This method should update an entity given the arguments it takes.
     * The method should be mapped on path '/employees/[id]'.
     * After the entity is updated, the list of entities should be displayed.
     *
     * @return The view "list.html".
     */
    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @RequestParam String name,
                         @RequestParam String email,
                         @RequestParam String password,
                         @RequestParam EmployeeType type,
                         @RequestParam List<Long> skillId,
                         @RequestParam LocalDate employmentDate) {
        this.service.update(id, name, email, password, type, skillId, employmentDate);
        return "redirect:/employees";
    }

    /**
     * This method should delete the entities that has the appropriate identifier.
     * The method should be mapped on path '/employees/[id]/delete'.
     * After the entity is deleted, the list of entities should be displayed.
     *
     * @return The view "list.html".
     */
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        this.service.delete(id);
        return "redirect:/employees";
    }
}
