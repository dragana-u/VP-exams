package mk.ukim.finki.wp.kol2021.restaurant.web;

import mk.ukim.finki.wp.kol2021.restaurant.model.Menu;
import mk.ukim.finki.wp.kol2021.restaurant.model.MenuItem;
import mk.ukim.finki.wp.kol2021.restaurant.model.MenuType;
import mk.ukim.finki.wp.kol2021.restaurant.service.MenuItemService;
import mk.ukim.finki.wp.kol2021.restaurant.service.MenuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping(path = {"/","/menu"})
public class MenuController {

    private final MenuService service;
    private final MenuItemService menuItemService;

    public MenuController(MenuService service, MenuItemService menuItemService) {
        this.service = service;
        this.menuItemService = menuItemService;
    }

    @GetMapping()
    public String showMenus(@RequestParam (required = false) String nameSearch,
                            @RequestParam (required = false) MenuType menuType,
                            Model model) {
//        if (nameSearch == null && menuType == null) {
//            service.listAll();
//        } else {
//            this.service.listMenuItemsByRestaurantNameAndMenuType(nameSearch,  menuType);
//        }
        List<Menu> menuList = service.listMenuItemsByRestaurantNameAndMenuType(nameSearch,  menuType);
        List<MenuItem> menuItemList = menuItemService.listAll();
        model.addAttribute("menu",menuList);
        model.addAttribute("menuItems", menuItemList);
        model.addAttribute("menuTypes", Arrays.stream(MenuType.values()).toList());
        model.addAttribute("nameSearch", nameSearch);
        model.addAttribute("menuType", menuType);
        return "list";
    }

    @GetMapping("/add")
    public String showAdd(Model model) {
        List<MenuItem> menuItemList = menuItemService.listAll();
        model.addAttribute("menuItems", menuItemList);
        model.addAttribute("menuTypes", Arrays.stream(MenuType.values()).toList());
        return "form";
    }

    @GetMapping("/{id}/edit")
    public String showEdit(@PathVariable Long id, Model model) {
        List<MenuItem> menuItemList = menuItemService.listAll();
        model.addAttribute("menuItems", menuItemList);
        model.addAttribute("menuTypes", Arrays.stream(MenuType.values()).toList());
        model.addAttribute("menu", this.service.findById(id));
        this.service.findById(id);
        return "form";
    }

    @PostMapping()
    public String create(@RequestParam String name,
                         @RequestParam MenuType menuType,
                         @RequestParam List<Long> menuItemIds) {
        this.service.create(name, menuType, menuItemIds);
        return "redirect:/menu";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @RequestParam String name,
                         @RequestParam MenuType menuType,
                         @RequestParam List<Long> menuItemIds) {
        this.service.update(id, name, menuType, menuItemIds);
        return "redirect:/menu";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        this.service.delete(id);
        return "redirect:/menu";
    }
}
