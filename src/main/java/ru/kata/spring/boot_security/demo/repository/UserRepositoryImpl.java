package ru.kata.spring.boot_security.demo.repository;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository{

    @PersistenceContext
    EntityManager em;

    @Override
    public List<User> findAll() {
        return em.createQuery("from User", User.class).getResultList();
    }

    @Override
    public User findByMail(String email) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> itemRoot = criteriaQuery.from(User.class);
        criteriaQuery.where(criteriaBuilder.equal(itemRoot.get("email"), email));
        return em.createQuery(criteriaQuery).getSingleResult();
    }

    @Override
    public User findByID(Long id) {
        TypedQuery<User> q = em.createQuery(
                "select user from User user where user.id = :id", User.class
        );
        q.setParameter("id", id);
        return q.getResultList().stream().findAny().orElse(null);
    }

    @Override
    public void save(User entity) {
        em.persist(entity);
    }

    @Override
    public void update(Long id, User updatedUser) {
        em.merge(updatedUser);
    }

    @Override
    public void delete(Long id) {
        em.remove(id);
    }
}
