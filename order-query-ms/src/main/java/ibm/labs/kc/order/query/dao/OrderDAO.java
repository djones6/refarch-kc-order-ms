package ibm.labs.kc.order.query.dao;

import java.util.Collection;
import java.util.Optional;

import ibm.labs.kc.order.query.model.Order;

public interface OrderDAO {

    public Optional<Order> getById(String orderId);
    public void add(Order o);
    public void update(Order order);
    public Collection<Order> getByManuf(String manuf);

}
