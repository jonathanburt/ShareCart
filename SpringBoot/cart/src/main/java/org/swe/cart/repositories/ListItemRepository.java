package org.swe.cart.repositories;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.swe.cart.embeddables.ListItemKey;
import org.swe.cart.entities.Item;
import org.swe.cart.entities.ListItem;
import org.swe.cart.entities.ShopList;

@Repository
public interface ListItemRepository extends JpaRepository<ListItem, ListItemKey>{
    boolean existsByListAndItem(ShopList list, Item item);
    List<ListItem> findByList(ShopList list);
    ListItem findByListAndItem(ShopList list, Item item);
    void deleteByListAndItem(ShopList list, Item item);
}
