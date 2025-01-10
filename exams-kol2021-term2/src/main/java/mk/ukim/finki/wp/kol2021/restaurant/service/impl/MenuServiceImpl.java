package mk.ukim.finki.wp.kol2021.restaurant.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mk.ukim.finki.wp.kol2021.restaurant.model.Menu;
import mk.ukim.finki.wp.kol2021.restaurant.model.MenuItem;
import mk.ukim.finki.wp.kol2021.restaurant.model.MenuType;
import mk.ukim.finki.wp.kol2021.restaurant.model.exceptions.InvalidMenuIdException;
import mk.ukim.finki.wp.kol2021.restaurant.repository.MenuItemRepository;
import mk.ukim.finki.wp.kol2021.restaurant.repository.MenuRepository;
import mk.ukim.finki.wp.kol2021.restaurant.service.MenuService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static mk.ukim.finki.wp.kol2021.restaurant.service.FieldFilterSpecification.filterEquals;
import static mk.ukim.finki.wp.kol2021.restaurant.service.FieldFilterSpecification.filterEqualsV;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;
    private final MenuItemRepository menuItemRepository;

    @Override
    public List<Menu> listAll() {
        return menuRepository.findAll();
    }

    @Override
    public Menu findById(Long id) {
        return menuRepository.findById(id).orElseThrow(InvalidMenuIdException::new);
    }

    @Override
    public Menu create(String restaurantName, MenuType menuType, List<Long> menuItems) {
        List<MenuItem> menuItemList =
                menuItems
                        .stream()
                        .map(menuItemRepository::findById)
                        .map(e -> e.orElseThrow(InvalidMenuIdException::new))
                        .toList();
        return menuRepository.save(new Menu(restaurantName,menuType,new ArrayList<>(menuItemList)));
    }

    @Override
    public Menu update(Long id, String restaurantName, MenuType menuType, List<Long> menuItems) {
        Menu menu = this.findById(id);
        List<MenuItem> menuItemList =
                menuItems
                        .stream()
                        .map(menuItemRepository::findById)
                        .map(e -> e.orElseThrow(InvalidMenuIdException::new))
                        .toList();
        menu.setRestaurantName(restaurantName);

        menu.setMenuType(menuType);
        menu.setMenuItems(new ArrayList<>(menuItemList));
        this.menuRepository.save(menu);
        return menu;
    }

    @Override
    public Menu delete(Long id) {
        Menu menu = this.findById(id);
        menuRepository.delete(menu);
        return menu;
    }

    @Override
    public List<Menu> listMenuItemsByRestaurantNameAndMenuType(String restaurantName, MenuType menuType) {
        Specification<Menu> specification = Specification
                .where(filterEquals(Menu.class, "restaurantName", restaurantName))
                .and(filterEqualsV(Menu.class, "menuType", menuType));

        return menuRepository.findAll(specification);
    }
}
